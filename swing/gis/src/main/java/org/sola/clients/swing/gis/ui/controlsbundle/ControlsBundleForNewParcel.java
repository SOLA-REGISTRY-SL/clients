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
package org.sola.clients.swing.gis.ui.controlsbundle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.swing.extended.exception.InitializeLayerException;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.observablecollections.ObservableListListener;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.referencedata.CadastreObjectTypeBean;
import org.sola.clients.swing.gis.beans.TransactionCadastreChangeBean;
import org.sola.clients.swing.gis.data.PojoDataAccess;
import org.sola.clients.swing.gis.layer.CadastreChangeNewCadastreObjectLayer;
import org.sola.clients.swing.gis.layer.CadastreChangeNewSurveyPointLayer;
import org.sola.clients.swing.gis.mapaction.CadastreChangePointSurveyListFormShow;
import org.sola.clients.swing.gis.mapaction.RemoveCadastreObjects;
import org.sola.clients.swing.gis.mapaction.SurveyPlanDetails;
import org.sola.clients.swing.gis.tool.CadastreChangeNewCadastreObjectTool;
import org.sola.clients.swing.gis.tool.CadastreChangeNodeTool;

/**
 * A control bundle that is used for creating new parcel. The necessary tools
 * and layers are added in the bundle.
 *
 */
public final class ControlsBundleForNewParcel extends ControlsBundleForTransaction {

    private TransactionCadastreChangeBean transactionBean;
    private CadastreChangeNewCadastreObjectLayer newCadastreObjectLayer = null;
    private CadastreChangeNewSurveyPointLayer newPointsLayer = null;
    private String lastPartEntry = "";
    private CadastreChangeNewCadastreObjectTool newCadastreObjectTool;
    private String lastPartTemplate = "SP %s";

    /**
     * Constructor. It sets up the bundle by adding layers and tools that are
     * relevant. Finally, it zooms in the interested zone. The interested zone
     * is defined in the following order: <br/> If bean has survey points it is
     * zoomed there, otherwise if baUnitId is present it is zoomed there else it
     * is zoomed in the application location.
     *
     * @param applicationBean The application where the transaction is started
     * identifiers
     * @param transactionStarterId The id of the starter of the application.
     * This will be the service id.
     */
    public ControlsBundleForNewParcel(
            ApplicationBean applicationBean,
            String transactionStarterId) {
        super(applicationBean, transactionStarterId);
        this.lastPartEntry = applicationBean.getNr();
        postInit();
    }

    private void postInit() {
        this.refreshTransactionFromServer();
        Integer srid = null;
        if (transactionBean.getCadastreObjectList() != null && transactionBean.getCadastreObjectList().size() > 0) {
            srid = transactionBean.getCadastreObjectList().get(0).getFeatureGeom().getSRID();
        }

        this.Setup(PojoDataAccess.getInstance(), srid);
        this.setTargetCadastreObjectTypeConfiguration(CadastreObjectTypeBean.CODE_PARCEL);
        this.setTransaction();

        this.zoomToInterestingArea(null, applicationBean.getLocation());

        // Listen to cadastre objects list events
        ((SolaObservableList) this.newCadastreObjectLayer.getBeanList())
                .addObservableListListener(new ObservableListListener() {
                    @Override
                    public void listElementsAdded(ObservableList list, int index, int length) {
                        getMap().getMapActionByName(CadastreChangeNewCadastreObjectTool.NAME).setEnabled(false);
                        getMap().getMapActionByName(RemoveCadastreObjects.NAME).setEnabled(true);
                        getMap().getMapActionByName(SurveyPlanDetails.MAPACTION_NAME).setEnabled(true);
                        getCrsControl().setEnabled(false);
                    }

                    @Override
                    public void listElementsRemoved(ObservableList list, int index, List oldElements) {
                        getMap().getMapActionByName(CadastreChangeNewCadastreObjectTool.NAME).setEnabled(list.size() < 1);
                        getMap().getMapActionByName(RemoveCadastreObjects.NAME).setEnabled(list.size() > 0);
                        getMap().getMapActionByName(SurveyPlanDetails.MAPACTION_NAME).setEnabled(list.size() > 0);
                        getCrsControl().setEnabled(list.size() < 1);
                    }

                    @Override
                    public void listElementReplaced(ObservableList list, int index, Object oldElement) {
                    }

                    @Override
                    public void listElementPropertyChanged(ObservableList list, int index) {
                    }
                });
        
        ((CadastreChangeNewCadastreObjectTool)(this.getMap()
                .getMapActionByName(CadastreChangeNewCadastreObjectTool.NAME)
                .getAttachedTool())).addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName()
                            .equalsIgnoreCase(CadastreChangeNewCadastreObjectTool.FEATURE_ADDED_PROPERTY) &&
                            newCadastreObjectLayer.getBeanList().size() > 0){
                        getMap().getMapActionByName(SurveyPlanDetails.MAPACTION_NAME).onClick();
                    }
            }
        });
    }

    @Override
    protected boolean transactionIsStarted() {
        return (this.newPointsLayer.getFeatureCollection().size() > 0);
    }

    /**
     * It zooms to the interesting area which is the area where the cadastre
     * changes is happening
     *
     * @param interestingArea
     * @param applicationLocation
     */
    @Override
    protected void zoomToInterestingArea(
            ReferencedEnvelope interestingArea, byte[] applicationLocation) {
        ReferencedEnvelope boundsToZoom = null;
        if (this.newPointsLayer.getFeatureCollection().size() > 0) {
            boundsToZoom = this.newPointsLayer.getFeatureCollection().getBounds();
        }
        super.zoomToInterestingArea(boundsToZoom, applicationLocation);
    }

    @Override
    public TransactionCadastreChangeBean getTransactionBean() {
        transactionBean.setCadastreObjectList(
                this.newCadastreObjectLayer.getBeanListForTransaction());
        transactionBean.setSurveyPointList(this.newPointsLayer.getBeanListForTransaction());
        if (this.getDocumentsPanel() != null) {
            transactionBean.setSourceIdList(this.getDocumentsPanel().getSourceIds());
        }
        return transactionBean;
    }

    @Override
    public void refreshTransactionFromServer() {
        if (this.getTransactionStarterId() != null) {
            this.transactionBean = PojoDataAccess.getInstance().getTransactionCadastreChange(
                    this.getTransactionStarterId());
        } else if (this.transactionBean != null) {
            this.transactionBean = PojoDataAccess.getInstance().getTransactionCadastreChangeById(
                    this.transactionBean.getId());
        } else {
            this.transactionBean = new TransactionCadastreChangeBean();
        }
    }

    @Override
    public final void setTransaction() {
        //Reset the lists of beans in the layers
        this.newCadastreObjectLayer.getBeanList().clear();
        this.newPointsLayer.getBeanList().clear();
        //Populate the lists of beans from the lists in transaction
        this.newCadastreObjectLayer.setBeanList(
                this.transactionBean.getCadastreObjectList());
        this.newPointsLayer.setBeanList(this.transactionBean.getSurveyPointList());
        if (this.getDocumentsPanel() != null) {
            this.getDocumentsPanel().setSourceIds(this.transactionBean.getSourceIdList());
        }
    }

    @Override
    protected void addLayers() throws InitializeLayerException, SchemaException {
        super.addLayers();

        this.newCadastreObjectLayer = new CadastreChangeNewCadastreObjectLayer(getLastPart(this.lastPartEntry));
        //This flag defines if the client side generator or server side generator
        // will be used for new cadastre object identifier.
        // If it is false the server side generator will be used. Then
        // the database functions: cadastre.get_new_cadastre_object_identifier_last_part
        // and cadastre.get_new_cadastre_object_identifier_first_part has to be updated
        // to reflect the algorithm that is used for generating the identifier.
        this.newCadastreObjectLayer.setUseClientSideIdentifierGenerator(true);
        this.getMap().addLayer(newCadastreObjectLayer);

        this.newPointsLayer = new CadastreChangeNewSurveyPointLayer(this.newCadastreObjectLayer);
        this.getMap().addLayer(newPointsLayer);
    }

    @Override
    protected void addToolsAndCommands() {
        this.getMap().addMapAction(
                new CadastreChangePointSurveyListFormShow(
                        this.getMap(), this.newPointsLayer.getHostForm()),
                this.getToolbar(),
                true);

        CadastreChangeNodeTool nodelinkingTool = new CadastreChangeNodeTool(newPointsLayer);
        //nodelinkingTool.getTargetSnappingLayers().add(this.targetParcelsLayer);
        this.addSnappingLayerToTool(nodelinkingTool, PARCEL_LAYER_NAME);
        this.addSnappingLayerToTool(nodelinkingTool, PARCEL_PENDING_LAYER_NAME);
        this.getMap().addTool(nodelinkingTool, this.getToolbar(), true);

        this.newCadastreObjectTool = new CadastreChangeNewCadastreObjectTool(this.newCadastreObjectLayer);
        this.newCadastreObjectTool.getTargetSnappingLayers().add(newPointsLayer);
        this.getMap().addTool(newCadastreObjectTool, this.getToolbar(), true);
        this.getMap().addMapAction(new SurveyPlanDetails(this.getMap(), newCadastreObjectLayer),
                this.getToolbar(), false);
        this.getMap().addMapAction(new RemoveCadastreObjects(this.getMap(), newCadastreObjectLayer),
                this.getToolbar(), false);

        super.addToolsAndCommands();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        this.getMap().getMapActionByName(
                CadastreChangePointSurveyListFormShow.MAPACTION_NAME).setEnabled(!readOnly);
        this.getMap().getMapActionByName(CadastreChangeNodeTool.NAME).setEnabled(!readOnly);
        this.getMap().getMapActionByName(CadastreChangeNewCadastreObjectTool.NAME).setEnabled(!readOnly);
        getCrsControl().setEnabled(newCadastreObjectLayer.getBeanList().size() < 1);
        ((SurveyPlanDetails)getMap().getMapActionByName(SurveyPlanDetails.MAPACTION_NAME)).setReadOnly(readOnly);
        getMap().getMapActionByName(SurveyPlanDetails.MAPACTION_NAME)
                .setEnabled(newCadastreObjectLayer.getBeanList().size() > 0);
        
        if (!readOnly) {
            this.getMap().getMapActionByName(CadastreChangeNewCadastreObjectTool.NAME)
                    .setEnabled(newCadastreObjectLayer.getBeanList().size() < 1);
            this.getMap().getMapActionByName(RemoveCadastreObjects.NAME).setEnabled(
                    newCadastreObjectLayer.getBeanList().size() > 0);
        }
    }

    private String getLastPart(String lastPartEntry) {
        return String.format(lastPartTemplate, lastPartEntry);
    }

    @Override
    protected void setTargetCadastreObjectTypeConfiguration(String targetCadastreObjectType) {
        this.newCadastreObjectTool.setCadastreObjectType(targetCadastreObjectType);
        this.newCadastreObjectLayer.getSpatialObjectDisplayPanel().setCadastreObjectType(
                targetCadastreObjectType);
    }
}
