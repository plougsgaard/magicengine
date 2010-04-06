<#include "../../macros/site.ftl">
<#include "../../macros/decks.ftl">

<@page>
<div class="grid_9 omega">
<div id="content-pane" class="grid_8 alpha">

    <div id="content-header" class="content">

        <h1><a href="${rc.getContextPath()}/decks">Decks</a></h1>

        <p>Listing decks that are created by <strong>${user.name}</strong> ordered by <strong>date</strong> of creation.</p>

        <h3>
            <@drawPageSelect deckPage />
        </h3>

    </div> <!-- end content-header -->

    <@drawPageItems deckPage />

</div> <!-- end content-pane -->


<div class="grid_1 omega">
&nbsp;
</div>
</div>
</@page>
