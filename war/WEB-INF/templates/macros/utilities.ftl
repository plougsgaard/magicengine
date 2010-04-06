<#macro drawSymbol name>
<img src="${rc.getContextPath()}/static/images/symbols/${name}.gif" width="15" height="15" />
</#macro>

<#macro drawSymbols name>
<#list name?split(",") as symbol><#if symbol?length != 0><img src="${rc.getContextPath()}/static/images/symbols/${symbol}.gif" width="20" height="20" /></#if></#list>
</#macro>

<#macro drawColourCheckbox colour>
<input name="colours" value="${colour}" type="checkbox"><@drawSymbol "${colour}" />
</#macro>

<#macro drawCardImage id>
<img src="${rc.getContextPath()}/services/card/image/${id}" width="220" />
</#macro>