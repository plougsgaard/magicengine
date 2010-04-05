<#include "../macros/site.ftl">

<@page>
<div id="content-pane" class="grid_6 alpha"> <!-- content pane -->

    <div class="content">

        <h1>Decks</h1>

        <p>Listing decks ordered by <b>date</b> of creation.</p>

        <h3>Page &ndash;
            1
            <a href="">2</a>
            <a href="">3</a>
            <a href="">4</a>
        </h3>

    </div>
    <!-- end content -->

    <div class="grid_2 alpha">
        <div class="list-image">
            <img src="images/temp1.png" style="width: 100%;"/>
        </div>
    </div>

    <div class="grid_4 omega">
        <div class="content">
            <div style="float:right;">
            <@drawSymbol "R" /><@drawSymbol "B" />
            </div>
            <h4><a href="">Henry Fonda's Twin Brother in Law
            </a></h4>

            <p>
                by <a href="">Thomas</a>
            </p>
        </div>
    </div>

    <div class="clear"></div>

    <!-- Deck item begin -->

    <div class="grid_2 alpha">
        <div class="list-image">
            <img src="images/temp2.png" style="width: 100%;"/>
        </div>
    </div>

    <div class="grid_4 omega">
        <div class="content">
            <div style="float:right;">
            <@drawSymbol "G" />
            </div>
            <h4><a href="">Tenderish Thinking
            </a></h4>

            <p>
                by <a href="">Thomas</a>
            </p>
        </div>
    </div>

    <div class="clear"></div>

    <!-- Deck item end -->

    <!-- Deck item begin -->

    <div class="grid_2 alpha">
        <div class="list-image">
            <img src="images/temp3.png" style="width: 100%;"/>
        </div>
    </div>

    <div class="grid_4 omega">
        <div class="content">
            <div style="float:right;">
            <@drawSymbol "B" /><@drawSymbol "W" /><@drawSymbol "R" />
            </div>
            <h4><a href="">Evil in the Making
            </a></h4>

            <p>
                by <a href="">Kasper</a>
            </p>
        </div>
    </div>

    <div class="clear"></div>

    <!-- Deck item end -->

</div>


<div id="filter-pane" class="grid_3 omega">
    <div class="content">
        <h2>Filter</h2>

        <form>

            <h3>+Colours</h3>

            <p class="option">
                <input id="colour-green-checkbox" name="colours" value="green" checked="checked"
                       type="checkbox"><@drawSymbol "G" />

                <input id="colour-white-checkbox" name="colours" value="white" checked="checked"
                       type="checkbox"><@drawSymbol "W" />

                <input id="colour-blue-checkbox" name="colours" value="blue" type="checkbox"><@drawSymbol "U" />
            </p>

            <p class="option">
                <input id="colour-black-checkbox" name="colours" value="black"
                       type="checkbox"><@drawSymbol "B" />

                <input id="colour-red-checkbox" name="colours" value="red" type="checkbox"><@drawSymbol "R" />
            </p>

            <h3>+Title</h3>

            <p>
                <input class="full-width" type="text" value="bitterblossom"/>
            </p>

            <h3>+Author</h3>

            <p>
                <select id="author-select" name="author" class="full-width">
                    <option value="Thomas" selected="selected">Thomas</option>
                    <option value="Bjarke">Bjarke</option>
                    <option value="Kasper">Kasper</option>
                </select>
            </p>

            <input type="submit" value="Submit Filter"/>

        </form>
    </div>
    <!-- end side -->
</div>
</@page>