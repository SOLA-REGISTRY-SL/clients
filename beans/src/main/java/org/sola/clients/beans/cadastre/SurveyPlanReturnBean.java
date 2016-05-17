/*
 * Copyright 2016 Food and Agriculture Organization of the United Nations (FAO).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sola.clients.beans.cadastre;

import org.sola.clients.beans.AbstractIdBean;
import org.sola.clients.beans.administrative.BaUnitSummaryBean;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.party.PartyBean;
import org.sola.clients.beans.referencedata.NotifyRelationshipTypeBean;
import org.sola.clients.beans.source.SourceBean;

/**
 *
 * @author soladev
 */
public class SurveyPlanReturnBean extends AbstractIdBean {

    public static final String SURVEYPLAN_VIEW_ID = "surveyPlanid";
    private transient boolean selected;
    private String ownerName;
    private String address;
    private String landType;
    private double parcelArea;
    private PartyBean licensedSurveyor;
    private String licensedSurveyorId;
    private String eastNeighbour;
    private String westNeighbour;
    private String southNeighbour;
    private String northNeighbour;
    private String surveyMethod;
    private String surveyDate;
    private String beaconNumber;
    private PartyBean chartingOfficer;
    private PartyBean stateLandClearingOfficer;
    private String chartingOfficerId;
    private String stateLandClearingOfficerId;

    private PartyBean party;
    
    private SolaList<SourceBean> sourceList;
    private SourceBean selectedSource;
    private SolaList<BaUnitSummaryBean> propertyList;
    //private SolaList<SurveyPlanReturnBean> propertyList;
    
    private String status;

    public SurveyPlanReturnBean() {
        super();
        sourceList = new SolaList();
        propertyList = new SolaList();
    }

    //I am a bit lost here, please help

    public static String getSURVEYPLAN_VIEW_ID() {
        return SURVEYPLAN_VIEW_ID;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getAddress() {
        return address;
    }

    public String getLandType() {
        return landType;
    }

    public double getParcelArea() {
        return parcelArea;
    }

    public PartyBean getLicensedSurveyor() {
        return licensedSurveyor;
    }

    public String getLicensedSurveyorId() {
        return licensedSurveyorId;
    }

    public String getEastNeighbour() {
        return eastNeighbour;
    }

    public String getWestNeighbour() {
        return westNeighbour;
    }

    public String getSouthNeighbour() {
        return southNeighbour;
    }

    public String getNorthNeighbour() {
        return northNeighbour;
    }

    public String getSurveyMethod() {
        return surveyMethod;
    }

    public String getSurveyDate() {
        return surveyDate;
    }

    public String getBeaconNumber() {
        return beaconNumber;
    }

    public PartyBean getChartingOfficer() {
        return chartingOfficer;
    }

    public PartyBean getStateLandClearingOfficer() {
        return stateLandClearingOfficer;
    }

    public String getChartingOfficerId() {
        return chartingOfficerId;
    }

    public String getStateLandClearingOfficerId() {
        return stateLandClearingOfficerId;
    }

    public PartyBean getParty() {
        return party;
    }

    public SolaList<SourceBean> getSourceList() {
        return sourceList;
    }

    public SourceBean getSelectedSource() {
        return selectedSource;
    }

    public SolaList<BaUnitSummaryBean> getPropertyList() {
        return propertyList;
    }

    public String getStatus() {
        return status;
    }
    
    
}
