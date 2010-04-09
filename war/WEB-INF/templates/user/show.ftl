<#include "../macros/site.ftl">
<#include "../macros/decks.ftl">
<#include "../macros/form.ftl">

<@page title="${user.name}">
<div class="grid_9 omega">

<div id="content-pane" class="grid_9 alpha">
    <div id="content-header" class="content">
        <h1><a href="${rc.getContextPath()}/users">Users</a> | ${user.name}</h1>
        <p>
            This is the user page for ${user.name}.
            <#if Session.userSession?? && Session.userSession.id == user.id>
            It seems you are that user. Welcome!
            </#if>
        </p>
    </div> <!-- end content-header -->
</div> <!-- end content-pane -->

<div class="grid_6 alpha">
    <div class="content">
        <h2><a href="${rc.getContextPath()}/decks/user/${user.id}">Public Decks</a></h2>

        <#if Session.userSession?? && Session.userSession.id == user.id>
        <h2>
            Hidden Decks
        </h2>
        <@drawPageSelect deckPage "/user/${user.id}/page/" />

        <@drawPageItems deckPage />

        <@drawPageSelect deckPage "/user/${user.id}/page/" />
        </#if>

    </div>
</div>

<div class="grid_3 omega">
    <div class="content">
        <#if Session.userSession?? && Session.userSession.id == user.id>
        <h2>Options</h2>
        <h4>
            <a href="${rc.getContextPath()}/user/${user.id}/edit">
                Edit Profile
            </a>
        </h4>
        </#if>
    </div>
</div>
</div>
</@page>
