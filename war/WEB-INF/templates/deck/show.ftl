<#include "../macros/site.ftl">
<#include "../macros/decks.ftl">

<@page title="${deck.title}">
<div class="grid_9 omega">

    <div class="grid_9 alpha omega">
        <div class="content">

            <h1><a href="${rc.getContextPath()}/decks">Decks</a> | ${deck.title}</h1>
<div class="content" style="float:right;">
                <img alt="Image Thumbnail" src="${rc.getContextPath()}/services/card/crop-image/${deck.id}"/>
            </div>
            <h2 style="margin:0 0 1em 0;">by ${deck.author.name}</h2>

            <p>
                <strong>Format: </strong>${deck.format}
            </p>
            <p>
                <strong>Colours: </strong><@drawSymbols deck.colours />
            </p>
            <p>
                <#assign price = 0 />
                <#list deck.cards as card>
                    <#assign price = price + card.count * card.price />
                </#list>
                <strong>Price: <span style="color:#4DB85F; /* green */">${price?string.currency}</span></strong>
            </p>
            <p>
                <#if deck.description?trim?length != 0>
                <strong>Description: </strong>
                ${deck.description?replace("\n", "<br />")}
                </#if>
            </p>
            <p>
                <a href="${rc.getContextPath()}/deck/${deck.id}/edit">Edit</a>
            </p>
        </div> <!-- end content -->
    </div>

    <div class="clear"></div>

    <div id="main-deck">

        <div class="grid_4 alpha">
            <div class="content">
                <h3>Lands</h3>
                <div class="card-list">
                <ul class="deck">
                <#list deck.cards as card>
                <#if card.types?contains("Land")><@drawCardItem card /></#if>
                </#list>
                </ul>
                </div>

                <h3>Spells</h3>
                <div class="card-list">
                <ul class="deck">
                <#list deck.cards as card>
                <#if !card.types?contains("Land") && !card.types?contains("Creature")><@drawCardItem card /></#if>
                </#list>
                </ul>
                </div>
            </div> <!-- end content -->
        </div>

        <div class="grid_5 omega">
            <div class="content">
                <h3>Creatures</h3>
                <div class="card-list">
                <ul class="deck">
                <#list deck.cards as card>
                <#if card.types?contains("Creature")><@drawCardItem card /></#if>
                </#list>
                </ul>
                </div>
            </div> <!-- end content -->
        </div>

    </div>

</div> <!-- right pane -->

<div class="grid_12 separator alpha omega">
    <img alt="Separator" src="${rc.getContextPath()}/static/images/site/separator.png"/>
</div>

<div class="grid_12">
<div style="padding:10px 0 10px 0; text-align: center;">
<#list deck.cards as card>
<@drawCardImage card />
</#list>
</div>
</div>
</@page>
