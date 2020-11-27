<#-- $This file is distributed under the terms of the license in /doc/license.txt$ -->

<#-- List of memberships for the individual -->
<#include "lib-individual-membership.ftl">
<#assign memberships = propertyGroups.pullProperty("${core}relatedBy", "${vivofr}MMB_0000001")!>

<#if memberships?has_content> <#-- true when the property is in the list, even if not populated (when editing) -->
    <ul id="individual-personInMembership" role="list">
        <#if memberships?has_content>
            <@showMembership memberships editable />
        </#if>
    </ul>
</#if>
