
<#function mapLabels statements>
  <#assign labelList = []>
  <#list statements as statement>
      <#assign label = computeStatementLabel(statement) >
      <#if !labelList?seq_contains(label)>
        <#assign labelList = labelList + [ "${label}" ] />
       </#if>
  </#list>
  <#return labelList>
</#function>

<#function computeStatementLabel statement>
  <#if statement.keepLabel?? && statement.keepLabel == "true">
      <#assign label = "${statement.membershipTitle?cap_first}">
      <#if statement.membershipLabel?has_content>
          <#assign label = label + ", ${statement.membershipLabel}">
      </#if>
    <#else>
      <#assign label = "${(statement.membershipLabel!i18n().unknown_membership)?cap_first}">
    </#if>
    <#return label>
</#function>

<#function filterStatements statements membershipLabel>
  <#assign filteredList = []>
  <#list statements as statement>
      <#assign label = computeStatementLabel(statement) >
      <#if label == membershipLabel>
        <#assign filteredList = filteredList + [ statement ] />
      </#if>
  </#list>
  <#return filteredList>
</#function>

<#macro propertyListItemMembership property statement editable >
    <#if property.rangeUri?? >
        <#local rangeUri = property.rangeUri />
    <#else>
        <#local rangeUri = "" />
    </#if>
    <#nested>
    <@p.editingLinks "${property.localName}" "${property.name}" statement editable rangeUri/>
    
</#macro>


<#macro propertyListMemberships property editable statements=property.statements template=property.template>
    <ul id="individual-personInMembership" role="list">
    <#assign membershipLabels = mapLabels(statements) />
    <#list membershipLabels as membershipLabel>
        <li role="listitem">
            <span>${membershipLabel}, </span>
            <#assign filteredStatements = filterStatements(statements, membershipLabel) />
            <#list filteredStatements as statement>
               <#assign localLabel = computeStatementLabel(statement) />
               <#if localLabel == membershipLabel>
                   <@propertyListItemMembership property statement editable><#include "${template}"></@propertyListItemMembership>
                   <#if (statements?seq_index_of(statement) < ((filteredStatements?size) -1)) >
                        <span>, </span>
                    </#if>
                </#if>
            </#list>
        </li>
    </#list>
</#macro>

<#macro showMembership property editable statements=property.statements template=property.template>
    <#if property?has_content> <#-- true when the property is in the list, even if not populated (when editing) -->
        <#assign localName = property.localName>
        <#if editable>
            <h2 id="${localName}" class="mainPropGroup" title="${property.publicDescription!}">${property.name?capitalize} <@p.addLink property editable /> <@p.verboseDisplay property /></h2>
                <@propertyListMemberships property editable />
            </ul>
        <#else>
            <h2 id="${localName}" class="mainPropGroup" title="${property.publicDescription!}">${property.name?capitalize}</h2>
             <@propertyListMemberships property editable />
        </#if>
    </#if>
</#macro>


