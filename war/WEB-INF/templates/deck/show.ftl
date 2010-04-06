<#include "../macros/site.ftl">
<#include "../macros/decks.ftl">

<@page>
<div class="grid_9 omega">
<div id="content-pane" class="grid_8 alpha">

    <div id="content-header" class="content">

        <h1><a href="${rc.getContextPath()}/decks">Decks</a> | Show Deck</h1>

        <h2>`${deck.title}`</h2>

        <p>This deck has <b>60</b> cards and cost <b>&pound;43.35</b>.</p>

        <!-- INDSÆT KORT HER eller heromkring -->

    </div> <!-- end content-header -->

</div> <!-- end content-pane -->


<div class="grid_1 omega">
&nbsp;
</div>
</div>

<div class="grid_12 alpha omega">
<div style="padding:13px;">
    <#list deck.cards as card>
    <@drawCardImage card.id />
    </#list>
</div>
</div>
</@page>