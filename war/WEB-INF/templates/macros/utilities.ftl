<#macro drawSymbol name>
<img src="${rc.getContextPath()}/static/images/symbols/${name}.gif" width="15" height="15" />
</#macro>

<#macro drawColourCheckbox colour>
<input name="colours" value="${colour}" type="checkbox"><@drawSymbol "${colour}" />
</#macro>
