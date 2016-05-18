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
    
    
    private String address;
    private String licensedSurveyorId;
   
    private String status;

    public SurveyPlanReturnBean() {
        super();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLicensedSurveyorId() {
        return licensedSurveyorId;
    }

    public void setLicensedSurveyorId(String licensedSurveyorId) {
        this.licensedSurveyorId = licensedSurveyorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    
    
}
