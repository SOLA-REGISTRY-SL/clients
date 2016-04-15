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
public class CordinateSystemTypeListBean extends AbstractBindingListBean {
    
    public static final String SELECTED_CordinateSystemType_PROPERTY = "selectedCordinateSystemType";
    private SolaCodeList<CordinateSystemTypeBean> cordinateSystemTypeListBean;
    private CordinateSystemTypeBean selectedCordinateSystemTypeBean;
    
    public CordinateSystemTypeListBean() {
        this(false);
    }

     /** 
     * Creates object instance.
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public CordinateSystemTypeListBean(boolean createDummy) {
        this(createDummy, (String) null);
            CordinateSystemTypeBean dummy = new CordinateSystemTypeBean();
            dummy.setDisplayValue(" ");
            cordinateSystemTypeListBean.add(0, dummy);
    }
    
    /** 
     * Creates object instance.
     * @param createDummy Indicates whether to add empty object on the list.
     * @param excludedCodes Codes, which should be skipped while filtering.
     */
    public CordinateSystemTypeListBean(boolean createDummy, String ... excludedCodes) {
        super();
        cordinateSystemTypeListBean = new SolaCodeList<CordinateSystemTypeBean>(excludedCodes);
        loadList(createDummy);
    }
    
    /** 
     * Loads list of {@link CordinateSystemTypeBean}.
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public final void loadList(boolean createDummy) {
        loadCodeList(CordinateSystemTypeBean.class, cordinateSystemTypeListBean, 
                CacheManager.getCordinateSystemTypes(), createDummy);
    }
    
    public ObservableList<CordinateSystemTypeBean> getCordinateSystemTypeList() {
        return cordinateSystemTypeListBean.getFilteredList();
    }
    
    public void setExcludedCodes(String ... codes){
        cordinateSystemTypeListBean.setExcludedCodes(codes);
    }
    
    public CordinateSystemTypeBean getCordinateSystemType() {
        return selectedCordinateSystemTypeBean;
    }

    public void setSelectedCordinateSystemType(CordinateSystemTypeBean value) {
        selectedCordinateSystemTypeBean = value;
        propertySupport.firePropertyChange(SELECTED_CordinateSystemType_PROPERTY, null, value);
    }
    
}
