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
package org.sola.clients.swing.gis.mapaction;

import org.geotools.swing.extended.Map;
import org.geotools.swing.mapaction.extended.ExtendedAction;
import org.sola.clients.beans.referencedata.RequestTypeBean;
import org.sola.clients.swing.gis.layer.CadastreChangeNewCadastreObjectLayer;
import org.sola.clients.swing.gis.ui.control.SurveyPlanDetailsDialog;
import org.sola.common.WindowUtility;
import org.sola.common.messaging.GisMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * This map action shows the form of survey plan details.
 */
public final class SurveyPlanDetails extends ExtendedAction {

    /**
     * The name of the map action
     */
    public final static String MAPACTION_NAME = "survey-plan-show";
    private CadastreChangeNewCadastreObjectLayer newCadastreObjectLayer = null;
    private Map map;
    private boolean readOnly;
    private String requestTypeCode;
    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
    
    /**
     * Constructor of the class
     *
     * @param map The map control that will be interacting with the map action
     * @param newCadastreObjectLayer New cadastre objects layer
     * @param requestTypeCode Request type code
     */
    public SurveyPlanDetails(Map map, 
            CadastreChangeNewCadastreObjectLayer newCadastreObjectLayer,
            String requestTypeCode) {
        super(map, MAPACTION_NAME,
                MessageUtility.getLocalizedMessage(
                        GisMessage.CADASTRE_CHANGE_SURVEY_PLAN).getMessage(),
                "resources/text_signature.png");
        this.map = map;
        this.newCadastreObjectLayer = newCadastreObjectLayer;
        this.requestTypeCode = requestTypeCode;
    }

    @Override
    public void onClick() {
        if (newCadastreObjectLayer.getBeanList() != null && newCadastreObjectLayer.getBeanList().size() > 0) {
            SurveyPlanDetailsDialog dlg = new SurveyPlanDetailsDialog(null, true, 
                    newCadastreObjectLayer.getBeanList().get(0), requestTypeCode, readOnly);
            WindowUtility.centerForm(dlg);
            dlg.setVisible(true);
        }
    }
}
