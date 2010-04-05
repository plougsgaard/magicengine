<#include "/macros/utilities.ftl">

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>Design w960 Clean</title>

<link rel="stylesheet" href="${rc.getContextPath()}/static/css/reset.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${rc.getContextPath()}/static/css/960.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${rc.getContextPath()}/static/css/text.css" type="text/css" media="screen" />

<link rel="stylesheet" href="${rc.getContextPath()}/static/css/default.css" type="text/css" media="screen" />

</head>
<body>
<div id="container" class="container_12">
    <div class="grid_12 alpha omega">
        <div id="logo">
            <img src="${rc.getContextPath()}/static/images/site/logo.png" />
        </div>
    </div>

    <div id="separator" class="grid_12 alpha omega">
        <img src="${rc.getContextPath()}/static/images/site/separator.png" />
    </div>

    <div class="grid_3 alpha">
        <div id="menu">
            <ul>
                <a href="" id="selected">Decks</a>
                <li><a href="">Advanced Search</a></li>
                <li><a href="">Create New</a></li>
            </ul>
            <ul>
                <a href="">Cards</a>
                <li><a href="">Advanced Search</a></li>
                <li><a href="">Update Queue</a></li>
            </ul>
            <ul>
                <a href="">Users</a>
                <li><a href="">Login</a></li>
            </ul>
        </div>

    </div>
    <div class="grid_6">
        <div class="content">

        <h1>Decks</h1>
        <p>Listing decks ordered by <b>date</b> of creation.</p>

        <h3>Page &ndash;
        1
        <a href="">2</a>
        <a href="">3</a>
        <a href="">4</a>
        </h3>

        </div> <!-- end content -->

        <div class="grid_2 alpha">
        <div class="list-image">
        <img src="images/temp1.png" style="width: 100%;" />
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
        <img src="images/temp2.png" style="width: 100%;" />
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
        <img src="images/temp3.png" style="width: 100%;" />
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


    <div class="grid_3 omega">
    <div class="content" id="filter-pane">
    <h2>Filter</h2>
    <form>

    <h3>+Colours</h3>
    <p class="option">
        <input id="colour-green-checkbox" name="colours" value="green" checked="checked" type="checkbox"><@drawSymbol "G" />

        <input id="colour-white-checkbox" name="colours" value="white" checked="checked" type="checkbox"><@drawSymbol "W" />

        <input id="colour-blue-checkbox" name="colours" value="blue" type="checkbox"><@drawSymbol "U" />
    </p>
    <p class="option">
        <input id="colour-black-checkbox" name="colours" value="black" type="checkbox"><@drawSymbol "B" />

        <input id="colour-red-checkbox" name="colours" value="red" type="checkbox"><@drawSymbol "R" />
    </p>

    <h3>+Title</h3>
    <p>
        <input class="full-width" type="text" value="bitterblossom" />
    </p>
    <h3>+Author</h3>
    <p>
        <select id="author-select" name="author" class="full-width">
            <option value="Thomas" selected="selected">Thomas</option>
            <option value="Bjarke">Bjarke</option>
            <option value="Kasper">Kasper</option>
        </select>
    </p>

    <input type="submit" value="Submit Filter" />

    </form>
    </div> <!-- end side -->
    </div>
</div>



</body>
</html>