<#-- $This file is distributed under the terms of the license in LICENSE$ -->

<#-- Custom object property statement view for faux property "people". See the PropertyConfig.n3 file for details.

     This template must be self-contained and not rely on other variables set for the individual page, because it
     is also used to generate the property statement during a deletion.
 -->

<#import "lib-sequence.ftl" as s>
<#import "lib-datetime.ftl" as dt>
<@showFunction statement />

<#-- Use a macro to keep variable assignments local; otherwise the values carry over to the
     next statement -->
<#macro showFunction statement>
<#local fncTitle>
    <span itemprop="jobTitle">
    <#if statement.keepLabel?? && statement.keepLabel == "true">${statement.subClassLabel?cap_first}<#if (statement.functionTitle!statement.hrJobTitle!)?has_content>, ${statement.functionTitle!statement.hrJobTitle!}</#if>
    <#else>${(statement.functionTitle!statement.hrJobTitle!)?cap_first}
    </#if>
    </span>
</#local>
<#if statement.hideThis?has_content>
    <span class="hideThis">&nbsp;</span>
    <script type="text/javascript" >
        $('span.hideThis').parent().parent().addClass("hideThis");
        if ( jQuery.isEmptyObject($('h3#relatedBy-Function').attr('class')) ) {
            $('h3#relatedBy-Function').addClass('hiddenPeople');
        }
        $('span.hideThis').parent().remove();
    </script>
<#else>
    <#local linkedIndividual>
        <#if statement.person??>
            <a href="${profileUrl(statement.uri("person"))}" title="${i18n().person_name}">${statement.personName}</a>
        <#else>
            <#-- This shouldn't happen, but we must provide for it -->
            <a href="${profileUrl(statement.uri("function"))}" title="${i18n().missing_person_in_fnct}">${i18n().missing_person_in_fnct}</a>
        </#if>
    </#local>

    <@s.join [ fncTitle, linkedIndividual] /> <@dt.yearIntervalSpan "${statement.dateTimeStart!}" "${statement.dateTimeEnd!}" />
</#if>
</#macro>
