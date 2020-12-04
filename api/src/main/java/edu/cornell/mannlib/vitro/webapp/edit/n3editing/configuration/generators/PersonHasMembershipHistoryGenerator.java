/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.vocabulary.XSD;

import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.AutocompleteRequiredInputValidator;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.DateTimeIntervalValidationVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.DateTimeWithPrecisionVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.EditConfigurationVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.ChildVClassesWithParent;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.FieldVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.RdfTypeOptions;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.preprocessors.BooleanValuesPreprocessor;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.validators.AntiXssValidation;
import edu.cornell.mannlib.vitro.webapp.utils.FrontEndEditingUtils.EditMode;
import edu.cornell.mannlib.vitro.webapp.utils.generators.EditModeUtils;

public class PersonHasMembershipHistoryGenerator extends VivoBaseGenerator implements EditConfigurationGenerator {

	final static String memberClassClass = vivofr + "CLS_0000001";
	public static final String API_MEMBER_CLASS = "vivofr:CLS_0000001";

	final static String researchOrganizationClass = vivoCore + "ResearchOrganization";
	final static String administrativeEntityClass = vivofr + "ORG_0000014";

	final static String orgClass = "http://xmlns.com/foaf/0.1/Organization";
	final static String membershipInOrgPred = vivoCore + "relates";
	final static String memberClassInMembership = vivoCore + "relates";
	final static String orgForMembershipPred = vivoCore + "relatedBy";
	final static String membershipToInterval = vivoCore + "dateTimeInterval";
	final static String intervalType = vivoCore + "DateTimeInterval";
	final static String intervalToStart = vivoCore + "start";
	final static String intervalToEnd = vivoCore + "end";
	final static String dateTimeValueType = vivoCore + "DateTimeValue";
	final static String dateTimeValue = vivoCore + "dateTime";
	final static String dateTimePrecision = vivoCore + "dateTimePrecision";

	final static String[] genericMembershipClasses = { vivofr + "MMB_0000002", vivofr + "MMB_0000003",
			vivofr + "MMB_0000004", vivofr + "MMB_0000005", vivofr + "MMB_0000006" };
	final static String[] preciseMembershipClasses = { vivofr + "MMB_0000007", vivofr + "MMB_0000008",
			ehess + "MBE_0000001", ehess + "MBE_0000002", ehess + "MBE_0000005", ehess + "MBE_0000006",
			ehess + "MBE_0000007" };

	public static final String[] ALLOWED_EHESS_ORGTYPES_MEMBERSHIP_EDITION_URIS = { researchOrganizationClass,
			administrativeEntityClass };

	public PersonHasMembershipHistoryGenerator() {
	}

	// There are 4 modes that this form can be in:
	// 1. Add. There is a subject and a predicate but no membership and
	// nothing else.
	//
	// 2. Normal edit where everything should already be filled out.
	// There is a subject, a object and an individual on
	// the other end of the object's core:personInOrganization stmt.
	//
	// 3. Repair a bad role node. There is a subject, predicate and object
	// but there is no individual on the other end of the object's
	// core:personInOrganization stmt. This should be similar to an add
	// but the form should be expanded.
	//
	// 4. Really bad node. multiple core:personInOrganization statements.

	@Override
	public EditConfigurationVTwo getEditConfiguration(VitroRequest vreq, HttpSession session) throws Exception {

		EditConfigurationVTwo conf = new EditConfigurationVTwo();

		initBasics(conf, vreq);
		initPropertyParameters(vreq, session, conf);
		initObjectPropForm(conf, vreq);

		conf.setTemplate("personHasMembershipHistory.ftl");

		conf.setVarNameForSubject("person");
		conf.setVarNameForPredicate("predicate");
		conf.setVarNameForObject("membership");

		conf.setN3Required(Arrays.asList(n3ForNewMembership, membershipTypeAssertion,
				memberClassAssertion, keepLabelAssertion));
		conf.setN3Optional(Arrays.asList(n3ForNewOrg, n3ForExistingOrg, membershipTitleAssertion, n3ForStart, n3ForEnd));

		conf.addNewResource("membership", DEFAULT_NS_FOR_NEW_RESOURCE);
		conf.addNewResource("newOrg", DEFAULT_NS_FOR_NEW_RESOURCE);
		conf.addNewResource("intervalNode", DEFAULT_NS_FOR_NEW_RESOURCE);
		conf.addNewResource("startNode", DEFAULT_NS_FOR_NEW_RESOURCE);
		conf.addNewResource("endNode", DEFAULT_NS_FOR_NEW_RESOURCE);

		// uris in scope: none
		// literals in scope: none

		conf.setUrisOnform(Arrays.asList("existingOrg", "orgType", "membershipType", "memberClass"));
		conf.setLiteralsOnForm(Arrays.asList("membershipTitle", "orgLabel", "orgLabelDisplay", "keepLabel"));

		conf.addSparqlForExistingLiteral("orgLabel", orgLabelQuery);
		conf.addSparqlForExistingLiteral("membershipTitle", membershipTitleQuery);
		conf.addSparqlForExistingLiteral("startField-value", existingStartDateQuery);
		conf.addSparqlForExistingLiteral("endField-value", existingEndDateQuery);
		conf.addSparqlForExistingLiteral("keepLabel", existingKeepLabelQuery);

		conf.addSparqlForExistingUris("existingOrg", existingOrgQuery);
		conf.addSparqlForExistingUris("orgType", orgTypeQuery);
		conf.addSparqlForExistingUris("memberClass", existingMemberClassQuery);
		conf.addSparqlForExistingUris("membershipType", membershipTypeQuery);
		conf.addSparqlForExistingUris("intervalNode", existingIntervalNodeQuery);
		conf.addSparqlForExistingUris("startNode", existingStartNodeQuery);
		conf.addSparqlForExistingUris("endNode", existingEndNodeQuery);
		conf.addSparqlForExistingUris("startField-precision", existingStartPrecisionQuery);
		conf.addSparqlForExistingUris("endField-precision", existingEndPrecisionQuery);
		// HACK EHESS validator non-empty removed Redmine 1193
		conf.addField(new FieldVTwo().setName("membershipTitle").setRangeDatatypeUri(XSD.xstring.toString()));

		conf.addField(new FieldVTwo().setName("membershipType").setValidators(list("nonempty"))
				.setOptions(new RdfTypeOptions(ArrayUtils.addAll(genericMembershipClasses, preciseMembershipClasses))));

		conf.addField(new FieldVTwo().setName("existingOrg")); // options set in
																// browser by
																// auto complete
																// JS

		conf.addField(new FieldVTwo().setName("memberClass").setValidators(list("nonempty"))
				.setOptions(new ChildVClassesWithParent(memberClassClass)));

		conf.addField(new FieldVTwo().setName("orgLabel").setRangeDatatypeUri(XSD.xstring.toString())
				.setValidators(list("datatype:" + XSD.xstring.toString())));

		conf.addField(new FieldVTwo().setName("orgLabelDisplay").setRangeDatatypeUri(XSD.xstring.toString()));

		conf.addField(new FieldVTwo().setName("keepLabel").setRangeDatatypeUri(XSD.xboolean.toString()));

		conf.addField(new FieldVTwo().setName("orgType")
				.setOptions(new RdfTypeOptions(researchOrganizationClass, administrativeEntityClass)));

		conf.addField(new FieldVTwo().setName("startField").setEditElement(new DateTimeWithPrecisionVTwo(null,
				VitroVocabulary.Precision.YEAR.uri(), VitroVocabulary.Precision.NONE.uri())));

		conf.addField(new FieldVTwo().setName("endField").setEditElement(new DateTimeWithPrecisionVTwo(null,
				VitroVocabulary.Precision.YEAR.uri(), VitroVocabulary.Precision.NONE.uri())));

		conf.addValidator(new DateTimeIntervalValidationVTwo("startField", "endField"));
		conf.addValidator(new AntiXssValidation());
		conf.addValidator(new AutocompleteRequiredInputValidator("existingOrg", "orgLabel"));
		// conf.addValidator(new
		// RelationshipMandatoryLabelValidator("membershipType",
		// "membershipTitle", preciseFunctionClasses));

		// Convert values from forms to xsd booleans
		conf.addEditSubmissionPreprocessor(new BooleanValuesPreprocessor(conf));
		// Adding additional data, specifically edit mode
		addFormSpecificData(conf, vreq);
		prepare(vreq, conf);
		return conf;
	}

	final static String n3ForNewMembership = "@prefix core: <" + vivoCore + "> . \n"
			+ "?person core:relatedBy  ?membership . \n" + "?membership a  ?membershipType . \n"
	        + "?membership <" + keepLabelPred + ">  ?keepLabel . \n"
			+ "?membership core:relates ?person ; ";

	final static String membershipTitleAssertion = "?membership <" + label + "> ?membershipTitle .";

	final static String memberClassAssertion = "@prefix core: <" + vivoCore + "> . \n"
			+ "?membership <" + memberClassInMembership + "> ?memberClass .";

	final static String membershipTypeAssertion = "?membership a ?membershipType .";

	final static String keepLabelAssertion = "?membership <" + keepLabelPred + "> ?keepLabel .";

	final static String n3ForNewOrg = "?membership <" + membershipInOrgPred + "> ?newOrg . \n" + "?newOrg <"
			+ orgForMembershipPred + "> ?membership . \n" + "?newOrg <" + label + "> ?orgLabel . \n"
			+ "?newOrg a ?orgType .";

	final static String n3ForExistingOrg = "?membership <" + membershipInOrgPred + "> ?existingOrg . \n"
			+ "?existingOrg <" + orgForMembershipPred + "> ?membership . \n" + "?existingOrg a ?orgType .";

	final static String n3ForStart = "?membership <" + membershipToInterval + "> ?intervalNode . \n"
			+ "?intervalNode a <" + intervalType + "> . \n" + "?intervalNode <" + intervalToStart + "> ?startNode . \n"
			+ "?startNode a <" + dateTimeValueType + "> . \n" + "?startNode  <" + dateTimeValue
			+ "> ?startField-value . \n" + "?startNode  <" + dateTimePrecision + "> ?startField-precision . \n";

	final static String n3ForEnd = "?membership <" + membershipToInterval + "> ?intervalNode . \n" + "?intervalNode a <"
			+ intervalType + "> . \n" + "?intervalNode <" + intervalToEnd + "> ?endNode . \n" + "?endNode a <"
			+ dateTimeValueType + "> . \n" + "?endNode  <" + dateTimeValue + "> ?endField-value . \n" + "?endNode  <"
			+ dateTimePrecision + "> ?endField-precision . \n";

	// Queries for existing values
	final static String orgLabelQuery = "SELECT ?existingOrgLabel WHERE { \n" + "  ?membership <" + membershipInOrgPred
			+ "> ?existingOrg . \n" + "  ?existingOrg a <" + orgClass + "> . \n" + "  ?existingOrg <" + label
			+ "> ?existingOrgLabel . \n" + "}";

	final static String membershipTitleQuery = "SELECT ?existingMembershipTitle WHERE { \n" + "?membership <" + label
			+ "> ?existingMembershipTitle . }";

	final static String existingKeepLabelQuery = "SELECT ?keepLabel WHERE { \n" + "optional {\n" + "?membership <"
			+ keepLabelPred + "> ?keepLabelSrc . " + "}\n" + "bind(coalesce(?keepLabelSrc, 'true') as ?keepLabel) \n"
			+ "}";

	final static String existingStartDateQuery = "SELECT ?existingDateStart WHERE { \n" + "  ?membership <"
			+ membershipToInterval + "> ?intervalNode . \n" + "  ?intervalNode a <" + intervalType + "> . \n"
			+ "  ?intervalNode <" + intervalToStart + "> ?startNode . \n" + "  ?startNode a <" + dateTimeValueType
			+ "> . \n" + "  ?startNode <" + dateTimeValue + "> ?existingDateStart . }";

	final static String existingEndDateQuery = "SELECT ?existingEndDate WHERE { \n" + "  ?membership <"
			+ membershipToInterval + "> ?intervalNode . \n" + "  ?intervalNode a <" + intervalType + "> . \n "
			+ "  ?intervalNode <" + intervalToEnd + "> ?endNode . \n" + "  ?endNode a <" + dateTimeValueType + "> . \n"
			+ "  ?endNode <" + dateTimeValue + "> ?existingEndDate . }";

	final static String existingOrgQuery = "SELECT ?existingOrg WHERE { \n" + "  ?membership <" + membershipInOrgPred
			+ "> ?existingOrg . \n" + "  ?existingOrg a <" + orgClass + ">  }";

	final static String existingMemberClassQuery = "SELECT ?existingMemberClass WHERE { \n" + "  ?membership <"
			+ membershipInOrgPred + "> ?existingMemberClass . }";

	final static String orgTypeQuery = "PREFIX rdfs: <" + rdfs + "> \n" + "SELECT ?existingOrgType WHERE { \n"
			+ "  ?membership <" + membershipInOrgPred + "> ?existingOrg . \n" + "  ?existingOrg a ?existingOrgType . \n"
			+ "  ?existingOrgType rdfs:subClassOf <" + orgClass + "> " + "} ";

	// Huda: changed this from rdf:type to vitro:mostSpecificType since
	// returning thing
	final static String membershipTypeQuery = "PREFIX vitro: <" + VitroVocabulary.vitroURI + "> \n"
			+ "SELECT ?existingMembershipType WHERE { \n"
			+ "  ?membership vitro:mostSpecificType ?existingMembershipType . }";

	final static String existingIntervalNodeQuery = "SELECT ?existingIntervalNode WHERE { \n" + "  ?membership <"
			+ membershipToInterval + "> ?existingIntervalNode . \n" + "  ?existingIntervalNode a <" + intervalType
			+ "> . }";

	final static String existingStartNodeQuery = "SELECT ?existingStartNode WHERE { \n" + "  ?membership <"
			+ membershipToInterval + "> ?intervalNode . \n" + "  ?intervalNode a <" + intervalType + "> . \n"
			+ "  ?intervalNode <" + intervalToStart + "> ?existingStartNode . \n" + "  ?existingStartNode a <"
			+ dateTimeValueType + "> .}   ";

	final static String existingEndNodeQuery = "SELECT ?existingEndNode WHERE { \n" + "  ?membership <"
			+ membershipToInterval + "> ?intervalNode . \n" + "  ?intervalNode a <" + intervalType + "> . \n"
			+ "  ?intervalNode <" + intervalToEnd + "> ?existingEndNode . \n" + "  ?existingEndNode a <"
			+ dateTimeValueType + "> } ";

	final static String existingStartPrecisionQuery = "SELECT ?existingStartPrecision WHERE { \n" + "  ?membership <"
			+ membershipToInterval + "> ?intervalNode . \n" + "  ?intervalNode a <" + intervalType + "> . \n"
			+ "  ?intervalNode <" + intervalToStart + "> ?startNode . \n" + "  ?startNode a  <" + dateTimeValueType
			+ "> . \n" + "  ?startNode <" + dateTimePrecision + "> ?existingStartPrecision . }";

	final static String existingEndPrecisionQuery = "SELECT ?existingEndPrecision WHERE { \n" + "  ?membership <"
			+ membershipToInterval + "> ?intervalNode . \n" + "  ?intervalNode a <" + intervalType + "> . \n"
			+ "  ?intervalNode <" + intervalToEnd + "> ?endNode . \n" + "  ?endNode a <" + dateTimeValueType + "> . \n"
			+ "  ?endNode <" + dateTimePrecision + "> ?existingEndPrecision . }";

	// Adding form specific data such as edit mode
	public void addFormSpecificData(EditConfigurationVTwo editConfiguration, VitroRequest vreq) {
		editConfiguration.addFormSpecificData("editMode", getEditMode(vreq).name().toLowerCase());
		editConfiguration.addFormSpecificData("orgTypes",
				StringUtils.join(ALLOWED_EHESS_ORGTYPES_MEMBERSHIP_EDITION_URIS, ","));
		// Needed for client side processing
		editConfiguration.addFormSpecificData("genericMembershipClasses", genericMembershipClasses); 
		editConfiguration.addFormSpecificData("apiMemberClass", API_MEMBER_CLASS);
	}

	public EditMode getEditMode(VitroRequest vreq) {
		List<String> predicates = new ArrayList<String>();
		predicates.add(membershipInOrgPred);
		return EditModeUtils.getEditMode(vreq, predicates);
	}
}
