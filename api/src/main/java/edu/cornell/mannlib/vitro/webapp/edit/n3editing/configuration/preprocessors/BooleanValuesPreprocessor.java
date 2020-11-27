/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.preprocessors;

import static edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators.VivoBaseGenerator.KEEP_LABEL_FIELD;
import static edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators.VivoBaseGenerator.KEEP_LABEL_FIELD_VALUE_ON;
/*
 * This preprocessor is used to fix "on" and "off" values sent by html form on "keepLabel" field
 */

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.vocabulary.XSD;

import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.BaseEditSubmissionPreprocessorVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.EditConfigurationVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.MultiValueEditSubmission;

public class BooleanValuesPreprocessor extends BaseEditSubmissionPreprocessorVTwo {

	
	public BooleanValuesPreprocessor(EditConfigurationVTwo editConfig) {
		super(editConfig);
	}

	@Override
	public void preprocess(MultiValueEditSubmission inputSubmission, VitroRequest vreq) {
		Map<String, List<Literal>> literalsFromForm = inputSubmission.getLiteralsFromForm();
		boolean findLiteral = literalsFromForm.entrySet().stream().filter(p -> p.getKey().equals(KEEP_LABEL_FIELD)).
				map(Map.Entry::getValue).flatMap(List::stream).filter(lit -> 
				KEEP_LABEL_FIELD_VALUE_ON.equals(lit.getLexicalForm())).findFirst().isPresent();

		Literal keepLabel = inputSubmission.createLiteral(String.valueOf(findLiteral), XSD.xboolean.getURI(), null);
		literalsFromForm.put("keepLabel", Arrays.asList(keepLabel));
	}
}
