/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.preprocessors;

import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.BaseEditSubmissionPreprocessorVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.EditConfigurationVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.MultiValueEditSubmission;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * This preprocessor is used to remove all information about research team if research team uri is blank
 */

public class RemoveEmptyResearchTeamForIndividualPreprocessor extends BaseEditSubmissionPreprocessorVTwo {


    public RemoveEmptyResearchTeamForIndividualPreprocessor(EditConfigurationVTwo editConfig) {
        super(editConfig);
    }

    @Override
    public void preprocess(MultiValueEditSubmission inputSubmission, VitroRequest vreq) {
        Map<String, List<String>> uris = inputSubmission.getUrisFromForm();
        Iterator<Map.Entry<String, List<String>>> itr = uris.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, List<String>> curr = itr.next();
            if (curr.getKey().equals("existingResearchTeam") && curr.getValue().contains(null) || curr.getValue().contains("")) {
                inputSubmission.getLiteralsFromForm().remove("researchTeamLabel");
                inputSubmission.getLiteralsFromForm().remove("researchTeamLabelDisplay");
                itr.remove();
            }
        }
    }

}
