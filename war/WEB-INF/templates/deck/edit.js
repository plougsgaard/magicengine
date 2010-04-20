/*
 * Array to hold the cards locally seen so far. Used to avoid
 * requesting the same (non-existant) card again and again,
 * and to make sure no card is added more than once.
 */
var cardHash = new Hash();
var selectedCard = undefined;

function SimpleCard(id, cardName, manaCost, convertedManaCost, types, price, count)
{
    this.id = id;
    this.cardName = cardName;
    this.manaCost = manaCost;
    this.convertedManaCost = convertedManaCost;
    this.types = types;
    this.price = parseFloat(price);
    this.count = parseInt(count);
}

function get_colour_images (mana_cost)
{
    var span = Builder.node('span');
    var mana_array = mana_cost.split(",");

    mana_array.each(function(symbol) {
        if (!symbol.isUndefined && symbol.length > 0) {
            span.appendChild(Builder.node('img',
                {src: "${rc.getContextPath()}/static/images/symbols/" + symbol + ".gif" }));
        }
    });

    return span;
}

function decorate_price (price, locale)
{
    var result = price.toFixed(2);
    if (locale == "da_DK") {
        result = result.split(".")[0] + "," + result.split(".")[1];
        result += " kr.";
    } else if (locale == "en_GB") {
        result = "&pound;" + result;
    } else if (locale == "en_US") {
        result = "$" + result;
    }
    return result;
}

var pendingCard = "";

function request_card_proxy (card_name)
{
    request_card (card_name, $('card-name'), $('card-image'), $('card-price'), undefined, $('card-search-status'));
}

function request_card_proxy_no_increment (card_name)
{
    request_card (card_name, $('card-name'), $('card-image'), $('card-price'), "true", $('card-search-status'));
}

function request_card (card_name, card_name_text, card_image_img, card_price_text, do_not_increment, card_search_status)
{
    // trim leading and trailing whitespace
    card_name = card_name.replace(/^\s+|\s+$/g, '');

    if (card_name.length == 0) {
        // ignore empty searches
        return;
    }

    var url = "${rc.getContextPath()}/services/card/by-name/" + encodeURIComponent(card_name);
    url = url.replace("%C3%86", "Ae"); // HOTFIX :)

    var key = card_name.toLowerCase();

    if (cardHash.get(key) != undefined)
    {
        // no need to request it, we already have it
        selectedCard = cardHash.get(key);
        if (do_not_increment == undefined) {
            // we were not asked to avoid incrementing, so let's do it
            increment_card_count (selectedCard);
        } else {
            // instead of incrementing we just showHandler the card (also a side-effect of inc)
            show_card_proxy (cardHash.get(key));
        }

        // no need to request it, we already have it
        show_card_proxy (cardHash.get(key));
        return;
    }
    
    new Ajax.Request(url, {
        method: 'get',
        onCreate: function() {
            // Put a nice little loading graphics up
            card_search_status.update("Searching for `" + card_name + "`.. <img src=\"${rc.getContextPath()}/static/images/site/progress-running.gif\"/>");
            pendingCard = card_name;
        },
        onSuccess: function(transport) {
            var card = transport.responseJSON;
            if (card.cardName.toLowerCase() != pendingCard.toLowerCase()) {
                return;
            }
            if (card.exists != "true")
            {
                card_search_status.update("Could not find <b>" + card.cardName + "</b>.");
                return;
            }

            card_search_status.update("&nbsp;");

            // Add an entry for the card so we don't overwrite
            if (cardHash.get(key) == undefined)
            {
                cardHash.set(key, new SimpleCard(
                        card.id,
                        card.cardName,
                        card.manaCost,
                        card.convertedManaCost,
                        card.types,
                        card.price,
                        0
                ));
            }
            show_card_proxy (cardHash.get(key));
        }
    });
}

function show_card_proxy (card)
{
    show_card (card, $('card-name'), $('card-image'), $('card-price'), $('search-input'));
}

function show_card (card, card_name_text, card_image_img, card_price_text, search_input)
{
    selectedCard = card;
    var price = parseFloat(card.price);
    card_image_img.src = "${rc.getContextPath()}/services/card/image/" + card.id;
    //card_name_text.update(Builder.node('span', [card.cardName, Builder.node('br'), get_colour_images(card.manaCost)]));
    card_price_text.update(decorate_price(price, "en_GB"));
    search_input.value = card.cardName;
}

function update_card (card)
{
    add_to_list_proxy (card);
}

function add_to_list_proxy (card)
{
    add_to_list (card, $('cards-count'),
                 $('creatures-count'), $('creatures-ul'),
                 $('lands-count'), $('lands-ul'),
                 $('spells-count'), $('spells-ul'));
}

function add_to_list (card, cards_count_box,
                      creatures_count_box, creatures_list_box,
                      lands_count_box, lands_list_box,
                      spells_count_box, spells_list_box)
{
    var types = card.types.toLowerCase();
    if (types.indexOf("creature") != -1) {
        add_to_list_helper(card, creatures_count_box, creatures_list_box);
    } else if (types.indexOf("land") != -1) {
        add_to_list_helper(card, lands_count_box, lands_list_box);
    } else {
        add_to_list_helper(card, spells_count_box, spells_list_box);
    }
    cards_count_box.update(get_card_count() +" cards");
    //$('card_' + card.id).update(decorate_price(card.count * card.price, "en_GB"));
}

function get_card_count()
{
    var result = 0;
    cardHash.each ( function (pair) {
        result += pair.value.count;
    });
    return result;
}

function form_input_is_int(input)
{
    return !isNaN(input) && parseInt(input)==input;
}

function build_element_card_name (card) {
    var element = Builder.node('a', { href: '' }, card.cardName );
    element.observe('click', function(e)
    {
        e.stop();
        show_card_proxy(card);
    });
    return element;
}

function build_element_count (card) {
    var element = Builder.node('input',
        { id: 'card_' + card.id, name: 'card_' + card.id, value: card.count });
    element.observe('change', function(e)
    {
        if (form_input_is_int(element.value)) {
            card.count = parseInt(element.value);
            update_card(card);
        } else {
            element.value = card.count;
        }
    });
    return element;
}

function build_element_price (card) {
    // TODO: can't display pound tag...
    return Builder.node('span', {id: 'price_' + card.id, class: 'price'}, [
        decorate_price(card.count * card.price, "").unescapeHTML()]);
}

/**
 * Handles both adding new cards and updating existing cards.
 *
 * @param card
 * @param count_box
 * @param list_box
 */
function add_to_list_helper (card, count_box, list_box)
{
    var cardElement = $('card_' + card.cardName);

    if (cardElement == undefined)
    {
        /*
         * New card is about to be added.
         */

        // Prepare elements
        var cardNameElement = build_element_card_name(card);
        var cardCountInput = build_element_count(card);
        var manaCostElement = get_colour_images(card.manaCost);
        var priceElement = build_element_price(card);
        var cardElementContents = Builder.node('li', [
            cardCountInput, ' ',
            cardNameElement, ' ',
            manaCostElement, ' ',
            priceElement]);
        var cardElementDiv = Builder.node('div', {
            id: 'card_' + card.cardName }, cardElementContents);

        // Add the element
        list_box.insert({ top: cardElementDiv });
    }
    else
    {
        /*
         * Update the existing card.
         *
         * What could have changed?
         *
         * 1) Price
         * 2) Count
         */
        $('card_' + card.id).value = card.count;
        $('price_' + card.id).update(decorate_price(card.count * card.price, "").unescapeHTML());

        // Hide it if it's being removed
        if (card.count <= 0) {
            cardElement.hide();
        } else {
            cardElement.show();
        }
    }
}

function alter_card_count_proxy (card, count)
{
    alter_card_count (card, count);
}

function increment_card_count (card)
{
    alter_card_count (card, card.count + 1);
}

function alter_card_count (card, count)
{
    if (card != undefined)
    {
        if (count < 0) {
            return;
        }

        card.count = count;
        show_card_proxy (card);
        update_card (card);
    }
}

document.observe('dom:loaded', function()
{
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
                cardHash.set(key, new SimpleCard(
                        card.id,
                        card.cardName,
                        card.manaCost,
                        card.convertedManaCost,
                        card.types,
                        card.price,
                        card.count
                ));
                add_to_list_proxy (cardHash.get(key));
            });
        }
    });

    /*
     * Initialize the asynchronous suggestion mechanism.
     */
    new Ajax.Autocompleter('search-input', 'suggestion-area',
        '${rc.getContextPath()}/services/card/suggestion',
    {
        afterUpdateElement: function () {
            var proposed_card_name = $('search-input').value;
            request_card_proxy_no_increment(proposed_card_name);
        }
    });

    /**
     * Listens for KEY_RETURN.
     *
     * If the suggestion box is not showing, try to add the card!
     */
    $('search-input').observe('keyup', function(e)
    {
        e.stop();

        if (e.keyCode == Event.KEY_RETURN)
        {
            var proposed_card_name = $('search-input').value;
            if (!$('suggestion-area').visible())
            {
                request_card_proxy(proposed_card_name);
            }
        }
    });

    $('findadd-button').observe('click', function(e)
    {
        e.stop();
        var proposed_card_name = $('search-input').value;
        if (!$('suggestion-area').visible())
        {
            request_card_proxy(proposed_card_name);
        }
    });

    /**
     * Normally the save button is not a submit button,
     * but whence it is clicked it becomes just that.
     *
     * This prevents return from causing decks to be
     * saved.
     */
    $('save-button').observe('click', function(event) {
        $('save-button').type = "submit";
    });

    /*
     * Move the cursor to the input box.
     */
    $('search-input').activate();
});

