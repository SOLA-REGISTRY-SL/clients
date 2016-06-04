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

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractTransactionedBean;
import org.sola.clients.beans.address.AddressBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.clients.beans.referencedata.CadastreObjectTypeBean;
import org.sola.clients.beans.referencedata.ChiefdomTypeBean;
import org.sola.clients.beans.referencedata.LandTypeBean;
import org.sola.clients.beans.referencedata.LandUseTypeBean;
import org.sola.clients.beans.referencedata.SurveyTypeBean;
import org.sola.clients.beans.referencedata.SurveyingMethodTypeBean;
import org.sola.clients.beans.validation.Localized;
import org.sola.common.messaging.ClientMessage;
import org.sola.webservices.transferobjects.cadastre.CadastreObjectTO;
import org.sola.webservices.transferobjects.EntityAction;

/** 
 * Contains properties and methods to manage <b>Cadastre</b> object of the 
 * domain model. Could be populated from the {@link CadastreObjectTO} object.
 */
public class CadastreObjectBean extends AbstractTransactionedBean {

    public static final String TYPE_CODE_PROPERTY = "typeCode";
    public static final String APPROVAL_DATETIME_PROPERTY = "approvalDatetime";
    public static final String HISTORIC_DATETIME_PROPERTY = "historicDatetime";
    public static final String SOURCE_REFERENCE_PROPERTY = "sourceReference";
    public static final String NAME_FIRSTPART_PROPERTY = "nameFirstpart";
    public static final String NAME_LASTPART_PROPERTY = "nameLastpart";
    public static final String CADASTRE_OBJECT_TYPE_PROPERTY = "cadastreObjectType";
    public static final String GEOM_POLYGON_PROPERTY = "geomPolygon";
    public static final String SELECTED_PROPERTY = "selected";
    public static final String PENDING_STATUS = "pending";
    public static final String LAND_USE_TYPE_PROPERTY = "landUseType";
    public static final String LAND_USE_CODE_PROPERTY = "landUseCode";
    public static final String ADDRESS_LIST_PROPERTY = "addressList";
    public static final String SELECTED_ADDRESS_PROPERTY = "selectedAddress";
    public static final String OFFICIAL_AREA_SIZE_PROPERTY = "officialAreaSize";
    public static String OWNER_NAME_PROPERTY = "ownerName";
    public static String ADDRESS_PROPERTY = "address";
    public static String LAND_TYPE_CODE_PROPERTY = "landTypeCode";
    public static String PARCEL_AREA_PROPERTY = "parcelArea";
    public static String PARCEL_AREA_ACRES_PROPERTY = "parcelAreaAcres";
    public static String LICENSED_SURVEYOR_ID_PROPERTY = "licensedSurveyorId";
    public static String EAST_NEIGHBOUR_PROPERTY = "eastNeighbour";
    public static String WEST_NEIGHBOUR_PROPERTY = "westNeighbour";
    public static String SOUTH_NEIGHBOUR_PROPERTY = "southNeighbour";
    public static String NORTH_NEIGHBOUR_PROPERTY = "northNeighbour";
    public static String SURVEY_METHOD_CODE_PROPERTY = "surveyMethodCode";
    public static String SURVEY_DATE_PROPERTY = "surveyDate";
    public static String BEACON_NUMBER_PROPERTY = "beaconNumber";
    public static String CHARTING_OFFICER_ID_PROPERTY = "chartingOfficerId";
    public static String STATE_LAND_CLEARING_OFFICER_ID_PROPERTY = "stateLandClearingOfficerId";
    public static String STATE_LAND_CLEARING_OFFICER_PROPERTY = "stateLandClearingOfficer";
    public static String CHARTING_OFFICER_PROPERTY = "chartingOfficer";
    public static String LICENSED_SURVEYOR_PROPERTY = "licensedSurveyor";
    public static String SURVEY_METHOD_PROPERTY = "surveyMethodCode";
    public static String LAND_TYPE_PROPERTY = "landType";
    public static String CHIEFDOM_TYPE_PROPERTY = "chiefdomType";
    public static String CHIEFDOM_TYPE_CODE_PROPERTY = "chiefdomTypeCode";
    public static String SURVEY_TYPE_CODE_PROPERTY = "surveyTypeCode";
    public static String REF_NAME_FIRST_PART_PROPERTY = "refNameFirstpart";
    public static String REF_NAME_LAST_PART_PROPERTY = "refNameLastpart";
    public static String SURVEY_NUMBER_PROPERTY = "surveyNumber";
    public static String CORRESPONDENCE_FILE_PROPERTY = "correspondenceFile";
    public static String COMPUTATION_FILE_PROPERTY = "computationFile";
    public static String DRAWN_BY_PROPERTY = "drawnBy";
    public static String CHECKED_BY_PROPERTY = "checkedBy";
    public static String CHECKING_DATE_PROPERTY = "checkingDate";
    public static String SURVEY_TYPE_PROPERTY = "surveyType";
    public static String DWG_OFF_NUMBER_PROPERTY = "dwgOffNumber";
    
    private Date approvalDatetime;
    private Date historicDatetime;
    @Length(max = 100, message =  ClientMessage.CHECK_FIELD_INVALID_LENGTH_SRCREF, payload=Localized.class)
    private String sourceReference;
    @Length(max = 20, message =  ClientMessage.CHECK_FIELD_INVALID_LENGTH_FIRSTPART, payload=Localized.class)
    @NotEmpty(message =  ClientMessage.CHECK_NOTNULL_CADFIRSTPART, payload=Localized.class)
    private String nameFirstpart;
    @Length(max = 50, message =  ClientMessage.CHECK_FIELD_INVALID_LENGTH_LASTPART, payload=Localized.class)
    @NotEmpty(message =  ClientMessage.CHECK_NOTNULL_CADLASTPART, payload=Localized.class)
    private String nameLastpart;
    @NotNull(message =  ClientMessage.CHECK_NOTNULL_CADOBJTYPE, payload=Localized.class)
    private CadastreObjectTypeBean cadastreObjectType;
    private byte[] geomPolygon;
    private transient boolean selected;
    private LandUseTypeBean landUseType;
    private SolaList<SpatialValueAreaBean> spatialValueAreaList;
    private SolaList<AddressBean> addressList;
    private transient AddressBean selectedAddress;
    private String ownerName;
    private String address;
    private double parcelArea;
    private String licensedSurveyorId;
    private String eastNeighbour;
    private String westNeighbour;
    private String southNeighbour;
    private String northNeighbour;
    private Date surveyDate;
    private SurveyingMethodTypeBean surveyMethod;
    private LandTypeBean landType;
    private ChiefdomTypeBean chiefdomType;
    private String beaconNumber;
    private String chartingOfficerId;
    private String stateLandClearingOfficerId;
    private PartySummaryBean stateLandClearingOfficer;
    private PartySummaryBean chartingOfficer;
    private PartySummaryBean licensedSurveyor;
    private SurveyTypeBean surveyType;
    private String refNameFirstpart;
    private String refNameLastpart;
    private String surveyNumber;
    private String correspondenceFile;
    private String computationFile;
    private String drawnBy;
    private String checkedBy;
    private String dwgOffNumber;
    private Date checkingDate;
    
    public CadastreObjectBean() {
        super();
        addressList = new SolaList<AddressBean>();
        spatialValueAreaList = new SolaList<SpatialValueAreaBean>();
    }

    public Date getApprovalDatetime() {
        return approvalDatetime;
    }

    public void setApprovalDatetime(Date approvalDatetime) {
        Date oldValue = approvalDatetime;
        this.approvalDatetime = approvalDatetime;
        propertySupport.firePropertyChange(APPROVAL_DATETIME_PROPERTY,
                oldValue, approvalDatetime);
    }

    public Date getHistoricDatetime() {
        return historicDatetime;
    }

    public void setHistoricDatetime(Date historicDatetime) {
        Date oldValue = historicDatetime;
        this.historicDatetime = historicDatetime;
        propertySupport.firePropertyChange(HISTORIC_DATETIME_PROPERTY,
                oldValue, historicDatetime);
    }

    public String getNameFirstpart() {
        return nameFirstpart;
    }

    public void setNameFirstpart(String nameFirstpart) {
        String oldValue = nameFirstpart;
        this.nameFirstpart = nameFirstpart;
        propertySupport.firePropertyChange(NAME_FIRSTPART_PROPERTY,
                oldValue, nameFirstpart);
    }

    public String getNameLastpart() {
        return nameLastpart;
    }

    public void setNameLastpart(String nameLastpart) {
        String oldValue = nameLastpart;
        this.nameLastpart = nameLastpart;
        propertySupport.firePropertyChange(NAME_LASTPART_PROPERTY,
                oldValue, nameLastpart);
    }

    public String getSourceReference() {
        return sourceReference;
    }

    public void setSourceReference(String sourceReference) {
        String oldValue = sourceReference;
        this.sourceReference = sourceReference;
        propertySupport.firePropertyChange(SOURCE_REFERENCE_PROPERTY,
                oldValue, sourceReference);
    }

    public String getTypeCode() {
        if (cadastreObjectType != null) {
            return cadastreObjectType.getCode();
        } else {
            return null;
        }
    }

    public void setTypeCode(String typeCode) {
        String oldValue = null;
        if (cadastreObjectType != null) {
            oldValue = cadastreObjectType.getCode();
        }
        setCadastreObjectType(CacheManager.getBeanByCode(
                CacheManager.getCadastreObjectTypes(), typeCode));
        propertySupport.firePropertyChange(TYPE_CODE_PROPERTY, oldValue, typeCode);
    }
    public String getLandUseCode() {
        if (landUseType != null) {
            return landUseType.getCode();
        } else {
            return null;
        }
    }

    public void setLandUseCode(String landUseCode) {
        String oldValue = null;
        if (landUseType != null) {
            oldValue = landUseType.getCode();
        }
        setLandUseType(CacheManager.getBeanByCode(
                CacheManager.getLandUseTypes(), landUseCode));
        propertySupport.firePropertyChange(LAND_USE_CODE_PROPERTY, oldValue, landUseCode);
    }

    public CadastreObjectTypeBean getCadastreObjectType() {
        return cadastreObjectType;
    }

    public void setCadastreObjectType(CadastreObjectTypeBean cadastreObjectType) {
        if(this.cadastreObjectType==null){
            this.cadastreObjectType = new CadastreObjectTypeBean();
        }
        this.setJointRefDataBean(this.cadastreObjectType, cadastreObjectType, CADASTRE_OBJECT_TYPE_PROPERTY);
    }
     public LandUseTypeBean getLandUseType() {
        return landUseType;
    }

    public void setLandUseType(LandUseTypeBean landUseType) {
        if(this.landUseType==null){
            this.landUseType = new LandUseTypeBean();
        }
        this.setJointRefDataBean(this.landUseType, landUseType, LAND_USE_TYPE_PROPERTY);
    }

    public String getRefNameFirstpart() {
        return refNameFirstpart;
    }

    public void setRefNameFirstpart(String refNameFirstpart) {
        String oldValue=this.refNameFirstpart;
        this.refNameFirstpart = refNameFirstpart;
        propertySupport.firePropertyChange(REF_NAME_FIRST_PART_PROPERTY, oldValue, refNameFirstpart);
    }

    public String getRefNameLastpart() {
        return refNameLastpart;
    }

    public void setRefNameLastpart(String refNameLastpart) {
        String oldValue=this.refNameLastpart;
        this.refNameLastpart = refNameLastpart;
        propertySupport.firePropertyChange(REF_NAME_LAST_PART_PROPERTY, oldValue, refNameLastpart);
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        String oldValue=this.ownerName;
        this.ownerName = ownerName;
        propertySupport.firePropertyChange(OWNER_NAME_PROPERTY, oldValue, ownerName);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        String oldValue=this.address;
        this.address = address;
        propertySupport.firePropertyChange(ADDRESS_PROPERTY, oldValue, address);
    }

    @NotEmpty(message = ClientMessage.CHECK_NOTNULL_LAND_TYPE, payload = Localized.class)
    public String getLandTypeCode() {
        if (landType == null) {
            return null;
        } else {
            return landType.getCode();
        }
    }

    public void setLandTypeCode(String landTypeCode) {
        setLandType(CacheManager.getBeanByCode(
                CacheManager.getLandTypes(), landTypeCode));
    }

    public LandTypeBean getLandType() {
        return landType;
    }

    public void setLandType(LandTypeBean landType) {
        if (this.landType == null) {
            this.landType = new LandTypeBean();
        }
        this.setJointRefDataBean(this.landType, landType, LAND_TYPE_PROPERTY);
    }

    public double getParcelArea() {
        return parcelArea;
    }

    public void setParcelArea(double parcelArea) {
        double oldValue=this.parcelArea;
        this.parcelArea = parcelArea;
        propertySupport.firePropertyChange(PARCEL_AREA_PROPERTY, oldValue, parcelArea);
    }

    public double getParcelAreaAcres() {
        return getParcelArea() / 4046.86;
    }

    public void setParcelAreaAcres(double parcelAreaAcres) {
        propertySupport.firePropertyChange(PARCEL_AREA_ACRES_PROPERTY, 0, parcelAreaAcres);
        setParcelArea(parcelAreaAcres * 4046.86);
    }
    
    public PartySummaryBean getLicensedSurveyor() {
        return licensedSurveyor;
    }

    public void setLicensedSurveyor(PartySummaryBean licensedSurveyor) {
        this.licensedSurveyor = licensedSurveyor;
        propertySupport.firePropertyChange(LICENSED_SURVEYOR_PROPERTY, null, licensedSurveyor);
        if(licensedSurveyor == null)
            setLicensedSurveyorId(null);
        else
            setLicensedSurveyorId(licensedSurveyor.getId());
    }

    public String getLicensedSurveyorId() {
        return licensedSurveyorId;
    }

    public void setLicensedSurveyorId(String licensedSurveyorId) {
        String oldValue=this.licensedSurveyorId;
        this.licensedSurveyorId = licensedSurveyorId;
        propertySupport.firePropertyChange(LICENSED_SURVEYOR_ID_PROPERTY, oldValue, licensedSurveyorId);
    }

    public String getEastNeighbour() {
        return eastNeighbour;
    }

    public void setEastNeighbour(String eastNeighbour) {
        String oldValue=this.eastNeighbour;
        this.eastNeighbour = eastNeighbour;
        propertySupport.firePropertyChange(EAST_NEIGHBOUR_PROPERTY, oldValue, eastNeighbour);
    }

    public String getWestNeighbour() {
        return westNeighbour;
    }

    public void setWestNeighbour(String westNeighbour) {
        String oldValue=this.westNeighbour;
        this.westNeighbour = westNeighbour;
        propertySupport.firePropertyChange(WEST_NEIGHBOUR_PROPERTY, oldValue, westNeighbour);
    }

    public String getSouthNeighbour() {
        return southNeighbour;
    }

    public void setSouthNeighbour(String southNeighbour) {
        String oldValue=this.southNeighbour;
        this.southNeighbour = southNeighbour;
        propertySupport.firePropertyChange(SOUTH_NEIGHBOUR_PROPERTY, oldValue, southNeighbour);
    }

    public String getNorthNeighbour() {
        return northNeighbour;
    }

    public void setNorthNeighbour(String northNeighbour) {
        String oldValue=this.northNeighbour;
        this.northNeighbour = northNeighbour;
        propertySupport.firePropertyChange(NORTH_NEIGHBOUR_PROPERTY, oldValue, northNeighbour);
    }

    @NotEmpty(message = ClientMessage.CHECK_NOTNULL_SURVEYING_METHOD, payload = Localized.class)
    public String getSurveyMethodCode() {
        if (surveyMethod == null) {
            return null;
        } else {
            return surveyMethod.getCode();
        }
    }

    public void setSurveyMethodCode(String surveyMethodCode) {
        setSurveyMethod(CacheManager.getBeanByCode(
                CacheManager.getSurveyingMethodTypes(), surveyMethodCode));
    }

    public SurveyingMethodTypeBean getSurveyMethod() {
        return surveyMethod;
    }

    public void setSurveyMethod(SurveyingMethodTypeBean surveyMethod) {
        if (this.surveyMethod == null) {
            this.surveyMethod = new SurveyingMethodTypeBean();
        }
        this.setJointRefDataBean(this.surveyMethod, surveyMethod, SURVEY_METHOD_PROPERTY);
    }

    public String getSurveyTypeCode() {
        if (surveyType == null) {
            return null;
        } else {
            return surveyType.getCode();
        }
    }

    public void setSurveyTypeCode(String surveyTypeCode) {
        setSurveyType(CacheManager.getBeanByCode(
                CacheManager.getSurveyTypes(), surveyTypeCode));
    }

    public SurveyTypeBean getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(SurveyTypeBean surveyType) {
        if (this.surveyType == null) {
            this.surveyType = new SurveyTypeBean();
        }
        this.setJointRefDataBean(this.surveyType, surveyType, SURVEY_TYPE_PROPERTY);
    }

    @NotEmpty(message = ClientMessage.CHECK_NOTNULL_CHIEFDOM_ADDRESS2, payload = Localized.class)
    public String getChiefdomTypeCode() {
        if (chiefdomType == null) {
            return null;
        } else {
            return chiefdomType.getCode();
        }
    }

    public void setChiefdomTypeCode(String chiefdomTypeCode) {
        setChiefdomType(CacheManager.getBeanByCode(
                CacheManager.getChiefdomTypes(), chiefdomTypeCode));
    }

    public ChiefdomTypeBean getChiefdomType() {
        return chiefdomType;
    }

    public void setChiefdomType(ChiefdomTypeBean chiefdomType) {
        if (this.chiefdomType == null) {
            this.chiefdomType = new ChiefdomTypeBean();
        }
        this.setJointRefDataBean(this.chiefdomType, chiefdomType, CHIEFDOM_TYPE_PROPERTY);
    }
    
    public Date getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(Date surveyDate) {
        Date oldValue=this.surveyDate;
        this.surveyDate = surveyDate;
        propertySupport.firePropertyChange(SURVEY_DATE_PROPERTY, oldValue, surveyDate);
    }

    public String getBeaconNumber() {
        return beaconNumber;
    }

    public void setBeaconNumber(String beaconNumber) {
        String oldValue=this.beaconNumber;
        this.beaconNumber = beaconNumber;
        propertySupport.firePropertyChange(BEACON_NUMBER_PROPERTY, oldValue, beaconNumber);
    }

    public PartySummaryBean getStateLandClearingOfficer() {
        return stateLandClearingOfficer;
    }

    public void setStateLandClearingOfficer(PartySummaryBean stateLandClearingOfficer) {
        this.stateLandClearingOfficer = stateLandClearingOfficer;
        propertySupport.firePropertyChange(STATE_LAND_CLEARING_OFFICER_PROPERTY, null, stateLandClearingOfficer);
        if(stateLandClearingOfficer == null)
            setStateLandClearingOfficerId(null);
        else
            setStateLandClearingOfficerId(stateLandClearingOfficer.getId());
    }

    public PartySummaryBean getChartingOfficer() {
        return chartingOfficer;
    }

    public void setChartingOfficer(PartySummaryBean chartingOfficer) {
        this.chartingOfficer = chartingOfficer;
        propertySupport.firePropertyChange(CHARTING_OFFICER_PROPERTY, null, chartingOfficer);
        if(chartingOfficer == null)
            setChartingOfficerId(null);
        else
            setChartingOfficerId(chartingOfficer.getId());
    }

    public String getChartingOfficerId() {
        return chartingOfficerId;
    }

    public void setChartingOfficerId(String chartingOfficerId) {
        String oldValue=this.chartingOfficerId;
        this.chartingOfficerId = chartingOfficerId;
        propertySupport.firePropertyChange(CHARTING_OFFICER_ID_PROPERTY, oldValue, chartingOfficerId);
    }

    public String getStateLandClearingOfficerId() {
        return stateLandClearingOfficerId;
    }

    public void setStateLandClearingOfficerId(String stateLandClearingOfficerId) {
        String oldValue=this.stateLandClearingOfficerId;
        this.stateLandClearingOfficerId = stateLandClearingOfficerId;
        propertySupport.firePropertyChange(STATE_LAND_CLEARING_OFFICER_ID_PROPERTY, oldValue, stateLandClearingOfficerId);
    }
    
    public String getSurveyNumber() {
        return surveyNumber;
    }

    public void setSurveyNumber(String surveyNumber) {
        String oldValue=this.surveyNumber;
        this.surveyNumber = surveyNumber;
        propertySupport.firePropertyChange(SURVEY_NUMBER_PROPERTY, oldValue, surveyNumber);
    }

    public String getCorrespondenceFile() {
        return correspondenceFile;
    }

    public void setCorrespondenceFile(String correspondenceFile) {
        String oldValue=this.correspondenceFile;
        this.correspondenceFile = correspondenceFile;
        propertySupport.firePropertyChange(CORRESPONDENCE_FILE_PROPERTY, oldValue, correspondenceFile);
    }

    public String getComputationFile() {
        return computationFile;
    }

    public void setComputationFile(String computationFile) {
        String oldValue=this.computationFile;
        this.computationFile = computationFile;
        propertySupport.firePropertyChange(COMPUTATION_FILE_PROPERTY, oldValue, computationFile);
    }

    public String getDrawnBy() {
        return drawnBy;
    }

    public void setDrawnBy(String drawnBy) {
        String oldValue=this.drawnBy;
        this.drawnBy = drawnBy;
        propertySupport.firePropertyChange(DRAWN_BY_PROPERTY, oldValue, drawnBy);
    }

    public String getDwgOffNumber() {
        return dwgOffNumber;
    }

    public void setDwgOffNumber(String dwgOffNumber) {
        String oldValue=this.dwgOffNumber;
        this.dwgOffNumber = dwgOffNumber;
        propertySupport.firePropertyChange(DWG_OFF_NUMBER_PROPERTY, oldValue, dwgOffNumber);
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(String checkedBy) {
        String oldValue=this.checkedBy;
        this.checkedBy = checkedBy;
        propertySupport.firePropertyChange(CHECKED_BY_PROPERTY, oldValue, checkedBy);
    }

    public Date getCheckingDate() {
        return checkingDate;
    }

    public void setCheckingDate(Date checkingDate) {
        Date oldValue=this.checkingDate;
        this.checkingDate = checkingDate;
        propertySupport.firePropertyChange(CHECKING_DATE_PROPERTY, oldValue, checkingDate);
    }
    
    public byte[] getGeomPolygon() {
        return geomPolygon;
    }

    public void setGeomPolygon(byte[] geomPolygon) { //NOSONAR
        byte[] old = this.geomPolygon;
        this.geomPolygon = geomPolygon; //NOSONAR
        propertySupport.firePropertyChange(GEOM_POLYGON_PROPERTY, old, this.geomPolygon);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        boolean oldValue = this.selected;
        this.selected = selected;
        propertySupport.firePropertyChange(SELECTED_PROPERTY, oldValue, this.selected);
    }
    
    /** Looks for officialArea code in the list of areas. */
    @NotNull(message=ClientMessage.CHECK_NOTNULL_AREA, payload=Localized.class)
    public BigDecimal getOfficialAreaSize(){
        if(getSpatialValueAreaFiletredList()==null || getSpatialValueAreaFiletredList().size() < 1){
            return null;
        }
        for(SpatialValueAreaBean areaBean : getSpatialValueAreaFiletredList()){
            if(areaBean.getTypeCode()!=null && areaBean.getTypeCode().equals(SpatialValueAreaBean.CODE_OFFICIAL_AREA)){
                return areaBean.getSize();
            }
        }
        return null;
    }

    /** Sets officialArea code. */
    public void setOfficialAreaSize(BigDecimal area){
        for(SpatialValueAreaBean areaBean : getSpatialValueAreaFiletredList()){
            if(areaBean.getTypeCode()!=null && areaBean.getTypeCode().equals(SpatialValueAreaBean.CODE_OFFICIAL_AREA)){
                // Delete area if provided value is null
                if(area == null){
                    areaBean.setEntityAction(EntityAction.DELETE);
                } else {
                    areaBean.setSize(area);
                }
                break;
            }
        }
        
        // Official area not found, add new if provided area not null
        if(area!=null){
            SpatialValueAreaBean areaBean = new SpatialValueAreaBean();
            areaBean.setSize(area);
            areaBean.setTypeCode(SpatialValueAreaBean.CODE_OFFICIAL_AREA);
            areaBean.setSpatialUnitId(this.getId());
            getSpatialValueAreaList().addAsNew(areaBean);
        }
        propertySupport.firePropertyChange(OFFICIAL_AREA_SIZE_PROPERTY, null, area);
    }
    
    @Valid
    public ObservableList<AddressBean> getAddressFilteredList() {
        return addressList.getFilteredList();
    }
    
    public SolaList<AddressBean> getAddressList() {
        return addressList;
    }

    public void setAddressList(SolaList<AddressBean> addressList) {
        this.addressList = addressList;
    }

    /** Returns merged string of addresses. */
    public String getAddressString(){
        String address = "";
        if(getAddressFilteredList()!=null){
            for (AddressBean addressBean : getAddressFilteredList()){
                if(addressBean.getDescription()!=null && !addressBean.getDescription().isEmpty()){
                    if(address.isEmpty()){
                        address = addressBean.getDescription();
                    } else {
                        address = address + "; " + addressBean.getDescription();
                    }
                }
            }
        }
        return address;
    }
    
    public AddressBean getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(AddressBean selectedAddress) {
        AddressBean oldValue = this.selectedAddress;
        this.selectedAddress = selectedAddress;
        propertySupport.firePropertyChange(SELECTED_ADDRESS_PROPERTY, oldValue, this.selectedAddress);
    }

    public SolaList<SpatialValueAreaBean> getSpatialValueAreaList() {
        return spatialValueAreaList;
    }

    @Valid
    public ObservableList<SpatialValueAreaBean> getSpatialValueAreaFiletredList() {
        return spatialValueAreaList.getFilteredList();
    }
    
    public void setSpatialValueAreaList(SolaList<SpatialValueAreaBean> spatialValueAreaList) {
        this.spatialValueAreaList = spatialValueAreaList;
    }
    
    /** Adds new cadastre object address. */
    public void addAddress(AddressBean address){
        if(address!=null){
            getAddressList().addAsNew(address);
        }
    }
    
    /** Removes selected address. */
    public void removeSelectedAddress(){
        if(selectedAddress!=null){
            if(selectedAddress.isNew()){
                getAddressList().remove(selectedAddress);
            } else {
                getAddressList().safeRemove(selectedAddress, EntityAction.DELETE);
            }
        }
    }
    
    /** Updates selected address. */
    public void updateSelectedAddress(AddressBean address){
        if(selectedAddress!=null && address!=null){
            selectedAddress.setDescription(address.getDescription());
        }
    }
    
    @Override
    public String toString() {
        String result = "";
        if(nameFirstpart!=null){
            result = nameFirstpart;
            if(nameLastpart!=null){
                result += " / " + nameLastpart;
            }
        }
        return result;
    }
    
}
