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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.AbstractBindingListBean;
import org.sola.clients.beans.application.NotifyBean;
//import static org.sola.clients.beans.application.;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.EntityAction;

/**
 *
 * @author soladev
 */
       
    public class SurveyPlanReturnListBean extends AbstractBindingListBean {

    public static final String SELECTED_SURVEYPLAN = "selectedSurveyPlan";
    private SolaList<SurveyPlanReturnBean> surveyPlanList;
    private SurveyPlanReturnBean selectedSurveyPlan;
    private String serviceId;

    public SurveyPlanReturnListBean() {
        super();
        surveyPlanList = new SolaList<SurveyPlanReturnBean>();
    }
    
    //I am a bit lost here, please help
    
     public ObservableList<SurveyPlanReturnBean> getFilteredSurveyPlanList() {
        return surveyPlanList.getFilteredList();
    }

    public SurveyPlanReturnBean getSelectedSurveyPlan() {
        return selectedSurveyPlan;
    }

    public void SelectedSurveyPlan(SurveyPlanReturnBean selected) {
        SurveyPlanReturnBean oldValue = this.selectedSurveyPlan;
        this.selectedSurveyPlan = selected;
        propertySupport.firePropertyChange(SELECTED_SURVEYPLAN, oldValue, this.selectedSurveyPlan);
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void addItem(SurveyPlanReturnBean item) {
        surveyPlanList.addAsNew(item);
    }

    public void removeItem(SurveyPlanReturnBean item) {
        surveyPlanList.safeRemove(item, EntityAction.DELETE);
    }


    /**
     * Retrieves the list of surveyplan from the database using the service
     * id.
     *
     * @param serviceId
     */
    public void loadList(String serviceId) {
        setServiceId(serviceId);
        surveyPlanList.clear();
        // Translate the TO's with the saved data to Beans and replace the original bean list
        TypeConverters.TransferObjectListToBeanList(
                WSManager.getInstance().getCaseManagementService().getNotifyParties(serviceId),
                NotifyBean.class, (List) surveyPlanList);
    }

    /**
     * Retrieves the list of notify parties from the database using the service
     * id.
     *
     * @param serviceId
     */
    public List getList(String serviceId) {
        setServiceId(serviceId);
        surveyPlanList.clear();
        // Translate the TO's with the saved data to Beans and replace the original bean list
        return TypeConverters.TransferObjectListToBeanList(
                WSManager.getInstance().getCaseManagementService().getNotifyParties(serviceId),
                SurveyPlanReturnBean.class, (List) surveyPlanList);


    }

    /**
     * Overrides the default validate method to ensure all
     * PublicDisplayItemBeans are validated as well
     *
     * @param <T>
     * @param showMessage
     * @param group
     * @return List of warning messages to display to the user
     */
    @Override
    public <T extends AbstractBindingBean> Set<ConstraintViolation<T>> validate(boolean showMessage, Class<?>... group) {

        Set<ConstraintViolation<T>> warningsList = super.validate(false, group);
        for (SurveyPlanReturnBean bean : surveyPlanList) {
            Set<ConstraintViolation<T>> beanWarningsList = bean.validate(false, group);
            warningsList.addAll(beanWarningsList);
        }

        if (showMessage) {
            showMessage(warningsList);
        }
        return warningsList;
    }

    /**
     * Returns list of checked notify parties.
     *
     * @param includeSelected Indicates whether to include in the list selected
     * notify party if there no notify parties checked.
     */
    public List<SurveyPlanReturnBean> getChecked(boolean includeSelected) {
        List<SurveyPlanReturnBean> checked = new ArrayList<SurveyPlanReturnBean>();
        for (SurveyPlanReturnBean bean : getFilteredSurveyPlanList()) {
            //if (bean.isChecked()) { //to check this code after build
                checked.add(bean);
            //}
        }
        if (includeSelected && checked.size() < 1 && getSelectedSurveyPlan() != null) {
            checked.add(getSelectedSurveyPlan());
        }
        return checked;
    }
}

