<#include "../macros/site.ftl">
<#include "../macros/decks.ftl">
<#include "../macros/form.ftl">

<@page title="${user.name}">
<div class="grid_9 omega">

<div id="content-pane" class="grid_9 alpha">
    <div id="content-header" class="content">
        <h1><a href="${rc.getContextPath()}/users">Users</a> | ${user.name}</h1>
        <p>This page is most interesting for ${user.name} and less for others.</p>
    </div> <!-- end content-header -->
</div> <!-- end content-pane -->

<div class="grid_6 alpha">
    <div class="content">

        <#if Session.userSession?? && Session.userSession.id == user.id>
            <p>
                It seems you are logged in as <strong>${user.name}</strong>.
                On this page you can view your hidden decks and edit your profile.
            </p>
        </#if>

        <#if RequestParameters.new??>
        <h2>Welcome!</h2>

        <p>My clever deductive skills tell me you are new on the site.
            Please <a href="${rc.getContextPath()}/user/login">log in</a>
            to start using the site.</p>
        </#if>

        <#if Session.userSession?? && Session.userSession.id == user.id>
        <h3>
            Your hidden decks
        </h3>
        <@drawPageSelect deckPage "/user/${user.id}/page/" />

        <@drawPageItems deckPage />

        <@drawPageSelect deckPage "/user/${user.id}/page/" />
        </#if>
    </div>
</div>

<div class="grid_3 omega">
    <div class="content">
        <h2>Options</h2>
            <#if Session.userSession?? && Session.userSession.id == user.id>
            <h4>
            <a href="${rc.getContextPath()}/user/${user.id}/edit">
                Edit Profile
            </a>
            </h4>
            </#if>
            <h4>
            <a href="${rc.getContextPath()}/user/${user.id}/edit">
                Public Decks
            </a>
            </h4>
    </div>
</div>
</div>
</@page>
