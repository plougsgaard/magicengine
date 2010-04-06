<#include "../macros/site.ftl">
<#include "../macros/decks.ftl">

<@page>
<div class="grid_9 omega">

    <div class="grid_9 alpha omega">
        <div class="content">
            <h1><a href="${rc.getContextPath()}/cards">Cards</a> | Show Card</h1>
        </div> <!-- end content -->
    </div>

    <div class="grid_5 alpha">
        <div class="content">
            <h2>${card.cardName}</h2>
            <p><b>Types</b>: ${card.types?replace("—", "&ndash;")}</p>
            <p><b>Rarity</b>: ${card.rarity}</p>
            <p><b>CMC</b>: ${card.convertedManaCost}</p>
            <p><b>Expansion</b>: ${card.expansion}</p>
            <p><b>Artist</b>: ${card.artist}</p>
            <p><b>Rules text</b>: ${card.cardText}</p>
            <h3>Lowest Price</h3>
            <p><b>${card.price?string.currency}</b></p>

            <h3>All Sellers</h3>
            <#list card.prices as price>
            <p><@getSellerName price.seller.id />: <b>${price.price?string.currency}</b>
               <small>${price.dateAdded?datetime?string.short}</small></p>
            </#list>
        </div> <!-- end content -->
    </div> <!-- end content-pane -->


    <div class="grid_4 omega">
        <div style="padding: 30px 10px 0 0;">
            <img src="${rc.getContextPath()}/services/card/image/${card.id}" style="width: 100%;" />
        </div> <!-- end content -->
    </div>

</div> <!-- right pane -->

</@page>
