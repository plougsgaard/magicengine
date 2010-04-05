<#include "../macros/site.ftl">

<@page>
<div id="content-pane" class="grid_6 alpha">

    <div id="content-header" class="content">

        <h1>Decks</h1>

        <p>Listing decks ordered by <b>date</b> of creation.</p>

        <h3>
            Page &ndash;
            <#list 1..deckPage.pageCount as i>
            <#if i != deckPage.pageNumber>
            <a href="${rc.getContextPath()}/decks/page/${i}">${i}</a>
            <#else>
            ${i}
            </#if>
            </#list>
        </h3>

    </div> <!-- end content-header -->

    <#list deckPage.items as deck>

    <!-- Deck item begin -->
    <div class="grid_2 alpha">
        <div class="list-image">
            <img src="${rc.getContextPath()}/static/images/symbols/0.gif" style="width: 100%;"/>
        </div>
    </div>

    <div class="grid_4 omega">
        <div class="content">
            <div style="float:right;">
            <@drawSymbol "R" /><@drawSymbol "B" />
            </div>
            <h4><a href="${rc.getContextPath()}/deck/${deck.id}">${deck.title}</a></h4>

            <p>
                by <a href="${rc.getContextPath()}/decks/user/${deck.author.id}">${deck.author.name}</a>
            </p>
        </div>
    </div>

    <div class="clear"></div>
    <!-- Deck item end -->
    </#list>

</div> <!-- end content-pane -->


<div id="filter-pane" class="grid_3 omega">

    <div class="content">
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
                <input name="title" id="title-input" class="full-width" type="text" value="bitterblossom"/>
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

</div> <!-- end filter-pane -->
</@page>