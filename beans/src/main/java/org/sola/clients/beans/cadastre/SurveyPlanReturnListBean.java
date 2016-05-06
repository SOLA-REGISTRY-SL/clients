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

import org.sola.clients.beans.address.AddressBean;
import org.sola.clients.beans.controls.SolaCodeList;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.party.PartyBean;
import org.sola.clients.beans.referencedata.CadastreObjectTypeBean;
import org.sola.clients.beans.referencedata.LandUseTypeBean;
import org.sola.clients.beans.referencedata.SurveyingMethodTypeBean;

/**
 *
 * @author Moses VB
 */
public class SurveyPlanReturnListBean extends CadastreObjectBean{
    
  
         /** Default constructor. */
    public SurveyPlanReturnListBean(){
        super();
    }
    
    private SurveyPlanReturnListBean cadastreObjectType;
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
    
    //I am a bit lost here, please help
}

