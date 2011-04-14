/*
 * Constants
 */

var CONSTANTS = {
    CONST_STATUS_HIDDEN: "hidden",
    CONST_STATUS_PUBLIC: "public",
    SORT_BY_COLOR: "sort-by-color",
    SORT_BY_CMC: "sort-by-cmc",
    GUI: {
        sourceWidth: 220,
        sourceHeight: 314,
        sx: 4,
        sy: 4,
        width: 212, //this.sourceWidth - 2 * this.sx,
        height: 306, // this.sourceHeight - 2 * this.sy,
        horizontalPadding: 19,
        verticalPadding: 27
    }
};

/*
 * Data objects
 */

var DATA = {
    cards: new Hash(),
    transportable_deck: new Hash()
};

/*
 * GUI memory
 */

var GUI = {
    card_grid: new Array(), // card_grid[x][y]
    highlighted_index: undefined, // { x: ?, y: ? }
    selected_index: undefined, // { x: ?, y: ? }
    selected_card: undefined, // *
    pending_card: undefined, // *
    sort_by: CONSTANTS.SORT_BY_CMC,
    count: {
        cards: 0,
        lands: 0,
        creatures: 0
    }
};

/*
 * Card data type
 */

function SimpleCard(id, cardName, manaCost, convertedManaCost, types, price, count) {
    this.id = id;
    this.cardName = cardName;
    this.manaCost = manaCost;
    this.convertedManaCost = convertedManaCost;
    this.types = types;
    this.price = parseFloat(price);
    this.count = parseInt(count);
    this.image = new Image();
    this.image.src = "${rc.getContextPath()}/services/card/image/" + id + "/thumbnail";
}

/*
 ***************************************************************
 ************************** FUNCTIONS **************************
 ***************************************************************
 */

function update_publish_status(optional_status) {
    if (optional_status != undefined) {
        DATA.transportable_deck.set("status", optional_status);
    }
    var status = DATA.transportable_deck.get("status").toLowerCase();
    if (status.indexOf("hidden") != -1) {
        $("publish-button").value = "Publish";
    } else {
        $("publish-button").value = "Hide";
    }
}

function update_selected_card(card) {
    GUI.selected_card = card;
    show_card(card, $('search-input'));
    var ctx = $("canvas_card").getContext("2d");
    prim_draw(ctx, card, 0, 0, CONSTANTS.GUI.height);
}

function show_card(card, search_input) {
    GUI.selected_card = card;
    search_input.value = card.cardName;
}

function request_card_proxy(card_name) {
    request_card(card_name, undefined, $('card-search-status'));
}

function request_card_proxy_no_increment(card_name) {
    request_card(card_name, "true", $('card-search-status'));
}

function request_card(card_name, do_not_increment, card_search_status) {
    // trim leading and trailing whitespace
    card_name = card_name.replace(/^\s+|\s+$/g, '');

    if (card_name.length == 0) {
        // ignore empty searches
        return;
    }

    var url = "${rc.getContextPath()}/services/card/by-name/" + encodeURIComponent(card_name);
    url = url.replace("%C3%86", "Ae"); // HOTFIX :)

    var key = card_name.toLowerCase();

    if (DATA.cards.get(key) != undefined) {
        if (do_not_increment == undefined) {
            // we were not asked to avoid incrementing, so let's do it
            increment_card_count(DATA.cards.get(key));
        }
        update_selected_card(DATA.cards.get(key));
        return;
    }

    new Ajax.Request(url, {
        method: 'get',
        onCreate: function() {
            GUI.pending_card = card_name;
        },
        onSuccess: function(transport) {
            var card = transport.responseJSON;
            if (card.cardName.toLowerCase() != GUI.pending_card.toLowerCase()) {
                return;
            }
            if (card.exists != "true") {
                card_search_status.update("Could not find " + card.cardName + ".");
                return;
            }

            card_search_status.update("&nbsp;");

            // Add an entry for the card so we don't overwrite
            if (DATA.cards.get(key) == undefined) {
                DATA.cards.set(key, new SimpleCard(
                        card.id,
                        card.cardName,
                        card.manaCost,
                        card.convertedManaCost,
                        card.types,
                        card.price,
                        0
                        ));
            }

            update_selected_card(DATA.cards.get(key));
        }
    });
}

function get_position(e) {
    var obj = e.target;
    var curleft = 0;
    var curtop = 0;

    if (obj.offsetParent) {
        do {
            curleft += obj.offsetLeft;
            curtop += obj.offsetTop;
        } while (obj = obj.offsetParent);
    }

    var pos = [curleft, curtop];

    return { x: (e.pageX - pos[0]), y: (e.pageY - pos[1]) };
}

function highlightCursorPosition(e) {
    var pos = get_position(e);
    highlight(pos.x, pos.y);
}

function activate(e) {
    var pos = get_position(e);
    var index = xy_to_index(pos.x, pos.y);
    if (index != undefined && GUI.card_grid[index.x] != undefined && GUI.card_grid[index.x][index.y] != undefined) {
        if (GUI.selected_index != undefined && GUI.selected_index.x == index.x && GUI.selected_index.y == index.y) {
            GUI.selected_index = undefined;
            decrement_card_count(GUI.card_grid[index.x][index.y]);
            var context = $("canvas_main").getContext("2d");
            context.clearRect(0, 0, $("canvas_main").width, $("canvas_main").height);
        } else {
            GUI.selected_index = index;
            update_selected_card(GUI.card_grid[index.x][index.y]);
        }
    } else {
        GUI.selected_index = undefined;
    }
    redraw();
}

function xy_to_index(x, y) {
    var col_index = parseInt(x / parseInt(CONSTANTS.GUI.width + CONSTANTS.GUI.horizontalPadding));
    var col_rem = x % parseInt(CONSTANTS.GUI.width + CONSTANTS.GUI.horizontalPadding);

    var col_arr = GUI.card_grid[col_index];
    if (col_arr == undefined || col_rem > CONSTANTS.GUI.width) {
        return undefined;
    }

    // all cards but one show only vertical padding area (name part)
    var row_pixel_length = (col_arr.length - 1) * CONSTANTS.GUI.verticalPadding + CONSTANTS.GUI.height;

    if (y > row_pixel_length) {
        return undefined;
    }

    var row_index = parseInt(y / CONSTANTS.GUI.verticalPadding);
    if (row_index >= col_arr.length) {
        row_index = col_arr.length - 1;
    }

    return { x: col_index, y: row_index};
}

function highlight(x, y) {
    GUI.highlighted_index = xy_to_index(x, y);
    if (GUI.highlighted_index != undefined) {
        $("canvas_main").style.cursor = "pointer";
    } else {
        $("canvas_main").style.cursor = "default";
    }
    redraw();
}

function is_highlighted(x, y) {
    return GUI.highlighted_index != undefined && x == GUI.highlighted_index.x && y == GUI.highlighted_index.y;
}

function prim_draw(context, card, x, y, height) {
    context.drawImage(card.image, CONSTANTS.GUI.sx, CONSTANTS.GUI.sy, CONSTANTS.GUI.width, height,
            CONSTANTS.GUI.width * x + CONSTANTS.GUI.horizontalPadding * x, CONSTANTS.GUI.verticalPadding * y, CONSTANTS.GUI.width, height);
}

function draw(context, card, x, y, only_border) {
    var h = only_border ? CONSTANTS.GUI.verticalPadding : CONSTANTS.GUI.height;
    prim_draw(context, card, x, y, h);

    if (is_highlighted(x, y)) {
        var ipic1 = context.getImageData(CONSTANTS.GUI.width * x + CONSTANTS.GUI.horizontalPadding * x, CONSTANTS.GUI.verticalPadding * y, CONSTANTS.GUI.width, h);
        var idata1 = ipic1.data;
        for (var i = 0; i < idata1.length; i += 4) {
            idata1[i + 3] = 200;
        }
        context.putImageData(ipic1, CONSTANTS.GUI.width * x + CONSTANTS.GUI.horizontalPadding * x, CONSTANTS.GUI.verticalPadding * y);
    }

    if (GUI.selected_index != undefined && GUI.selected_index.x == x && GUI.selected_index.y == y) {
        var ipic = context.getImageData(CONSTANTS.GUI.width * x + CONSTANTS.GUI.horizontalPadding * x, CONSTANTS.GUI.verticalPadding * y, CONSTANTS.GUI.width, h);
        var idata = ipic.data;
        for (var j = 0; j < idata.length; j += 4) {
            idata[j + 2] = 255;
        }
        context.putImageData(ipic, CONSTANTS.GUI.width * x + CONSTANTS.GUI.horizontalPadding * x, CONSTANTS.GUI.verticalPadding * y);
    }

    var types = card.types.toLowerCase();
    if (types.indexOf("basic land") != -1) {
        context.font = "bold 13px sans-serif";
        context.textAlign = "right";
        context.textBaseline = "top";
        context.fillText(card.count, CONSTANTS.GUI.width * x + CONSTANTS.GUI.horizontalPadding * x + CONSTANTS.GUI.width - 10, CONSTANTS.GUI.verticalPadding * y + 10);
    }
}

function redraw() {
    var context = $("canvas_main").getContext("2d");
    for (var x = 0; x < GUI.card_grid.length; x++) {
        if (GUI.card_grid[x] == undefined) {
            continue;
        }
        for (var y = 0; y < GUI.card_grid[x].length; y++) {
            var card = GUI.card_grid[x][y];

            if (y == GUI.card_grid[x].length - 1) {
                draw(context, card, x, y, false);
            } else {
                draw(context, card, x, y, true);
            }
        }
    }
}

function update_columns() {

    var cardList = new Hash();
    GUI.count.cards = GUI.count.lands = GUI.count.creatures = 0;

    GUI.card_grid = new Array();
    DATA.cards.each(function(pair) {
        var card = pair.value;
        if (card.count == 0) {
            // ignored
        } else {
            //UWRBG
            var column = 0;
            if (GUI.sort_by == CONSTANTS.SORT_BY_CMC) {
                column = Math.max(card.convertedManaCost - 1, 0);
            } else if (GUI.sort_by == CONSTANTS.SORT_BY_COLOR) {
                if (card.manaCost.indexOf("U") != -1) {
                    column = 1;
                } else if (card.manaCost.indexOf("W") != -1) {
                    column = 2;
                } else if (card.manaCost.indexOf("R") != -1) {
                    column = 3;
                } else if (card.manaCost.indexOf("B") != -1) {
                    column = 4;
                } else if (card.manaCost.indexOf("G") != -1) {
                    column = 5;
                } else {
                    column = 0;
                }

            }

            if (GUI.card_grid[column] == undefined) {
                GUI.card_grid[column] = new Array();
            }

            var types = card.types.toLowerCase();
            if (types.indexOf("basic land") != -1) {
                GUI.card_grid[column][GUI.card_grid[column].length] = card;
            } else {
                for (var i = 1; i <= card.count; ++i) {
                    GUI.card_grid[column][GUI.card_grid[column].length] = card;
                }
            }

            if (types.indexOf("land") != -1) {
                GUI.count.lands += card.count;
            } else if (types.indexOf("creature") != -1) {
                GUI.count.creatures += card.count;
            }

            GUI.count.cards += card.count;

            cardList.set("" + card.id, "" + card.count);
        }
    });

    DATA.transportable_deck.set("card_list", cardList.toJSON());
    DATA.transportable_deck.set("title", $("title-input").value);

    $("count-cards").update(GUI.count.cards);
    $("count-lands").update(GUI.count.lands);
    $("count-creatures").update(GUI.count.creatures);

    GUI.card_grid = GUI.card_grid.compact();

    var max = 0;
    for (var i = 0; i < GUI.card_grid.length; ++i) {
        if (GUI.card_grid[i] == undefined) {
            continue;
        }
        max = Math.max(GUI.card_grid[i].length, max);
    }

    GUI.card_grid = GUI.card_grid.compact();

    $("canvas_main").height = max * CONSTANTS.GUI.verticalPadding + CONSTANTS.GUI.height;
    $("canvas_main").width = GUI.card_grid.length * (CONSTANTS.GUI.horizontalPadding + CONSTANTS.GUI.width) - CONSTANTS.GUI.horizontalPadding;
}

function increment_card_count(card) {
    alter_card_count(card, card.count + 1);
}

function decrement_card_count(card) {
    alter_card_count(card, card.count - 1);
}

function alter_card_count(card, count) {
    if (card != undefined) {
        if (count < 0) {
            return;
        }

        card.count = count;
        update_selected_card(card);

        update_columns();
        redraw();
    }
}

function save_deck() {
    new Ajax.Request("${rc.getContextPath()}/services/deck/save/${deck.id?c}/key/${deckKey}", {
        method: 'post',
        postBody: DATA.transportable_deck.toJSON(),
        contentType: "application/json",
        onCreate: function() {
            $("publish-button").disable();
            $("save-button").disable();
        },
        onComplete: function() {
            $("publish-button").enable();
            $("save-button").enable();
            update_publish_status();
        }
    });
}

/*
 ***************************************************************
 ********************** INITIALIZATION *************************
 ***************************************************************
 */

window.onload = function() {
    /**
     * Populate the lists with cards already in the deck.
     */
    new Ajax.Request("${rc.getContextPath()}/services/deck/fetch/${deck.id?c}/key/${deckKey}", {
        method: 'get',
        onCreate: function() {
        },
        onSuccess: function(transport) {
            var cards = transport.responseJSON.cards;

            cards.each(function(card) {
                var key = card.cardName.toLowerCase();
                DATA.cards.set(key, new SimpleCard(
                        card.id,
                        card.cardName,
                        card.manaCost,
                        card.convertedManaCost,
                        card.types,
                        card.price,
                        card.count
                        ));
            });

            update_columns();
            redraw();
        }
    });

    /*
     * Initialize the asynchronous suggestion mechanism.
     */
    new Ajax.Autocompleter('search-input', 'suggestion-area',
            '${rc.getContextPath()}/services/card/suggestion',
    {
        afterUpdateElement: function () {
            request_card_proxy_no_increment($('search-input').value);
        }
    });

    /*
     * Update interface & Initialize data set
     */
    DATA.transportable_deck.set("id", "${deck.id?c}");
    DATA.transportable_deck.set("status", "${deck.status}");
    update_publish_status("${deck.status}");

    $('search-input').observe('keyup', function(e) {
        e.stop();

        if (e.keyCode == Event.KEY_RETURN) {
            request_card_proxy($('search-input').value);
        }
    });

    $('findadd-button').observe('click', function(e) {
        e.stop();
        var proposed_card_name = $('search-input').value;
        if (!$('suggestion-area').visible()) {
            request_card_proxy(proposed_card_name);
        }
    });

    $("sort-by-cmc").observe('click', function(e) {
        GUI.sort_by = CONSTANTS.SORT_BY_CMC;
        update_columns();
        redraw();
    });

    $("sort-by-color").observe('click', function(e) {
        GUI.sort_by = CONSTANTS.SORT_BY_COLOR;
        update_columns();
        redraw();
    });

    /*
     * Ready the canvas
     */
    $("canvas_main").observe('click', activate);
    $("canvas_main").observe('mousemove', highlightCursorPosition);

    $("canvas_card").observe("click", function (e) {
        if (GUI.selected_card != undefined) {
            increment_card_count(GUI.selected_card);
        }
    });

    $("save-button").observe("click", function (e) {
        save_deck();
    });

    $("publish-button").observe("click", function (e) {
        DATA.transportable_deck.set("status",
                DATA.transportable_deck.get("status").toLowerCase().indexOf("hidden") != -1 ?
                        CONSTANTS.CONST_STATUS_PUBLIC : CONSTANTS.CONST_STATUS_HIDDEN);
        save_deck();
    });

    /*
     * Draw GUI for the first time
     */
    redraw();

    /*
     * Move the cursor to the input box.
     */
    $('search-input').activate();
};



