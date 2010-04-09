<#macro drawSymbol name>
<img alt="Mana Symbol" src="${rc.getContextPath()}/static/images/symbols/${name}.gif" width="15" height="15" />
</#macro>

<#macro drawSymbols name>
<#list name?split(",") as symbol><#if symbol?trim?length != 0><img alt="Mana Symbol" src="${rc.getContextPath()}/static/images/symbols/${symbol}.gif" width="20" height="20" /></#if></#list>
</#macro>

<#macro drawSymbolsSmall name>
<#list name?split(",") as symbol><#if symbol?trim?length != 0><img alt="Mana Symbol" src="${rc.getContextPath()}/static/images/symbols/${symbol}.gif" width="15" height="15" /></#if></#list>
</#macro>

<#macro drawColourCheckboxClear colour>
<label>
<input name="colours" value="${colour}" type="checkbox">
</label><@drawSymbol "${colour}" />
</#macro>

<#macro drawColourCheckbox colours colour>
<label>
<input name="colours" value="${colour}" type="checkbox"
<#if colours?contains(colour)> checked="checked"</#if>
>
</label><@drawSymbol "${colour}" />
</#macro>

<#macro drawCardImage card>
<div style="display:inline; float:left; margin: 7px 7px 14px 7px;">
    <a href="${rc.getContextPath()}/card/${card.id}"><img alt="Mana Symbol" src="${rc.getContextPath()}/services/card/image/${card.id}/thumbnail" width="220" /></a><br />
    <strong>${card.count}</strong>x &ndash; ${(card.count * card.price)?string.currency}
</div>
</#macro>

<#macro getSellerName id>
<#switch id>
<#case 1>ManaLeak.com<#break>
<#case 2>MagicMadhouse.co.uk<#break>
<#default>Unknown Seller
</#switch>
</#macro>

<#macro drawPageSelect page prefix>
<#assign padding = 4 />
<#assign count = 0 />
<#assign distFirst = (page.pageNumber - 1) />
<#assign distLast = (page.pageCount - page.pageNumber) />
<#assign offset = (page.pageNumber - padding) />
<#if (distLast < 4)>
    <#assign offset = (offset - (padding - distLast) ) />
</#if>

<h3 class="page-select">

<#if page.pageNumber == 1>
&laquo;
<#else >
<a href="${rc.getContextPath()}${prefix}${page.pageNumber - 1}">&laquo;</a>
</#if>

<#if (distFirst > 4)>
<a href="${rc.getContextPath()}${prefix}1">1</a> .. 
</#if>

<#list offset..(page.pageNumber - 1) as i>
    <#if (i >= 1 && i <= page.pageCount)>
        <#assign count = count + 1 />
        <a href="${rc.getContextPath()}${prefix}${i}">${i}</a>
    </#if>
</#list>

<b>${page.pageNumber}</b>

<#assign offset = (page.pageNumber + 1) />
<#list offset..(offset + (padding * 2) - count - 1) as i>
    <#if (i >= 1 && i <= page.pageCount && page.pageNumber != page.pageCount)>
        <#assign count = count + 1 />
        <a href="${rc.getContextPath()}${prefix}${i}">${i}</a>
    </#if>
</#list>



<#if (distLast > 4)>
 .. <a href="${rc.getContextPath()}${prefix}${page.pageCount}">${page.pageCount}</a>
</#if>


<#if page.pageNumber == page.pageCount>
&raquo;
<#else >
<a href="${rc.getContextPath()}${prefix}${page.pageNumber + 1}">&raquo;</a>
</#if>

</h3>

</#macro>
