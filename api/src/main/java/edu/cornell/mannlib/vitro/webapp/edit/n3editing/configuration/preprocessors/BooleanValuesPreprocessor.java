/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.preprocessors;

import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.BaseEditSubmissionPreprocessorVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.EditConfigurationVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.MultiValueEditSubmission;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.vocabulary.XSD;

/*
 * This preprocessor is used to fix "on" and "off" values sent by html form on "keepLabel" field
 */

public class BooleanValuesPreprocessor extends BaseEditSubmissionPreprocessorVTwo {

	public BooleanValuesPreprocessor(EditConfigurationVTwo editConfig) {
		super(editConfig);
	}

	@Override
	public void preprocess(MultiValueEditSubmission inputSubmission, VitroRequest vreq) {
		Map<String, List<Literal>> literalsFromForm = inputSubmission.getLiteralsFromForm();
		Iterator<Entry<String, List<Literal>>> itr = literalsFromForm.entrySet().iterator();
		boolean bool = false;
		while (itr.hasNext()) {
			Entry<String, List<Literal>> curr = itr.next();
			if (curr.getKey().equals("keepLabel")) {
				Iterator<Literal> itr2 = curr.getValue().iterator();
				while (itr2.hasNext()) {
					Literal lit = itr2.next();
					if (lit.getLexicalForm().equals("on")) {
						bool = true;
						break;
					}
				}
			}
		}
		Literal keepLabel = inputSubmission.createLiteral(bool ? "true" : "false", XSD.xboolean.getURI(), null);
		//Literal keepLabel = inputSubmission.createLiteral(bool ? "true" : "false", null, null);
		ArrayList<Literal> keepLabels = new ArrayList<Literal>();
		keepLabels.add(keepLabel);
		literalsFromForm.remove("keepLabel");
		literalsFromForm.put("keepLabel", keepLabels);
	}

}
