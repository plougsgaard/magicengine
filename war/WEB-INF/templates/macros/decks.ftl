<#include "utilities.ftl" />

<#macro drawPageItems deckPage>
<#list deckPage.items as deck>

<!-- Deck item begin -->
<div class="grid_8 alpha omega" style="margin-bottom:0.5em;">
    <div class="list-image" style="float:left; padding: 0 1em 0 1em;">
        <a href="${rc.getContextPath()}/deck/${deck.id?c}">
        <img alt="Image Thumbnail" src="${rc.getContextPath()}/services/card/image/${deck.featureCardId?c}/cutout"/>
        </a>
    </div>
    <div class="list-deck" style="margin: 0 0 0 0.5em;">
        <h3><a href="${rc.getContextPath()}/deck/${deck.id?c}">${deck.title} <@drawSymbols deck.colours /></a>
        </h3>
        <p>
            by <a href="${rc.getContextPath()}/decks/user/${deck.author.id?c}">${deck.author.name}</a>
        </p>
    </div>
</div>

<div class="clear"></div>
<!-- Deck item end -->
</#list>
</#macro>

<#macro drawCardItem card>
<li>
    <strong>${card.count}</strong>
    <a href="${rc.getContextPath()}/card/${card.id?c}"
            title="<img src='${rc.getContextPath()}/services/card/image/${card.id?c}/thumbnail' width='220' />">
        ${card.cardName} <@drawSymbolsSmall name=card.manaCost />
    </a>
    &ndash;
    <span class="price">${(card.count * card.price)?string.currency}</span>
</li>
</#macro>

<#macro drawFilterForm>
<div class="content" style="background-color:#eee;">
<h2>Filter</h2>

<form action="${rc.getContextPath()}/decks/filter" method="post">

    <h3>+Colours</h3>

    <p class="option">
        <@drawColourCheckboxClear "G" />
        <@drawColourCheckboxClear "W" />
        <@drawColourCheckboxClear "U" />
    </p>

    <p class="option">
        <@drawColourCheckboxClear "B" />
        <@drawColourCheckboxClear "R" />
   </p>

    <h3><label for="title-input">+Title</label></h3>

    <p>
        <input name="title" id="title-input" class="full-width" type="text" value=""/>
    </p>

    <h3><label for="author-select">+Author</label></h3>

    <p>
        <select name="author" id="author-select" class="full-width">
            <option value="Thomas" selected="selected">Thomas</option>
            <option value="Bjarke">Bjarke</option>
            <option value="Kasper">Kasper</option>
        </select>
    </p>

    <input type="submit" value="Submit Filter"/>

</form>
</div>
</#macro>
