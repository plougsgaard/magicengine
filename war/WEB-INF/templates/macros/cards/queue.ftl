<#include "../utilities.ftl" />

<#macro drawPageItems page>
<ul>
<#list page.items as queueItem>
<!-- User item begin -->
    <li style="margin:0.2em 0 0 0; list-style-type:none;">
    <@getSellerName queueItem.sellerId /> &rarr;
        <a href="${rc.getContextPath()}/card/${queueItem.cardId}">
        ${queueItem.cardName}
        </a> &ndash;
        <#if queueItem.price?? && !(queueItem.price == 0)>
            <strong>${queueItem.price?string.currency}</strong>
            <#else>No recorded price
        </#if>
        <#if queueItem.dateAdded??>(${queueItem.dateAdded?datetime?string.short})</#if>
    </li>
</#list>
</ul>
</#macro>

