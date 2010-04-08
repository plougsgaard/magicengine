<#include "../macros/site.ftl">
<#include "../macros/users.ftl">

<@page title="Users">
<div class="grid_9 omega">

<div id="content-pane" class="grid_9 alpha">
    <div class="content">
        <h1><a href="${rc.getContextPath()}/users">Users</a></h1>
        <p>Listing users currently registered on the site.</p>
    </div> <!-- end content -->
</div> <!-- end content-pane -->

    <div class="grid_6 alpha">
        <div class="content">
            <@drawPageSelect userPage "/users/page/" />
            <@drawPageItems userPage />
            <@drawPageSelect userPage "/users/page/" />
        </div> <!-- end content -->
    </div>

    <div class="grid_3 omega">
        <div class="content">
        <#if !Session.userSession??>
        <h2>Options</h2>
        <h4>
            <a href="${rc.getContextPath()}/user/create">
                Create new user
            </a>
        </h4>
        </#if>
        </div>
    </div>

</div>
</@page>
