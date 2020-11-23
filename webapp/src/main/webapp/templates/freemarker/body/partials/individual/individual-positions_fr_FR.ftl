<#-- $This file is distributed under the terms of the license in /doc/license.txt$ -->

<#-- List of positions for the individual -->
<#include "lib-individual-members.ftl">
<#include "lib-individual-membership.ftl">
<#assign positions = propertyGroups.pullProperty("${core}relatedBy", "${core}Position")!>
<#assign memberships = propertyGroups.pullProperty("${core}relatedBy", "${vivofr}MMB_0000001")!>

<#--<#import "debugger.ftl" as debugger />-->

<#--<@debugger.debug debugObject=membershipsTemporaryNotEditable?first?has_content depth=3 />-->

<#--Temporary : replace editable by special variable to remove membership edition button from organisation pages-->
<#--<#assign temporaryEditable = editable && !membershipsTemporaryNotEditable !>-->
<#assign temporaryEditable = editable && !(membershipsTemporaryNotEditable![])?first?has_content !>
<#if positions?has_content || memberships?has_content> <#-- true when the property is in the list, even if not populated (when editing) -->
    <#if positions?has_content>
        <#assign localName = positions.localName>
        <h2 id="${localName}" class="mainPropGroup" title="${positions.publicDescription!}">${positions.name?capitalize} <@p.addLink positions temporaryEditable /> <@p.verboseDisplay positions /></h2>
    </#if>
    <ul id="individual-personInPosition" role="list">
        <#if positions?has_content>
            <@p.objectProperty positions temporaryEditable />
        </#if>
    </ul>
    <ul id="individual-personInPosition" role="list">
        <#if memberships?has_content>
            <@showMembership memberships temporaryEditable />
        </#if>
    </ul>
</#if>
