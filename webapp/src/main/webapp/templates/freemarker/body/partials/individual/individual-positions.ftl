<#-- $This file is distributed under the terms of the license in /doc/license.txt$ -->

<#-- List of positions for the individual -->
<#include "lib-individual-members.ftl">
<#assign functions = propertyGroups.pullProperty("${core}relatedBy", "${vivofr}FNC_0000001")!>
<#assign positions = propertyGroups.pullProperty("${core}relatedBy", "${core}Position")!>
<#assign statutoryMembers = propertyGroups.pullProperty("${core}relatedBy", "${ehess}AppartenanceStatutaire")!>
<#assign associateMembers = propertyGroups.pullProperty("${core}relatedBy", "${ehess}AppartenanceATitreDAssocie")!>
<#assign phdMembers = propertyGroups.pullProperty("${core}relatedBy", "${ehess}AppartenanceEnTantQueDoctorant")!>
<#assign postdocMembers = propertyGroups.pullProperty("${core}relatedBy", "${ehess}AppartenanceEnTantQuePostDoctorant")!>

<#--<#import "debugger.ftl" as debugger />-->

<#--<@debugger.debug debugObject=membershipsTemporaryNotEditable?first?has_content depth=3 />-->

<#--Temporary : replace editable by special variable to remove membership edition button from organisation pages-->
<#--<#assign temporaryEditable = editable && !membershipsTemporaryNotEditable !>-->
<#assign temporaryEditable = editable && !(membershipsTemporaryNotEditable![])?first?has_content !>
<#if positions?has_content || statutoryMembers?has_content || members?has_content || associateMembers?has_content || phdMembers?has_content || postdocMembers?has_content || functions?has_content > <#-- true when the property is in the list, even if not populated (when editing) -->
    <#if positions?has_content>
        <#assign localName = positions.localName>
        <h2 id="${localName}" class="mainPropGroup" title="${positions.publicDescription!}">${positions.name?capitalize} <@p.addLink positions temporaryEditable /> <@p.verboseDisplay positions /></h2>
    </#if>
    <ul id="individual-personInPosition" role="list">
        <#if positions?has_content>
            <@p.objectProperty positions temporaryEditable />
        </#if>
        <span>FUNCTIONS_START</span>
        <#if functions?has_content>
            <span>FUNCTIONS</span>
            <@p.objectProperty functions temporaryEditable />
        </#if>
        <span>FUNCTIONS_END</span>
        <#if statutoryMembers?has_content>
            <@showMember statutoryMembers temporaryEditable />
        </#if>
    </ul>
    <ul id="individual-personInPosition" role="list">
        <#if associateMembers?has_content>
            <@showMember associateMembers temporaryEditable />
        </#if>

        <#if postdocMembers?has_content>
            <@showMember postdocMembers temporaryEditable />
        </#if>

        <#if phdMembers?has_content>
            <@showMember phdMembers temporaryEditable />
        </#if>
    </ul>
</#if>
