<#-- $This file is distributed under the terms of the license in /doc/license.txt$ -->

<#-- List of memberships for the individual -->
<#include "lib-individual-membership.ftl">
<#assign memberships = propertyGroups.pullProperty("${core}relatedBy", "${vivofr}MMB_0000001")!>

<#if individual.person() >
    <#if memberships?has_content> <#-- true when the property is in the list, even if not populated (when editing) -->
        <ul id="individual-personInMembership" role="list">
            <@showMembership memberships editable />
        </ul>
    </#if>
</#if>



<#if individual.organization() >
    <#if memberships?has_content> <#-- true when the property is in the list, even if not populated (when editing) -->
        <#assign localName = memberships.localName>
        <#if editable > 
        <h2 id="${localName}" class="mainPropGroup" title="${memberships.publicDescription!}">${memberships.name} <@p.addLink memberships editable /> <@p.verboseDisplay memberships /></h2>
        </#if>
        <ul id="individual-personInMembership" role="list">
            <@p.objectProperty memberships editable />
        </ul>
    </#if>
</#if>