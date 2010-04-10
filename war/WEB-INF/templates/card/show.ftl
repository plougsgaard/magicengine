<#include "../macros/site.ftl">
<#include "../macros/decks.ftl">

<@page title="${card.cardName}">
<div class="grid_9 omega">

    <div class="grid_9 alpha omega">
        <div class="content">
            <h1><a href="${rc.getContextPath()}/cards">Cards</a> | ${card.cardName}</h1>
        </div> <!-- end content -->
    </div>

    <div class="grid_4 alpha">
        <div style="padding: 0 1em 0 0; text-align: center;">
            <img alt="Card Image" src="${rc.getContextPath()}/services/card/image/${card.id}" />
            <br /><h3>${card.price?string.currency}</h3>
        </div> <!-- end content -->
    </div> <!-- end content-pane -->


    <div class="grid_5 omega">
        <div class="content">
            <p><strong>Types</strong>: ${card.types?replace("�", "&ndash;")}</p>
            <p><strong>Rarity</strong>: ${card.rarity}</p>
            <p><strong>CMC</strong>: ${card.convertedManaCost}</p>
            <p><strong>Expansion</strong>: ${card.expansion} <#if card.setCode??>(${card.setCode})</#if></p>
            <p><strong>Artist</strong>: ${card.artist}</p>
            <p><strong>Rules text</strong>:</p> ${card.cardText}
        </div> <!-- end content -->
    </div>

    <div class="grid_9 alpha omega">
        <div class="content">
        <h2>Sellers</h2>


        </div> <!-- end content -->
    </div>

    <div class="grid_9 alpha omega">
        <#list card.prices as price>

            <div class="grid_3 alpha">
                <div style="padding-left: 1em;">
                    <p><@getSellerName price.seller.id /></p>
                </div>
            </div>


            <div class="grid_1">
                <#if (price.price > 0)>
                    <span style="
                    <#if (card.price == price.price)>
                    color: #006600; font-weight: bold;
                    </#if>
                    ">${price.price?string.currency}</span>

                <#else>
                    N/A
                </#if>
            </div>

            <div class="grid_5 omega">
                ${price.dateAdded?datetime?string.short}
            </div>

            <div class="clear">&nbsp;</div>
        </#list>
    </div>

    <div class="grid_9 alpha omega">
        <div class="content">
            <#if sessionUser??>
            <p>
            <form action="${rc.getContextPath()}/card/${card.id}/price/update" method="post">
                <input type="submit" value="Update Price" alt="Update Price (blocking operation).." />
            </form>
            </p>
            <p>
            <form action="${rc.getContextPath()}/card/${card.id}/update" method="post">
                <input type="submit" value="Update Card" alt="Update Card (blocking operation).." />
            </form>
            </p>
            </#if>
        </div> <!-- end content -->
    </div>

</div> <!-- right pane -->

</@page>
