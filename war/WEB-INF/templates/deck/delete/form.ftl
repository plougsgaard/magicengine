<#include "../../macros/site.ftl">
<#include "../../macros/form.ftl">

<@page title="Delete Deck">
<div class="grid_9 omega">

<div id="content-pane" class="grid_9 alpha">
    <div id="content-header" class="content">
        <h1><a href="${rc.getContextPath()}/decks">Decks</a> | Delete</h1>
    </div> <!-- end content-header -->
</div> <!-- end content-pane -->

<div class="grid_3 alpha">
    <div class="content">
        <form action="" method="post">
            <p>Please confirm that you wish to <b>delete</b> this deck.</p>

            <input type="hidden" name="email" value="${Session.userSession.email}">
            <p>
                Password
            </p>
            <@createInputText "credentials.password" />

            <div style="text-align: right; margin: 1em;">
                <input type="submit" value="Confirm" />
            </div>
        </form>
    </div>
</div>

<div class="grid_6 omega">
    &nbsp;
</div>

</div>
</@page>
