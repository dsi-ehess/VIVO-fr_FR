/* $This file is distributed under the terms of the license in /doc/license.txt$ */
package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators;

import org.apache.jena.vocabulary.XSD;
import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary;
import edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary.Precision;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.FirstAndLastNameValidator;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.DateTimeIntervalValidationVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.DateTimeWithPrecisionVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.EditConfigurationVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.ChildVClassesWithParent;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.FieldVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.validators.AntiXssValidation;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

public class OrganizationHasAssociateMemberHistoryGenerator extends OrganizationHasMemberHistoryGenerator {

	private final static String URI_ASSOCIATE_MEMBERSHIP_CLASS = ehess + "AppartenanceATitreDAssocie";
	private final static String URI_TARGET_ASSOCIATE_CLASS =  vivoCore + "Person";

	public String getMemberClass() {
		return URI_ASSOCIATE_MEMBERSHIP_CLASS;
	}
	public String getTargetMemberClass() {
		return URI_TARGET_ASSOCIATE_CLASS;
	}

	@Override
	protected String getTemplate() {
		return "organizationHasAssociateMemberHistory.ftl";
	}
}
