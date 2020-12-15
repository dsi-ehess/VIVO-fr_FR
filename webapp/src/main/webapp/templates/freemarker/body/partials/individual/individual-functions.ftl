<#-- $This file is distributed under the terms of the license in LICENSE$ -->

<#-- List of functions for the individual -->
<#assign functions = propertyGroups.pullProperty("${core}relatedBy", "${vivofr}FNC_0000001")!>
<#if functions?has_content> <#-- true when the property is in the list, even if not populated (when editing) -->
    <#assign localName = functions.localName>
    <#if editable > 
    <h2 id="${localName}" class="mainPropGroup" title="${functions.publicDescription!}">${functions.name?cap_first} <@p.addLink functions editable /> <@p.verboseDisplay functions /></h2>
    </#if>
    <ul id="individual-personInFunction" role="list">
        <@p.objectProperty functions editable />
    </ul>
</#if>
