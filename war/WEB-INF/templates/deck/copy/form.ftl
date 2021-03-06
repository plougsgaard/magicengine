<#include "../../macros/site.ftl">
<#include "../../macros/form.ftl">

<@page title="Copy Deck">
<div class="grid_9 omega">

<div id="content-pane" class="grid_9 alpha">
    <div id="content-header" class="content">
        <h1><a href="${rc.getContextPath()}/decks">Decks</a> | Copy</h1>
    </div> <!-- end content-header -->
</div> <!-- end content-pane -->

<div class="grid_3 alpha">
    <div class="content">
        <form action="" method="post">
            <p>Title of new deck:</p>
            <@createInputText "deck.title" />

            <div style="text-align: right;">
                <input type="submit" value="Copy" />
            </div>
        </form>
    </div>
</div>

<div class="grid_6 omega">
    &nbsp;
</div>

</div>
</@page>
