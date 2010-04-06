<#macro drawSymbol name>
<img src="${rc.getContextPath()}/static/images/symbols/${name}.gif" width="15" height="15" />
</#macro>

<#macro drawSymbols name>
<#list name?split(",") as symbol><#if symbol?length != 0><img src="${rc.getContextPath()}/static/images/symbols/${symbol}.gif" width="20" height="20" /></#if></#list>
</#macro>

<#macro drawColourCheckbox colour>
<label>
<input name="colours" value="${colour}" type="checkbox">
</label><@drawSymbol "${colour}" />
</#macro>

<#macro drawCardImage card>
<div style="display:inline; float:left; margin: 7px 7px 14px 7px;">
    <a href="${rc.getContextPath()}/card/${card.id}"><img src="${rc.getContextPath()}/services/card/image/${card.id}" width="220" /></a><br />
    <strong>${card.count}</strong>x &ndash; &pound;${card.count * card.price}
</div>
</#macro>

<#macro getSellerName id>
<#switch id>
<#case 1>ManaLeak.com<#break>
<#case 2>MagicMadhouse.co.uk<#break>
<#default>Unknown Seller
</#switch>
</#macro>