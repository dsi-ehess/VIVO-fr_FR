<#-- $This file is distributed under the terms of the license in LICENSE$ -->

<#-- Custom object property statement view for faux property "memberships". See the PropertyConfig.n3 file for details.
    
     This template must be self-contained and not rely on other variables set for the individual page, because it
     is also used to generate the property statement during a deletion.  
 -->

<#import "lib-sequence.ftl" as s>
<#import "lib-datetime.ftl" as dt>

<@showMembership statement />

<#-- Use a macro to keep variable assignments local; otherwise the values carry over to the
     next statement -->
<#macro showMembership statement>
    
    <#local posTitle>
    <#--HACK EHESS rename variable : membershipTitle does not exist, poisition lab il the rdfs lable of the poition, Redmine 1193-->
        <span itemprop="jobTitle">${statement.membershipTitle!i18n().unknown_membership}</span>
    </#local>
    <#local linkedIndividual>
        <#if statement.org??>
            <span itemprop="worksFor" itemscope itemtype="http://schema.org/Organization">
               <a href="${profileUrl(statement.uri("org"))}" title="${i18n().organization_name}"><span itemprop="name">${statement.orgName}</span></a>
            </span>
        <#else>
            <#-- This shouldn't happen, but we must provide for it -->
            <a href="${profileUrl(statement.uri("membership"))}" title="${i18n().missing_organization}">${i18n().missing_organization}</a>
        </#if>
    </#local>
    <#-- The sparql query returns both the org's parent (parentOrg) and grandparent (outerOrg).
         For now, we are only displaying the parent in the list view. -->
    <#local parentOrganization>
        <#if statement.parentOrg??>
            <span itemprop="worksFor" itemscope itemtype="http://schema.org/Organization">
                <a href="${profileUrl(statement.uri("parentOrg"))}" title="${i18n().parent_organization}"><span itemprop="name">${statement.parentOrgName!}</span></a>
            </span>
        </#if>
    </#local>
    
    ${linkedIndividual} <@dt.yearIntervalSpan "${statement.dateTimeStart!}" "${statement.dateTimeEnd!}" />

</#macro>
