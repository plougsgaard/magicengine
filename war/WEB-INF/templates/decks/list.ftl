<#include "../macros/site.ftl">
<#include "../macros/decks.ftl">

<@page>
<div class="grid_9 omega">
<div id="content-pane" class="grid_8 alpha">

    <div id="content-header" class="content">

        <h1><a href="${rc.getContextPath()}/decks">Decks</a></h1>

        <p>Listing decks ordered by <strong>date</strong> of creation.</p>

        <h3>
            <@drawPageSelect deckPage />
        </h3>

    </div> <!-- end content-header -->

    <@drawPageItems deckPage />

</div> <!-- end content-pane -->


<div id="filter-pane" class="grid_1 omega">

</div> <!-- end filter-pane -->
</div>
</@page>
