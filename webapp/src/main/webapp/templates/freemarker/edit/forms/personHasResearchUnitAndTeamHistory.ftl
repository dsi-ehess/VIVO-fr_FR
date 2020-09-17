<#-- $This file is distributed under the terms of the license in /doc/license.txt$ -->

<#-- Template for adding a statutoryMembership history-->

<#import "lib-vivo-form.ftl" as lvf>

<#--Retrieve certain edit configuration information-->
<#assign htmlForElements = editConfiguration.pageData.htmlForElements />
<#assign editMode = editConfiguration.pageData.editMode />
<#assign entryTypeForTitle = editConfiguration.pageData.entryTypeForTitle />

<#assign blankSentinel = "" />
<#if editConfigurationConstants?has_content && editConfigurationConstants?keys?seq_contains("BLANK_SENTINEL")>
    <#assign blankSentinel = editConfigurationConstants["BLANK_SENTINEL"] />
</#if>

<#--This flag is for clearing the label field on submission for an existing object being selected from autocomplete.
Set this flag on the input acUriReceiver where you would like this behavior to occur. -->
<#assign flagClearLabelForExisting = "flagClearLabelForExisting" />

<#--Get existing value for specific data literals and uris-->
<#assign existingResearchUnitValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "existingResearchUnit")/>
<#assign researchUnitLabelValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "researchUnitLabel")/>
<#assign researchUnitLabelDisplayValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "researchUnitLabelDisplay")/>
<#assign existingResearchTeamValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "existingResearchTeam")/>
<#assign researchTeamLabelValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "researchTeamLabel")/>
<#assign researchTeamLabelDisplayValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "researchTeamLabelDisplay")/>

<#--If edit submission exists, then retrieve validation errors if they exist-->
<#if editSubmission?has_content && editSubmission.submissionExists = true && editSubmission.validationErrors?has_content>
    <#assign submissionErrors = editSubmission.validationErrors/>
</#if>

<#assign disabledVal = ""/>
<#if editMode == "edit">
    <#assign formAction="${i18n().edit_capitalized}">
    <#assign submitButtonText="${i18n().save_changes}">
    <#assign disabledVal="disabled">
<#else>
    <#assign formAction="${i18n().create_capitalized}">
    <#assign submitButtonText="${i18n().create_entry}">
    <#assign disabledVal="">
</#if>

<#assign requiredHint="<span class='requiredHint'> *</span>"/>
<#assign yearHint     = "<span class='hint'>(${i18n().year_hint_format})</span>" />

<h2>${formAction} ${i18n()[entryTypeForTitle]} ${editConfiguration.subjectName}</h2>

<#--Display error messages if any-->
<#if submissionErrors?has_content>
    <#if researchUnitLabelDisplayValue?has_content >
        <#assign researchUnitLabelValue = researchUnitLabelDisplayValue />
    </#if>
    <#if researchTeamLabelDisplayValue?has_content >
        <#assign researchTeamLabelValue = researchTeamLabelDisplayValue />
    </#if>
    <section id="error-alert" role="alert">
        <img src="${urls.images}/iconAlert.png" width="24" height="24" alert="${i18n().error_alert_icon}" />
        <p>
        <#--below shows examples of both printing out all error messages and checking the error message for a specific field-->
        <#list submissionErrors?keys as errorFieldName>
            <#if errorFieldName == "startField">
                <#if submissionErrors[errorFieldName]?contains("before")>
                    ${i18n().start_year_must_precede_end}
                <#else>
                    ${submissionErrors[errorFieldName]}
                </#if>
        	    <br />
            <#elseif errorFieldName == "endField">
                <#if submissionErrors[errorFieldName]?contains("after")>
                    ${i18n().end_year_must_be_later}
                <#else>
                    ${submissionErrors[errorFieldName]}
                </#if>
            </#if>
        </#list>
        <#--Checking if researchUnit Name field is empty-->
         <#if lvf.submissionErrorExists(editSubmission, "researchUnit")>
             ${i18n().select_an_organization_name}<br />
         </#if>
        </p>
    </section>
</#if>

<@lvf.unsupportedBrowser urls.base />

<form class="customForm" action ="${submitUrl}" class="customForm noIE67" role="${formAction} statutory membership entry">
    <p>
        <label for="researchUnitLabel">${i18n().researchUnitTypeName} ${i18n().name_capitalized} ${requiredHint}</label>
        <input type="text" name="researchUnitLabel" id="researchUnitLabel" acGroupName="researchUnit" size="50" class="acSelector" value="${researchUnitLabelValue}" >
        <input class="display" type="hidden" id="researchUnitDisplay" acGroupName="researchUnit" name="researchUnitLabelDisplay" value="${researchUnitLabelDisplayValue}">
    </p>
    <div class="acSelection" acGroupName="researchUnit">
        <p class="inline">
            <label>${i18n().selected_research_unit}:</label>
            <span class="acSelectionInfo"></span>
            <a href="" class="verifyMatch"  title="${i18n().verify_match_capitalized}">(${i18n().verify_match_capitalized}</a> ${i18n().or}
            <a href="#" class="changeSelection" id="changeSelectionResearchUnit">${i18n().change_selection})</a>
        </p>
        <input class="acUriReceiver" type="hidden" id="researchUnitUri" name="existingResearchUnit" value="${existingResearchUnitValue}" ${flagClearLabelForExisting}="true" />
    </div>
    <p>
        <label for="researchTeamLabel">${i18n().researchTeamTypeName} ${i18n().name_capitalized}</label>
        <input type="text" name="researchTeamLabel" id="researchTeamLabel" acGroupName="researchTeam" size="50" class="acSelector" value="${researchTeamLabelValue}" >
        <input class="display" type="hidden" id="researchTeamDisplay" acGroupName="researchTeam" name="researchTeamLabelDisplay" value="${researchTeamLabelDisplayValue}">
    </p>
    <div class="acSelection" acGroupName="researchTeam">
        <p class="inline">
            <label>${i18n().selected_research_team}:</label>
            <span class="acSelectionInfo"></span>
            <a href="" class="verifyMatch"  title="${i18n().verify_match_capitalized}">(${i18n().verify_match_capitalized}</a> ${i18n().or}
            <a href="#" class="changeSelection" id="changeSelectionResearchTeam">${i18n().change_selection})</a>
        </p>
        <input class="acUriReceiver" type="hidden" id="researchTeamUri" name="existingResearchTeam" value="${existingResearchTeamValue}" ${flagClearLabelForExisting}="true" />
    </div>


    <p></p>
<#--Need to draw edit elements for dates here-->
       <#if htmlForElements?keys?seq_contains("startField")>
  			<label class="dateTime" for="startField">${i18n().start_capitalized}</label>
           ${htmlForElements["startField"]} ${yearHint}
       </#if>
    <p></p>
       <#if htmlForElements?keys?seq_contains("endField")>
  			<label class="dateTime" for="endField">${i18n().end_capitalized}</label>
           ${htmlForElements["endField"]} ${yearHint}
       </#if>

<#--End draw elements-->

    <input type="hidden" name = "editKey" value="${editKey}" role="input"/>

    <p class="submit">
        <#if editMode == "edit">
            <input type="submit" id="submit" name="submit-${formAction}" value="${submitButtonText}" class="submit" />
        <#else>
            <input type="submit" id="submit" name="submit-${formAction}" value="${submitButtonText}" class="submit" />
        </#if>

        <span class="or"> ${i18n().or} </span><a class="cancel" href="${editConfiguration.cancelUrl}" title="${i18n().cancel_title}">${i18n().cancel_link}</a>
    </p>
    <p class="requiredHint"  id="requiredLegend" >* ${i18n().required_fields}</p>

</form>

<script type="text/javascript">
    var customFormData  = {
        acUrl: '${urls.base}/autocomplete?tokenize=true',
        acSelectOnly : 'true',
        acTypes: {researchUnit: 'http://data.ehess.fr/ontology/vivo#UniteDeRecherche,http://data.ehess.fr/ontology/vivo#EntiteAdministrative', researchTeam : 'http://data.ehess.fr/ontology/vivo#ComposanteDUnite'},
        acMultipleTypes: 'true',
        editMode: '${editMode}',
        defaultTypeName: 'researchUnit', // used in repair mode, to generate button text and org name field label
        multipleTypeNames: {researchUnitLabel:  '${i18n().researchUnitTypeName}', researchTeamLabel: '${i18n().researchTeamTypeName}'},
        baseHref: '${urls.base}/individual?uri=',
        blankSentinel: '${blankSentinel}',
        flagClearLabelForExisting: '${flagClearLabelForExisting}'
    };
    var i18nStrings = {
        selectAnExisting: '${i18n().select_an_existing}',
        selectedString: '${i18n().selected}',
        orCreateNewOne: ''
    };
</script>

${stylesheets.add('<link rel="stylesheet" href="${urls.base}/js/jquery-ui/css/smoothness/jquery-ui-1.8.9.custom.css" />')}
${stylesheets.add('<link rel="stylesheet" href="${urls.base}/templates/freemarker/edit/forms/css/customForm.css" />')}
${stylesheets.add('<link rel="stylesheet" href="${urls.base}/templates/freemarker/edit/forms/css/customFormWithAutocomplete.css" />')}


${scripts.add('<script type="text/javascript" src="${urls.base}/js/jquery-ui/js/jquery-ui-1.8.9.custom.min.js"></script>',
'<script type="text/javascript" src="${urls.base}/js/customFormUtils.js"></script>',
'<script type="text/javascript" src="${urls.base}/js/extensions/String.js"></script>',
'<script type="text/javascript" src="${urls.base}/js/browserUtils.js"></script>',
'<script type="text/javascript" src="${urls.base}/js/jquery_plugins/jquery.bgiframe.pack.js"></script>',
'<script type="text/javascript" src="${urls.base}/templates/freemarker/edit/forms/js/customFormWithAutocomplete.js"></script>',
'<script type="text/javascript" src="${urls.base}/templates/freemarker/edit/forms/js/customFormWithAutocompleteForTeamMembership.js"></script>')}
