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
import org.sola.clients.beans.AbstractIdBean;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.webservices.transferobjects.cadastre.SurveyPlanListReturnReportTO;
import org.sola.webservices.transferobjects.cadastre.SurveyPlanListReturnReportParamsTO;
import org.sola.services.boundary.wsclients.WSManager;

/**
 * Contains summary properties of the LodgementView object. Could be populated
 * from the {@link LodgementViewTO} object.<br /> For more information see UC
 * <b>Lodgement Report</b> schema.
 */
public class SurveyPlanReturnListBean extends  AbstractIdBean  {
    
    private ObservableList<SurveyPlanReturnListBean> menagementList;
    private String LSNo;
    private String nameofOwner;
    private String address;
    private String landtype;
    private double areaOfLand;
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
        menagementList = ObservableCollections.observableList(new LinkedList<SurveyPlanReturnListBean>());
    }

    public ObservableList<SurveyPlanReturnListBean> getMenagementList() {
        return menagementList;
    }

    public void setMenagementList(ObservableList<SurveyPlanReturnListBean> menagementList) {
        this.menagementList = menagementList;
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

    public double getAreaOfLand() {
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

    public void setLSNo(String LSNo) {
        this.LSNo = LSNo;
    }

    public void setNameofOwner(String nameofOwner) {
        this.nameofOwner = nameofOwner;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLandtype(String landtype) {
        this.landtype = landtype;
    }

    public void setAreaOfLand(double areaOfLand) {
        this.areaOfLand = areaOfLand;
    }

    public void setEastNeighborPlotHolder(String eastNeighborPlotHolder) {
        this.eastNeighborPlotHolder = eastNeighborPlotHolder;
    }

    public void setWestNeighborPlotHolder(String westNeighborPlotHolder) {
        this.westNeighborPlotHolder = westNeighborPlotHolder;
    }

    public void setNorthNeighborPlotHolder(String northNeighborPlotHolder) {
        this.northNeighborPlotHolder = northNeighborPlotHolder;
    }

    public void setSouthNeighborPlotHolder(String southNeighborPlotHolder) {
        this.southNeighborPlotHolder = southNeighborPlotHolder;
    }

    public void setSurveyingMethod(String surveyingMethod) {
        this.surveyingMethod = surveyingMethod;
    }

    public void setDateSurveyed(Date DateSurveyed) {
        this.DateSurveyed = DateSurveyed;
    }

    public void setNameofLicenseSurveyor(String nameofLicenseSurveyor) {
        this.nameofLicenseSurveyor = nameofLicenseSurveyor;
    }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }

    public void setRfSurvey(String rfSurvey) {
        this.rfSurvey = rfSurvey;
    }

    public void setSurveyNumber(String surveyNumber) {
        this.surveyNumber = surveyNumber;
    }

    public void setDslDate(Date dslDate) {
        this.dslDate = dslDate;
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
        SurveyPlanListReturnReportParamsTO paramsTO = TypeConverters.BeanToTrasferObject(params,
                SurveyPlanListReturnReportParamsTO.class);

        List<SurveyPlanListReturnReportTO> managementViewTO =
                WSManager.getInstance().getCadastreService().getSurveyPlanListReturnReportTO(searchString, paramsTO);
        TypeConverters.TransferObjectListToBeanList(managementViewTO,SurveyPlanReturnListBean.class, (List) this.getMenagementList());
    }
}