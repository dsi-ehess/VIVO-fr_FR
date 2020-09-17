<#-- $This file is distributed under the terms of the license in /doc/license.txt$ -->

<#-- Custom object property statement view for faux property "positions". See the PropertyConfig.n3 file for details.
     This template must be self-contained and not rely on other variables set for the individual page, because it
     is also used to generate the property statement during a deletion.
 -->

<#import "lib-sequence.ftl" as s>
<#import "lib-datetime.ftl" as dt>

<#assign map = statement.getAllData()>

<@showPosition statement />


<#-- Use a macro to keep variable assignments local; otherwise the values carry over to the
     next statement -->
<#macro showPosition statement>


    <#local unitComponent>
        <#if statement.uniteRecherche??>
            <span itemprop="worksFor" itemscope itemtype="http://data.ehess.fr/ontology/vivo#UniteDeRecherche">
               <a href="${profileUrl(statement.uri("uniteRecherche"))}" title="${i18n().organization_name}"><span itemprop="name">${statement.labelAppStatutaire!"NC"}</span></a>
            </span>
        </#if>
    </#local>
    <#local teamComponent>
        <#if statement.composanteUnite??>
            <span itemprop="worksFor" itemscope itemtype="http://data.ehess.fr/ontology/vivo#ComposanteDUnite">
               <a href="${profileUrl(statement.uri("composanteUnite"))}" title="${i18n().organization_name}"><span itemprop="name">${statement.labelAppartenance}</span></a>
            </span>
       </#if>
    </#local>

<#-- The sparql query returns both the org's parent (middleOrg) and grandparent (outerOrg).
     For now, we are only displaying the parent in the list view. -->

    <@s.join [unitComponent, teamComponent! ]/>  <@dt.yearIntervalSpan "${statement.dateTimeStart!}" "${statement.dateTimeEnd!}" />
</#macro>
