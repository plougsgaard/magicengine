<#include "utilities.ftl" />

<#macro drawPageItems page>
<ul>
<#list page.items as user>
<!-- User item begin -->
<li style="margin:0.8em 0 0 0; list-style-type:none;">
    <a href="${rc.getContextPath()}/decks/user/${user.id}">
        ${user.name}
    </a>
</li>
</#list>
</ul>
</#macro>

