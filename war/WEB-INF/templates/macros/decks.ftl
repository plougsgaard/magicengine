<#macro drawPageSelect deckPage>
Page &ndash;
<#list 1..deckPage.pageCount as i>
<#if i != deckPage.pageNumber>
<a href="${rc.getContextPath()}/decks/page/${i}">${i}</a>
<#else>
${i}
</#if>
</#list>
</#macro>

<#macro drawPageItems deckPage>
<#list deckPage.items as deck>

<!-- Deck item begin -->
<div class="grid_3 alpha">
    <div class="list-image">
        <img src="${rc.getContextPath()}/services/card/crop-image/${deck.id}" style="width: 100%;"/>
    </div>
</div>

<div class="grid_5 omega">
    <div class="content">
        <h3><a href="${rc.getContextPath()}/deck/${deck.id}">${deck.title}</a>
        </h3>

        <p>
            by <a href="${rc.getContextPath()}/decks/user/${deck.author.id}">${deck.author.name}</a>
        </p>
        <p>
            <@drawSymbols deck.colours />
        </p>
    </div>
</div>

<div class="clear"></div>
<!-- Deck item end -->
</#list>
</#macro>

<#macro drawFilterForm>
<div class="content" style="background-color:#eee;">
<h2>Filter</h2>

<form action="${rc.getContextPath()}/decks/filter" method="post">

    <h3>+Colours</h3>

    <p class="option">
        <@drawColourCheckbox "G" />
        <@drawColourCheckbox "W" />
        <@drawColourCheckbox "U" />
    </p>

    <p class="option">
        <@drawColourCheckbox "B" />
        <@drawColourCheckbox "R" />
   </p>

    <h3>+Title</h3>

    <p>
        <input name="title" id="title-input" class="full-width" type="text" value=""/>
    </p>

    <h3>+Author</h3>

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