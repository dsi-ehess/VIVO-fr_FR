<#-- $This file is distributed under the terms of the license in /doc/license.txt$ -->

<#-- List of positions for the individual -->
<#assign positions = propertyGroups.pullProperty("${core}relatedBy", "${core}Position")!>

<#if positions?has_content || memberships?has_content> <#-- true when the property is in the list, even if not populated (when editing) -->
    <#if positions?has_content>
        <#assign localName = positions.localName>
        <h2 id="${localName}" class="mainPropGroup" title="${positions.publicDescription!}">${i18n().positions_section_title?cap_first} <@p.addLink positions editable /> <@p.verboseDisplay positions /></h2>
    </#if>
    <ul id="individual-personInPosition" role="list">
        <#if positions?has_content>
            <@p.objectProperty positions editable />
        </#if>
    </ul>
</#if>
