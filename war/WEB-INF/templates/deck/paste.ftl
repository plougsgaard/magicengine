<#include "../macros/site.ftl">
<#include "../macros/decks.ftl">
<#include "../macros/form.ftl">

<@page title="Deck Pastebin">
<div class="grid_9 omega">

    <div class="grid_9 alpha omega">
        <div class="content">

            <h1><a href="${rc.getContextPath()}/decks">Decks</a> | Deck Pastebin</h1>

            <p>Paste deck here.</p>

            <form action="" method="post">
                <p>
                    <@createInputTextArea "pasteBean.cards" 12 />
                </p>
                <p>
                <input type="submit" value="Process" />
                </p>
            </form>

            <#if deck??>
            <h2 style="margin:1.5em 0 0.7em 0;">Resulting Deck</h2>
            <p></p>
            <p>
                <#assign price = 0 />
                <#list deck.cards as card>
                    <#assign price = price + card.count * card.price />
                </#list>
                <strong>Cards: <span style="color:#660000; /* red */">${landsCount + spellsCount + creaturesCount}</span></strong>
                &ndash;
                <strong>Price: <span style="color:#4DB85F; /* green */">${price?string.currency}</span></strong>
            </p>
            </#if>
        </div> <!-- end content -->
    </div>

    <div class="clear"></div>


    <#if deck??>

    <div id="main-deck">

        <div class="grid_4 alpha">
            <div class="content">
                <h3>Lands (${landsCount})</h3>
                <div class="card-list">
                <ul class="deck">
                <#list lands as card>
                <@drawCardItem card />
                </#list>
                </ul>
                </div>

                <h3 style="margin-top:1em;">Spells (${spellsCount})</h3>
                <div class="card-list">
                <ul class="deck">
                <#list spells as card>
                <@drawCardItem card />
                </#list>
                </ul>
                </div>
            </div> <!-- end content -->
        </div>

        <div class="grid_5 omega">
            <div class="content">
                <h3>Creatures (${creaturesCount})</h3>
                <div class="card-list">
                <ul class="deck">
                <#list creatures as card>
                <@drawCardItem card />
                </#list>
                </ul>
                </div>
            </div> <!-- end content -->
        </div>

    </div>

    </#if>

</div> <!-- right pane -->


<#if deck??>

<div class="grid_12 separator alpha omega">
    <img alt="Separator" src="${rc.getContextPath()}/static/images/site/separator.png"/>
</div>

<div id="card-pictures">

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

</#if>

</@page>
