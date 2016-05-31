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
package org.sola.clients.beans.cadastre;

import org.sola.clients.beans.cadastre.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingListBean;
import org.sola.clients.beans.AbstractIdBean;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.webservices.transferobjects.cadastre.SurveyPlanListReturnReportTO;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.cadastre.SurveyPlanListReturnReportParamsTO;

/**
 * Contains summary properties of the LodgementView object. Could be populated
 * from the {@link LodgementViewTO} object.<br /> For more information see UC
 * <b>Lodgement Report</b> schema.
 */
public class SurveyPlanReturnListBean extends AbstractBindingListBean  {
    public static final String SELECTED_SURVEYPLAN = "selectedSurveyPlan";
    private SolaObservableList<SurveyPlanReturnListBean> menagementList;
    private SurveyPlanReturnBean selectedSurveyPlan;
   
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
 
       

    public SurveyPlanReturnListBean() {
        super();
        menagementList = new SolaObservableList<SurveyPlanReturnListBean>();
    }

    public SolaObservableList<SurveyPlanReturnListBean> getMenagementList() {
        return menagementList;
    }

    public void setMenagementList(SolaObservableList<SurveyPlanReturnListBean> menagementList) {
        this.menagementList = menagementList;
    }

    public String getLandManagement() {
        return landManagement;
    }

    public void setLandManagement(String landManagement) {
        this.landManagement = landManagement;
    }

    public static String getSELECTED_SURVEYPLAN() {
        return SELECTED_SURVEYPLAN;
    }

    public SurveyPlanReturnBean getSelectedSurveyPlan() {
        return selectedSurveyPlan;
    }

    public String getId() {
        return id;
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

    
    
    /**
     * Returns collection of {@link ApplicationBean} objects. This method is
     * used by Jasper report designer to extract properties of application bean
     * to help design a report.
     */
    public static java.util.Collection generateCollection() {
        java.util.Vector collection = new java.util.Vector();
        SurveyPlanReturnListBean bean = new SurveyPlanReturnListBean();
        collection.add(bean);
        return collection;
    }

    //      /** Passes from date and to date search criteria. */
    public void passParameter(String searchString, SurveyPlanReturnListParamsBean params) {
//        applicationSearchResultsList.clear();
        SurveyPlanListReturnReportParamsTO paramsTO = TypeConverters.BeanToTrasferObject(params,
                SurveyPlanListReturnReportParamsTO.class);

        List<SurveyPlanListReturnReportTO> managementViewTO =
                WSManager.getInstance().getCadastreService().getSurveyPlanListReturnReportTO(searchString, paramsTO);
        
        System.out.println("paramsTO.getFromDate()  "+paramsTO.getFromDate());
        System.out.println("paramsTO.getToDate()  "+paramsTO.getToDate());
        TypeConverters.TransferObjectListToBeanList(managementViewTO,
                SurveyPlanReturnListBean.class, (List) this.getMenagementList());
    }
}