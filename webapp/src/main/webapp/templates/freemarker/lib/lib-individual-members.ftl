
<#macro showMember members editable>
    <#if members?has_content> <#-- true when the property is in the list, even if not populated (when editing) -->
        <#assign localName = members.localName>
        <#if editable>
            <h2 id="${localName}" class="mainPropGroup" title="${members.publicDescription!}">${members.name?capitalize} <@p.addLink members editable /> <@p.verboseDisplay members /></h2>
            <ul id="individual-personInPosition" role="list">
                <@p.objectProperty members editable />
            </ul>
        <#else>
            <h2 id="${localName}" class="mainPropGroup" title="${members.publicDescription!}">${members.name?capitalize}</h2>
            <@p.objectProperty members editable />
        </#if>
    </#if>
</#macro>

