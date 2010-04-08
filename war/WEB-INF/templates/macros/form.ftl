<#import "/spring.ftl" as spring />

<#macro createInputText field>
<@spring.bind field />
<input
    <#if (field?contains(".password"))>type="password"<#else>type="text"</#if>
    name="${spring.status.expression}"
    value="${spring.status.value?default("")}" /><br />
<#list spring.status.errorMessages as error>
<b>Error:</b> ${error}<br />
</#list>
</#macro>

<!--

TODO: Replace the use of these with the proper ones...

-->

<#macro drawFormInput fieldName bindingObject>
<#if fieldName?lower_case?contains("password") >
<#assign fieldType = "password" />
<#else>
<#assign fieldType = "text" />
</#if>
<p>
    <input class="text" type="${fieldType}" name="${fieldName}"
    <#if "${bindingObject}"?eval?? && !fieldName?lower_case?contains("password")>
    value="${"${bindingObject}.${fieldName}"?eval}"
    </#if>
    />
</p>
<#if bindingResult??>
<#list bindingResult.fieldErrors as error>
<#if error.field == fieldName>
<p class="error">Error: ${error.defaultMessage}</p>
</#if>
</#list>
</#if>
</#macro>

<#macro drawFormInputClear fieldName>
<#if fieldName?lower_case?contains("password") >
<#assign fieldType = "password" />
<#else>
<#assign fieldType = "text" />
</#if>
<p>
    <input class="text" type="${fieldType}" name="${fieldName}" />
</p>
<#if bindingResult??>
<#list bindingResult.fieldErrors as error>
<#if error.field == fieldName>
<p class="error">Error: ${error.defaultMessage}</p>
</#if>
</#list>
</#if>
</#macro>

<#macro drawTextarea fieldName rows>
<p>
    <textarea class="text" name="${fieldName}" rows="${rows}"></textarea>
</p>
<#if bindingResult??>
<#list bindingResult.fieldErrors as error>
<#if error.field == fieldName>
<p class="error">Error: ${error.defaultMessage}</p>
</#if>
</#list>
</#if>
</#macro>
