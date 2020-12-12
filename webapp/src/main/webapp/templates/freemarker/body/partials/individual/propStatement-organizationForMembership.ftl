<#-- $This file is distributed under the terms of the license in LICENSE$ -->

<#-- Custom object property statement view for faux property "people". See the PropertyConfig.n3 file for details.

     This template must be self-contained and not rely on other variables set for the individual page, because it
     is also used to generate the property statement during a deletion.
 -->

<#import "lib-sequence.ftl" as s>
<#import "lib-datetime.ftl" as dt>
<#import "lib-individual-membership.ftl" as memfunc>
<@showMembershipForOrganization statement />

<#-- Use a macro to keep variable assignments local; otherwise the values carry over to the
     next statement -->
<#macro showMembershipForOrganization statement>
<#if statement.hideThis?has_content>
    <span class="hideThis">&nbsp;</span>
    <script type="text/javascript" >
        $('span.hideThis').parent().parent().addClass("hideThis");
        if ( jQuery.isEmptyObject($('h3#relatedBy-Membership').attr('class')) ) {
            $('h3#relatedBy-Membership').addClass('hiddenPeople');
        }
        $('span.hideThis').parent().remove();
    </script>
<#else>
    <#assign membershipTitleLabel = memfunc.computeStatementLabelOrg(statement)>
    
    <#local linkedIndividual>
        <#if statement.person??>
            <a href="${profileUrl(statement.uri("person"))}" title="${i18n().person_name}">${statement.personName}</a>
        <#else>
            <#-- This shouldn't happen, but we must provide for it -->
            <a href="${profileUrl(statement.uri("membership"))}" title="${i18n().missing_person_in_membership}">${i18n().missing_person_in_membership}</a>
        </#if>
    </#local>

    <@s.join [ linkedIndividual, membershipTitleLabel! ] /> <@dt.yearIntervalSpan "${statement.dateTimeStart!}" "${statement.dateTimeEnd!}" />
</#if>
</#macro>
