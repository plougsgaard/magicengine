<#include "../macros/site.ftl">
<#include "../macros/decks.ftl">

<@page title="Decks">
<div class="grid_9 omega">
<div id="content-pane" class="grid_8 alpha">

    <div class="content">

        <h1><a href="${rc.getContextPath()}/decks">Decks</a></h1>

        <p>Listing decks ordered by <strong>date</strong> of creation.</p>

        <@drawPageSelect deckPage "/decks/page/" />

    </div>

    <@drawPageItems deckPage />

    <div class="content">
        <@drawPageSelect deckPage "/decks/page/" />
    </div>

</div> <!-- end content-pane -->


<div id="filter-pane" class="grid_1 omega">

</div> <!-- end filter-pane -->
</div>
</@page>
