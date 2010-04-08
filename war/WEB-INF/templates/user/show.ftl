<#include "../macros/site.ftl">
<#include "../macros/form.ftl">

<@page title="${user.name}">
<div class="grid_9 omega">

<div id="content-pane" class="grid_9 alpha">
    <div id="content-header" class="content">
        <h1><a href="${rc.getContextPath()}/users">Users</a> | ${user.name}</h1>
    </div> <!-- end content-header -->
</div> <!-- end content-pane -->

<div class="grid_3 alpha">
    <div class="content">
        <h2>Options</h2>
        <h4>
            <a href="${rc.getContextPath()}/decks/user/${user.id}">
                Public Decks
            </a>
        </h4>

        <#if Session.userSession?? && Session.userSession.id == user.id>
        <h4>
            <a href="${rc.getContextPath()}/user/${user.id}/edit">
                Edit Profile
            </a>
        </h4>

        <h2>All Decks</h2>

        <p>Lacks implementation..</p>

        </#if>
    </div>
</div>

<div class="grid_6 omega">
    &nbsp;
</div>
</div>
</@page>
