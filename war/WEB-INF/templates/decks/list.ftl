<#include "../macros/site.ftl">
<#include "../macros/decks.ftl">

<@page>
<div class="grid_9 omega">
<div id="content-pane" class="grid_8 alpha">

    <div class="content">

        <h1><a href="${rc.getContextPath()}/decks">Decks</a></h1>

        <p>Listing decks ordered by <strong>date</strong> of creation.</p>

        <h3>
            <@drawPageSelect deckPage "/decks/page/" />
        </h3>

    </div>

    <@drawPageItems deckPage />

    <div class="content">
        <h3>
            <@drawPageSelect deckPage "/decks/page/" />
        </h3>
    </div>

</div> <!-- end content-pane -->


<div id="filter-pane" class="grid_1 omega">

</div> <!-- end filter-pane -->
</div>
</@page>
