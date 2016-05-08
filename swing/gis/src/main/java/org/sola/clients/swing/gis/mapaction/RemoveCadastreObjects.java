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

import org.geotools.swing.mapaction.extended.ExtendedAction;
import org.sola.clients.swing.gis.tool.CadastreChangeNewCadastreObjectTool;
import org.sola.clients.swing.gis.ui.controlsbundle.ControlsBundleForApplicationLocation;
import org.sola.common.messaging.GisMessage;
import org.sola.common.messaging.MessageUtility;
import org.geotools.swing.extended.Map;
import org.sola.clients.swing.gis.layer.CadastreChangeNewCadastreObjectLayer;

/**
 * This map action is used to remove created cadastre objects on the map.
 *
 */
public final class RemoveCadastreObjects extends ExtendedAction {

    /**
     * Constructor of the map action that is used to remove the application location geometry.
     * 
     * @param map  The map control with which the map action will interact 
     */
    private Map map;
    private CadastreChangeNewCadastreObjectLayer newCadastreObjectLayer = null;
    public final static String NAME = "co-remove";
    
    public RemoveCadastreObjects(Map map, CadastreChangeNewCadastreObjectLayer newCadastreObjectLayer) {
        super(map, "co-remove",
                MessageUtility.getLocalizedMessage(
                GisMessage.CADASTRE_CHANGE_TOOLTIP_REMOVE_CO).getMessage(),
                "resources/cadastre-redefinition-reset.png");
        this.map = map;
        this.newCadastreObjectLayer = newCadastreObjectLayer;
    }

    @Override
    public void onClick() {
        if(newCadastreObjectLayer.getBeanList() != null && newCadastreObjectLayer.getBeanList().size() > 0){
            newCadastreObjectLayer.getBeanList().removeAll(newCadastreObjectLayer.getBeanList());
        }
    }
}
