<#include "../macros/site.ftl">
<#include "../macros/decks.ftl">

<@page>
<div class="grid_9 omega">

    <div class="grid_9 alpha omega">
        <div class="content">
            <h1><a href="${rc.getContextPath()}/cards">Cards</a> | ${card.cardName}</h1>
        </div> <!-- end content -->
    </div>

    <div class="grid_5 alpha">
        <div class="content">
            <p><strong>Types</strong>: ${card.types?replace("�", "&ndash;")}</p>
            <p><strong>Rarity</strong>: ${card.rarity}</p>
            <p><strong>CMC</strong>: ${card.convertedManaCost}</p>
            <p><strong>Expansion</strong>: ${card.expansion}</p>
            <p><strong>Artist</strong>: ${card.artist}</p>
            <p><strong>Rules text</strong>:</p> ${card.cardText}


        </div> <!-- end content -->



    </div> <!-- end content-pane -->


    <div class="grid_4 omega">
        <div style="padding: 0 1em 0 0; text-align: center;">
            <img alt="Card Image" src="${rc.getContextPath()}/services/card/image/${card.id}" style="width: 100%;" />
            <br /><h3>${card.price?string.currency}</h3>
        </div> <!-- end content -->
    </div>

    <div class="grid_9 alpha omega">
        <div class="content">
       <h3>All Sellers</h3>
            <#list card.prices as price>
            <p><@getSellerName price.seller.id />: <strong>${price.price?string.currency}</strong>
               <span style="font-size: small;">${price.dateAdded?datetime?string.short}</span></p>
            </#list></div> <!-- end content -->
    </div>

</div> <!-- right pane -->

</@page>
