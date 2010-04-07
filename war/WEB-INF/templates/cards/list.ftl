<#include "../macros/site.ftl">
<#include "../macros/cards.ftl">

<@page title="Cards" scripts=["/cards/list.js"]>
<div class="grid_9 omega">

<div id="content-pane" class="grid_9 alpha">

    <div id="content-header" class="content">

        <h1><a href="${rc.getContextPath()}/decks">Cards</a></h1>

        <form id="search-form" action="${rc.getContextPath()}/card/search" method="post">
        <h3>Card Search</h3>
            <p>
                <input class="text" id="search-input" name="search-input" type="text" value=""/>
                <input class="button" id="search-submit" name="search-submit" type="submit" value="Find"/>
            </p>
            <div id="suggestion-area">
                <!-- Reserved for suggestions -->
            </div>
        </form>

        <@drawPageSelect cardPage "/cards/page/" />

        <@drawPageItems cardPage />

        <@drawPageSelect cardPage "/cards/page/" />

    </div> <!-- end content-header -->

</div> <!-- end content-pane -->

</div>
</@page>
 