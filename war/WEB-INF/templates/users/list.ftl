<#include "../macros/site.ftl">
<#include "../macros/users.ftl">

<@page title="Users">
<div class="grid_9 omega">

<div id="content-pane" class="grid_9 alpha">

    <div id="content-header" class="content">

        <h1><a href="${rc.getContextPath()}/users">Users</a></h1>

        <p>Listing users currently registered on the site.</p>

        <@drawPageSelect userPage "/users/page/" />

        <@drawPageItems userPage />

        <@drawPageSelect userPage "/users/page/" />

    </div> <!-- end content-header -->

</div> <!-- end content-pane -->

</div>
</@page>
