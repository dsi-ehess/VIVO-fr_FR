/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.edit.n3editing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Literal;

import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.N3ValidatorVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.EditConfigurationVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.MultiValueEditSubmission;

public class RelationshipMandatoryLabelValidator implements N3ValidatorVTwo {

	private static final String KEEP_LABEL_FIELD = "keepLabel";

	private static String MISSING_LABEL_ERROR = "Please select an existing value or enter a new value in the Name field.";

	private String positionField;
	private String labelField;

	private List<String> preciseRelationClasses;

	public RelationshipMandatoryLabelValidator(String positionField, String labelField,
			String[] preciseRelationClasses) {
		this.positionField = positionField;
		this.labelField = labelField;
		this.preciseRelationClasses = Arrays.asList(preciseRelationClasses);
	}

	@Override
	public Map<String, String> validate(EditConfigurationVTwo editConfig, MultiValueEditSubmission editSub) {
		Map<String, List<String>> urisFromForm = editSub.getUrisFromForm();
		Map<String, List<Literal>> literalsFromForm = editSub.getLiteralsFromForm();

		Map<String, String> errors = new HashMap<String, String>();

		String selectedRelationship = null;
		if (urisFromForm.size() > 0 && urisFromForm.containsKey(positionField)) {
			selectedRelationship = urisFromForm.get(positionField).get(0);
		}
		if (selectedRelationship == null) {
			errors.put(positionField, "Missing position field. This may not occur.");
			return errors;
		}
		String relationShipLabel = null;
		if (!literalsFromForm.isEmpty() && literalsFromForm.containsKey(labelField)
				&& literalsFromForm.get(labelField).size() > 0) {
			relationShipLabel = literalsFromForm.get(labelField).get(0).toString();
		}
		Literal keepLabelFlag = null;
		if (!literalsFromForm.isEmpty() && literalsFromForm.containsKey(KEEP_LABEL_FIELD)
				&& literalsFromForm.get(KEEP_LABEL_FIELD).size() > 0) {
			keepLabelFlag = literalsFromForm.get(KEEP_LABEL_FIELD).get(0);
		}
		if (StringUtils.isNotBlank(relationShipLabel)) {
			return null;
		}
		if (preciseRelationClasses.contains(selectedRelationship) && keepLabelFlag != null
				&& keepLabelFlag.toString().equals("on^^http://www.w3.org/2001/XMLSchema#boolean")) {
			return null;
		} else {
			errors.put(labelField, MISSING_LABEL_ERROR);
		}

		return errors.size() != 0 ? errors : null;
	}

}
