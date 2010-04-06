<#include "../macros/site.ftl">
<#include "../macros/decks.ftl">

<@page>
<div class="grid_9 omega">

    <div class="grid_9 alpha omega">
        <div class="content">
            <h1><a href="${rc.getContextPath()}/decks">Decks</a> | Show Deck</h1>
            <h3>`${deck.title}` by ${deck.author.name}</h3>
        </div> <!-- end content -->
    </div>

    <div id="content-pane" class="grid_6 alpha">
        <div class="content">
            ${deck.description?replace("\n", "<br />")}
        </div> <!-- end content -->



    </div> <!-- end content-pane -->


    <div class="grid_3 omega">
        <div class="content">
            <p>
            ${deck.format}
            </p>
            <p>
            <@drawSymbols deck.colours /> &ndash; &pound; 12
            </p>
        </div> <!-- end content -->
    </div>

</div> <!-- right pane -->

<div class="grid_12">
<div style="padding:10px 0 10px 0; text-align: center;">
<#list deck.cards as card>
<@drawCardImage card />
</#list>
</div>
</div>
</@page>
