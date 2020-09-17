<#-- $This file is distributed under the terms of the license in /doc/license.txt$ -->

<#-- List of research areas for the individual -->
<#assign subjectAreas = propertyGroups.pullProperty("${core}hasSubjectArea")!>
<#assign concepts = propertyGroups.pullProperty("${core}hasAssociatedConcept")!> 
<#if subjectAreas?has_content> <#-- true when the property is in the list, even if not populated (when editing) -->
    <#assign localName = subjectAreas.localName>
    <h2 id="${localName}" class="mainPropGroup" title="${subjectAreas.publicDescription!}">
        ${subjectAreas.name?capitalize} 
        <img id="researchAreaIcon" src="${urls.images}/individual/research-group-icon.png" alt="${i18n().research_areas}" />
        <@p.addLink subjectAreas editable /> <@p.verboseDisplay subjectAreas />
    </h2>
    <ul id="individual-${localName}" role="list" >
        <@p.objectProperty subjectAreas editable />
    </ul> 
</#if>   
