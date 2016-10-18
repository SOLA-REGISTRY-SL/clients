package org.sola.clients.swing.desktop;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.JLabel;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationSearchResultBean;
import org.sola.clients.beans.application.ApplicationSearchResultsListBean;
import org.sola.clients.beans.application.ApplicationSummaryBean;
import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.swing.common.labels.HyperLink;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.application.ApplicationAssignmentDialog;
import org.sola.clients.swing.desktop.application.ApplicationPanel;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.clients.swing.ui.renderers.DateTimeRenderer;
import org.sola.common.RolesConstants;
import org.sola.common.WindowUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;
import org.sola.webservices.transferobjects.search.DashboardStatisticsTO;

/**
 * Detailed dashboard panel
 */
public class DashboardDetailedPanel extends ContentPanel {

    private class AssignmentPanelListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            if (e.getPropertyName().equals(ApplicationAssignmentDialog.ASSIGNMENT_CHANGED)) {
                refreshApplications();
            }
        }
    }

    private DashboardDetailedPanel.AssignmentPanelListener assignmentPanelListener;
    private boolean forceRefresh;
    private HyperLink selectedLink;
    private boolean autoRefresh = false;

    /**
     * Creates new form DashboardDetailedPanel
     */
    public DashboardDetailedPanel() {
        initComponents();
    }

    public DashboardDetailedPanel(boolean forceRefresh) {
        this.forceRefresh = forceRefresh;
        assignmentPanelListener = new DashboardDetailedPanel.AssignmentPanelListener();
        initComponents();
        postInit();
    }

    private void postInit() {
        setHeaderPanel(headerPanel);
        tblApplications.setColumnSelectionAllowed(false);
        tblApplications.setRowSelectionAllowed(true);
        tblApplications.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        btnRefresh.setEnabled(SecurityBean.isInRole(RolesConstants.DASHBOARD_VIEW_ASSIGNED_APPS, RolesConstants.DASHBOARD_VIEW_UNASSIGNED_APPS));
        if (forceRefresh) {
            refreshApplications();
        }

        customizeToolbarButtons();

        applicationSearchResults.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(ApplicationSearchResultsListBean.APPLICATION_CHECKED_PROPERTY)
                        || evt.getPropertyName().equals(ApplicationSearchResultsListBean.SELECTED_APPLICATION_PROPERTY)) {
                    customizeToolbarButtons();
                }
            }
        });
    }

    /**
     * Enables or disables toolbar buttons.
     */
    private void customizeToolbarButtons() {
        boolean isUnassignEnabled;
        boolean isEditEnabled = false;
        boolean isAssignEnabled;

        if (applicationSearchResults.hasUnassignedChecked()
                && (SecurityBean.isInRole(RolesConstants.APPLICATION_UNASSIGN_FROM_YOURSELF)
                || SecurityBean.isInRole(RolesConstants.APPLICATION_UNASSIGN_FROM_OTHERS))) {
            isAssignEnabled = true;
        } else {
            isAssignEnabled = false;
        }

        if (applicationSearchResults.hasAssignedChecked()
                && (SecurityBean.isInRole(RolesConstants.APPLICATION_ASSIGN_TO_YOURSELF)
                || SecurityBean.isInRole(RolesConstants.APPLICATION_ASSIGN_TO_OTHERS))) {
            isUnassignEnabled = true;
        } else {
            isUnassignEnabled = false;
        }

        if (!isUnassignEnabled && !isAssignEnabled
                && SecurityBean.isInRole(RolesConstants.APPLICATION_EDIT_APPS, RolesConstants.APPLICATION_VIEW_APPS)) {
            isEditEnabled = applicationSearchResults.getSelectedApplication() != null;
        }

        btnUnassign.setEnabled(isUnassignEnabled);
        btnOpen.setEnabled(isEditEnabled);
        btnAssign.setEnabled(isAssignEnabled);
    }

    /**
     * Opens application assignment form with selected applications.
     *
     * @param appList Selected applications to assign or unassign.
     * @param assign Indicates whether to assign or unassign applications
     */
    private void assignUnassign(final List<ApplicationSearchResultBean> appList, final boolean assign) {
        if (appList == null || appList.size() < 1) {
            return;
        }

        for (ApplicationSearchResultBean app : appList) {
            if (!app.isFeePaid()) {
                MessageUtility.displayMessage(ClientMessage.CHECK_FEES_NOT_PAID, new Object[]{app.getNr()});
                return;
            }
        }

        if (assign) {
            ApplicationAssignmentDialog form = new ApplicationAssignmentDialog(appList, MainForm.getInstance(), true);
            WindowUtility.centerForm(form);
            form.addPropertyChangeListener(assignmentPanelListener);
            form.setVisible(true);
        } else if (MessageUtility.displayMessage(ClientMessage.APPLICATION_CONFIRM_UNASSIGN) == MessageUtility.BUTTON_ONE) {
            for (ApplicationSearchResultBean app : appList) {
                String assigneeId = app.getAssigneeId();
                if (assigneeId != null) {
                    if (assigneeId.equals(SecurityBean.getCurrentUser().getId())
                            && !SecurityBean.isInRole(RolesConstants.APPLICATION_UNASSIGN_FROM_YOURSELF,
                                    RolesConstants.APPLICATION_UNASSIGN_FROM_OTHERS)) {
                        // Can't unassign from yourself
                        MessageUtility.displayMessage(ClientMessage.APPLICATION_UNASSIGN_FROM_SELF_FORBIDDEN,
                                new Object[]{app.getNr()});
                        break;
                    }
                    if (!assigneeId.equals(SecurityBean.getCurrentUser().getId())
                            && !SecurityBean.isInRole(RolesConstants.APPLICATION_UNASSIGN_FROM_OTHERS)) {
                        MessageUtility.displayMessage(ClientMessage.APPLICATION_UNASSIGN_FROM_OTHERS_FORBIDDEN,
                                new Object[]{app.getNr()});
                        break;
                    }
                    ApplicationBean.assignUser(app, null);
                }
            }
            refreshApplications();
        }
    }

    /**
     * Opens application form.
     *
     * @param appBean Selected application summary bean.
     */
    private void openApplication(final ApplicationSummaryBean appBean) {
        if (appBean == null || getMainContentPanel() == null) {
            return;
        }

        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_APP));
                if (getMainContentPanel() != null) {
                    ApplicationPanel applicationPanel = new ApplicationPanel(appBean.getId());
                    getMainContentPanel().addPanel(applicationPanel, MainContentPanel.CARD_APPLICATION, true);
                }
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    /**
     * Refreshes assigned and unassigned application lists.
     */
    private void refreshApplications() {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                if (btnRefresh.isEnabled()) {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_STATISTICS));
                    DashboardStatisticsTO stat = applicationSearchResults.getDashboardStatistics();
                    if(stat!=null){
                        lblMyApplicationsCount.setText(String.format("(%s)", Long.toString(stat.getMyApps())));
                        lblAssignedCount.setText(String.format("(%s)", Long.toString(stat.getAssignedAll())));
                        lblUnassignedCount.setText(String.format("(%s)", Long.toString(stat.getUnassignedAll())));
                        lblPLLodgedCount.setText(String.format("(%s)", Long.toString(stat.getPlLodged())));
                        lblPLForSurveyPlanCapturingCount.setText(String.format("(%s)", Long.toString(stat.getPlForCapture())));
                        lblPLForStateLandClearanceCount.setText(String.format("(%s)", Long.toString(stat.getPlForStateClearance())));
                        lblPLForPlanningClearanceCount.setText(String.format("(%s)", Long.toString(stat.getPlForPlanningClearance())));
                        lblPLForEnvironmentClearanceCount.setText(String.format("(%s)", Long.toString(stat.getPlForEnvClearance())));
                        lblPLForCompletionCount.setText(String.format("(%s)", Long.toString(stat.getPlForCompletion())));
                        lblPLForApprovalCount.setText(String.format("(%s)", Long.toString(stat.getPlForApproval())));
                        lblSLLodgedCount.setText(String.format("(%s)", Long.toString(stat.getSlLodged())));
                        lblSLForSurveyPlanCapturingCount.setText(String.format("(%s)", Long.toString(stat.getSlForCapture())));
                        lblSLForCompletionCount.setText(String.format("(%s)", Long.toString(stat.getSlForCompletion())));
                        lblSLForApprovalCount.setText(String.format("(%s)", Long.toString(stat.getSlForApproval())));
                        lblSLApprovedCount.setText(String.format("(%s)", Long.toString(stat.getSlApproved())));
                        lblPLApprovedCount.setText(String.format("(%s)", Long.toString(stat.getPlApproved())));
                    }
                    
                    if (selectedLink == null || selectedLink.equals(lblMyApplications)) {
                        setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_MY));
                        applicationSearchResults.FillMyApplications();
                        if(selectedLink == null){
                            lblMyApplications.setSelected(true);
                            selectedLink = lblMyApplications;
                        }
                    } else {
                        if (selectedLink.equals(lblUnassigned)) {
                            setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_UNASSIGNED));
                            applicationSearchResults.FillUnassigned();
                        }
                        if (selectedLink.equals(lblAssigned)) {
                            setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_ASSIGNED));
                            applicationSearchResults.FillAssigned();
                        }
                        if (selectedLink.equals(lblPLLodged)) {
                            setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_PL_LODGED));
                            applicationSearchResults.FillPlLodged();
                        }
                        if (selectedLink.equals(lblPLForSurveyPlanCapturing)) {
                            setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_PL_PLAN_CAPTURING));
                            applicationSearchResults.FillPlApplicationsForPlanCapturing();
                        }
                        if (selectedLink.equals(lblPLForStateLandClearance)) {
                            setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_PL_SL_CLEARANCE));
                            applicationSearchResults.FillPlApplicationsForStateLandClearance();
                        }
                        if (selectedLink.equals(lblPLForPlanningClearance)) {
                            setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_PL_PLANNING_CLEARANCE));
                            applicationSearchResults.FillPlApplicationsForPlanningClearance();
                        }
                        if (selectedLink.equals(lblPLForEnvironmentClearance)) {
                            setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_PL_ENV_CLEARANCE));
                            applicationSearchResults.FillPlApplicationsForEnvironmentClearance();
                        }
                        if (selectedLink.equals(lblPLForCompletion)) {
                            setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_PL_COMPLETION));
                            applicationSearchResults.FillPlApplicationsForCompletion();
                        }
                        if (selectedLink.equals(lblPLForApproval)) {
                            setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_PL_APPROVAL));
                            applicationSearchResults.FillPlApplicationsForApproval();
                        }

                        if (selectedLink.equals(lblSLLodged)) {
                            setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_SL_LODGED));
                            applicationSearchResults.FillSlLodgedApplications();
                        }
                        if (selectedLink.equals(lblSLForSurveyPlanCapturing)) {
                            setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_SL_PLAN_CAPTURING));
                            applicationSearchResults.FillSlApplicationsForPlanCapturing();
                        }
                        if (selectedLink.equals(lblSLForCompletion)) {
                            setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_SL_COMPLETION));
                            applicationSearchResults.FillSlApplicationsForCompletion();
                        }
                        if (selectedLink.equals(lblSLForApproval)) {
                            setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_SL_APPROVAL));
                            applicationSearchResults.FillSlApplicationsForApproval();
                        }
                        if (selectedLink.equals(lblSLApproved)) {
                            setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_SL_APPROVED));
                            applicationSearchResults.FillSlApprovedApplications();
                        }
                        if (selectedLink.equals(lblPLApproved)) {
                            setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.APPLICATION_LOADING_PL_APPROVED));
                            applicationSearchResults.FillPlApprovedApplications();
                        }
                    }
                }
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    /**
     * Opens form to assign application.
     */
    private void assignApplication() {
        assignUnassign(applicationSearchResults.getChecked(true), true);
    }

    /**
     * Opens form to unassign application.
     */
    private void unassignApplication() {
        assignUnassign(applicationSearchResults.getChecked(true), false);
    }

    /**
     * Opens application form for the selected application from assigned list.
     */
    private void editApplication() {
        openApplication(applicationSearchResults.getSelectedApplication());
    }

    private void autoRefresh() {
        if (autoRefresh) {
            setAutoRefresh(false);
            refreshApplications();
        }
    }

    /**
     * Returns autorefresh value.
     */
    public boolean isAutoRefresh() {
        return autoRefresh;
    }

    /**
     * Sets autorefresh value. If <code>true</code> is assigned Dashboard will
     * be refreshed automatically at the time of component shown event.
     */
    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    private void selectLink(Component comp) {
        HyperLink link = (HyperLink) comp;
        if (selectedLink != null) {
            if (link.equals(selectedLink)) {
                return;
            }
            selectedLink.setSelected(false);
        }
        selectedLink = link;
        JLabel lblGroup = (JLabel) link.getLabelFor();
        if (lblGroup != null) {
            lblHeader.setText(lblGroup.getText() + " - " + link.getText());
        } else {
            lblHeader.setText(link.getText());
        }
        refreshApplications();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        applicationSearchResults = new org.sola.clients.beans.application.ApplicationSearchResultsListBean();
        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnRefresh = new org.sola.clients.swing.common.buttons.BtnRefresh();
        btnOpen = new org.sola.clients.swing.common.buttons.BtnOpen();
        btnAssign = new org.sola.clients.swing.common.buttons.BtnAssign();
        btnUnassign = new org.sola.clients.swing.common.buttons.BtnUnassign();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblApplications = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jPanel3 = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblGeneral = new javax.swing.JLabel();
        lblPrivateLand = new javax.swing.JLabel();
        lblStateLand = new javax.swing.JLabel();
        lblMyApplicationsCount = new javax.swing.JLabel();
        lblUnassignedCount = new javax.swing.JLabel();
        lblAssignedCount = new javax.swing.JLabel();
        lblUnassigned = new org.sola.clients.swing.common.labels.HyperLink();
        lblAssigned = new org.sola.clients.swing.common.labels.HyperLink();
        lblMyApplications = new org.sola.clients.swing.common.labels.HyperLink();
        lblPLLodged = new org.sola.clients.swing.common.labels.HyperLink();
        lblPLLodgedCount = new javax.swing.JLabel();
        lblPLForSurveyPlanCapturingCount = new javax.swing.JLabel();
        lblPLForStateLandClearanceCount = new javax.swing.JLabel();
        lblPLForEnvironmentClearanceCount = new javax.swing.JLabel();
        lblPLForPlanningClearanceCount = new javax.swing.JLabel();
        lblPLForCompletionCount = new javax.swing.JLabel();
        lblPLForApprovalCount = new javax.swing.JLabel();
        lblPLForSurveyPlanCapturing = new org.sola.clients.swing.common.labels.HyperLink();
        lblPLForStateLandClearance = new org.sola.clients.swing.common.labels.HyperLink();
        lblPLForEnvironmentClearance = new org.sola.clients.swing.common.labels.HyperLink();
        lblPLForPlanningClearance = new org.sola.clients.swing.common.labels.HyperLink();
        lblPLForCompletion = new org.sola.clients.swing.common.labels.HyperLink();
        lblPLForApproval = new org.sola.clients.swing.common.labels.HyperLink();
        lblSLLodged = new org.sola.clients.swing.common.labels.HyperLink();
        lblSLLodgedCount = new javax.swing.JLabel();
        lblSLForSurveyPlanCapturing = new org.sola.clients.swing.common.labels.HyperLink();
        lblSLForSurveyPlanCapturingCount = new javax.swing.JLabel();
        lblSLForCompletion = new org.sola.clients.swing.common.labels.HyperLink();
        lblSLForCompletionCount = new javax.swing.JLabel();
        lblSLForApproval = new org.sola.clients.swing.common.labels.HyperLink();
        lblSLForApprovalCount = new javax.swing.JLabel();
        lblSLApproved = new org.sola.clients.swing.common.labels.HyperLink();
        lblSLApprovedCount = new javax.swing.JLabel();
        lblPLApproved = new org.sola.clients.swing.common.labels.HyperLink();
        lblPLApprovedCount = new javax.swing.JLabel();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/Bundle"); // NOI18N
        headerPanel.setTitleText(bundle.getString("DashboardDetailedPanel.headerPanel.titleText")); // NOI18N

        jSplitPane1.setDividerLocation(220);
        jSplitPane1.setAutoscrolls(true);

        jPanel1.setPreferredSize(new java.awt.Dimension(400, 306));

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshMouseClicked(evt);
            }
        });
        jToolBar1.add(btnRefresh);

        btnOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenMouseClicked(evt);
            }
        });
        jToolBar1.add(btnOpen);

        btnAssign.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAssign.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAssignMouseClicked(evt);
            }
        });
        jToolBar1.add(btnAssign);

        btnUnassign.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnUnassign.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUnassignMouseClicked(evt);
            }
        });
        jToolBar1.add(btnUnassign);

        jScrollPane1.setAutoscrolls(true);

        tblApplications.setColumnSelectionAllowed(true);
        tblApplications.setName("tblApplications"); // NOI18N
        tblApplications.setShowVerticalLines(false);
        tblApplications.getTableHeader().setReorderingAllowed(false);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${applicationSearchResultsList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, applicationSearchResults, eLProperty, tblApplications);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${checked}"));
        columnBinding.setColumnName(" ");
        columnBinding.setColumnClass(Boolean.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${nr}"));
        columnBinding.setColumnName("Nr");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${lodgingDatetime}"));
        columnBinding.setColumnName("Lodging Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${contactPerson}"));
        columnBinding.setColumnName("Applicant");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${serviceList}"));
        columnBinding.setColumnName("Services");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${parcel}"));
        columnBinding.setColumnName("LS or LOA number");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${assigneeName}"));
        columnBinding.setColumnName("Assignee Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${status}"));
        columnBinding.setColumnName("Status");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, applicationSearchResults, org.jdesktop.beansbinding.ELProperty.create("${selectedApplication}"), tblApplications, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane1.setViewportView(tblApplications);
        if (tblApplications.getColumnModel().getColumnCount() > 0) {
            tblApplications.getColumnModel().getColumn(0).setMinWidth(25);
            tblApplications.getColumnModel().getColumn(0).setPreferredWidth(25);
            tblApplications.getColumnModel().getColumn(0).setMaxWidth(25);
            tblApplications.getColumnModel().getColumn(2).setCellRenderer(new DateTimeRenderer() );
            tblApplications.getColumnModel().getColumn(4).setCellRenderer(new org.sola.clients.swing.ui.renderers.CellDelimitedListRenderer());
            tblApplications.getColumnModel().getColumn(7).setMinWidth(80);
            tblApplications.getColumnModel().getColumn(7).setPreferredWidth(80);
            tblApplications.getColumnModel().getColumn(7).setMaxWidth(80);
        }

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblHeader.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblHeader.setText(" General - My applications");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(lblHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblHeader, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE))
        );

        jScrollPane3.setViewportView(jPanel1);

        jSplitPane1.setRightComponent(jScrollPane3);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        lblGeneral.setBackground(new java.awt.Color(0, 102, 102));
        lblGeneral.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblGeneral.setForeground(new java.awt.Color(255, 255, 255));
        lblGeneral.setText(" General");
        lblGeneral.setOpaque(true);

        lblPrivateLand.setBackground(new java.awt.Color(0, 102, 102));
        lblPrivateLand.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPrivateLand.setForeground(new java.awt.Color(255, 255, 255));
        lblPrivateLand.setText(" Private land");
        lblPrivateLand.setOpaque(true);

        lblStateLand.setBackground(new java.awt.Color(0, 102, 102));
        lblStateLand.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblStateLand.setForeground(new java.awt.Color(255, 255, 255));
        lblStateLand.setText(" State land");
        lblStateLand.setOpaque(true);

        lblMyApplicationsCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMyApplicationsCount.setText("(0)");

        lblUnassignedCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUnassignedCount.setText("(0)");

        lblAssignedCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAssignedCount.setText("(0)");

        lblUnassigned.setLabelFor(lblGeneral);
        lblUnassigned.setText("Unassigned");
        lblUnassigned.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblUnassignedMouseClicked(evt);
            }
        });

        lblAssigned.setLabelFor(lblGeneral);
        lblAssigned.setText("Assigned");
        lblAssigned.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAssignedMouseClicked(evt);
            }
        });

        lblMyApplications.setLabelFor(lblGeneral);
        lblMyApplications.setText("My applications");
        lblMyApplications.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMyApplicationsMouseClicked(evt);
            }
        });

        lblPLLodged.setLabelFor(lblPrivateLand);
        lblPLLodged.setText("Lodged");
        lblPLLodged.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPLLodgedMouseClicked(evt);
            }
        });

        lblPLLodgedCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPLLodgedCount.setText("(0)");

        lblPLForSurveyPlanCapturingCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPLForSurveyPlanCapturingCount.setText("(0)");

        lblPLForStateLandClearanceCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPLForStateLandClearanceCount.setText("(0)");

        lblPLForEnvironmentClearanceCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPLForEnvironmentClearanceCount.setText("(0)");

        lblPLForPlanningClearanceCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPLForPlanningClearanceCount.setText("(0)");

        lblPLForCompletionCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPLForCompletionCount.setText("(0)");

        lblPLForApprovalCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPLForApprovalCount.setText("(0)");

        lblPLForSurveyPlanCapturing.setLabelFor(lblPrivateLand);
        lblPLForSurveyPlanCapturing.setText("For survey plan capturing");
        lblPLForSurveyPlanCapturing.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPLForSurveyPlanCapturingMouseClicked(evt);
            }
        });

        lblPLForStateLandClearance.setLabelFor(lblPrivateLand);
        lblPLForStateLandClearance.setText("For state land clearance");
        lblPLForStateLandClearance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPLForStateLandClearanceMouseClicked(evt);
            }
        });

        lblPLForEnvironmentClearance.setLabelFor(lblPrivateLand);
        lblPLForEnvironmentClearance.setText("For environment clearance");
        lblPLForEnvironmentClearance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPLForEnvironmentClearanceMouseClicked(evt);
            }
        });

        lblPLForPlanningClearance.setLabelFor(lblPrivateLand);
        lblPLForPlanningClearance.setText("For planning clearance");
        lblPLForPlanningClearance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPLForPlanningClearanceMouseClicked(evt);
            }
        });

        lblPLForCompletion.setLabelFor(lblPrivateLand);
        lblPLForCompletion.setText("For completion");
        lblPLForCompletion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPLForCompletionMouseClicked(evt);
            }
        });

        lblPLForApproval.setLabelFor(lblPrivateLand);
        lblPLForApproval.setText("For approval");
        lblPLForApproval.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPLForApprovalMouseClicked(evt);
            }
        });

        lblSLLodged.setLabelFor(lblStateLand);
        lblSLLodged.setText("Lodged");
        lblSLLodged.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSLLodgedMouseClicked(evt);
            }
        });

        lblSLLodgedCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSLLodgedCount.setText("(0)");

        lblSLForSurveyPlanCapturing.setLabelFor(lblStateLand);
        lblSLForSurveyPlanCapturing.setText("For survey plan capturing");
        lblSLForSurveyPlanCapturing.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSLForSurveyPlanCapturingMouseClicked(evt);
            }
        });

        lblSLForSurveyPlanCapturingCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSLForSurveyPlanCapturingCount.setText("(0)");

        lblSLForCompletion.setLabelFor(lblStateLand);
        lblSLForCompletion.setText("For completion");
        lblSLForCompletion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSLForCompletionMouseClicked(evt);
            }
        });

        lblSLForCompletionCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSLForCompletionCount.setText("(0)");

        lblSLForApproval.setLabelFor(lblStateLand);
        lblSLForApproval.setText("For approval");
        lblSLForApproval.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSLForApprovalMouseClicked(evt);
            }
        });

        lblSLForApprovalCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSLForApprovalCount.setText("(0)");

        lblSLApproved.setLabelFor(lblStateLand);
        lblSLApproved.setText("Approved");
        lblSLApproved.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSLApprovedMouseClicked(evt);
            }
        });

        lblSLApprovedCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSLApprovedCount.setText("(0)");

        lblPLApproved.setLabelFor(lblPrivateLand);
        lblPLApproved.setText("Approved");
        lblPLApproved.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPLApprovedMouseClicked(evt);
            }
        });

        lblPLApprovedCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPLApprovedCount.setText("(0)");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblPrivateLand, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblStateLand, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblUnassigned, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblUnassignedCount))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblAssigned, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblAssignedCount))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblMyApplications, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblMyApplicationsCount))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblPLLodged, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblPLLodgedCount))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblPLForSurveyPlanCapturing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblPLForSurveyPlanCapturingCount))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblPLForStateLandClearance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblPLForStateLandClearanceCount))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblPLForEnvironmentClearance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addComponent(lblPLForEnvironmentClearanceCount))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblPLForPlanningClearance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblPLForPlanningClearanceCount))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblPLForCompletion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblPLForCompletionCount))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblPLForApproval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblPLForApprovalCount))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblPLApproved, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblPLApprovedCount))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblSLLodged, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSLLodgedCount))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblSLForSurveyPlanCapturing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSLForSurveyPlanCapturingCount))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblSLForCompletion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSLForCompletionCount))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblSLForApproval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSLForApprovalCount))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblSLApproved, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSLApprovedCount)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(lblGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMyApplications, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMyApplicationsCount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUnassignedCount)
                    .addComponent(lblUnassigned, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAssignedCount)
                    .addComponent(lblAssigned, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(lblPrivateLand, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPLLodged, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPLLodgedCount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPLForSurveyPlanCapturingCount)
                    .addComponent(lblPLForSurveyPlanCapturing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPLForStateLandClearanceCount)
                    .addComponent(lblPLForStateLandClearance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPLForEnvironmentClearanceCount)
                    .addComponent(lblPLForEnvironmentClearance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPLForPlanningClearanceCount)
                    .addComponent(lblPLForPlanningClearance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPLForCompletionCount)
                    .addComponent(lblPLForCompletion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPLForApprovalCount)
                    .addComponent(lblPLForApproval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPLApproved, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPLApprovedCount))
                .addGap(18, 18, 18)
                .addComponent(lblStateLand, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSLLodged, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSLLodgedCount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSLForSurveyPlanCapturingCount)
                    .addComponent(lblSLForSurveyPlanCapturing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSLForCompletionCount)
                    .addComponent(lblSLForCompletion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSLForApprovalCount)
                    .addComponent(lblSLForApproval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSLApproved, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSLApprovedCount))
                .addContainerGap(76, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void lblMyApplicationsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMyApplicationsMouseClicked
        selectLink(evt.getComponent());
    }//GEN-LAST:event_lblMyApplicationsMouseClicked

    private void lblUnassignedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblUnassignedMouseClicked
        selectLink(evt.getComponent());
    }//GEN-LAST:event_lblUnassignedMouseClicked

    private void lblAssignedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAssignedMouseClicked
        selectLink(evt.getComponent());
    }//GEN-LAST:event_lblAssignedMouseClicked

    private void lblPLLodgedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPLLodgedMouseClicked
        selectLink(evt.getComponent());
    }//GEN-LAST:event_lblPLLodgedMouseClicked

    private void lblPLForSurveyPlanCapturingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPLForSurveyPlanCapturingMouseClicked
        selectLink(evt.getComponent());
    }//GEN-LAST:event_lblPLForSurveyPlanCapturingMouseClicked

    private void lblPLForStateLandClearanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPLForStateLandClearanceMouseClicked
        selectLink(evt.getComponent());
    }//GEN-LAST:event_lblPLForStateLandClearanceMouseClicked

    private void lblPLForEnvironmentClearanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPLForEnvironmentClearanceMouseClicked
        selectLink(evt.getComponent());
    }//GEN-LAST:event_lblPLForEnvironmentClearanceMouseClicked

    private void lblPLForPlanningClearanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPLForPlanningClearanceMouseClicked
        selectLink(evt.getComponent());
    }//GEN-LAST:event_lblPLForPlanningClearanceMouseClicked

    private void lblPLForCompletionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPLForCompletionMouseClicked
        selectLink(evt.getComponent());
    }//GEN-LAST:event_lblPLForCompletionMouseClicked

    private void lblPLForApprovalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPLForApprovalMouseClicked
        selectLink(evt.getComponent());
    }//GEN-LAST:event_lblPLForApprovalMouseClicked

    private void lblSLLodgedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSLLodgedMouseClicked
        selectLink(evt.getComponent());
    }//GEN-LAST:event_lblSLLodgedMouseClicked

    private void lblSLForSurveyPlanCapturingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSLForSurveyPlanCapturingMouseClicked
        selectLink(evt.getComponent());
    }//GEN-LAST:event_lblSLForSurveyPlanCapturingMouseClicked

    private void lblSLForCompletionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSLForCompletionMouseClicked
        selectLink(evt.getComponent());
    }//GEN-LAST:event_lblSLForCompletionMouseClicked

    private void lblSLForApprovalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSLForApprovalMouseClicked
        selectLink(evt.getComponent());
    }//GEN-LAST:event_lblSLForApprovalMouseClicked

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        autoRefresh();
    }//GEN-LAST:event_formComponentShown

    private void btnRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshMouseClicked
        refreshApplications();
    }//GEN-LAST:event_btnRefreshMouseClicked

    private void btnOpenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenMouseClicked
        editApplication();
    }//GEN-LAST:event_btnOpenMouseClicked

    private void btnAssignMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAssignMouseClicked
        assignApplication();
    }//GEN-LAST:event_btnAssignMouseClicked

    private void btnUnassignMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUnassignMouseClicked
        unassignApplication();
    }//GEN-LAST:event_btnUnassignMouseClicked

    private void lblSLApprovedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSLApprovedMouseClicked
        selectLink(evt.getComponent());
    }//GEN-LAST:event_lblSLApprovedMouseClicked

    private void lblPLApprovedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPLApprovedMouseClicked
        selectLink(evt.getComponent());
    }//GEN-LAST:event_lblPLApprovedMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.beans.application.ApplicationSearchResultsListBean applicationSearchResults;
    private org.sola.clients.swing.common.buttons.BtnAssign btnAssign;
    private org.sola.clients.swing.common.buttons.BtnOpen btnOpen;
    private org.sola.clients.swing.common.buttons.BtnRefresh btnRefresh;
    private org.sola.clients.swing.common.buttons.BtnUnassign btnUnassign;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private org.sola.clients.swing.common.labels.HyperLink lblAssigned;
    private javax.swing.JLabel lblAssignedCount;
    private javax.swing.JLabel lblGeneral;
    private javax.swing.JLabel lblHeader;
    private org.sola.clients.swing.common.labels.HyperLink lblMyApplications;
    private javax.swing.JLabel lblMyApplicationsCount;
    private org.sola.clients.swing.common.labels.HyperLink lblPLApproved;
    private javax.swing.JLabel lblPLApprovedCount;
    private org.sola.clients.swing.common.labels.HyperLink lblPLForApproval;
    private javax.swing.JLabel lblPLForApprovalCount;
    private org.sola.clients.swing.common.labels.HyperLink lblPLForCompletion;
    private javax.swing.JLabel lblPLForCompletionCount;
    private org.sola.clients.swing.common.labels.HyperLink lblPLForEnvironmentClearance;
    private javax.swing.JLabel lblPLForEnvironmentClearanceCount;
    private org.sola.clients.swing.common.labels.HyperLink lblPLForPlanningClearance;
    private javax.swing.JLabel lblPLForPlanningClearanceCount;
    private org.sola.clients.swing.common.labels.HyperLink lblPLForStateLandClearance;
    private javax.swing.JLabel lblPLForStateLandClearanceCount;
    private org.sola.clients.swing.common.labels.HyperLink lblPLForSurveyPlanCapturing;
    private javax.swing.JLabel lblPLForSurveyPlanCapturingCount;
    private org.sola.clients.swing.common.labels.HyperLink lblPLLodged;
    private javax.swing.JLabel lblPLLodgedCount;
    private javax.swing.JLabel lblPrivateLand;
    private org.sola.clients.swing.common.labels.HyperLink lblSLApproved;
    private javax.swing.JLabel lblSLApprovedCount;
    private org.sola.clients.swing.common.labels.HyperLink lblSLForApproval;
    private javax.swing.JLabel lblSLForApprovalCount;
    private org.sola.clients.swing.common.labels.HyperLink lblSLForCompletion;
    private javax.swing.JLabel lblSLForCompletionCount;
    private org.sola.clients.swing.common.labels.HyperLink lblSLForSurveyPlanCapturing;
    private javax.swing.JLabel lblSLForSurveyPlanCapturingCount;
    private org.sola.clients.swing.common.labels.HyperLink lblSLLodged;
    private javax.swing.JLabel lblSLLodgedCount;
    private javax.swing.JLabel lblStateLand;
    private org.sola.clients.swing.common.labels.HyperLink lblUnassigned;
    private javax.swing.JLabel lblUnassignedCount;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tblApplications;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
