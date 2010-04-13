package dk.ratio.magic.services.card.crawler;

import dk.ratio.magic.domain.db.card.Card;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
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

    private String getHtml(String url)
    {
        try {
            URL u = new URL(url);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader((InputStream) u.getContent(), "UTF-8"));

            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            reader.close();

            // eat all unnecessary whitespace
            return sb.toString().replaceAll("\\s+", " ");
        } catch (IOException e) {
            logger.warn("Failed to read HTML from request.", e);
        }
        return null;
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

        String html = getHtml(PATH + URLEncoder.encode(cardName, "latin1").replaceAll("%", "%u00"));

        /*
        * This is the matcher name we will be using to match the different
        * attributes of magic cards henceforth.
        */
        Matcher matcher;
        card = new Card();

        /*
         * Find expansion & set code
         */
        matcher = getCardAttributeMatcher("Expansion:", html);
        if (matcher.find()) {
            String expansionHtml = matcher.group(1);

            Pattern setPattern = Pattern.compile("set=(\\w+?)&");
            matcher = setPattern.matcher(expansionHtml);

            /*
             * http://gatherer.wizards.com/Pages/Card/
             *
             * <a href="Details.aspx?multiverseid=207935">
             * <img title="Premium Deck Series: Slivers (Land)"
             * src="../../Handlers/Image.ashx?type=symbol&amp;
             * set=H09&amp;size=small&amp;rarity=L"
             * alt="Premium Deck Series: Slivers (Land)"
             * style="border-width: 0px;" align="absmiddle"></a>
             */

            matcher.find();
            String setCode = matcher.group(1);

            if (MagicSet.isMagicSet(setCode)) {
                /*
                 * If the expansion is a proper magic set we are done.
                 */
                card.setExpansion(trimHtmlTags(expansionHtml));
                card.setSetCode(MagicSet.mostRecent(setCode).getCode());
            } else {
                /*
                 * Figure out if this card exists in an expansion which
                 * is a proper magic set.
                 *
                 * If it's not, we just parse the card normally.
                 */
                matcher = getCardAttributeMatcher("Other Sets:", html);

                if (matcher.find()) {
                    String setsHtml = matcher.group(1);
                    StringBuilder sb = new StringBuilder();

                    matcher = setPattern.matcher(setsHtml);
                    while (matcher.find()) {
                        sb.append(matcher.group(1));
                        sb.append(",");
                    }

                    MagicSet set = MagicSet.mostRecent(sb.toString());

                    Pattern p = Pattern.compile("<a href=\"(.+?)\">(.+?)</a>");
                    matcher = p.matcher(setsHtml);

                    while (matcher.find()) {
                        if (matcher.group(2).contains("set=" + set.getGathererCode())) {

                            html = getHtml("http://gatherer.wizards.com/Pages/Card/" + matcher.group(1));

                            logger.info("Found correct expansion for card and loaded corresponding site." +
                                        "[set: " + set + "] " +
                                        "");

                            card.setExpansion(set.getTitle());
                            card.setSetCode(set.getCode());

                            break;
                        }
                    }
                } else {
                    /*
                     * This card only exists as a non-proper card. So
                     * we just add it.
                     *
                     * Empty block (purposely).
                     */
                    logger.info("Card exists only as a non-proper card. Has no other expansions.");
                }
            }
        }


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
            StringBuilder sb = new StringBuilder();
            while (matcher.find()) {
                sb.append(matcher.group(1) + ",");
            }
            card.setManaCost(sb.toString());
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

    public enum MagicSet
    {
        al("_1e", "Limited Edition Alpha"),
        be("_2e", "Limited Edition Beta"),
        un("_2u", "Unlimited Edition"),
        an("an", "Arabian Nights"),
        aq("aq", "Antiquities"),
        rv("_3e", "Revised Edition"),
        lg("le", "Legends"),
        dk("dk", "The Dark"),
        fe("fe", "Fallen Empires"),
        _4e("_4e", "Fourth Edition"),
        hl("hm", "Homelands"),
        ia("ia", "Ice Age"),
        ai("al", "Alliances"),
        mr("mi", "Mirage"),
        vi("vi", "Visions"),
        _5e("_5e", "Fifth Edition"),
        wl("wl", "Weatherlight"),
        tp("te", "Tempest"),
        sh("st", "Stronghold"),
        ex("ex", "Exodus"),
        us("uz", "Urza's Saga"),
        ul("ug", "Urza's Legacy"),
        _6e("_6e", "Classic Sixth Edition"),
        ud("cg", "Urza's Destiny"),
        mm("mm", "Mercadian Masques"),
        ne("ne", "Nemesis"),
        pr("pr", "Prophecy"),
        in("in", "Invasion"),
        ps("ps", "Planeshift"),
        _7e("_7e", "Seventh Edition"),
        ap("ap", "Apocalypse"),
        od("od", "Odyssey"),
        tr("tor", "Torment"),
        ju("jud", "Judgment"),
        on("ons", "Onslaught"),
        le("lgn", "Legions"),
        sc("scg", "Scourge"),
        _8e("_8ed", "Eighth Edition"),
        mi("mrd", "Mirrodin"),
        ds("dst", "Darksteel"),
        _5dn("_5dn", "Fifth Dawn"),
        chk("chk", "Champions of Kamigawa"),
        bok("bok", "Betrayers of Kamigawa"),
        sok("sok", "Saviors of Kamigawa"),
        _9e("_9ed", "Ninth Edition"),
        rav("rav", "Ravnica: City of Guilds"),
        gp("gpt", "Guildpact"),
        di("dis", "Dissension"),
        cs("csp", "Coldsnap"),
        ts("tsp", "Time Spiral"),
        tsts("tsb", "Time Spiral \"Timeshifted\""),
        pc("plc", "Planar Chaos"),
        fut("fut", "Future Sight"),
        _10e("_10e", "Tenth Edition"),
        lw("lrw", "Lorwyn"),
        mt("mor", "Morningtide"),
        shm("shm", "Shadowmoor"),
        eve("eve", "Eventide"),
        ala("ala", "Shards of Alara"),
        cfx("con", "Conflux"),
        arb("arb", "Alara Reborn"),
        m10("m10", "Magic 2010"),
        zen("zen", "Zendikar"),
        wwk("wwk", "Worldwake"),
        roe("roe", "Rise of the Eldrazi"),
        m11("m11", "Magic 2011");

        private String gathererCode;
        private String title;

        MagicSet(String gathererCode, String title)
        {
            this.gathererCode = gathererCode;
            this.title = title;
        }

        public String getCode() {
            return name().replace("_", "").toLowerCase();
        }

        public String getGathererCode()
        {
            return gathererCode.replace("_", "").toUpperCase();
        }

        public String getTitle() {
            return title;
        }

        public static MagicSet mostRecent(String sets) {
            for (int i = MagicSet.values().length-1; i >= 0; --i) {
                MagicSet set = MagicSet.values()[i];
                if (sets.contains(set.getGathererCode())) {
                    return set;
                }
            }
            return null;
        }

        public String translate(String gathererCode) {
            return MagicSet.valueOf(gathererCode).getCode();
        }

        public static boolean isMagicSet(String set) {
            return mostRecent(set) != null;
        }
    }
}