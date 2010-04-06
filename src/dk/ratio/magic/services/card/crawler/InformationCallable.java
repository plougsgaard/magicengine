package dk.ratio.magic.services.card.crawler;

import dk.ratio.magic.domain.db.card.Card;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Fetches a card from the Gatherer service.
 * <p/>
 * NOTICE: We need to do some rewriting of the URLs for the
 * service to understand it other than normal url
 * enconding. I think it's because of JavaScript or
 * something similar.
 * <p/>
 */
public class InformationCallable implements Callable<Card>
{
    private final Log logger = LogFactory.getLog(getClass());

    private final String PATH = "http://gatherer.wizards.com/Pages/Card/Details.aspx?name=";

    private String cardName;

    public InformationCallable(String cardName)
    {
        this.cardName = cardName;
    }

    public Card call() throws Exception
    {
        return getCard(cardName);
    }

    private Card getCard(String cardName) throws Exception
    {
        Card card;

        /*
         * First we check if it's a multi-card.
         *
         * Example: Crime // Punishment
         */
        if (cardName.contains(" // ")) {
            logger.info("Found Multi-Card. " +
                        "[cardName: " + cardName + "]");
            return getMultiPartCard(cardName);
        }

        /*
        * The beforeRequireLogin-mentioned quirk:
        *
        * Æthermage's Touch -> encode(latin1) -> %C6thermage%27s+Touch
        *
        * The Gatherer expects:                  %u00C6thermage%u0027+Touch
        *
        * So replaceAll("%", "%u00")
        */
        URL url = new URL(PATH + URLEncoder.encode(cardName, "latin1").replaceAll("%", "%u00"));

        InputStream inputStream = (InputStream) url.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        /*
        * Read the entire file into a string and kill unnecessary whitespace.
        */
        String line, html = "";
        while ((line = reader.readLine()) != null) {
            html += line;
        }
        html = html.replaceAll("\\s+", " ");

        /*
        * This is the matcher name we will be using to match the different
        * attributes of magic cards henceforth.
        */
        Matcher matcher;
        card = new Card();

        /*
        * Find name
        */
        matcher = getCardAttributeMatcher("Card Name:", html);
        if (matcher.find()) {
            card.setCardName(trimHtmlTags(matcher.group(1)));
        }

        /*
        * Find mana cost
        */
        matcher = getCardAttributeMatcher("Mana Cost:", html);
        if (matcher.find()) {
            String rawManaCost = matcher.group(1);
            matcher = Pattern.compile("&amp;name=(.+?)&amp;t").matcher(rawManaCost);
            String manaCost = "";
            while (matcher.find()) {
                manaCost += matcher.group(1) + ",";
            }
            card.setManaCost(manaCost);
        } else {
            // Mana cost is allowed to be nothing (example: land)
            card.setManaCost("");
        }

        /*
        * Find converted mana cost
        */
        matcher = getCardAttributeMatcher("Converted Mana Cost:", html);
        if (matcher.find()) {
            card.setConvertedManaCost(trimHtmlTags(matcher.group(1)));
        } else {
            // Converted mana cost is allowed to be nothing (example: land)
            card.setConvertedManaCost("");
        }

        /*
        * Find types
        */
        matcher = getCardAttributeMatcher("Types:", html);
        if (matcher.find()) {
            card.setTypes(trimHtmlTags(matcher.group(1)));
        }

        /*
        * Find card text
        */
        String cardText = getCardValue("Card Text:", html);

        if (cardText == null) {
            card.setCardText("");
        } else {
            // Handle paragraphs (linebreaks)
            cardText = cardText.replaceAll("<div class=\"cardtextbox\">", "<p>");
            cardText = cardText.replaceAll("</div>", "</p>");

            // Handle meaning-bearing symbols (such as a cost or tap)
            // e.g. <img ... tap ../> becomes {tap}
            String pattern = "(.*)<img.*?name=(\\w+?)&.*?>(.*)";
            matcher = Pattern.compile(pattern).matcher(cardText);

            while (matcher.find()) {
                cardText = matcher.group(1) + "{" + matcher.group(2) + "}" + matcher.group(3);
                matcher = Pattern.compile(pattern).matcher(cardText);
            }

            card.setCardText(cardText.trim());
        }

        /*
         * Find expansion
         */
        matcher = getCardAttributeMatcher("Expansion:", html);
        if (matcher.find()) {
            card.setExpansion(trimHtmlTags(matcher.group(1)));
        }

        /*
        * Find rarity
        */
        matcher = getCardAttributeMatcher("Rarity:", html);
        if (matcher.find()) {
            card.setRarity(trimHtmlTags(matcher.group(1)));
        }

        /*
        * Find card number
        */
        matcher = getCardAttributeMatcher("Card #:", html);
        if (matcher.find()) {
            card.setCardNumber(trimHtmlTags(matcher.group(1)));
        }

        /*
        * Find artist
        */
        matcher = getCardAttributeMatcher("Artist:", html);
        if (matcher.find()) {
            card.setArtist(trimHtmlTags(matcher.group(1)));
        }

        return card;
    }

    private Matcher getCardAttributeMatcher(String needle, String haystack)
    {
        String pattern = ">\\s*" + needle + "</div>\\s*<div.*?class=\"value\">(.*?)</div>";
        return Pattern.compile(pattern).matcher(haystack);
    }

    /**
     * Trim all HTML tags, but the paragraph (<p/>) tag.
     *
     * @param input text string to strip
     *
     * @return stripped string
     */
    private String trimHtmlTags(String input)
    {
        return input.replaceAll("<.+?>", "").trim();
    }

    /**
     * Finds the contents of the value div. This is quirkier than perhaps
     * expected because we need to balance out the internal divs to find
     * the ending div.
     *
     * The arity of the expression is the number of start div tags minus
     * the number of end div tags seen. When this is 0 the first tag is
     * balanced out.
     *
     * @param needle    the card property to search for (typically `Card Text:`)
     * @param haystack  the string to search in
     *
     * @return          the entire contents of the value div tag
     */
    private String getCardValue(String needle, String haystack)
    {
        String pattern = ">\\s*" + needle + "</div>\\s*<div.*?class=\"value\">(.*)";
        Matcher matcher = Pattern.compile(pattern).matcher(haystack);

        if (matcher.find()) {
            String content = matcher.group(1);
            int arity = 1;
            matcher = Pattern.compile(".*?(<div|</div>)").matcher(content);
            while (matcher.find()) {
                if ("<div".equals(matcher.group(1))) {
                    arity++;
                }
                else if ("</div>".equals(matcher.group(1))) {
                    arity--;
                }
                else {
                    logger.error("Could not figure out how to write a simple regex.");
                    return null;
                }
                if (arity == 0) {
                    return content.substring(0, matcher.start());
                }
            }
        }
        else {
            logger.error("Did not find anything.");
        }

        return null;
    }

    private Card getMultiPartCard(String cardName) throws Exception
    {
        Card compositeCard = new Card();

        Card cardFirst = getCard(cardName.split(" // ")[0]);
        Card cardSecond = getCard(cardName.split(" // ")[1]);

        compositeCard.setCardName(cardFirst.getCardName() + " // " + cardSecond.getCardName());
        compositeCard.setCardNumber(cardFirst.getCardNumber()); // the same for both
        compositeCard.setCardText(
                "<p>" + cardFirst.getCardName() + ":</p>" +
                cardFirst.getCardText() +
                "<p>" + cardSecond.getCardName() + ":</p>" +
                cardSecond.getCardText());
        compositeCard.setArtist(cardFirst.getArtist());
        compositeCard.setConvertedManaCost(cardFirst.getConvertedManaCost() + "," +
                                           cardSecond.getConvertedManaCost());
        compositeCard.setManaCost(cardFirst.getManaCost() + cardSecond.getManaCost());
        compositeCard.setExpansion(cardFirst.getExpansion());
        compositeCard.setRarity(cardFirst.getRarity());
        compositeCard.setTypes(cardFirst.getTypes());

        return compositeCard;
    }
}