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
package org.sola.clients.swing.gis.beans.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.sola.clients.swing.gis.beans.CadastreObjectBean;
import org.sola.common.StringUtility;

/**
 * Used to make validation for {@link CadastreObjectSurveyTypeCheck}.
 */
public class CadastreObjectSurveyTypeValidator implements ConstraintValidator<CadastreObjectSurveyTypeCheck, CadastreObjectBean> {

    @Override
    public void initialize(CadastreObjectSurveyTypeCheck constraintAnnotation) {
    }

    @Override
    public boolean isValid(CadastreObjectBean co, ConstraintValidatorContext context) {
        if (co == null) {
            return true;
        }
        if (!StringUtility.isEmpty(co.getSurveyTypeCode()) && StringUtility.isEmpty(co.getRefNameFirstpart())) {
            return false;
        }
        if ((!StringUtility.isEmpty(co.getRefNameFirstpart()) || !StringUtility.isEmpty(co.getRefNameLastpart()))
                && StringUtility.isEmpty(co.getSurveyTypeCode())) {
            return false;
        }
        return true;
    }
}
