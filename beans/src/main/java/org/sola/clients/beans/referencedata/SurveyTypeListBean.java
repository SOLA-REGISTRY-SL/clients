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

/**
 * Holds list of {@link SurveyTypeBean} objects.
 */
public class SurveyTypeListBean extends AbstractBindingListBean {

    public static final String SELECTED_SURVEY_TYPE_PROPERTY = "selectedSurveyType";
    private SolaCodeList<SurveyTypeBean> surveyTypes;
    private SurveyTypeBean selectedSurveyType;
    
    /** Default constructor. */
    public SurveyTypeListBean(){
        this(false);
    }
    
    /** 
     * Creates object instance.
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public SurveyTypeListBean(boolean createDummy) {
        this(createDummy, (String) null);
    }
    
    /** 
     * Creates object instance.
     * @param createDummy Indicates whether to add empty object on the list.
     * @param excludedCodes Codes, which should be skipped while filtering.
     */
    public SurveyTypeListBean(boolean createDummy, String ... excludedCodes) {
        super();
        surveyTypes = new SolaCodeList<SurveyTypeBean>(excludedCodes);
        loadList(createDummy);
    }
    
    /** 
     * Loads list of {@link RequestCategoryTypeBean}.
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public final void loadList(boolean createDummy) {
        loadCodeList(SurveyTypeBean.class, surveyTypes, CacheManager.getSurveyTypes(), createDummy);
    }

    public ObservableList<SurveyTypeBean> getSurveyTypes() {
        return surveyTypes.getFilteredList();
    }

    public void setExcludedCodes(String ... codes){
        surveyTypes.setExcludedCodes(codes);
    }
    
    public SurveyTypeBean getSelectedSurveyType() {
        return selectedSurveyType;
    }

    public void setSelectedSurveyType(SurveyTypeBean selectedSurveyType) {
        this.selectedSurveyType = selectedSurveyType;
        SurveyTypeBean oldValue = this.selectedSurveyType;
        this.selectedSurveyType = selectedSurveyType;
        propertySupport.firePropertyChange(SELECTED_SURVEY_TYPE_PROPERTY, oldValue, this.selectedSurveyType);
    }
}
