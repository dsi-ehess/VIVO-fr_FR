<#-- $This file is distributed under the terms of the license in /doc/license.txt$ -->

<#-- List of positions for the individual -->
<#include "lib-individual-members.ftl">

<p>Le template individual-positions_fr_FR</p>

<#assign functions = propertyGroups.pullProperty("${core}relatedBy", "${core}Position")!>

<#import "debugger.ftl" as debugger />

<p>A-t-on trouvé des fonctions ? </p>

<@debugger.debug debugObject=functions?has_content depth=3 />

<#--Temporary : replace editable by special variable to remove membership edition button from organisation pages-->
<#--<#assign temporaryEditable = editable && !membershipsTemporaryNotEditable !>-->

<#assign temporaryEditable = editable && !(membershipsTemporaryNotEditable![])?first?has_content !>
<#if positions?has_content || statutoryMembers?has_content || members?has_content || associateMembers?has_content || phdMembers?has_content || postdocMembers?has_content || functions?has_content> <#-- true when the property is in the list, even if not populated (when editing) -->
    <#if positions?has_content>
        <#assign localName = positions.localName>
        <h2 id="${localName}" class="mainPropGroup" title="${positions.publicDescription!}">${positions.name?capitalize} <@p.addLink positions temporaryEditable /> <@p.verboseDisplay positions /></h2>
    </#if>
    <ul id="individual-personInPosition" role="list">        
        <span>FUNCTIONS_START</span>
        <#if functions?has_content>
            <span>FUNCTIONS</span>
            <@p.objectProperty functions temporaryEditable />
        </#if>
         <span>FUNCTIONS_END</span>
    </ul>
</#if>
