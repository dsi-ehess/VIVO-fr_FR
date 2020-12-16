<#function computeStatementLabelOrg statement>
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

<#function mapMemberClass statements>
  <#assign memberClasslMap = {}>
  <#list statements as statement>
      <#if ! (statement.memberClass?? && statement.memberClass?has_content)>
        <#assign  memberClasslMap = {"EMPTY" : "${i18n().no_memberclass}"} + memberClasslMap>
       <#else>
          <#assign memberClasslMap = memberClasslMap + {"${statement.memberClass}" : "${statement.memberClassLabel}"}>
       </#if>    
  </#list>
  <#return memberClasslMap>
</#function>

<#function filteredMemberClass statements memberClass>
  <#assign filteredList = []>
  <#list statements as statement>
      <#if memberClass == "EMPTY" || !(statement.memberClass?? && statement.memberClass?has_content)>
        <#assign filteredList = filteredList + [ statement ] />
      <#elseif statement.memberClass == memberClass>
        <#assign filteredList = filteredList + [ statement ] />
      </#if>
  </#list>
  <#return filteredList>
</#function>



<#function mapLabels statements membershipOrgUriList parentOrgList>
  <#assign labelList = []>
  <#list statements as statement>
      <#assign label = computeStatementLabel(statement, membershipOrgUriList, parentOrgList) >
      <#if !labelList?seq_contains(label)>
        <#assign labelList = labelList + [ "${label}" ] />
       </#if>
  </#list>
  <#return labelList>
</#function>


<#function computeStatementLabel statement membershipOrgUriList parentOrgList)>
    <#if statement.keepLabel?? && statement.keepLabel == "true">
      <#assign label = "${statement.membershipTitle?cap_first}">
      <#if statement.membershipLabel?has_content>
          <#assign label = label + ", ${statement.membershipLabel}">
      </#if>
    <#else>
      <#assign label = "${(statement.membershipLabel!i18n().unknown_membership)?cap_first}">
    </#if>
    <#if parentOrgList?seq_contains(statement.org)>
        <#assign label = label + "--${statement.org}">
    <#elseif statement.parentOrg??>
        <#if membershipOrgUriList?seq_contains(statement.parentOrg)>
            <#assign label = label + "--${statement.parentOrg}">
        </#if>
    </#if>
    <#return label>
</#function>

<#function filterStatements statements membershipLabel membershipOrgUriList parentOrgList)>
  <#assign filteredList = []>
  <#list statements as statement>
      <#assign label = computeStatementLabel(statement, membershipOrgUriList, parentOrgList) >
      <#if label == membershipLabel>
        <#if parentOrgList?seq_contains(statement.org)>
            <#assign filteredList = [ statement ] + filteredList/> 
        <#else>
            <#assign filteredList = filteredList + [ statement ] />
        </#if>
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

<#function mapOrgStatements statements>
  <#assign orgList = []>
  <#list statements as statement>
    <#assign orgList = orgList + [ statement.org ] />
  </#list>
  <#return orgList>
</#function>

<#function mapParentOrgStatements statements membershipOrgUriList>
  <#assign parentOrgList = []>
  <#list statements as statement>
    <#if statement.parentOrg?? >
        <#if membershipOrgUriList?seq_contains(statement.parentOrg)>
            <#assign parentOrgList = parentOrgList + [ statement.parentOrg ] />
        </#if>
    </#if>
  </#list>
  <#return parentOrgList>
</#function>

<#macro propertyListMemberships property editable statements=property.statements template=property.template>
    <ul id="individual-personInMembership" role="list">
        <#assign membershipOrgUriList = mapOrgStatements(statements) />
        <#assign parentOrgList = mapParentOrgStatements(statements, membershipOrgUriList) />
        <#assign membershipLabels = mapLabels(statements, membershipOrgUriList, parentOrgList) />

        <#list membershipLabels as membershipLabel>
            <li role="listitem">
                <span>${membershipLabel?keep_before("--")}, </span>
                <#assign filteredStatements = filterStatements(statements, membershipLabel, membershipOrgUriList, parentOrgList) />
                <#assign membershipContentList = []>
                <#list filteredStatements as statement>
                   <#assign localLabel = computeStatementLabel(statement, membershipOrgUriList, parentOrgList) />
                   <#if localLabel == membershipLabel>
                       <#local membershipContent>
                            <@propertyListItemMembership property statement editable><#include "${template}"></@propertyListItemMembership>
                       </#local>
                       
                       <#assign membershipContentList = membershipContentList + [ membershipContent ] />
                    </#if>
                </#list>
                ${membershipContentList?join(", ")}
            </li>
        </#list>
    </ul>
</#macro>

<#macro showMembership property editable statements=property.statements template=property.template>
    <#if property?has_content> <#-- true when the property is in the list, even if not populated (when editing) -->
        <#assign localName = property.localName>
        <#if editable>
            <h2 id="${localName}" class="mainPropGroup" title="${property.publicDescription!}">${property.name?cap_first} <@p.addLink property editable /> <@p.verboseDisplay property /></h2>
        </#if>
         <@propertyListMemberships property editable />
    </#if>
</#macro>


