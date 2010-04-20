<#include "utilities.ftl" />

<#macro drawPageItems page>
<ul>
<#list page.items as card>
<!-- User item begin -->
<li style="margin:0.8em 0 0 0; list-style-type:none;">
    <a href="${rc.getContextPath()}/card/${card.id?c}">
        ${card.cardName}
    </a>
</li>
</#list>
</ul>
</#macro>

