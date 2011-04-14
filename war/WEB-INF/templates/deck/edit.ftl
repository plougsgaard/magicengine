<#include "../macros/utilities.ftl">

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <style>
        body, html {
            margin: 0;
            padding: 0;
        }

        #canvas_main {
            position: absolute;
            top: 314px;
            left: 0px;
        }

        #canvas_card {
            background-color: #778899;
        }

        #top {
            background-color: #ebe9e9;
            width: 781px;
            height: 306px;
            margin: 0 0 10px 0;
        }

        input#findadd-button {
            width: 10px;
        }

        input.text {
            width: 240px;
        }

        input#title-input {
            width:100%;
        }

        div#suggestion-area {
            position: absolute;
            width: 250px;
            background-color: #eee;
            border: 1px solid #242424; /* happy black */
            margin: 0;
            padding: 0;
        }

        div#suggestion-area ul {
            list-style-type: none;
            margin: 0;
            padding: 0;
        }

        div#suggestion-area ul li.selected {
            background-color: #B9D7D9; /* sky blue */
        }

        div#suggestion-area ul li {
            list-style-type: none;
            display: block;
            margin: 0;
            padding: 2px;
            cursor: pointer;
        }

        .count-span {
            font-weight:bold;
        }

    </style>
    <script src="${rc.getContextPath()}/static/javascript/lib/prototype.js" type="text/javascript"></script>
    <script src="${rc.getContextPath()}/static/javascript/src/scriptaculous.js" type="text/javascript"></script>
    <script type="text/javascript">
        <#include "edit.js"/>
    </script>
</head>

<body>

<div id="top">
    <div style="float:left; width: 250px; height:306px;">
        <div style="padding:1em;">
            <p>Deck name</p>
            <p>
                <input id="title-input" name="search-input" type="text" value="${deck.title}"/>
            </p>

            <p>Statistics</p>

            <ul>
                <li><span id="count-cards" class="count-span">0</span> cards</li>
                <li><span id="count-creatures" class="count-span">0</span> creatures</li>
                <li><span id="count-lands" class="count-span">0</span> lands</li>
            </ul>

            <p>
                <input class="button" id="save-button" type="button" value="Save"/>
                <input class="button" id="publish-button" type="button" value="Publish"/>
            </p>
        </div>
    </div>

    <div style="float:left; width: 212px; height:306px; border-width:0 10px 0 9px; border-style:solid; border-color:white;">
        <canvas id="canvas_card" width="212" height="306" style="cursor:pointer; background-color:#d2691e;">
            <p>Browser doesn't support canvas element.</p>
        </canvas>
    </div>

    <div style="float:left; width: 300px; height:306px;">
        <div style="padding:1em;">
            <p>Search</p>
            <p>
                <input class="text" id="search-input" name="search-input" type="text"/>
                <input class="button" id="findadd-button" type="button" value="!"/>
            </p>

            <div id="suggestion-area">
                <!-- Reserved for suggestions -->
            </div>

            <span id="loading-indicator" style="display:none;">
                <img src="${rc.getContextPath()}/static/images/site/progress-running.gif"/>
            </span>

            <p>Sort by</p>
            <input id="sort-by-cmc" type="radio" name="sort-by" value="CMC" checked="true">CMC<br>
            <input id="sort-by-color" type="radio" name="sort-by" value="Color">Color<br>

            <p id="card-search-status" class="center">&nbsp;</p>
        </div>
    </div>
</div>

<canvas id="canvas_main" width="100" height="100">
    <p>Browser doesn't support canvas element.</p>
</canvas>

</body>
</html>
