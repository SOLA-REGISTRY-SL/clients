/**
 * ******************************************************************************************
 * Copyright (C) 2014 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.clients.swing.gis.ui.control;

import com.vividsolutions.jts.geom.Coordinate;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.extended.layer.ExtendedLayer;
import org.geotools.swing.extended.Map;
import org.geotools.swing.extended.util.GeometryUtility;
import org.sola.clients.swing.gis.Messaging;
import org.sola.clients.swing.gis.beans.AbstractListSpatialBean;
import org.sola.clients.swing.gis.beans.SpatialBean;
import org.sola.clients.swing.gis.beans.SurveyPointBean;
import org.sola.clients.swing.gis.beans.SurveyPointListBean;
import org.sola.clients.swing.gis.layer.CadastreChangeNewSurveyPointLayer;
import org.sola.common.messaging.GisMessage;

/**
 * A User Interface component that handles the management of the survey points.
 *
 * @author Elton Manoku
 */
public class SurveyPointListPanel extends javax.swing.JPanel {

    private class CrsItem {

        private int srid = 0;
        private String name = "";

        public CrsItem(String name, int srid) {
            this.name = name;
            this.srid = srid;
        }

        public int getSrid() {
            return srid;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return name;
        }
    }

    private SurveyPointListBean theBean;
    private Map map;

    /**
     * This constructor must be used to initialize the bean.
     *
     * @param listBean
     * @param map Map control
     */
    public SurveyPointListPanel(SurveyPointListBean listBean, Map map) {
        this.theBean = listBean;
        this.map = map;

        initComponents();
        postInit();
    }

    private void postInit() {
        // Add a listner to the bean property of selected bean
        theBean.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(AbstractListSpatialBean.SELECTED_BEAN_PROPERTY)) {
                    customizeButtons((SpatialBean) evt.getNewValue());
                }
            }
        });
    }

    /**
     * It changes the availability of buttons based in the selected bean
     *
     * @param selectedSource
     */
    private void customizeButtons(SpatialBean selectedSource) {
        cmdRemove.setEnabled(selectedSource != null);
    }

    private void populateCrs() {
        cbxCrs.removeAllItems();
        cbxCrs.addItem(new CrsItem(
                map.getMapContent().getViewport().getCoordinateReferenceSystem().getName().getCode(),
                map.getSrid()));
        cbxCrs.addItem(new CrsItem("Colonial Grid System", 0));
        cbxCrs.setSelectedIndex(0);
    }

    /**
     * This constructor is only for the designer.
     */
    public SurveyPointListPanel() {
        initComponents();
    }

    /**
     * It creates the bean. It is called from the generated code.
     *
     * @return
     */
    private SurveyPointListBean createBean() {
        if (this.theBean == null) {
            return new SurveyPointListBean();
        }
        return this.theBean;
    }

    private void zoomToPoints() {
        ExtendedLayer pointLayer = map.getSolaLayer(CadastreChangeNewSurveyPointLayer.LAYER_NAME);
        if (pointLayer != null) {
            ReferencedEnvelope zoomArea = ((CadastreChangeNewSurveyPointLayer) pointLayer).getBounds();
            if (zoomArea != null) {
                zoomArea.expandBy(30);
                map.setDisplayArea(zoomArea);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        surveyPointListBean = createBean();
        urbanRural = new javax.swing.ButtonGroup();
        coordinateSysListBean = new org.sola.clients.beans.referencedata.CordinateSystemTypeListBean();
        cordinateSysTypeBean = new org.sola.clients.beans.referencedata.CordinateSystemTypeBean();
        txtY = new javax.swing.JTextField();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        txtX = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePointList = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbxCrs = new javax.swing.JComboBox();
        lblCrs = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                formAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/gis/ui/control/Bundle"); // NOI18N
        cmdAdd.setText(bundle.getString("SurveyPointListPanel.cmdAdd.text")); // NOI18N
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });

        cmdRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sola/clients/swing/gis/mapaction/resources/cadastre-redefinition-reset.png"))); // NOI18N
        cmdRemove.setText(bundle.getString("SurveyPointListPanel.cmdRemove.text")); // NOI18N
        cmdRemove.setToolTipText(bundle.getString("SurveyPointListPanel.cmdRemove.toolTipText")); // NOI18N
        cmdRemove.setEnabled(false);
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${beanList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, surveyPointListBean, eLProperty, tablePointList);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${id}"));
        columnBinding.setColumnName("Id");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${x}"));
        columnBinding.setColumnName("X");
        columnBinding.setColumnClass(Double.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${y}"));
        columnBinding.setColumnName("Y");
        columnBinding.setColumnClass(Double.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${boundary}"));
        columnBinding.setColumnName("Boundary");
        columnBinding.setColumnClass(Boolean.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${linked}"));
        columnBinding.setColumnName("Linked");
        columnBinding.setColumnClass(Boolean.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, surveyPointListBean, org.jdesktop.beansbinding.ELProperty.create("${selectedBean}"), tablePointList, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane1.setViewportView(tablePointList);
        if (tablePointList.getColumnModel().getColumnCount() > 0) {
            tablePointList.getColumnModel().getColumn(0).setMinWidth(40);
            tablePointList.getColumnModel().getColumn(0).setPreferredWidth(40);
            tablePointList.getColumnModel().getColumn(0).setMaxWidth(40);
            tablePointList.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("SurveyPointListPanel.tablePointList.columnModel.title0")); // NOI18N
            tablePointList.getColumnModel().getColumn(1).setPreferredWidth(20);
            tablePointList.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("SurveyPointListPanel.tablePointList.columnModel.title1")); // NOI18N
            tablePointList.getColumnModel().getColumn(2).setPreferredWidth(20);
            tablePointList.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("SurveyPointListPanel.tablePointList.columnModel.title2")); // NOI18N
            tablePointList.getColumnModel().getColumn(3).setPreferredWidth(20);
            tablePointList.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("SurveyPointListPanel.tablePointList.columnModel.title3")); // NOI18N
            tablePointList.getColumnModel().getColumn(4).setPreferredWidth(20);
            tablePointList.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("SurveyPointListPanel.tablePointList.columnModel.title4")); // NOI18N
        }

        jLabel3.setText(bundle.getString("SurveyPointListPanel.jLabel3.text")); // NOI18N

        jLabel4.setText(bundle.getString("SurveyPointListPanel.jLabel4.text")); // NOI18N

        lblCrs.setText(bundle.getString("SurveyPointListPanel.lblCrs.text")); // NOI18N

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/geotools/swing/tool/extended/resources/zoomin.png"))); // NOI18N
        jButton1.setText(bundle.getString("SurveyPointListPanel.jButton1.text")); // NOI18N
        jButton1.setToolTipText(bundle.getString("SurveyPointListPanel.jButton1.toolTipText")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdRemove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(lblCrs, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxCrs, 0, 155, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtX, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtY, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(cmdAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmdRemove)
                        .addComponent(lblCrs)
                        .addComponent(cbxCrs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)
                        .addComponent(cmdAdd)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        try {
            Double x = Double.valueOf(this.txtX.getText());
            Double y = Double.valueOf(this.txtY.getText());

            if (cbxCrs.getSelectedIndex() == 1) {
                // Colonial is selected
                Coordinate coord;
                if (((CrsItem) cbxCrs.getItemAt(0)).getSrid() == 32628) {
                    coord = GeometryUtility.convertFromColonialToUTM28(x, y);
                } else {
                    coord = GeometryUtility.convertFromColonialToUTM29(x, y);
                }
                if (coord != null) {
                    x = coord.x;
                    y = coord.y;
                }
            }

            SurveyPointBean bean = new SurveyPointBean();
            bean.setSrid(((CrsItem) cbxCrs.getItemAt(0)).getSrid());
            bean.setX(x);
            bean.setY(y);
            this.theBean.getBeanList().add(bean);

            txtX.setText("");
            txtY.setText("");

        } catch (NumberFormatException ex) {
            Messaging.getInstance().show(GisMessage.CADASTRE_SURVEY_ADD_POINT);
        }
    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        if (theBean.getSelectedBean() != null) {
            theBean.getBeanList().remove((SurveyPointBean) theBean.getSelectedBean());
            theBean.setSelectedBean(null);
        }
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void formAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_formAncestorAdded
        populateCrs();
    }//GEN-LAST:event_formAncestorAdded

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        zoomToPoints();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbxCrs;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdRemove;
    private org.sola.clients.beans.referencedata.CordinateSystemTypeListBean coordinateSysListBean;
    private org.sola.clients.beans.referencedata.CordinateSystemTypeBean cordinateSysTypeBean;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCrs;
    private org.sola.clients.swing.gis.beans.SurveyPointListBean surveyPointListBean;
    private javax.swing.JTable tablePointList;
    private javax.swing.JTextField txtX;
    private javax.swing.JTextField txtY;
    private javax.swing.ButtonGroup urbanRural;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
