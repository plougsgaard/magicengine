<#include "../../macros/site.ftl">
<#include "../../macros/cards/queue.ftl">

<@page title="Card Queue">
<div class="grid_9 omega">

<div id="content-pane" class="grid_9 alpha">

    <div id="content-header" class="content">

        <h1><a href="${rc.getContextPath()}/decks">Cards</a> | Queue</h1>

        <@drawPageSelect cardPage "/cards/queue/page/" />

        <@drawPageItems cardPage />

        <@drawPageSelect cardPage "/cards/queue/page/" />

    </div> <!-- end content-header -->

</div> <!-- end content-pane -->

</div>
</@page>
