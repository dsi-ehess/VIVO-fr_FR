<#-- $This file is distributed under the terms of the license in /doc/license.txt$ -->

<#-- Template for browsing individuals in class groups for menupages -->

<#import "lib-string.ftl" as str>
<noscript>
<p style="padding: 20px 20px 20px 20px;background-color:#f8ffb7">${i18n().browse_page_javascript_one} <a href="${urls.base}/browse" title="${i18n().index_page}">${i18n().index_page}</a> ${i18n().browse_page_javascript_two}</p>
</noscript>

<section id="noJavascriptContainer" class="hidden">
<section id="browse-by" role="region">
        <nav role="navigation" class="menu" id="menu">
        <ul id="browse-classes">
            <#list vClassGroup?sort_by("displayRank") as vClass>

                <#------------------------------------------------------------
                Need to replace vClassCamel with full URL that allows function
                to degrade gracefully in absence of JavaScript. Something
                similar to what Brian had setup with widget-browse.ftl
                ------------------------------------------------------------->
                <#assign vClassCamel = str.camelCase(vClass.name) />
                <#-- Only display vClasses with individuals -->
                <#if (vClass.entityCount > 0)>
                    <li id="${vClassCamel}">
                        <a href="#${vClassCamel}" title="${i18n().browse_all_in_class}" data-uri="${vClass.URI}">
                             <#if vClass.URI?contains("http://xmlns.com/foaf/0.1/Organization")>
                                 ${i18n().search_view_all}
                             <#else>
                                 ${vClass.name}
                             </#if>
                            <span class="count-classes">(${vClass.entityCount})</span>
                        </a>

                        <#list vChildrenClassGroup?keys as key>
                            <#if key == (vClass.URI) >
                                <#assign childs = (vChildrenClassGroup[key]) />

                                <#list childs?sort_by("displayRank") as vClassChild>
                                    <#if (vClassChild.entityCount > 0)>
                                        <#assign vClassChildCamel = str.camelCase(vClassChild.name) />
                                <ul style="display: none;">
                                    <li id="${vClassChildCamel}"><a href="#${vClassChildCamel}" title="${i18n().browse_all_in_class}" data-uri="${vClassChild.URI}">${vClassChild.name} <span class="count-classes">(${vClassChild.entityCount})</span></a></li>
                                </ul>
                                    </#if>
                                </#list>
                            </#if>
                        </#list>

                    </li>
                </#if>
            </#list>
        </ul>
        <nav id="alpha-browse-container" role="navigation">
            <h3 class="selected-class"></h3>
            <#assign alphabet = ["A", "B", "C", "D", "E", "F", "G" "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"] />
            <ul id="alpha-browse-individuals">
                <li><a href="#" class="selected" data-alpha="all" title="${i18n().select_all}">${i18n().all}</a></li>
                <#list alphabet as letter>
                    <li><a href="#" data-alpha="${letter?lower_case}" title="${i18n().browse_all_starts_with(letter)}">${letter}</a></li>
                </#list>
            </ul>
        </nav>
    </nav>
    
    <section id="individuals-in-class" role="region">
        <ul role="list">

            <#-- Will be populated dynamically via AJAX request -->
        </ul>
    </section>
</section>
</section>
<script type="text/javascript">
    $('section#noJavascriptContainer').removeClass('hidden');
    $(document).ready(function(){
        $("#menu li").hover(function(){
            $(".dropdown-menu", this).slideDown(100);
        }, function(){
            $(".dropdown-menu", this).stop().slideUp(100);
        });
    })

</script>