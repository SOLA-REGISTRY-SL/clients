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
package org.sola.clients.beans.referencedata;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingListBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.controls.SolaCodeList;
import static org.sola.clients.beans.referencedata.CordinateSystemTypeListBean.SELECTED_CordinateSystemType_PROPERTY;

/**
 *
 * @author Moses VB
 */
public class ChiefdomTypeListBean extends AbstractBindingListBean {
    
     public static final String SELECTED_ChiefdomType_PROPERTY = "selectedChiefdomType";
    private SolaCodeList<ChiefdomTypeBean> chiefdomTypeListBean;
    private ChiefdomTypeBean selectedChiefdomTypeBean;
    
    public ChiefdomTypeListBean() {
        this(false);
    }

     /** 
     * Creates object instance.
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public ChiefdomTypeListBean(boolean createDummy) {
        this(createDummy, (String) null);
            ChiefdomTypeBean dummy = new ChiefdomTypeBean();
            dummy.setDisplayValue(" ");
            chiefdomTypeListBean.add(0, dummy);
    }
    
    /** 
     * Creates object instance.
     * @param createDummy Indicates whether to add empty object on the list.
     * @param excludedCodes Codes, which should be skipped while filtering.
     */
    public ChiefdomTypeListBean(boolean createDummy, String ... excludedCodes) {
        super();
        chiefdomTypeListBean = new SolaCodeList<ChiefdomTypeBean>(excludedCodes);
        loadList(createDummy);
    }
    
    /** 
     * Loads list of {@link CordinateSystemTypeBean}.
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public final void loadList(boolean createDummy) {
        loadCodeList(ChiefdomTypeBean.class, chiefdomTypeListBean, 
                CacheManager.getChiefdomTypes(), createDummy);
    }

    public SolaCodeList<ChiefdomTypeBean> getChiefdomTypeListBean() {
        return chiefdomTypeListBean;
    }

    public void setChiefdomTypeListBean(SolaCodeList<ChiefdomTypeBean> chiefdomTypeListBean) {
        this.chiefdomTypeListBean = chiefdomTypeListBean;
    }

    public ChiefdomTypeBean getSelectedChiefdomTypeBean() {
        return selectedChiefdomTypeBean;
    }

    public void setSelectedChiefdomTypeBean(ChiefdomTypeBean selectedChiefdomTypeBean) {
        this.selectedChiefdomTypeBean = selectedChiefdomTypeBean;
    }

    
    
}
