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
public class SurveyingMethodTypeListBean extends AbstractBindingListBean {
    
    public static final String SELECTED_SURVEYINGMETHODTYPE_PROPERTY = "selectedSurveyingMethodType";
    private SolaCodeList<SurveyingMethodTypeBean> surveyingMethodTypeListBean;
    private SurveyingMethodTypeBean selectedSurveyingMethodTypeBean;
    
    public SurveyingMethodTypeListBean()
    {
        this(false);
    }
    
     /** 
     * Creates object instance.
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public SurveyingMethodTypeListBean(boolean createDummy) {
        this(createDummy, (String) null);
            SurveyingMethodTypeBean dummy = new SurveyingMethodTypeBean();
            dummy.setDisplayValue(" ");
            surveyingMethodTypeListBean.add(0, dummy);
    }
    
    /** 
     * Creates object instance.
     * @param createDummy Indicates whether to add empty object on the list.
     * @param excludedCodes Codes, which should be skipped while filtering.
     */
    public SurveyingMethodTypeListBean(boolean createDummy, String ... excludedCodes) {
        super();
        surveyingMethodTypeListBean = new SolaCodeList<SurveyingMethodTypeBean>(excludedCodes);
        loadList(createDummy);
    }
    
    /** 
     * Loads list of {@link CordinateSystemTypeBean}.
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public final void loadList(boolean createDummy) {
        loadCodeList(SurveyingMethodTypeBean.class, surveyingMethodTypeListBean, 
                CacheManager.getSurveyingMethodTypes(), createDummy);
    }
    
    public ObservableList<SurveyingMethodTypeBean> getSurveyingMethodTypeList() {
        return surveyingMethodTypeListBean.getFilteredList();
    }
    
    public void setExcludedCodes(String ... codes){
        surveyingMethodTypeListBean.setExcludedCodes(codes);
    }
    
    public SurveyingMethodTypeBean getSurveyingMethodType() {
        return selectedSurveyingMethodTypeBean;
    }

    public void setSurveyingMethodType(SurveyingMethodTypeBean value) {
        selectedSurveyingMethodTypeBean = value;
        propertySupport.firePropertyChange(SELECTED_CordinateSystemType_PROPERTY, null, value);
    }
}
