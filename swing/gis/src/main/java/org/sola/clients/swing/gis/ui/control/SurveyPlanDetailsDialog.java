package org.sola.clients.swing.gis.ui.control;

import java.awt.GridLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.validation.groups.Default;
import org.sola.clients.beans.party.PartySearchResultListBean;
import org.sola.clients.beans.referencedata.ChiefdomTypeListBean;
import org.sola.clients.beans.referencedata.LandTypeBean;
import org.sola.clients.beans.referencedata.LandTypeListBean;
import org.sola.clients.beans.referencedata.PartyRoleTypeBean;
import org.sola.clients.beans.referencedata.RequestTypeBean;
import org.sola.clients.beans.referencedata.SurveyTypeListBean;
import org.sola.clients.beans.referencedata.SurveyingMethodTypeListBean;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.utils.FormattersFactory;
import org.sola.clients.swing.gis.beans.CadastreObjectBean;
import org.sola.clients.swing.gis.beans.validation.PrivateLandValidationGroup;
import org.sola.clients.swing.gis.beans.validation.StateLandValidationGroup;
import org.sola.common.StringUtility;

/**
 * Survey plan details form
 */
public class SurveyPlanDetailsDialog extends javax.swing.JDialog {

    /**
     * Creates new form
     */
    private CadastreObjectBean originalCadastreObject;
    private boolean autoAreaCalc = false;

    public SurveyPlanDetailsDialog(java.awt.Frame parent, boolean modal,
            CadastreObjectBean cadastreObject, String requestTypeCode, boolean readOnly) {
        super(parent, modal);
        if (StringUtility.isEmpty(cadastreObject.getLandTypeCode())) {
            if (JOptionPane.showConfirmDialog(this, "Do you want to create private land?",
                    "Land type", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                cadastreObject.setLandTypeCode(LandTypeBean.TYPE_PRIVATE_LAND);
            } else {
                cadastreObject.setLandTypeCode(LandTypeBean.TYPE_STATE_LAND);
                cadastreObject.setNameLastpart(null);
            }
        }
        this.originalCadastreObject = cadastreObject;
        this.cadastreObjectBean = cadastreObject.copy();
//        autoAreaCalc = requestTypeCode.equals(RequestTypeBean.CODE_EXISTING_PARCEL);
        autoAreaCalc = requestTypeCode.equals(RequestTypeBean.CODE_NEW_PARCEL);
        initComponents();
        setReadOnly(!readOnly);
        postInit();
    }

    private void postInit() {
        // Show/hide panels depending on land type
        if (isPrivateLand()) {
            pnlMainGrid.remove(pnlSurveyNumCorrFile);
            pnlMainGrid.remove(pnlCompFileDwgOffNum);
            pnlMainGrid.remove(pnlDrawnBy);
            pnlMainGrid.remove(pnlCheckedByAndDate);

            ((GridLayout) pnlMainGrid.getLayout()).setRows(8);
            this.setSize((int) this.getSize().getWidth(), (int) this.getSize().getHeight() - 120);
        } else {
            pnlMainGrid.remove(pnlStateLandOfficer);
            pnlMainGrid.remove(pnlChartingOfficer);
            pnlParcelNumber.remove(pnlNameLastPart);
            lblSurveyor.setText("Surveyor");

            ((GridLayout) pnlMainGrid.getLayout()).setRows(9);
            this.setSize((int) this.getSize().getWidth(), (int) this.getSize().getHeight() - 60);
        }
    }

    private void setReadOnly(boolean enabled) {
        txtNameFirstPart.setEnabled(enabled);
        txtNameLastPart.setEnabled(enabled);
        txtOwnerName.setEnabled(enabled);
        txtAddress1.setEnabled(enabled);
        cbxAddress2.setEnabled(enabled);
        txtSurveyDate.setEnabled(enabled);
        txtParcelArea.setEnabled(enabled && !autoAreaCalc);
        txtBeaconNumber.setEnabled(enabled);
        //cbxLandType.setEnabled(enabled);
        cbxSurveyMethod.setEnabled(enabled);
        cbxLicensedSurveyor.setEnabled(enabled);
        txtNeighbourEast.setEnabled(enabled);
        txtNeighbourNorth.setEnabled(enabled);
        txtNeighbourSouth.setEnabled(enabled);
        txtNeighbourWest.setEnabled(enabled);
        cbxChartingOfficer.setEnabled(enabled);
        cbxClearingStateLandOfficer.setEnabled(enabled);
        cbxSurveyType.setEnabled(enabled);
        txtSurveyNumber.setEnabled(enabled);
        txtRefNameFirstPart.setEnabled(enabled);
        txtRefNameLastPart.setEnabled(enabled);
        txtCorrespondenceFile.setEnabled(enabled);
        txtComputationFile.setEnabled(enabled);
        txtCheckedBy.setEnabled(enabled);
        txtCheckingDate.setEnabled(enabled);
        txtDrawingOfficeNumber.setEnabled(enabled);
        txtDrawnBy.setEnabled(enabled);
        btnSurveyDate.setEnabled(enabled);
        btnCheckingDate.setEnabled(enabled);
        btnSave.setVisible(enabled);
    }

    /**
     * It creates the bean. It is called from the generated code.
     *
     * @return
     */
    private CadastreObjectBean createBean() {
        if (cadastreObjectBean == null) {
            return new CadastreObjectBean();
        }
        return cadastreObjectBean;
    }

    private LandTypeListBean createLandTypeList() {
        if (landTypeListBean == null) {
            landTypeListBean = new LandTypeListBean(true);
        }
        return landTypeListBean;
    }

    private SurveyingMethodTypeListBean createSurveyMethodTypeList() {
        if (surveyingMethodTypeListBean == null) {
            surveyingMethodTypeListBean = new SurveyingMethodTypeListBean(true);
        }
        return surveyingMethodTypeListBean;
    }

    private ChiefdomTypeListBean createChiefdomTypeList() {
        if (chiefdomTypeListBean == null) {
            chiefdomTypeListBean = new ChiefdomTypeListBean(true);
        }
        return chiefdomTypeListBean;
    }

    private PartySearchResultListBean createLicensedSurveyorsList() {
        if (licensedSurveyors == null) {
            licensedSurveyors = new PartySearchResultListBean();
            if (isPrivateLand()) {
                licensedSurveyors.searchByRole(PartyRoleTypeBean.ROLE_LICENSED_SURVEYOR, true);
            } else {
                licensedSurveyors.searchByRole(PartyRoleTypeBean.ROLE_SURVEYOR, true);
            }
        }
        return licensedSurveyors;
    }

    private boolean isPrivateLand() {
        if (!StringUtility.isEmpty(cadastreObjectBean.getLandTypeCode())) {
            return cadastreObjectBean.getLandTypeCode().equalsIgnoreCase(LandTypeBean.TYPE_PRIVATE_LAND);
        }
        return true;
    }

    private PartySearchResultListBean createChartingOfficersList() {
        if (chartingOfficers == null) {
            chartingOfficers = new PartySearchResultListBean();
            chartingOfficers.searchByRole(PartyRoleTypeBean.ROLE_CHARTING_OFFICER, true);
        }
        return chartingOfficers;
    }

    private PartySearchResultListBean createClearingStateLandOfficiersList() {
        if (clearingStateLandOfficers == null) {
            clearingStateLandOfficers = new PartySearchResultListBean();
            clearingStateLandOfficers.searchByRole(PartyRoleTypeBean.ROLE_CLEARING_STATE_LAND_OFFICER, true);
        }
        return clearingStateLandOfficers;
    }

    private SurveyTypeListBean createSurveyTypesList() {
        if (surveyTypeListBean == null) {
            surveyTypeListBean = new SurveyTypeListBean(true);
        }
        return surveyTypeListBean;
    }

    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }

    private boolean save() {
        if (isPrivateLand()) {
            if (cadastreObjectBean.validate(true, Default.class, PrivateLandValidationGroup.class).size() > 0) {
                return false;
            }
        } else if (cadastreObjectBean.validate(true, Default.class, StateLandValidationGroup.class).size() > 0) {
            return false;
        }
        
        originalCadastreObject.copyFromObject(cadastreObjectBean);
        return true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        cadastreObjectBean = createBean();
        landTypeListBean = createLandTypeList();
        chiefdomTypeListBean = createChiefdomTypeList();
        surveyingMethodTypeListBean = createSurveyMethodTypeList();
        licensedSurveyors = createLicensedSurveyorsList();
        chartingOfficers = createChartingOfficersList();
        clearingStateLandOfficers = createClearingStateLandOfficiersList();
        surveyTypeListBean = createSurveyTypesList();
        btnSave = new javax.swing.JButton();
        pnlMainGrid = new javax.swing.JPanel();
        pnlParcelNumber = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNameFirstPart = new javax.swing.JTextField();
        pnlNameLastPart = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNameLastPart = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtOwnerName = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtAddress1 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cbxAddress2 = new javax.swing.JComboBox();
        jPanel20 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        btnSurveyDate = new javax.swing.JButton();
        txtSurveyDate = new org.sola.clients.swing.common.controls.WatermarkDate();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtParcelArea = new javax.swing.JFormattedTextField();
        jPanel21 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtBeaconNumber = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cbxLandType = new javax.swing.JComboBox();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cbxSurveyMethod = new javax.swing.JComboBox();
        jPanel17 = new javax.swing.JPanel();
        lblSurveyor = new javax.swing.JLabel();
        cbxLicensedSurveyor = new javax.swing.JComboBox();
        jPanel22 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        cbxSurveyType = new javax.swing.JComboBox();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        txtRefNameFirstPart = new javax.swing.JTextField();
        jPanel25 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtRefNameLastPart = new javax.swing.JTextField();
        pnlChartingOfficer = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        cbxChartingOfficer = new javax.swing.JComboBox();
        pnlStateLandOfficer = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        cbxClearingStateLandOfficer = new javax.swing.JComboBox();
        pnlSurveyNumCorrFile = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        txtSurveyNumber = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtCorrespondenceFile = new javax.swing.JTextField();
        pnlCompFileDwgOffNum = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        txtComputationFile = new javax.swing.JTextField();
        jPanel31 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        txtDrawingOfficeNumber = new javax.swing.JTextField();
        pnlDrawnBy = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        txtDrawnBy = new javax.swing.JTextField();
        pnlCheckedByAndDate = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        btnCheckingDate = new javax.swing.JButton();
        txtCheckingDate = new org.sola.clients.swing.common.controls.WatermarkDate();
        jLabel17 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        txtCheckedBy = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtNeighbourNorth = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtNeighbourSouth = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtNeighbourWest = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtNeighbourEast = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Survey plan details");
        setMaximumSize(new java.awt.Dimension(665, 433));

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        pnlMainGrid.setMaximumSize(new java.awt.Dimension(647, 370));
        pnlMainGrid.setLayout(new java.awt.GridLayout(10, 2, 15, 15));

        pnlParcelNumber.setLayout(new java.awt.GridLayout(1, 2, 10, 0));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel1.setText("Name first part:");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${nameFirstpart}"), txtNameFirstPart, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 59, Short.MAX_VALUE))
            .addComponent(txtNameFirstPart)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNameFirstPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlParcelNumber.add(jPanel1);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel2.setText("Name last part:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${nameLastpart}"), txtNameLastPart, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout pnlNameLastPartLayout = new javax.swing.GroupLayout(pnlNameLastPart);
        pnlNameLastPart.setLayout(pnlNameLastPartLayout);
        pnlNameLastPartLayout.setHorizontalGroup(
            pnlNameLastPartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNameLastPartLayout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 61, Short.MAX_VALUE))
            .addComponent(txtNameLastPart)
        );
        pnlNameLastPartLayout.setVerticalGroup(
            pnlNameLastPartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNameLastPartLayout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNameLastPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlParcelNumber.add(pnlNameLastPart);

        pnlMainGrid.add(pnlParcelNumber);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel3.setText("Owner name:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${ownerName}"), txtOwnerName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 230, Short.MAX_VALUE))
            .addComponent(txtOwnerName)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtOwnerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlMainGrid.add(jPanel3);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel6.setText("Address1");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${address}"), txtAddress1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(txtAddress1)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlMainGrid.add(jPanel6);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel4.setText("Address2:");

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${chiefdomTypeListBean}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, chiefdomTypeListBean, eLProperty, cbxAddress2);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${chiefdomType}"), cbxAddress2, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 246, Short.MAX_VALUE))
            .addComponent(cbxAddress2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlMainGrid.add(jPanel4);

        jPanel20.setLayout(new java.awt.GridLayout(1, 2, 10, 0));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/ui/source/Bundle"); // NOI18N
        jLabel14.setText(bundle.getString("DocumentPanel.jLabel10.text")); // NOI18N

        btnSurveyDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnSurveyDate.setText(bundle.getString("DocumentPanel.btnExpirationDate.text")); // NOI18N
        btnSurveyDate.setBorder(null);
        btnSurveyDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSurveyDateActionPerformed(evt);
            }
        });

        txtSurveyDate.setText(bundle.getString("DocumentPanel.txtExpiration.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${surveyDate}"), txtSurveyDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(txtSurveyDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSurveyDate))
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSurveyDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSurveyDate)))
        );

        jPanel20.add(jPanel14);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel7.setText("Parcel area (acres)");

        txtParcelArea.setFormatterFactory(FormattersFactory.getInstance().getDoubleFormatterFactory(3));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${parcelAreaAcres}"), txtParcelArea, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addGap(0, 44, Short.MAX_VALUE))
            .addComponent(txtParcelArea)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtParcelArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel20.add(jPanel7);

        pnlMainGrid.add(jPanel20);

        jLabel9.setText("Beacon No:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${beaconNumber}"), txtBeaconNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addGap(0, 40, Short.MAX_VALUE))
            .addComponent(txtBeaconNumber)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBeaconNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel5.setText("Land type:");

        cbxLandType.setEnabled(false);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${landTypeList}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, landTypeListBean, eLProperty, cbxLandType);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${landType}"), cbxLandType, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(0, 138, Short.MAX_VALUE))
            .addComponent(cbxLandType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxLandType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pnlMainGrid.add(jPanel21);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel8.setText("Surveying method:");

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${surveyingMethodTypeList}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, surveyingMethodTypeListBean, eLProperty, cbxSurveyMethod);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${surveyMethod}"), cbxSurveyMethod, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addGap(0, 204, Short.MAX_VALUE))
            .addComponent(cbxSurveyMethod, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxSurveyMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlMainGrid.add(jPanel8);

        lblSurveyor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        lblSurveyor.setText("Licensed surveyor:");

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${partySearchResults}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, licensedSurveyors, eLProperty, cbxLicensedSurveyor);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${licensedSurveyor}"), cbxLicensedSurveyor, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(lblSurveyor)
                .addGap(0, 204, Short.MAX_VALUE))
            .addComponent(cbxLicensedSurveyor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(lblSurveyor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxLicensedSurveyor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlMainGrid.add(jPanel17);

        jLabel18.setText("Survey/Service type:");

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${surveyTypes}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, surveyTypeListBean, eLProperty, cbxSurveyType);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${surveyType}"), cbxSurveyType, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jLabel18)
                .addGap(0, 207, Short.MAX_VALUE))
            .addComponent(cbxSurveyType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxSurveyType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlMainGrid.add(jPanel22);

        jPanel23.setLayout(new java.awt.GridLayout(1, 2, 10, 0));

        jLabel19.setText("Ref. Name first part:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${refNameFirstpart}"), txtRefNameFirstPart, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jLabel19)
                .addGap(0, 49, Short.MAX_VALUE))
            .addComponent(txtRefNameFirstPart)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRefNameFirstPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel23.add(jPanel24);

        jLabel20.setText("Ref. name last part:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${refNameLastpart}"), txtRefNameLastPart, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(jLabel20)
                .addGap(0, 52, Short.MAX_VALUE))
            .addComponent(txtRefNameLastPart)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRefNameLastPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel23.add(jPanel25);

        pnlMainGrid.add(jPanel23);

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel15.setText("Charting officer:");

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${partySearchResults}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, chartingOfficers, eLProperty, cbxChartingOfficer);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${chartingOfficer}"), cbxChartingOfficer, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout pnlChartingOfficerLayout = new javax.swing.GroupLayout(pnlChartingOfficer);
        pnlChartingOfficer.setLayout(pnlChartingOfficerLayout);
        pnlChartingOfficerLayout.setHorizontalGroup(
            pnlChartingOfficerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChartingOfficerLayout.createSequentialGroup()
                .addComponent(jLabel15)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(cbxChartingOfficer, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlChartingOfficerLayout.setVerticalGroup(
            pnlChartingOfficerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChartingOfficerLayout.createSequentialGroup()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxChartingOfficer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlMainGrid.add(pnlChartingOfficer);

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel16.setText("State land clearing officer:");

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${partySearchResults}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, clearingStateLandOfficers, eLProperty, cbxClearingStateLandOfficer);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${stateLandClearingOfficer}"), cbxClearingStateLandOfficer, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout pnlStateLandOfficerLayout = new javax.swing.GroupLayout(pnlStateLandOfficer);
        pnlStateLandOfficer.setLayout(pnlStateLandOfficerLayout);
        pnlStateLandOfficerLayout.setHorizontalGroup(
            pnlStateLandOfficerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStateLandOfficerLayout.createSequentialGroup()
                .addComponent(jLabel16)
                .addGap(0, 168, Short.MAX_VALUE))
            .addComponent(cbxClearingStateLandOfficer, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlStateLandOfficerLayout.setVerticalGroup(
            pnlStateLandOfficerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStateLandOfficerLayout.createSequentialGroup()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxClearingStateLandOfficer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlMainGrid.add(pnlStateLandOfficer);

        pnlSurveyNumCorrFile.setLayout(new java.awt.GridLayout(1, 2, 10, 0));

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel21.setText("Survey number:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${surveyNumber}"), txtSurveyNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jLabel21)
                .addGap(0, 58, Short.MAX_VALUE))
            .addComponent(txtSurveyNumber)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSurveyNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlSurveyNumCorrFile.add(jPanel27);

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel22.setText("Correspondence file No:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${correspondenceFile}"), txtCorrespondenceFile, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(jLabel22)
                .addGap(0, 19, Short.MAX_VALUE))
            .addComponent(txtCorrespondenceFile)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCorrespondenceFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlSurveyNumCorrFile.add(jPanel28);

        pnlMainGrid.add(pnlSurveyNumCorrFile);

        pnlCompFileDwgOffNum.setLayout(new java.awt.GridLayout(1, 2, 10, 0));

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel23.setText("Computation file No:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${computationFile}"), txtComputationFile, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addComponent(jLabel23)
                .addGap(0, 37, Short.MAX_VALUE))
            .addComponent(txtComputationFile)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtComputationFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlCompFileDwgOffNum.add(jPanel30);

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel24.setText("Drawing office number:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${dwgOffNumber}"), txtDrawingOfficeNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addComponent(jLabel24)
                .addGap(0, 23, Short.MAX_VALUE))
            .addComponent(txtDrawingOfficeNumber)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDrawingOfficeNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlCompFileDwgOffNum.add(jPanel31);

        pnlMainGrid.add(pnlCompFileDwgOffNum);

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel25.setText("Drawn by:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${drawnBy}"), txtDrawnBy, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout pnlDrawnByLayout = new javax.swing.GroupLayout(pnlDrawnBy);
        pnlDrawnBy.setLayout(pnlDrawnByLayout);
        pnlDrawnByLayout.setHorizontalGroup(
            pnlDrawnByLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDrawnByLayout.createSequentialGroup()
                .addComponent(jLabel25)
                .addGap(0, 245, Short.MAX_VALUE))
            .addComponent(txtDrawnBy)
        );
        pnlDrawnByLayout.setVerticalGroup(
            pnlDrawnByLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDrawnByLayout.createSequentialGroup()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDrawnBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlMainGrid.add(pnlDrawnBy);

        btnCheckingDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnCheckingDate.setText(bundle.getString("DocumentPanel.btnExpirationDate.text")); // NOI18N
        btnCheckingDate.setBorder(null);
        btnCheckingDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckingDateActionPerformed(evt);
            }
        });

        txtCheckingDate.setText(bundle.getString("DocumentPanel.txtExpiration.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${checkingDate}"), txtCheckingDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel17.setText("Checking date:");

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addComponent(txtCheckingDate, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCheckingDate))
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addComponent(jLabel17)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCheckingDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCheckingDate)))
        );

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/ui/control/red_asterisk.gif"))); // NOI18N
        jLabel27.setText("Checked by:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${checkedBy}"), txtCheckedBy, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addComponent(jLabel27)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(txtCheckedBy)
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCheckedBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout pnlCheckedByAndDateLayout = new javax.swing.GroupLayout(pnlCheckedByAndDate);
        pnlCheckedByAndDate.setLayout(pnlCheckedByAndDateLayout);
        pnlCheckedByAndDateLayout.setHorizontalGroup(
            pnlCheckedByAndDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCheckedByAndDateLayout.createSequentialGroup()
                .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlCheckedByAndDateLayout.setVerticalGroup(
            pnlCheckedByAndDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pnlMainGrid.add(pnlCheckedByAndDate);

        jLabel10.setText("Neighbour at north:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${northNeighbour}"), txtNeighbourNorth, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel10)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(txtNeighbourNorth)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNeighbourNorth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlMainGrid.add(jPanel10);

        jLabel11.setText("Neighbour at south:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${southNeighbour}"), txtNeighbourSouth, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addGap(0, 213, Short.MAX_VALUE))
            .addComponent(txtNeighbourSouth)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNeighbourSouth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlMainGrid.add(jPanel11);

        jLabel13.setText("Neighbour at west:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${westNeighbour}"), txtNeighbourWest, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addGap(0, 217, Short.MAX_VALUE))
            .addComponent(txtNeighbourWest)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNeighbourWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlMainGrid.add(jPanel13);

        jLabel12.setText("Neighbour at east:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectBean, org.jdesktop.beansbinding.ELProperty.create("${eastNeighbour}"), txtNeighbourEast, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel12)
                .addGap(0, 219, Short.MAX_VALUE))
            .addComponent(txtNeighbourEast)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNeighbourEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlMainGrid.add(jPanel12);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlMainGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 634, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlMainGrid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSave)
                .addContainerGap())
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (save()) {
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSurveyDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSurveyDateActionPerformed
        showCalendar(txtSurveyDate);
    }//GEN-LAST:event_btnSurveyDateActionPerformed

    private void btnCheckingDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckingDateActionPerformed
        showCalendar(txtCheckingDate);
    }//GEN-LAST:event_btnCheckingDateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCheckingDate;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSurveyDate;
    private org.sola.clients.swing.gis.beans.CadastreObjectBean cadastreObjectBean;
    private javax.swing.JComboBox<String> cbxAddress2;
    private javax.swing.JComboBox<String> cbxChartingOfficer;
    private javax.swing.JComboBox<String> cbxClearingStateLandOfficer;
    private javax.swing.JComboBox<String> cbxLandType;
    private javax.swing.JComboBox<String> cbxLicensedSurveyor;
    private javax.swing.JComboBox<String> cbxSurveyMethod;
    private javax.swing.JComboBox<String> cbxSurveyType;
    private org.sola.clients.beans.party.PartySearchResultListBean chartingOfficers;
    private org.sola.clients.beans.referencedata.ChiefdomTypeListBean chiefdomTypeListBean;
    private org.sola.clients.beans.party.PartySearchResultListBean clearingStateLandOfficers;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private org.sola.clients.beans.referencedata.LandTypeListBean landTypeListBean;
    private javax.swing.JLabel lblSurveyor;
    private org.sola.clients.beans.party.PartySearchResultListBean licensedSurveyors;
    private javax.swing.JPanel pnlChartingOfficer;
    private javax.swing.JPanel pnlCheckedByAndDate;
    private javax.swing.JPanel pnlCompFileDwgOffNum;
    private javax.swing.JPanel pnlDrawnBy;
    private javax.swing.JPanel pnlMainGrid;
    private javax.swing.JPanel pnlNameLastPart;
    private javax.swing.JPanel pnlParcelNumber;
    private javax.swing.JPanel pnlStateLandOfficer;
    private javax.swing.JPanel pnlSurveyNumCorrFile;
    private org.sola.clients.beans.referencedata.SurveyTypeListBean surveyTypeListBean;
    private org.sola.clients.beans.referencedata.SurveyingMethodTypeListBean surveyingMethodTypeListBean;
    private javax.swing.JTextField txtAddress1;
    private javax.swing.JTextField txtBeaconNumber;
    private javax.swing.JTextField txtCheckedBy;
    public org.sola.clients.swing.common.controls.WatermarkDate txtCheckingDate;
    private javax.swing.JTextField txtComputationFile;
    private javax.swing.JTextField txtCorrespondenceFile;
    private javax.swing.JTextField txtDrawingOfficeNumber;
    private javax.swing.JTextField txtDrawnBy;
    private javax.swing.JTextField txtNameFirstPart;
    private javax.swing.JTextField txtNameLastPart;
    private javax.swing.JTextField txtNeighbourEast;
    private javax.swing.JTextField txtNeighbourNorth;
    private javax.swing.JTextField txtNeighbourSouth;
    private javax.swing.JTextField txtNeighbourWest;
    private javax.swing.JTextField txtOwnerName;
    private javax.swing.JFormattedTextField txtParcelArea;
    private javax.swing.JTextField txtRefNameFirstPart;
    private javax.swing.JTextField txtRefNameLastPart;
    public org.sola.clients.swing.common.controls.WatermarkDate txtSurveyDate;
    private javax.swing.JTextField txtSurveyNumber;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
