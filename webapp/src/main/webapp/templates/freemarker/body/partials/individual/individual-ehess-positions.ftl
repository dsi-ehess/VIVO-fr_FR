<#-- $This file is distributed under the terms of the license in /doc/license.txt$ -->

<#-- List of positions for the individual -->
<#assign statutoryMembers = propertyGroups.pullProperty("${core}relatedBy", "${ehess}AppartenanceStatutaire")!>
<#if statutoryMembers?has_content> <#-- true when the property is in the list, even if not populated (when editing) -->
    <#assign localName = statutoryMembers.localName>
    <#if editable>
        <h2 id="${localName}" class="mainPropGroup" title="${statutoryMembers.publicDescription!}">${statutoryMembers.name?capitalize} <@p.addLink statutoryMembers editable /> <@p.verboseDisplay statutoryMembers /></h2>
        <ul id="individual-personInPosition" role="list">
            <@p.objectProperty statutoryMembers editable />
        </ul>
    <#else>
        <@p.objectProperty statutoryMembers editable />
    </#if>
<#else>
    <span>NO STAT</span>
</#if>
