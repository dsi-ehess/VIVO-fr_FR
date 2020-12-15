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
import static edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators.VivoBaseGenerator.KEEP_LABEL_FIELD;
import static edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators.VivoBaseGenerator.KEEP_LABEL_FIELD_VALUE_ON;;
public class RelationshipMandatoryLabelValidator implements N3ValidatorVTwo {


	private static String MISSING_LABEL_ERROR = "Please select an existing value or enter a new value in the Name field.";

	private String relationshipField;
	private String labelField;
	private List<String> preciseRelationClasses;
	private String errorMessage;
	
	public RelationshipMandatoryLabelValidator(String positionField, String labelField,
			String[] preciseRelationClasses) {
		this.relationshipField = positionField;
		this.labelField = labelField;
		this.preciseRelationClasses = Arrays.asList(preciseRelationClasses);
	}
	
	public RelationshipMandatoryLabelValidator(String relationshipField, String labelField,
			String[] preciseRelationClasses,  String errorMessage) {
		this.relationshipField = relationshipField;
		this.labelField = labelField;
		this.preciseRelationClasses = Arrays.asList(preciseRelationClasses);
		this.errorMessage = errorMessage;
	}

	@Override
	public Map<String, String> validate(EditConfigurationVTwo editConfig, MultiValueEditSubmission editSub) {
		Map<String, List<String>> urisFromForm = editSub.getUrisFromForm();
		Map<String, List<Literal>> literalsFromForm = editSub.getLiteralsFromForm();

		Map<String, String> errors = new HashMap<String, String>();

		String selectedRelationship = null;
		if (urisFromForm.containsKey(relationshipField)) {
			selectedRelationship = urisFromForm.get(relationshipField).get(0);
		}
		if (selectedRelationship == null) {
			errors.put(relationshipField, "Missing position field. This may not occur.");
			return errors;
		}
		String relationShipLabel = null;
		if (literalsFromForm.containsKey(labelField) && literalsFromForm.get(labelField).size() > 0) {
			relationShipLabel = literalsFromForm.get(labelField).get(0).getString();
		}
		if (StringUtils.isNotBlank(relationShipLabel)) {
			return null;
		}
		
		String keepLabelFlag = null;
		if (literalsFromForm.containsKey(KEEP_LABEL_FIELD)
				&& literalsFromForm.get(KEEP_LABEL_FIELD).size() > 0) {
			keepLabelFlag = literalsFromForm.get(KEEP_LABEL_FIELD).get(0).getString();
		}
		if (preciseRelationClasses.contains(selectedRelationship) && (KEEP_LABEL_FIELD_VALUE_ON).equals(keepLabelFlag)) {
			return null;
		} else {
			errors.put(labelField, errorMessage != null? errorMessage: MISSING_LABEL_ERROR);
		}

		return errors.size() != 0 ? errors : null;
	}

}
