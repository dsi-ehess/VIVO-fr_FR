<#-- $This file is distributed under the terms of the license in LICENSE$ -->

<#-- this template is for adding a person's membership to an organization -->

<#import "lib-vivo-form.ftl" as lvf>

<#if editConfiguration.objectUri?has_content>
    <#assign editMode = "edit">
<#else>
    <#assign editMode = "add">
</#if>

<#if editMode == "edit">
        <#assign titleVerb="${i18n().edit_capitalized}">
        <#assign submitButtonText="${i18n().save_changes}">
        <#assign disabledVal="disabled">
<#else>
        <#assign titleVerb="${i18n().create_capitalized}">
        <#assign submitButtonText="${i18n().create_entry}">
        <#assign disabledVal=""/>
</#if>

<#assign requiredHint = "<span class='requiredHint'> *</span>" />
<#assign yearHint     = "<span class='hint'>(${i18n().year_hint_format})</span>" />

<#assign blankSentinel = "" />
<#if editConfigurationConstants?has_content && editConfigurationConstants?keys?seq_contains("BLANK_SENTINEL")>
	<#assign blankSentinel = editConfigurationConstants["BLANK_SENTINEL"] />
</#if>

<#--This flag is for clearing the label field on submission for an existing object being selected from autocomplete.
Set this flag on the input acUriReceiver where you would like this behavior to occur. -->
<#assign flagClearLabelForExisting = "flagClearLabelForExisting" />
<#assign keepLabelValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "keepLabel")/>
<#assign membershipTitleValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "membershipTitle") />
<#assign membershipTypeValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "membershipType") />
<#assign memberClassValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "memberClass") />
<#assign existingPersonValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "existingPerson") />
<#assign personLabelValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "personLabel") />
<#assign personLabelDisplayValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "personLabelDisplay") />
<#assign firstNameValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "firstName") />
<#assign lastNameValue = lvf.getFormFieldValue(editSubmission, editConfiguration, "lastName") />
<#assign genericMembershipClasses = editConfiguration.pageData.genericMembershipClasses />

<#if editSubmission?has_content && editSubmission.submissionExists = true && editSubmission.validationErrors?has_content>
	<#assign submissionErrors = editSubmission.validationErrors/>
</#if>

<h2>${titleVerb}&nbsp;${i18n().membership_history_entry_for} ${editConfiguration.subjectName}</h2>

<#if submissionErrors?has_content>
    <#if personLabelDisplayValue?has_content >
        <#assign personLabelValue = personLabelDisplayValue />
    </#if>
    <section id="error-alert" role="alert">
        <img src="${urls.images}/iconAlert.png" width="24" height="24" alert="${i18n().error_alert_icon}" />
        <p>
        <#if lvf.submissionErrorExists(editSubmission, "membershipTitle")>
            ${i18n().enter_membership_title_value}<br />
        </#if>
        <#if lvf.submissionErrorExists(editSubmission, "membershipType")>
            ${i18n().enter_membership_type_value}<br />
        </#if>
        <#if lvf.submissionErrorExists(editSubmission, "personLabel")>
 	        ${i18n().enter_or_select_person_value}
        </#if>
        <#if lvf.submissionErrorExists(editSubmission, "memberClass")>
            ${i18n().enter_membership_memberClass_value}<br />
        </#if>
        <#list submissionErrors?keys as errorFieldName>
        	<#if errorFieldName == "startField">
        	    <#if submissionErrors[errorFieldName]?contains("before")>
        	        ${i18n().start_year_must_precede_end}
        	    <#else>
        	        ${submissionErrors[errorFieldName]}
        	    </#if>

        	<#elseif errorFieldName == "endField">
    	        <#if submissionErrors[errorFieldName]?contains("after")>
    	            ${i18n().end_year_must_be_later}
    	        <#else>
    	            ${submissionErrors[errorFieldName]}
    	        </#if>
	        <#else>
    	        ${submissionErrors[errorFieldName]}
	        </#if><br />
        </#list>
        </p>
    </section>
</#if>

<@lvf.unsupportedBrowser urls.base/>

<section id="organizationHasMembershipHistory" role="region">

	<form id="organizationHasMembershipHistory" class="customForm noIE67" action="${submitUrl}"  role="add/edit membership history">
	    <p>
	       <input id="keepLabelChkBox" type="checkbox" name="keepLabel" <#if keepLabelValue == "true" >checked="checked"</#if> />${i18n().func_keepLabel}
	    </p>
	    <p>
	        <label for="membershipTitle">${i18n().membership_title} ${requiredHint}</label>
	        <input size="30" type="text" id="membershipTitle" name="membershipTitle" value="${membershipTitleValue}" />
	    </p>

	    <label for="membershipType">${i18n().membership_type} ${requiredHint}</label>
        <#assign membershipTypeOpts = editConfiguration.pageData.membershipType />
	    <select id="membershipType" name="membershipType">
	        <option value="" selected="selected">${i18n().select_one}</option>
	        <#if (membershipTypeOpts?keys)??>
		        <#list membershipTypeOpts?keys as key>
                    <#if membershipTypeValue?has_content && membershipTypeValue = key>
                        <option value="${key}" selected >${membershipTypeOpts[key]}</option>
                    <#else>
                        <option value="${key}">${membershipTypeOpts[key]}</option>
                    </#if>
                </#list>
	        </#if>
	    </select>
  	    <p>
	        <label for="relatedIndLabel">${i18n().person_capitalized}: ${i18n().last_name} ${requiredHint}<span style="padding-left:322px">${i18n().first_name}  ${requiredHint}</span></label>
	            <input class="acSelector" size="50"  type="text" id="person" name="personLabel" acGroupName="person" value="${personLabelValue}" >
                <input  size="30"  type="text" id="firstName" name="firstName" value="${firstNameValue}" ><br />
                <input type="hidden" id="lastName" name="lastName" value="">
                <input class="display" type="hidden" acGroupName="person" id="personDisplay" name="personLabelDisplay" value="${personLabelDisplayValue}" >
	    </p>

	    <div class="acSelection" id="personAcSelection" acGroupName="person">
	        <p class="inline">
	            <label>${i18n().selected_person}:</label>
	            <span class="acSelectionInfo"></span>
                <a href="" class="verifyMatch"  title="${i18n().verify_match_capitalized}">(${i18n().verify_match_capitalized}</a> ${i18n().or}
                <a href="#" class="changeSelection" id="changeSelection">${i18n().change_selection})</a>
	        </p>
	        <input class="acUriReceiver" type="hidden" id="personUri" name="existingPerson" value="${existingPersonValue}" ${flagClearLabelForExisting}="true" />
	    </div>

        <br />
        <#--Need to draw edit elements for dates here-->
        <#assign htmlForElements = editConfiguration.pageData.htmlForElements />
        <#if htmlForElements?keys?seq_contains("startField")>
  	        <label class="dateTime" for="startField">${i18n().start_capitalized}</label>
  			${htmlForElements["startField"]} ${yearHint}
        </#if>
        <br/>
        <#if htmlForElements?keys?seq_contains("endField")>
  			<label class="dateTime" for="endField">${i18n().end_capitalized}</label>
  		 	${htmlForElements["endField"]} ${yearHint}
        </#if>
    	<#--End draw elements-->

        <div id="memberClassContent">
            <div id="left">    
                <label>${i18n().member_class_label} - </label><span id="memberClassOrg">${editConfiguration.subjectName} ${requiredHint}</span> 
            </div>       
            <div id="right">    
                <div id="memberClassRadioList">
	                <#assign memberClassOpts = editConfiguration.pageData.memberClass />
			        <#if (memberClassOpts?keys)??>
			            <#list memberClassOpts?keys as key>
			                <#if memberClassValue?has_content && memberClassValue = key>
			                    <input type="radio" name="memberClass" value="${key}" selected >${memberClassOpts[key]}</input><br/>
			                <#else>
			                    <input type="radio" name="memberClass" value="${key}">${memberClassOpts[key]}</input><br/>
			                </#if>
			            </#list>
			        </#if>
			        <#if !memberClassValue?has_content>
			             <input type="radio" name="memberClass" value="">Test2</input><br/>
                 </div>
            </div>
      </div>

    
        



        <p class="submit">
            <input type="hidden" id="editKey" name="editKey" value="${editKey}" />
            <input type="submit" id="submit" value="${submitButtonText}"/>
            <span class="or"> ${i18n().or} </span><a class="cancel" href="${cancelUrl}" title="${i18n().cancel_title}">${i18n().cancel_link}</a>
        </p>

	    <p id="requiredLegend" class="requiredHint">* ${i18n().required_fields}</p>

	</form>


	<script type="text/javascript">
	var customFormData  = {
	    acUrl: '${urls.base}/autocomplete?tokenize=true&stem=true',
        acTypes: {person: 'http://xmlns.com/foaf/0.1/Person'},
        relType: 'membership',
	    editMode: '${editMode}',
	    defaultTypeName: 'person',
	    baseHref: '${urls.base}/individual?uri=',
        blankSentinel: '${blankSentinel}',
        flagClearLabelForExisting: '${flagClearLabelForExisting}'

	};
	var i18nStrings = {
        selectAnExisting: '${i18n().select_an_existing?js_string}',
        orCreateNewOne: '${i18n().or_create_new_one?js_string}',
        selectedString: '${i18n().selected?js_string}'
    };
    </script>

</section>
<script type="text/javascript">
$(document).ready(function(){
    orgHasMembershipUtils.onLoad('${blankSentinel}');
});
</script>

${stylesheets.add('<link rel="stylesheet" href="${urls.base}/js/jquery-ui/css/smoothness/jquery-ui-1.12.1.css" />')}
${stylesheets.add('<link rel="stylesheet" href="${urls.base}/templates/freemarker/edit/forms/css/customForm.css" />')}
${stylesheets.add('<link rel="stylesheet" href="${urls.base}/templates/freemarker/edit/forms/css/customFormWithAutocomplete.css" />')}
${stylesheets.add('<link rel="stylesheet" href="${urls.base}/templates/freemarker/edit/forms/css/personAndOrganizationHasHistoryMembership.css" />')}

${scripts.add('<script type="text/javascript" src="${urls.base}/js/jquery-ui/js/jquery-ui-1.12.1.min.js"></script>',
             '<script type="text/javascript" src="${urls.base}/js/customFormUtils.js"></script>',
             '<script type="text/javascript" src="${urls.base}/js/extensions/String.js"></script>',
             '<script type="text/javascript" src="${urls.base}/templates/freemarker/edit/forms/js/orgHasMembershipUtils.js"></script>',
             '<script type="text/javascript" src="${urls.base}/js/browserUtils.js"></script>',
             '<script type="text/javascript" src="${urls.base}/js/jquery_plugins/jquery.bgiframe.pack.js"></script>',
             '<script type="text/javascript" src="${urls.base}/templates/freemarker/edit/forms/js/customFormWithAutocomplete.js"></script>')}
             
<#list genericMembershipClasses as genericMembershipClass>
<input type="hidden" id="generic-membership-class-${genericMembershipClass?index}" class="generic-membership-class-indicator" value=${genericMembershipClass} />
</#list>
${scripts.add('<script type="text/javascript" src="${urls.base}/js/relationship/keepRelationshipLabelFlag.js"></script>')}
