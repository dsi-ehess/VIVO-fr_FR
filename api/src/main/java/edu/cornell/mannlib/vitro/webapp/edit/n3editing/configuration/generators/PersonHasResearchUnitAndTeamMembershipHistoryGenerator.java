/* $This file is distributed under the terms of the license in /doc/license.txt$ */

package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators;

import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.AutocompleteRequiredInputValidator;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.DateTimeIntervalValidationVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.DateTimeWithPrecisionVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.EditConfigurationVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.FieldVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.preprocessors.RemoveEmptyResearchTeamForIndividualPreprocessor;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.validators.AntiXssValidation;
import edu.cornell.mannlib.vitro.webapp.utils.FrontEndEditingUtils.EditMode;
import edu.cornell.mannlib.vitro.webapp.utils.generators.EditModeUtils;
import org.apache.jena.vocabulary.XSD;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class PersonHasResearchUnitAndTeamMembershipHistoryGenerator extends VivoBaseGenerator implements
        EditConfigurationGenerator {

    final static String membershipInOrgPred = vivoCore + "relates";
    final static String orgForMembershipPred = vivoCore + "relatedBy";
    final static String membershipToInterval = vivoCore + "dateTimeInterval";
    final static String intervalType = vivoCore + "DateTimeInterval";
    final static String intervalToStart = vivoCore + "start";
    final static String intervalToEnd = vivoCore + "end";
    final static String dateTimeValueType = vivoCore + "DateTimeValue";
    final static String dateTimeValue = vivoCore + "dateTime";
    final static String dateTimePrecision = vivoCore + "dateTimePrecision";

    public PersonHasResearchUnitAndTeamMembershipHistoryGenerator() {
    }

    // There are 4 modes that this form can be in:
    //  1. Add. There is a subject and a predicate but no specificplus large, elle touche d'abord à la qualité des données. Membership and
    //     nothing else.
    //
    //  2. Normal edit where everything should already be filled out.
    //     There is a subject, a object and an individual on
    //     the other end of the object's core:personInOrganization stmt.
    //
    //  3. Repair a bad role node.  There is a subject, predicate and object
    //     but there is no individual on the other end of the object's
    //     core:personInOrganization stmt.  This should be similar to an add
    //     but the form should be expanded.
    //
    //  4. Really bad node. multiple core:personInOrganization statements.

    @Override
    public EditConfigurationVTwo getEditConfiguration(VitroRequest vreq,
                                                      HttpSession session) throws Exception {

        EditConfigurationVTwo conf = new EditConfigurationVTwo();

        initBasics(conf, vreq);
        initPropertyParameters(vreq, session, conf);
        initObjectPropForm(conf, vreq);

        conf.setTemplate("personHasResearchUnitAndTeamHistory.ftl");

        conf.setVarNameForSubject("person");
        conf.setVarNameForPredicate("predicate");
        conf.setVarNameForObject("specificResearchUnitMembership");

        conf.setN3Required(Arrays.asList(n3ForNewSpecificMembership, n3ForExistingResearchUnit));
        conf.setN3Optional(Arrays.asList(n3ForNewMembershipInExistingResearchTeam, n3ForStart, n3ForEnd));

        conf.addNewResource("specificResearchUnitMembership", DEFAULT_NS_FOR_NEW_RESOURCE);
        conf.addNewResource("specificTeamMembership", DEFAULT_NS_FOR_NEW_RESOURCE);
        conf.addNewResource("newResearchUnit", DEFAULT_NS_FOR_NEW_RESOURCE);
        conf.addNewResource("newResearchTeam", DEFAULT_NS_FOR_NEW_RESOURCE);
        conf.addNewResource("intervalNode", DEFAULT_NS_FOR_NEW_RESOURCE);
        conf.addNewResource("startNode", DEFAULT_NS_FOR_NEW_RESOURCE);
        conf.addNewResource("endNode", DEFAULT_NS_FOR_NEW_RESOURCE);

        //uris in scope: none
        //literals in scope: none

        conf.setUrisOnform(Arrays.asList("existingResearchUnit", "existingResearchTeam"));
        conf.setLiteralsOnForm(Arrays.asList("researchUnitLabel", "researchUnitLabelDisplay", "researchTeamLabel", "researchTeamLabelDisplay"));

        conf.addSparqlForExistingLiteral("researchUnitLabel", researchUnitLabelQuery);
        conf.addSparqlForExistingLiteral("researchTeamLabel", researchTeamLabelQuery);
        conf.addSparqlForExistingLiteral(
                "startField-value", existingStartDateQuery);
        conf.addSparqlForExistingLiteral(
                "endField-value", existingEndDateQuery);

        conf.addSparqlForExistingUris("existingResearchUnit", existingResearchUnitQuery);
        conf.addSparqlForExistingUris("existingResearchTeam", existingResearchTeamQuery);
        conf.addSparqlForExistingUris("specificTeamMembership", existingMembershipQuery);

        conf.addSparqlForExistingUris(
                "intervalNode", existingIntervalNodeQuery);
        conf.addSparqlForExistingUris("startNode", existingStartNodeQuery);
        conf.addSparqlForExistingUris("endNode", existingEndNodeQuery);
        conf.addSparqlForExistingUris("startField-precision",
                existingStartPrecisionQuery);
        conf.addSparqlForExistingUris("endField-precision",
                existingEndPrecisionQuery);

        conf.addField(new FieldVTwo().
                setName("existingResearchUnit")); //options set in browser by auto complete JS

        conf.addField(new FieldVTwo().
                setName("existingResearchTeam")); //options set in browser by auto complete JS

        conf.addField(new FieldVTwo().
                setName("researchUnitLabel").
                setRangeDatatypeUri(XSD.xstring.toString()).
                setValidators(list("datatype:" + XSD.xstring.toString())));

        conf.addField(new FieldVTwo().
                setName("researchTeamLabel").
                setRangeDatatypeUri(XSD.xstring.toString()).
                setValidators(list("datatype:" + XSD.xstring.toString())));

        conf.addField(new FieldVTwo().
                setName("researchUnitLabelDisplay").
                setRangeDatatypeUri(XSD.xstring.toString()));

        conf.addField(new FieldVTwo().
                setName("researchTeamLabelDisplay").
                setRangeDatatypeUri(XSD.xstring.toString()));

        conf.addField(new FieldVTwo().setName("startField").
                setEditElement(
                        new DateTimeWithPrecisionVTwo(null,
                                VitroVocabulary.Precision.YEAR.uri(),
                                VitroVocabulary.Precision.NONE.uri())
                )
        );

        conf.addField(new FieldVTwo().setName("endField").
                setEditElement(
                        new DateTimeWithPrecisionVTwo(null,
                                VitroVocabulary.Precision.YEAR.uri(),
                                VitroVocabulary.Precision.NONE.uri())
                )
        );

        conf.addValidator(new DateTimeIntervalValidationVTwo("startField", "endField"));
        conf.addValidator(new AntiXssValidation());

        conf.addValidator(new AutocompleteRequiredInputValidator("existingResearchUnit", "researchUnitLabel"));
        //conf.addValidator(new AutocompleteRequiredInputValidator("existingResearchTeam", "researchTeamLabel"));

        //Adding additional data, specifically edit mode
        addFormSpecificData(conf, vreq);
        prepare(vreq, conf);

        //conf.addEditSubmissionPreprocessor(new StatutoryMembershipPredicatePreprocessor(conf,  vreq.getWebappDaoFactory()));
        conf.addEditSubmissionPreprocessor(new RemoveEmptyResearchTeamForIndividualPreprocessor(conf));
        return conf;
    }

    final static String N3PrefixString =
            "@prefix core: <" + vivoCore + "> .\n" +
                    "@prefix ehess: <" + ehess + "> . \n";

    static final String select_prefixes =
            "PREFIX core: <" + vivoCore + "> \n" +
                    "PREFIX ehess: <" + ehess + "> \n";

    final String n3ForNewSpecificMembership =
            N3PrefixString +
                    "?person core:relatedBy  ?specificResearchUnitMembership . \n" +
                    "?specificResearchUnitMembership a <"+ getSpecificMembershipClass() +"> . \n" +
                    "?specificResearchUnitMembership <" + label + "> " + getSpecificResearchUnitMembershipLabel() + " . \n" +
                    "?specificResearchUnitMembership core:relates ?person ;";

    abstract String getSpecificResearchUnitMembershipLabel();
    abstract String getSpecificMembershipClass();
    abstract String getSpecificTeamMembershipLabel();

    final static String n3ForExistingResearchUnit =
            N3PrefixString +
                    "?specificResearchUnitMembership <" + membershipInOrgPred + "> ?existingResearchUnit . \n" +
                    "?existingResearchUnit <" + orgForMembershipPred + "> ?specificResearchUnitMembership .";

    final static String n3ForDeleteResearchUnit =
            N3PrefixString +
                    "?specificResearchUnitMembership <" + membershipInOrgPred + "> ?newResearchUnit . \n" +
                    "?newResearchUnit <" + orgForMembershipPred + "> ?specificResearchUnitMembership . \n" +
                    "?newResearchUnit <" + label + "> ?researchUnitLabel . \n" +
                    "?newResearchUnit a ehess:UniteDeRecherche .";

    final String n3ForNewMembershipInExistingResearchTeam =
            N3PrefixString +
                    "?person core:relatedBy ?specificTeamMembership . \n" +
                    "?specificTeamMembership a  <"+ getSpecificMembershipClass() +"> . \n" +
                    "?specificTeamMembership <" + label + "> " + getSpecificTeamMembershipLabel() + " . \n" +
                    "?specificTeamMembership core:relates ?person . \n" +
                    "?existingResearchTeam core:relatedBy ?specificTeamMembership . \n" +
                    "?specificTeamMembership core:relates ?existingResearchTeam . ";

    // Queries for existing values
    final static String researchUnitLabelQuery =
            select_prefixes +
                    "SELECT ?existingResearchUnitLabel WHERE { \n" +
                    "  ?specificResearchUnitMembership <" + membershipInOrgPred + "> ?existingResearchUnit . \n" +
                    "  ?existingResearchUnit a ehess:UniteDeRecherche . \n" +
                    "  ?existingResearchUnit <" + label + "> ?existingResearchUnitLabel . \n" +
                    "}";

    final String researchTeamLabelQuery =
            select_prefixes +
                    "SELECT ?existingResearchTeamLabel WHERE { \n" +
                    "  ?specificResearchUnitMembership <" + membershipInOrgPred + "> ?existingResearchUnit . \n" +
                    "  ?specificResearchUnitMembership core:relates ?person . \n" +
                    "  ?person core:relatedBy ?specificTeamMembership . \n" +
                    "  ?specificTeamMembership a  <"+ getSpecificMembershipClass() +"> . \n" +
                    "  ?specificTeamMembership core:relates ?researchTeam . \n" +
                    "  ?researchTeam a ehess:ComposanteDUnite . \n" +
                    "  ?researchTeam <" + label + "> ?existingResearchTeamLabel . \n" +
                    "}";

    final String existingMembershipQuery =
            select_prefixes +
                    "SELECT ?specificTeamMembership WHERE { \n" +
                    "  ?specificTeamMembership <" + membershipInOrgPred + "> ?person . \n" +
                    "  ?specificTeamMembership <" + membershipInOrgPred + "> ?existingResearchTeam . \n" +
                    "  ?existingResearchTeam a ehess:ComposanteDUnite  . \n" +
                    "  ?specificTeamMembership a <"+ getSpecificMembershipClass() +">  }";

    final static String existingResearchUnitQuery =
            "SELECT ?existingResearchUnit WHERE { \n" +
                    "  ?specificResearchUnitMembership <" + membershipInOrgPred + "> ?existingResearchUnit . \n" +
                    "  ?existingResearchUnit a <http://data.ehess.fr/ontology/vivo#UniteDeRecherche>  }";

    final String existingResearchTeamQuery =
            select_prefixes +
                    "SELECT ?existingResearchTeam WHERE { \n" +
                    "  ?specificTeamMembership <" + membershipInOrgPred + "> ?existingResearchTeam . \n" +
                    "  ?person core:relatedBy ?specificTeamMembership . \n" +
                    "  ?specificTeamMembership a  <"+ getSpecificMembershipClass() +"> . \n" +
                    "  ?specificTeamMembership core:relates ?researchTeam . \n" +
                    "  ?existingResearchTeam a ehess:ComposanteDUnite  }";


    final static String n3ForStart =
            "?specificResearchUnitMembership <" + membershipToInterval + "> ?intervalNode . \n" +
                    "?intervalNode a <" + intervalType + "> . \n" +
                    "?intervalNode <" + intervalToStart + "> ?startNode . \n" +
                    "?startNode a <" + dateTimeValueType + "> . \n" +
                    "?startNode  <" + dateTimeValue + "> ?startField-value . \n" +
                    "?startNode  <" + dateTimePrecision + "> ?startField-precision . \n";

    final static String n3ForEnd =
            "?specificResearchUnitMembership <" + membershipToInterval + "> ?intervalNode . \n" +
                    "?intervalNode a <" + intervalType + "> . \n" +
                    "?intervalNode <" + intervalToEnd + "> ?endNode . \n" +
                    "?endNode a <" + dateTimeValueType + "> . \n" +
                    "?endNode  <" + dateTimeValue + "> ?endField-value . \n" +
                    "?endNode  <" + dateTimePrecision + "> ?endField-precision . \n";


    final static String existingStartDateQuery =
            "SELECT ?existingDateStart WHERE { \n" +
                    "  ?specificResearchUnitMembership <" + membershipToInterval + "> ?intervalNode . \n" +
                    "  ?intervalNode a <" + intervalType + "> . \n" +
                    "  ?intervalNode <" + intervalToStart + "> ?startNode . \n" +
                    "  ?startNode a <" + dateTimeValueType + "> . \n" +
                    "  ?startNode <" + dateTimeValue + "> ?existingDateStart . }";

    final static String existingEndDateQuery =
            "SELECT ?existingEndDate WHERE { \n" +
                    "  ?specificResearchUnitMembership <" + membershipToInterval + "> ?intervalNode . \n" +
                    "  ?intervalNode a <" + intervalType + "> . \n " +
                    "  ?intervalNode <" + intervalToEnd + "> ?endNode . \n" +
                    "  ?endNode a <" + dateTimeValueType + "> . \n" +
                    "  ?endNode <" + dateTimeValue + "> ?existingEndDate . }";


    final static String existingIntervalNodeQuery =
            "SELECT ?existingIntervalNode WHERE { \n" +
                    "  ?specificResearchUnitMembership <" + membershipToInterval + "> ?existingIntervalNode . \n" +
                    "  ?existingIntervalNode a <" + intervalType + "> . }";

    final static String existingStartNodeQuery =
            "SELECT ?existingStartNode WHERE { \n" +
                    "  ?specificResearchUnitMembership <" + membershipToInterval + "> ?intervalNode . \n" +
                    "  ?intervalNode a <" + intervalType + "> . \n" +
                    "  ?intervalNode <" + intervalToStart + "> ?existingStartNode . \n" +
                    "  ?existingStartNode a <" + dateTimeValueType + "> .}   ";

    final static String existingEndNodeQuery =
            "SELECT ?existingEndNode WHERE { \n" +
                    "  ?specificResearchUnitMembership <" + membershipToInterval + "> ?intervalNode . \n" +
                    "  ?intervalNode a <" + intervalType + "> . \n" +
                    "  ?intervalNode <" + intervalToEnd + "> ?existingEndNode . \n" +
                    "  ?existingEndNode a <" + dateTimeValueType + "> } ";

    final static String existingStartPrecisionQuery =
            "SELECT ?existingStartPrecision WHERE { \n" +
                    "  ?specificResearchUnitMembership <" + membershipToInterval + "> ?intervalNode . \n" +
                    "  ?intervalNode a <" + intervalType + "> . \n" +
                    "  ?intervalNode <" + intervalToStart + "> ?startNode . \n" +
                    "  ?startNode a  <" + dateTimeValueType + "> . \n" +
                    "  ?startNode <" + dateTimePrecision + "> ?existingStartPrecision . }";

    final static String existingEndPrecisionQuery =
            "SELECT ?existingEndPrecision WHERE { \n" +
                    "  ?specificResearchUnitMembership <" + membershipToInterval + "> ?intervalNode . \n" +
                    "  ?intervalNode a <" + intervalType + "> . \n" +
                    "  ?intervalNode <" + intervalToEnd + "> ?endNode . \n" +
                    "  ?endNode a <" + dateTimeValueType + "> . \n" +
                    "  ?endNode <" + dateTimePrecision + "> ?existingEndPrecision . }";

    //Adding form specific data such as edit mode
    public void addFormSpecificData(EditConfigurationVTwo editConfiguration, VitroRequest vreq) {
        HashMap<String, Object> formSpecificData = new HashMap<String, Object>();
        formSpecificData.put("editMode", getEditMode(vreq).name().toLowerCase());
        formSpecificData.put("entryTypeForTitle", getEntryTypeForTitle());
        editConfiguration.setFormSpecificData(formSpecificData);
    }

    abstract String getEntryTypeForTitle();

    public EditMode getEditMode(VitroRequest vreq) {
        List<String> predicates = new ArrayList<String>();
        predicates.add(membershipInOrgPred);
        return EditModeUtils.getEditMode(vreq, predicates);
    }

}
