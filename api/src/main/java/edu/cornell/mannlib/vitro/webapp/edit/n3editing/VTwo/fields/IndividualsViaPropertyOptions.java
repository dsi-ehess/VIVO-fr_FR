/* $This file is distributed under the terms of the license in LICENSE$ */
package edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.beans.Individual;
import edu.cornell.mannlib.vitro.webapp.beans.ObjectProperty;
import edu.cornell.mannlib.vitro.webapp.beans.ObjectPropertyStatement;
import edu.cornell.mannlib.vitro.webapp.beans.VClass;
import edu.cornell.mannlib.vitro.webapp.dao.VClassDao;
import edu.cornell.mannlib.vitro.webapp.dao.WebappDaoFactory;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.EditConfigurationVTwo;
import edu.cornell.mannlib.vitro.webapp.i18n.I18nBundle;

public class IndividualsViaPropertyOptions implements FieldOptions {

	private static final Log log = LogFactory.getLog(IndividualsViaPropertyOptions.class);

	private static final String LEFT_BLANK = "";
	private String subjectUri;
	private String predicateUri;
	private String objectUri;
	private boolean hasSubclasses = true;

	private String defaultOptionLabel;

	public IndividualsViaPropertyOptions(String subjectUri, String predicateUri, String objectUri) throws Exception {
		super();

		if (subjectUri == null || subjectUri.equals("")) {
			throw new Exception("no subjectUri found for field ");
		}
		if (predicateUri == null || predicateUri.equals("")) {
			throw new Exception("no predicateUri found for field ");
		}

		this.subjectUri = subjectUri;
		this.predicateUri = predicateUri;
		this.objectUri = objectUri;
	}


	public IndividualsViaPropertyOptions setDefaultOptionLabel(String label) {
		this.defaultOptionLabel = label;
		return this;
	}

	@Override
	public Map<String, String> getOptions(EditConfigurationVTwo editConfig, String fieldName, WebappDaoFactory wDaoFact,
			I18nBundle i18n) {
		HashMap<String, String> optionsMap = new LinkedHashMap<String, String>();
		// first test to see whether there's a default "leave blank"
		// value specified with the literal options
		if ((defaultOptionLabel) != null) {
			optionsMap.put(LEFT_BLANK, defaultOptionLabel);
		}

		Individual subject = wDaoFact.getIndividualDao().getIndividualByURI(subjectUri);
		List<ObjectPropertyStatement> stmts = subject.getObjectPropertyStatements(predicateUri);
		List<Individual> stmts2 = subject.getRelatedIndividuals(predicateUri);
		List<ObjectProperty> stmts3 = subject.getPopulatedObjectPropertyList();
		
		List<Individual> individuals = stmts.stream().filter(obj -> obj.getObject().isVClass(objectUri))
				.map(ObjectPropertyStatement::getObject).collect(Collectors.toList());

		// individuals = FieldUtils.removeIndividualsAlreadyInRange(individuals,
		// stmts, predicateUri, objectUri);
		// Collections.sort(individuals,new compareIndividualsByName());a

		for (Individual ind : individuals) {
			String uri = ind.getURI();
			if (uri != null) {
				// The picklist should only display individuals with rdfs
				// labels.
				// SO changing the following line -- tlw72
				// String label = ind.getName().trim();
				String label = ind.getRdfsLabel();
				optionsMap.put(uri, label);
			}
		}
		return optionsMap;
	}

	public Comparator<String[]> getCustomComparator() {
		return null;
	}

	private String getMsTypeLocalName(String theUri, WebappDaoFactory wDaoFact) {
		VClassDao vcDao = wDaoFact.getVClassDao();
		VClass vClass = vcDao.getVClassByURI(theUri);
		String theType = "";
		if (vClass == null) {
			return "";
		} else {
			theType = ((vClass.getName() == null) ? "" : vClass.getName());
		}
		return theType;
	}

}
