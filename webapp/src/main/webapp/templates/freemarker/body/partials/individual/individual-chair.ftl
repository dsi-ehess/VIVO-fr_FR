<#-- $This file is distributed under the terms of the license in /doc/license.txt$ -->

<#-- Chair on individual profile page -->
<#assign chair = propertyGroups.pullProperty("${ehess}chaire")!>
<#if chair?has_content> <#-- true when the property is in the list, even if not populated (when editing) -->
    <@p.addLinkWithLabel chair editable />
    <#list chair.statements as statement>
        <div class="individual-overview">
            <div class="overview-value">
                ${statement.value}
            </div>
            <@p.editingLinks "${chair.name}" "" statement editable />
        </div>
    </#list>
</#if>
