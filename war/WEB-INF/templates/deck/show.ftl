<#include "../macros/site.ftl">
<#include "../macros/decks.ftl">

<@page title="${deck.title}" scripts=["/deck/show.js"]>
<div class="grid_9 omega">

    <div class="grid_9 alpha omega">
        <div class="content">

            <h1><a href="${rc.getContextPath()}/decks">Decks</a> | ${deck.title}</h1>
<div class="content" style="float:right;">
                <img alt="Image Thumbnail" src="${rc.getContextPath()}/services/card/image/${deck.featureCardId}/cutout"/>
            </div>
            <h2 style="margin:0 0 1em 0;">
                by <a href="${rc.getContextPath()}/decks/user/${deck.author.id}">${deck.author.name}</a></h2>

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

    <div class="grid_9 alpha omega">
        <div class="content">
            <h3>Mana Curve <a name="charts"> </a> <a id="expand-extra-charts" href="#charts">+</a></h3>

            <img alt="Mana Curve" src="${rc.getContextPath()}/services/deck/${deck.id}/chart/coalesced"/>

            <div id="extra-charts" style="display:none;">
                <h3>Mana Curve (creatures)</h3>
                <img alt="Mana Curve" src="${rc.getContextPath()}/services/deck/${deck.id}/chart/creature"/>

                <h3>Mana Curve (other spells)</h3>
                <img alt="Mana Curve" src="${rc.getContextPath()}/services/deck/${deck.id}/chart/spell"/>
            </div>

            <h3>Pictures <a name="pictures"> </a> <a id="expand-card-pictures" href="#pictures">+</a></h3>

        </div>
    </div>

</div> <!-- right pane -->


<div class="grid_12 separator alpha omega">
    <img alt="Separator" src="${rc.getContextPath()}/static/images/site/separator.png"/>
</div>

<div id="card-pictures" style="display:none;">

<div class="grid_12">
<div style="padding:10px 0 10px 0; text-align: center;">
<#assign cardCount = 0 />
<#list deck.cards as card>
<@drawCardImage card />
<#assign cardCount = cardCount + 1 />
<#if cardCount % 4 == 0><div class="clear">&nbsp;</div></#if>
</#list>
</div>
</div>

</div>
</@page>
