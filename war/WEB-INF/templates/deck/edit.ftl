<#include "../macros/site.ftl">
<#include "../macros/decks.ftl">

<@page title="Edit Deck" scripts=["/deck/edit.js"]>
<div class="grid_9 omega">

    <div id="content-pane" class="grid_9 alpha">

        <div id="content-header" class="content">

            <h1><a href="${rc.getContextPath()}/decks">Decks</a> | Edit</h1>

        </div>

        <form action="" method="post">
            <input type="hidden" name="id" value="${deck.id}"/>

            <div class='grid_4 alpha'>
                <div id="title-chooser" class="content">
                    <h4>Deck Title</h4>

                    <p>
                        <input id="title-input" name="title" class="text" value="${deck.title}"/>
                    </p>
                </div>
            </div>

            <div class='grid_3'>
                <div class="content">
                    <h4>&nbsp;</h4>

                    <p>
                        <a href="${rc.getContextPath()}/deck/${deck.id?c}" style="text-decoration: none;"
                           title="View or Preview Deck">
                            <img src="${rc.getContextPath()}/static/images/site/view.gif"
                                 style="vertical-align:middle;"/></a>
                        <a href="${rc.getContextPath()}/deck/${deck.id?c}/copy" style="text-decoration: none;"
                           title="Duplicate Deck">
                            <img src="${rc.getContextPath()}/static/images/site/duplicate.gif"
                                 style="vertical-align:middle;"/></a>
                        <a href="${rc.getContextPath()}/deck/${deck.id?c}/delete" style="text-decoration: none;"
                           title="Delete Deck">
                            <img src="${rc.getContextPath()}/static/images/site/delete.gif"
                                 style="vertical-align:middle;"/></a>
                    </p>
                </div>
            </div>

            <div class='grid_2 omega'>
                <div id="controls" class="content">
                    <h4>&nbsp;</h4>

                    <p>
                        <input id="save-button" type="button" class="button" value="Save"/>
                    </p>
                </div>
            </div>

            <!-- Format, Colours and Description -->
            <div class="clear">&nbsp;</div>

            <div class='grid_2 alpha'>
                <div id="format-chooser" class="content">
                    <h4>Format</h4>

                    <p>
                        <select id="format-select" name="format" class="text">
                            <option value="Extended"<#if deck.format?lower_case == "extended">
                                    selected="selected"</#if>>Extended
                            </option>
                            <option value="Standard"<#if deck.format?lower_case == "standard">
                                    selected="selected"</#if>>Standard
                            </option>
                            <option value="Other"<#if deck.format?lower_case == "none"> selected="selected"</#if>>
                                Other
                            </option>
                        </select>
                    </p>
                </div>

                <div id="status-chooser" class="content">
                    <h4>Status</h4>

                    <p>
                        <select id="status-select" name="status" class="text">
                            <option value="Hidden"<#if deck.status?lower_case == "hidden"> selected="selected"</#if>>
                                Hidden
                            </option>
                            <option value="Public"<#if deck.status?lower_case == "public"> selected="selected"</#if>>
                                Public
                            </option>
                        </select>
                    </p>
                </div>
            </div>

            <div class='grid_2'>
                <div id="colours-chooser" class="content">
                    <h4>Colours</h4>
                <@drawColourCheckbox deck.colours "G" /><br/>
                <@drawColourCheckbox deck.colours "W" /><br/>
                <@drawColourCheckbox deck.colours "U" /><br/>
                <@drawColourCheckbox deck.colours "B" /><br/>
                <@drawColourCheckbox deck.colours "R" />
                </div>
            </div>

            <div class='grid_5 omega'>
                <div style="display:none;" class="content">
                    <h4>Description</h4>

                    <p><textarea id="description-textarea" name="description" class="text"
                                 rows="6" cols="0">${deck.description}</textarea></p>
                </div>
                <!-- Card search -->
                <div id="card-search" class="content">
                    <h3>Card Search</h3>

                    <p>
                        <input class="text" id="search-input" name="search-input" type="text"/>
                        <input class="button" id="findadd-button" type="button" value="Find/Add"/>
                    </p>

                    <div id="suggestion-area">
                        <!-- Reserved for suggestions -->
                    </div>
                </div>
            </div>

            <div class='clear'>&nbsp;</div>

            <!-- Card image, information & Main Deck -->
            <div style="display:none;" class='grid_4 alpha'>

                <div id="card-information" class="content">
                    <h3>&nbsp;</h3>

                    <p class="center">
                        <img id="card-image" class="card" src="${rc.getContextPath()}/static/images/site/card.jpg"
                             alt=""/>
                    </p>
                    <h4 id="card-price" class="center">&nbsp;</h4>

                    <p id="card-search-status" class="center">&nbsp;</p>
                </div>

            </div>

            <div style="display:none;" class='grid_5 omega'>



                <div id="main-deck" class="content">
                    <h3>Main Deck (<span id="cards-count"></span>)</h3>

                    <div class="card-list">
                        <h4>Lands <span id="lands-count"></span></h4>
                        <ul class="deck" id="lands-ul">
                            <!-- Filled in by JavaScript -->
                        </ul>
                    </div>

                    <div class="card-list">
                        <h4>Creatures <span id="creatures-count"></span></h4>
                        <ul class="deck" id="creatures-ul">
                            <!-- Filled in by JavaScript -->
                        </ul>
                    </div>

                    <div class="card-list">
                        <h4>Spells <span id="spells-count"></span></h4>
                        <ul class="deck" id="spells-ul">
                            <!-- Filled in by JavaScript -->
                        </ul>
                    </div>
                </div>

                <div class="content">
                    <h4>Feature Card</h4>

                    <p>
                        <select name="featureCardId" class="text">
                            <#list deck.cards as card>
                                <option value="${card.id?c}"
                                    <#if (card.id == deck.featureCardId)>
                                        selected="selected"
                                    </#if>
                                        >${card.cardName}</option>
                            </#list>
                        </select>
                    </p>
                </div>
            </div>
        </form>

    </div>
    <!-- end content-pane -->

</div>

<div class='clear'>&nbsp;</div>

<div class='grid_12 alpha omega'>

    <img id="img1" src="${rc.getContextPath()}/services/card/image/1538/thumbnail" style="display:none"/>

    <div class="content">

    <canvas id="canvas" width="925" height="600" style="margin-left:10px;">
        <p>Browser doesn't support canvas element.</p>
    </canvas>

        </div>

</div>

</@page>
