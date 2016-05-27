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

import java.util.Date;
import org.sola.clients.beans.AbstractIdBean;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.party.PartyBean;
import org.sola.clients.beans.source.SourceBean;

/**
 *
 * @author soladev
 */
public class SurveyPlanReturnBean extends AbstractIdBean {

    public static final String SURVEYPLAN_VIEW_ID = "surveyPlanid";
    private transient boolean selected;
    
   
    private String landManagement;
 
    private String id;

    private String LSNo;
   
    private String nameofOwner;

    private String address;

    private String landtype;

    private String areaOfLand;

    private String eastNeighborPlotHolder;

    private String westNeighborPlotHolder;

    private String northNeighborPlotHolder;

    private String southNeighborPlotHolder;

    private String surveyingMethod;

    private Date DateSurveyed;
    
    private String nameofLicenseSurveyor;

    private String surveyType;
    
    private String rfSurvey;
    
    private String surveyNumber;
    
    private Date dslDate;
 
    public SurveyPlanReturnBean() {
        super();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static String getSURVEYPLAN_VIEW_ID() {
        return SURVEYPLAN_VIEW_ID;
    }

    public String getLandManagement() {
        return landManagement;
    }

    public String getLSNo() {
        return LSNo;
    }

    public String getNameofOwner() {
        return nameofOwner;
    }

    public String getAddress() {
        return address;
    }

    public String getLandtype() {
        return landtype;
    }

    public String getAreaOfLand() {
        return areaOfLand;
    }

    public String getEastNeighborPlotHolder() {
        return eastNeighborPlotHolder;
    }

    public String getWestNeighborPlotHolder() {
        return westNeighborPlotHolder;
    }

    public String getNorthNeighborPlotHolder() {
        return northNeighborPlotHolder;
    }

    public String getSouthNeighborPlotHolder() {
        return southNeighborPlotHolder;
    }

    public String getSurveyingMethod() {
        return surveyingMethod;
    }

    public Date getDateSurveyed() {
        return DateSurveyed;
    }

    public String getNameofLicenseSurveyor() {
        return nameofLicenseSurveyor;
    }

    public String getSurveyType() {
        return surveyType;
    }

    public String getRfSurvey() {
        return rfSurvey;
    }

    public String getSurveyNumber() {
        return surveyNumber;
    }

    public Date getDslDate() {
        return dslDate;
    }

    
    
    
}
