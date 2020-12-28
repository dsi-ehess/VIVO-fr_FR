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
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.RelationshipMandatoryLabelValidator;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.DateTimeIntervalValidationVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.DateTimeWithPrecisionVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.EditConfigurationVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.FieldVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.RdfTypeOptions;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.preprocessors.BooleanValuesPreprocessor;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.validators.AntiXssValidation;
import edu.cornell.mannlib.vitro.webapp.i18n.I18n;
import edu.cornell.mannlib.vitro.webapp.utils.FrontEndEditingUtils.EditMode;
import edu.cornell.mannlib.vitro.webapp.utils.generators.EditModeUtils;

public class PersonHasFunctionHistoryGenerator extends VivoBaseGenerator implements
        EditConfigurationGenerator {

	final static String universityClass = vivoCore + "ResearchOrganization";
	
    final static String orgClass = "http://xmlns.com/foaf/0.1/Organization";
    final static String functionInOrgPred = vivoCore + "relates";
    final static String orgForFunctionPred = vivoCore + "relatedBy";
    final static String functionToInterval = vivoCore + "dateTimeInterval";
    final static String intervalType = vivoCore + "DateTimeInterval";
    final static String intervalToStart = vivoCore + "start";
    final static String intervalToEnd = vivoCore + "end";
    final static String dateTimeValueType = vivoCore + "DateTimeValue";
    final static String dateTimeValue = vivoCore + "dateTime";
    final static String dateTimePrecision = vivoCore + "dateTimePrecision";
	final static String[] institutionClasses = { 
			vivoCore + "University" , 
			vivofr + "ORG_0000016", 
			vivofr + "ORG_0000017", 
			vivofr + "ORG_0000018", 
			vivofr + "ORG_0000019", 
			vivofr + "ORG_0000020", 
			vivofr + "ORG_0000021",
			vivofr + "ORG_0000022"
			};
	final static String[] genericFunctionClasses = { vivofr + "FNC_0000002", vivofr + "FNC_0000004",
			vivofr + "FNC_0000005", vivofr + "FNC_0000006", vivofr + "FNC_0000007" };
	final static String[] preciseFunctionClasses = { vivofr + "FNC_0000008",
			vivofr + "FNC_0000009", vivofr + "FNC_0000010", vivofr + "FNC_0000011", vivofr + "FNC_0000012",
			vivofr + "FNC_0000013", vivofr + "FNC_0000014", vivofr + "FNC_0000015", vivofr + "FNC_0000016",
			vivofr + "FNC_0000017", vivofr + "FNC_0000018", vivofr + "FNC_0000019", vivofr + "FNC_0000020",
			vivofr + "FNC_0000021" };

	public static final String[] ALLOWED_EHESS_ORGTYPES_FUNCTION_EDITION_URIS = { universityClass };
	
    public PersonHasFunctionHistoryGenerator() {}

    // There are 4 modes that this form can be in:
    //  1. Add. There is a subject and a predicate but no position and
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

        conf.setTemplate("personHasFunctionHistory.ftl");

        conf.setVarNameForSubject("person");
        conf.setVarNameForPredicate("predicate");
        conf.setVarNameForObject("function");

        conf.setN3Required( Arrays.asList( n3ForNewFunction,                                           
                                           functionTypeAssertion,
                                           keepLabelAssertion) );
        conf.setN3Optional( Arrays.asList( functionTitleAssertion, n3ForNewOrg, n3ForExistingOrg, n3ForStart, n3ForEnd ) );

        conf.addNewResource("function", DEFAULT_NS_FOR_NEW_RESOURCE);
        conf.addNewResource("newOrg", DEFAULT_NS_FOR_NEW_RESOURCE);
        conf.addNewResource("intervalNode", DEFAULT_NS_FOR_NEW_RESOURCE);
        conf.addNewResource("startNode", DEFAULT_NS_FOR_NEW_RESOURCE);
        conf.addNewResource("endNode", DEFAULT_NS_FOR_NEW_RESOURCE);

        //uris in scope: none
        //literals in scope: none

        conf.setUrisOnform(Arrays.asList("existingOrg", "orgType", "functionType"));
        conf.setLiteralsOnForm(Arrays.asList("functionTitle", "orgLabel", "orgLabelDisplay", "keepLabel"));

        conf.addSparqlForExistingLiteral("orgLabel", orgLabelQuery);
        conf.addSparqlForExistingLiteral("functionTitle", functionTitleQuery);
        conf.addSparqlForExistingLiteral("keepLabel", existingKeepLabelQuery);
        conf.addSparqlForExistingLiteral(
                "startField-value", existingStartDateQuery);
        conf.addSparqlForExistingLiteral(
                "endField-value", existingEndDateQuery);

        conf.addSparqlForExistingUris("existingOrg", existingOrgQuery);
        conf.addSparqlForExistingUris("orgType", orgTypeQuery);
        conf.addSparqlForExistingUris("functionType", functionTypeQuery);
        conf.addSparqlForExistingUris(
                "intervalNode", existingIntervalNodeQuery);
        conf.addSparqlForExistingUris("startNode", existingStartNodeQuery);
        conf.addSparqlForExistingUris("endNode", existingEndNodeQuery);
        conf.addSparqlForExistingUris("startField-precision",
                existingStartPrecisionQuery);
        conf.addSparqlForExistingUris("endField-precision",
                existingEndPrecisionQuery);

        conf.addField( new FieldVTwo().
                setName("functionTitle")
                .setRangeDatatypeUri( XSD.xstring.toString() ) );

        conf.addField( new FieldVTwo().
                setName("functionType").
                setValidators( list("nonempty") ).
                setOptions(
                		new RdfTypeOptions(ArrayUtils.addAll(genericFunctionClasses , preciseFunctionClasses))));


        conf.addField( new FieldVTwo().
                setName("existingOrg")); //options set in browser by auto complete JS

        conf.addField( new FieldVTwo().
                setName("orgLabel").
                setRangeDatatypeUri(XSD.xstring.toString() ).
                setValidators( list("datatype:" + XSD.xstring.toString()) ) );

        conf.addField( new FieldVTwo().
                setName("orgLabelDisplay").
                setRangeDatatypeUri(XSD.xstring.toString() ) );

        conf.addField( new FieldVTwo().
                setName("keepLabel").
                setRangeDatatypeUri(XSD.xboolean.toString() ) );

        conf.addField( new FieldVTwo().
                setName("orgType").
                setOptions(
                        new RdfTypeOptions(institutionClasses)));
        
        conf.addField( new FieldVTwo().setName("startField").
                setEditElement(
                        new DateTimeWithPrecisionVTwo(null,
                                VitroVocabulary.Precision.YEAR.uri(),
                                VitroVocabulary.Precision.NONE.uri())
                              )
                );

        conf.addField( new FieldVTwo().setName("endField").
                setEditElement(
                        new DateTimeWithPrecisionVTwo(null,
                                VitroVocabulary.Precision.YEAR.uri(),
                                VitroVocabulary.Precision.NONE.uri())
                              )
                );

        conf.addValidator(new DateTimeIntervalValidationVTwo("startField","endField"));
        conf.addValidator(new AntiXssValidation());
        conf.addValidator(new AutocompleteRequiredInputValidator("existingOrg", "orgLabel"));
        
		String msgErreur = I18n.text(vreq, "enter_function_title_value");
        conf.addValidator(new RelationshipMandatoryLabelValidator("functionType", "functionTitle", preciseFunctionClasses, msgErreur));
        
        //Convert values from forms to xsd booleans
        conf.addEditSubmissionPreprocessor(
 			   new BooleanValuesPreprocessor(conf));
        //Adding additional data, specifically edit mode
        addFormSpecificData(conf, vreq);
        prepare(vreq, conf);
        return conf;
    }

    final static String n3ForNewFunction =
        "@prefix core: <" + vivoCore + "> . \n" +
        "?person core:relatedBy  ?function . \n" +
        "?function a  ?functionType . \n" +
        "?function <" + keepLabelPred + ">  ?keepLabel . \n" +
        "?function core:relates ?person ; ";

    final static String functionTitleAssertion =
        "?function <" + label + "> ?functionTitle .";

    final static String functionTypeAssertion =
        "?function a ?functionType .";

    final static String keepLabelAssertion =
        "?function <" + keepLabelPred + "> ?keepLabel .";

    final static String n3ForNewOrg =
        "?function <" + functionInOrgPred + "> ?newOrg . \n" +
        "?newOrg <" + orgForFunctionPred + "> ?function . \n" +
        "?newOrg <" + label + "> ?orgLabel . \n" +
        "?newOrg a ?orgType .";

    final static String n3ForExistingOrg =
        "?function <" + functionInOrgPred + "> ?existingOrg . \n" +
        "?existingOrg <" + orgForFunctionPred + "> ?function . \n" +
        "?existingOrg a ?orgType .";

    final static String n3ForStart =
        "?function <" + functionToInterval + "> ?intervalNode . \n" +
        "?intervalNode a <" + intervalType + "> . \n" +
        "?intervalNode <" + intervalToStart + "> ?startNode . \n" +
        "?startNode a <" + dateTimeValueType + "> . \n" +
        "?startNode  <" + dateTimeValue + "> ?startField-value . \n" +
        "?startNode  <" + dateTimePrecision + "> ?startField-precision . \n";

    final static String n3ForEnd =
        "?function <" + functionToInterval + "> ?intervalNode . \n" +
        "?intervalNode a <" + intervalType + "> . \n" +
        "?intervalNode <" + intervalToEnd + "> ?endNode . \n" +
        "?endNode a <" + dateTimeValueType + "> . \n" +
        "?endNode  <" + dateTimeValue + "> ?endField-value . \n" +
        "?endNode  <" + dateTimePrecision + "> ?endField-precision . \n";

// Queries for existing values
    final static String orgLabelQuery =
        "SELECT ?existingOrgLabel WHERE { \n" +
        "  ?function <" + functionInOrgPred + "> ?existingOrg . \n" +
        "  ?existingOrg a <" + orgClass + "> . \n" +
        "  ?existingOrg <" + label + "> ?existingOrgLabel . \n" +
        "}";

    final static String functionTitleQuery =
        "SELECT ?existingFunctionTitle WHERE { \n" +
        "?function <" + label + "> ?existingFunctionTitle . }";
    
	final static String existingKeepLabelQuery = "SELECT ?keepLabel WHERE { \n" 
			+ "optional {\n" 
			+ "?function <"+ keepLabelPred + "> ?keepLabelSrc . "
			+ "}\n"
			+ "bind(coalesce(?keepLabelSrc, 'true') as ?keepLabel) \n"
			+ "}";

    final static String existingStartDateQuery =
        "SELECT ?existingDateStart WHERE { \n" +
        "  ?function <" + functionToInterval + "> ?intervalNode . \n" +
        "  ?intervalNode a <" + intervalType + "> . \n" +
        "  ?intervalNode <" + intervalToStart + "> ?startNode . \n" +
        "  ?startNode a <" + dateTimeValueType +"> . \n" +
        "  ?startNode <" + dateTimeValue + "> ?existingDateStart . }";

    final static String existingEndDateQuery =
        "SELECT ?existingEndDate WHERE { \n" +
        "  ?function <" + functionToInterval + "> ?intervalNode . \n" +
        "  ?intervalNode a <" + intervalType + "> . \n " +
        "  ?intervalNode <" + intervalToEnd + "> ?endNode . \n" +
        "  ?endNode a <" + dateTimeValueType + "> . \n" +
        "  ?endNode <" + dateTimeValue + "> ?existingEndDate . }";

    final static String existingOrgQuery =
        "SELECT ?existingOrg WHERE { \n" +
        "  ?function <" + functionInOrgPred + "> ?existingOrg . \n" +
        "  ?existingOrg a <" + orgClass + ">  }";

    final static String existingDesignationQuery =
            "SELECT ?existingOrg WHERE { \n" +
                    "  ?function <" + functionInOrgPred + "> ?existingOrg . \n" +
                    "  ?existingOrg a <" + orgClass + ">  }";
    
    final static String orgTypeQuery =
        "PREFIX rdfs: <" + rdfs + "> \n" +
        "SELECT ?existingOrgType WHERE { \n" +
        "  ?function <" + functionInOrgPred + "> ?existingOrg . \n" +
        "  ?existingOrg a ?existingOrgType . \n" +
        "  ?existingOrgType rdfs:subClassOf <" + orgClass + "> " +
        "} ";

    //Huda: changed this from rdf:type to vitro:mostSpecificType since returning thing
    final static String functionTypeQuery =
    	"PREFIX vitro: <" + VitroVocabulary.vitroURI + "> \n" +
        "SELECT ?existingFunctionType WHERE { \n" +
        "  ?function vitro:mostSpecificType ?existingFunctionType . }";

    final static String existingIntervalNodeQuery =
        "SELECT ?existingIntervalNode WHERE { \n" +
        "  ?function <" + functionToInterval + "> ?existingIntervalNode . \n" +
        "  ?existingIntervalNode a <" + intervalType + "> . }";

    final static String existingStartNodeQuery =
        "SELECT ?existingStartNode WHERE { \n" +
        "  ?function <" + functionToInterval + "> ?intervalNode . \n" +
        "  ?intervalNode a <" + intervalType + "> . \n" +
        "  ?intervalNode <" + intervalToStart + "> ?existingStartNode . \n" +
        "  ?existingStartNode a <" + dateTimeValueType + "> .}   ";

    final static String existingEndNodeQuery =
        "SELECT ?existingEndNode WHERE { \n" +
        "  ?function <" + functionToInterval + "> ?intervalNode . \n" +
        "  ?intervalNode a <" + intervalType + "> . \n" +
        "  ?intervalNode <" + intervalToEnd + "> ?existingEndNode . \n" +
        "  ?existingEndNode a <" + dateTimeValueType + "> } ";

    final static String existingStartPrecisionQuery =
        "SELECT ?existingStartPrecision WHERE { \n" +
        "  ?function <" + functionToInterval + "> ?intervalNode . \n" +
        "  ?intervalNode a <" + intervalType + "> . \n" +
        "  ?intervalNode <" + intervalToStart + "> ?startNode . \n" +
        "  ?startNode a  <" + dateTimeValueType + "> . \n" +
        "  ?startNode <" + dateTimePrecision + "> ?existingStartPrecision . }";

    final static String existingEndPrecisionQuery =
        "SELECT ?existingEndPrecision WHERE { \n" +
        "  ?function <" + functionToInterval + "> ?intervalNode . \n" +
        "  ?intervalNode a <" + intervalType + "> . \n" +
        "  ?intervalNode <" + intervalToEnd + "> ?endNode . \n" +
        "  ?endNode a <" + dateTimeValueType + "> . \n" +
        "  ?endNode <" + dateTimePrecision + "> ?existingEndPrecision . }";

    //Adding form specific data such as edit mode
  	public void addFormSpecificData(EditConfigurationVTwo editConfiguration, VitroRequest vreq) {
  		editConfiguration.addFormSpecificData("editMode", getEditMode(vreq).name().toLowerCase());  
  		editConfiguration.addFormSpecificData("orgTypes",
				StringUtils.join(ALLOWED_EHESS_ORGTYPES_FUNCTION_EDITION_URIS, ","));
  		// Needed for client side processing
  		editConfiguration.addFormSpecificData("genericFunctionClasses", genericFunctionClasses);  		
  	}

  	public EditMode getEditMode(VitroRequest vreq) {
  		List<String> predicates = new ArrayList<String>();
  		predicates.add(functionInOrgPred);
  		return EditModeUtils.getEditMode(vreq, predicates);
  	}

}
