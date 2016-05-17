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
package org.sola.clients.swing.gis.beans;

import com.vividsolutions.jts.geom.Geometry;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import org.geotools.swing.extended.util.GeometryUtility;
import org.hibernate.validator.constraints.NotEmpty;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.clients.beans.referencedata.ChiefdomTypeBean;
import org.sola.clients.beans.referencedata.LandTypeBean;
import org.sola.clients.beans.referencedata.SurveyingMethodTypeBean;
import org.sola.clients.beans.validation.Localized;
import org.sola.common.messaging.ClientMessage;

/**
 * Defines a cadastre object bean.
 * 
 * @author Elton Manoku
 */
public class CadastreObjectBean extends SpatialBean {
    
    public static String NAME_FIRST_PART_PROPERTY = "nameFirstpart";
    public static String NAME_LAST_PART_PROPERTY = "nameLastpart";
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
    
    private String id;
    @NotEmpty(message = ClientMessage.CHECK_NOTNULL_LS_NO, payload = Localized.class)
    private String nameFirstpart = "";
    @NotEmpty(message = ClientMessage.CHECK_NOTNULL_SUB_LS, payload = Localized.class)
    private String nameLastpart = "";
    private String typeCode = "parcel";
    private byte[] geomPolygon;
    private List<SpatialValueAreaBean> spatialValueAreaList = new ArrayList<SpatialValueAreaBean>();
    @NotEmpty(message = ClientMessage.CHECK_NOTNULL_OWNER_NAME, payload = Localized.class)
    private String ownerName;
    @NotEmpty(message = ClientMessage.CHECK_NOTNULL_PARCEL_ADDRESS, payload = Localized.class)
    private String address;
    @DecimalMin(message = ClientMessage.CHECK_NOTNULL_PARCEL_AREA, value = "1.0", payload = Localized.class)
    private double parcelArea;
    @NotEmpty(message = ClientMessage.CHECK_NOTNULL_LICENSED_SURVEYOR, payload = Localized.class)
    private String licensedSurveyorId;
    private String eastNeighbour;
    private String westNeighbour;
    private String southNeighbour;
    private String northNeighbour;
    @NotNull(message = ClientMessage.CHECK_NOTNULL_SURVEY_DATE, payload = Localized.class)
    @Past(message = ClientMessage.CHECK_SURVEY_DATE_IN_PAST, payload = Localized.class)
    private Date surveyDate;
    private SurveyingMethodTypeBean surveyMethod;
    private LandTypeBean landType;
    private ChiefdomTypeBean chiefdomType;
    private String beaconNumber;
    @NotEmpty(message = ClientMessage.CHECK_NOTNULL_CHARTING_OFFICER, payload = Localized.class)
    private String chartingOfficerId;
    @NotEmpty(message = ClientMessage.CHECK_NOTNULL_STATE_LAND_OFFICER, payload = Localized.class)
    private String stateLandClearingOfficerId;
    private PartySummaryBean stateLandClearingOfficer;
    private PartySummaryBean chartingOfficer;
    private PartySummaryBean licensedSurveyor;
    
    /**
     * Creates a cadastre object bean
     */
    public CadastreObjectBean(){
        super();
        generateId();
    }

    /** 
     * Generates new ID for the cadastre object 
     */
    public final void generateId(){
        setId(UUID.randomUUID().toString());
    }
    
    @Override
    public void setFeatureGeom(Geometry geometryValue) {
        super.setFeatureGeom(geometryValue);
        this.setGeomPolygon(GeometryUtility.getWkbFromGeometry(geometryValue));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getNameFirstpart() {
        return nameFirstpart;
    }

    /**
     * Sets the Name first part. It fires the change event to notify the corresponding feature
     * for the change.
     * 
     * @param nameFirstpart 
     */
    public void setNameFirstpart(String nameFirstpart) {
        String oldValue = this.nameFirstpart;
        this.nameFirstpart = nameFirstpart;
        propertySupport.firePropertyChange(NAME_FIRST_PART_PROPERTY, oldValue, nameFirstpart);
    }

    public String getNameLastpart() {
        return nameLastpart;
    }

    public void setNameLastpart(String nameLastpart) {
        String oldValue=this.nameLastpart;
        this.nameLastpart = nameLastpart;
        propertySupport.firePropertyChange(NAME_LAST_PART_PROPERTY, oldValue, nameLastpart);
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

    public byte[] getGeomPolygon() {
        return geomPolygon;
    }

    /**
     * Sets the geometry of the cadastre object. If the feature related geometry is not present,
     * it is also set.
     * 
     * @param geomPolygon 
     */
    public void setGeomPolygon(byte[] geomPolygon) {
        this.geomPolygon = geomPolygon.clone();
        if (getFeatureGeom() == null){
            super.setFeatureGeom(GeometryUtility.getGeometryFromWkb(geomPolygon));
        }
    }
   
    public List<SpatialValueAreaBean> getSpatialValueAreaList() {
        return spatialValueAreaList;
    }

    public void setSpatialValueAreaList(List<SpatialValueAreaBean> spatialValueAreaList) {
        this.spatialValueAreaList = spatialValueAreaList;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * Gets the calculated area of the cadastre object. The calculated area is the 
     * area value found in the {@see SpatialValueAreaBean.TYPE_CALCULATED} of the
     * {@see spatialValueAreaList} property.
     * 
     * @return 
     */
    public Double getCalculatedArea() {
        for(SpatialValueAreaBean valueAreaBean: this.getSpatialValueAreaList()){
            if (valueAreaBean.getTypeCode().equals(SpatialValueAreaBean.TYPE_OFFICIAL)){
                return valueAreaBean.getSize().doubleValue()/4046.86;
            }
        }
        return null;
    }

    /**
     * Sets the calculated area of the cadastre object. The calculated area is set in the 
     * {@see SpatialValueAreaBean} of type {@see SpatialValueAreaBean.TYPE_CALCULATED} found
     * in the {@see spatialValueAreaList} property.
     * 
     * @param calculatedArea 
     */
    public void setCalculatedArea(Double calculatedArea) {
        this.setArea((calculatedArea), SpatialValueAreaBean.TYPE_CALCULATED);
    }

    /**
     * Gets the official area of the cadastre object. The official area is the 
     * area value found in the {@see SpatialValueAreaBean.TYPE_OFFICIAL} of the
     * {@see spatialValueAreaList} property.
     * 
     * @return 
     */
    public Double getOfficialArea() {
        for(SpatialValueAreaBean valueAreaBean: this.getSpatialValueAreaList()){
            if (valueAreaBean.getTypeCode().equals(SpatialValueAreaBean.TYPE_OFFICIAL)){
                return valueAreaBean.getSize().doubleValue()/4046.86;
            }
        }
        return null;
    }

    /**
     * Sets the official area of the cadastre object. The official area is set in the 
     * {@see SpatialValueAreaBean} of type {@see SpatialValueAreaBean.TYPE_OFFICIAL} found
     * in the {@see spatialValueAreaList} property.
     * 
     * @param officialArea 
     */
    public void setOfficialArea(Double officialArea) {
        if(officialArea == null)
            officialArea=0.0;
        else
            officialArea=officialArea*4046.86;
        this.setArea(officialArea, SpatialValueAreaBean.TYPE_OFFICIAL);
    }

    /**
     * It sets the area for the cadastre object. The area is stored in the SpatialValueAreaBeans
     * attached to this bean. So for changing the area, first it is located the appropriate
     * SpatialValueAreaBean. if not found it is added one.
     * 
     * @param areaSize The size
     * @param areaType The area type
     */
    private void setArea(Double areaSize, String areaType) {
        if(areaSize == null)
            areaSize = 0.0;
        
        SpatialValueAreaBean valueAreaBeanFound = null;
        for(SpatialValueAreaBean valueAreaBean: this.getSpatialValueAreaList()){
            if (valueAreaBean.getTypeCode().equals(areaType)){
                valueAreaBeanFound = valueAreaBean;
                break;
            }
        }
        if (valueAreaBeanFound == null){
            valueAreaBeanFound = new SpatialValueAreaBean();
            valueAreaBeanFound.setTypeCode(areaType);
            this.getSpatialValueAreaList().add(valueAreaBeanFound);
        }
        valueAreaBeanFound.setSize(BigDecimal.valueOf(areaSize));
    }
    
    @Override
    public CadastreObjectBean copy(){
        CadastreObjectBean co = new CadastreObjectBean();
        co.setAddress(this.getAddress());
        co.setBeaconNumber(this.getBeaconNumber());
        co.setCalculatedArea(this.getCalculatedArea());
        if(this.getChartingOfficer() != null)
            co.setChartingOfficer((PartySummaryBean) this.getChartingOfficer().copy());
        else
            co.setChartingOfficer(null);
        co.setChartingOfficerId(this.getChartingOfficerId());
        if(this.getChiefdomType() != null)
            co.setChiefdomType((ChiefdomTypeBean) this.getChiefdomType().copy());
        else
            co.setChiefdomType(null);
        co.setClassificationCode(this.getClassificationCode());
        co.setEastNeighbour(this.getEastNeighbour());
        co.setEntityAction(this.getEntityAction());
        co.setFeatureGeom(this.getFeatureGeom());
        co.setGeomPolygon(this.getGeomPolygon());
        co.setId(this.getId());
        if(this.getLandType() != null)
            co.setLandType((LandTypeBean) this.getLandType().copy());
        else
            co.setLandType(null);
        if(this.getLicensedSurveyor() != null)
            co.setLicensedSurveyor((PartySummaryBean) this.getLicensedSurveyor().copy());
        else
            co.setLicensedSurveyor(null);
        co.setNameFirstpart(this.getNameFirstpart());
        co.setNameLastpart(this.getNameLastpart());
        co.setNorthNeighbour(this.getNorthNeighbour());
        co.setOfficialArea(this.getOfficialArea());
        co.setOwnerName(this.getOwnerName());
        co.setParcelArea(this.getParcelArea());
        co.setRedactCode(this.getRedactCode());
        co.setRowId(this.getRowId());
        co.setRowVersion(this.getRowVersion());
        co.setSouthNeighbour(this.getSouthNeighbour());
        co.setSpatialValueAreaList(this.getSpatialValueAreaList());
        if(this.getStateLandClearingOfficer() != null)
            co.setStateLandClearingOfficer((PartySummaryBean) this.getStateLandClearingOfficer().copy());
        else
            co.setStateLandClearingOfficer(null);
        co.setSurveyDate(this.getSurveyDate());
        if(this.getSurveyMethod() != null)
            co.setSurveyMethod((SurveyingMethodTypeBean) this.getSurveyMethod().copy());
        else
            co.setSurveyMethod(null);
        co.setTypeCode(this.getTypeCode());
        co.setWestNeighbour(this.getWestNeighbour());
        
        return co;
    }
    
    public void copyFromObject(CadastreObjectBean co){
        if(co == null)
            return;
        
        this.setAddress(co.getAddress());
        this.setBeaconNumber(co.getBeaconNumber());
        this.setCalculatedArea(co.getCalculatedArea());
        if(co.getChartingOfficer() != null)
            this.setChartingOfficer((PartySummaryBean) co.getChartingOfficer().copy());
        else
            this.setChartingOfficer(null);
        this.setChartingOfficerId(co.getChartingOfficerId());
        if(co.getChiefdomType() != null)
            this.setChiefdomType((ChiefdomTypeBean) co.getChiefdomType().copy());
        else
            this.setChiefdomType(null);
        this.setClassificationCode(co.getClassificationCode());
        this.setEastNeighbour(co.getEastNeighbour());
        this.setEntityAction(co.getEntityAction());
        this.setFeatureGeom(co.getFeatureGeom());
        this.setGeomPolygon(co.getGeomPolygon());
        this.setId(co.getId());
        if(co.getLandType() != null)
            this.setLandType((LandTypeBean) co.getLandType().copy());
        else
            this.setLandType(null);
        if(co.getLicensedSurveyor() != null)
            this.setLicensedSurveyor((PartySummaryBean) co.getLicensedSurveyor().copy());
        else
            this.setLicensedSurveyor(null);
        this.setNameFirstpart(co.getNameFirstpart());
        this.setNameLastpart(co.getNameLastpart());
        this.setNorthNeighbour(co.getNorthNeighbour());
        this.setOfficialArea(co.getOfficialArea());
        this.setOwnerName(co.getOwnerName());
        this.setParcelArea(co.getParcelArea());
        this.setRedactCode(co.getRedactCode());
        this.setRowId(co.getRowId());
        this.setRowVersion(co.getRowVersion());
        this.setSouthNeighbour(co.getSouthNeighbour());
        this.setSpatialValueAreaList(co.getSpatialValueAreaList());
        if(co.getStateLandClearingOfficer() != null)
            this.setStateLandClearingOfficer((PartySummaryBean) co.getStateLandClearingOfficer().copy());
        else
            this.setStateLandClearingOfficer(null);
        this.setSurveyDate(co.getSurveyDate());
        if(co.getSurveyMethod() != null)
            this.setSurveyMethod((SurveyingMethodTypeBean) co.getSurveyMethod().copy());
        else
            this.setSurveyMethod(null);
        this.setTypeCode(co.getTypeCode());
        this.setWestNeighbour(co.getWestNeighbour());
    }
    
    @Override
    public String toString() {
        return String.format("%s / %s",this.nameFirstpart, this.nameLastpart);
    }    
}
