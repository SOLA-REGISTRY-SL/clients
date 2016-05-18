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
    private String surveyPlanid; //= id
    private String lSNo;
    private String nameOfOwner;
    private String propertyNameOfStreet;
    private String propertyAddressNo;
    private String landtype;
    private String areaOfLand;
    private String landManagement;
    private String nameOfLicenseSurveyor;
    private String eastNeighborPlotHolder;
    private String westNeighborPlotHolder;
    private String northNeighborPlotHolder;
    private String southNeighborPlotHolder;
    private String surveyingMethod;
    private String directorOfSurveys;
    private Date dateSurveyed;
    private String beaconnumber;
    private String nameOfCO;
    private String nameOfSLCO;
    

    public SurveyPlanReturnListBean() {
        super();
        menagementList = new SolaObservableList<SurveyPlanReturnListBean>();
    }

    public ObservableList<SurveyPlanReturnListBean> getMenagementList() {
        return menagementList;
    }

    public void setMenagementList(SolaObservableList<SurveyPlanReturnListBean> menagementList) {
        this.menagementList = menagementList;
    }

    public String getSurveyPlanid() {
        return surveyPlanid;
    }

    public void setSurveyPlanid(String surveyPlanid) {
        this.surveyPlanid = surveyPlanid;
    }

    public String getlSNo() {
        return lSNo;
    }

    public void setlSNo(String lSNo) {
        this.lSNo = lSNo;
    }

    public String getNameOfOwner() {
        return nameOfOwner;
    }

    public void setNameOfOwner(String nameOfOwner) {
        this.nameOfOwner = nameOfOwner;
    }

    public String getPropertyNameOfStreet() {
        return propertyNameOfStreet;
    }

    public void setPropertyNameOfStreet(String propertyNameOfStreet) {
        this.propertyNameOfStreet = propertyNameOfStreet;
    }

    public String getPropertyAddressNo() {
        return propertyAddressNo;
    }

    public void setPropertyAddressNo(String propertyAddressNo) {
        this.propertyAddressNo = propertyAddressNo;
    }

    public String getLandtype() {
        return landtype;
    }

    public void setLandtype(String landtype) {
        this.landtype = landtype;
    }

    public String getAreaOfLand() {
        return areaOfLand;
    }

    public void setAreaOfLand(String areaOfLand) {
        this.areaOfLand = areaOfLand;
    }

    public String getLandManagement() {
        return landManagement;
    }

    public void setLandManagement(String landManagement) {
        this.landManagement = landManagement;
    }

    public String getNameOfLicenseSurveyor() {
        return nameOfLicenseSurveyor;
    }

    public void setNameOfLicenseSurveyor(String nameOfLicenseSurveyor) {
        this.nameOfLicenseSurveyor = nameOfLicenseSurveyor;
    }

    public String getEastNeighborPlotHolder() {
        return eastNeighborPlotHolder;
    }

    public void setEastNeighborPlotHolder(String eastNeighborPlotHolder) {
        this.eastNeighborPlotHolder = eastNeighborPlotHolder;
    }

    public String getWestNeighborPlotHolder() {
        return westNeighborPlotHolder;
    }

    public void setWestNeighborPlotHolder(String westNeighborPlotHolder) {
        this.westNeighborPlotHolder = westNeighborPlotHolder;
    }

    public String getNorthNeighborPlotHolder() {
        return northNeighborPlotHolder;
    }

    public void setNorthNeighborPlotHolder(String northNeighborPlotHolder) {
        this.northNeighborPlotHolder = northNeighborPlotHolder;
    }

    public String getSouthNeighborPlotHolder() {
        return southNeighborPlotHolder;
    }

    public void setSouthNeighborPlotHolder(String southNeighborPlotHolder) {
        this.southNeighborPlotHolder = southNeighborPlotHolder;
    }

    public String getSurveyingMethod() {
        return surveyingMethod;
    }

    public void setSurveyingMethod(String surveyingMethod) {
        this.surveyingMethod = surveyingMethod;
    }

    public String getDirectorOfSurveys() {
        return directorOfSurveys;
    }

    public void setDirectorOfSurveys(String directorOfSurveys) {
        this.directorOfSurveys = directorOfSurveys;
    }

    public Date getDateSurveyed() {
        return dateSurveyed;
    }

    public void setDateSurveyed(Date dateSurveyed) {
        this.dateSurveyed = dateSurveyed;
    }

    public String getBeaconnumber() {
        return beaconnumber;
    }

    public void setBeaconnumber(String beaconnumber) {
        this.beaconnumber = beaconnumber;
    }

    public String getNameOfCO() {
        return nameOfCO;
    }

    public void setNameOfCO(String nameOfCO) {
        this.nameOfCO = nameOfCO;
    }

    public String getNameOfSLCO() {
        return nameOfSLCO;
    }

    public void setNameOfSLCO(String nameOfSLCO) {
        this.nameOfSLCO = nameOfSLCO;
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
        TypeConverters.TransferObjectListToBeanList(managementViewTO,
                SurveyPlanReturnListBean.class, (List) this.getMenagementList());
    }
}