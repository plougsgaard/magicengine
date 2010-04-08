package dk.ratio.magic.services.deck.chart;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.deck.Deck;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;

public class DeckStatistics {

    private final Log logger = LogFactory.getLog(getClass());

    private ArrayList<Pair<Integer, Integer>> spellCMCs; // CMC, number of non-creature spells of this CMC
    private ArrayList<Pair<Integer, Integer>> creatureCMCs; // CMC, number of creature spells of this CMC
    private ArrayList<Pair<Integer, Integer>> coalescedCMCs; // CMC, number of spells of this CMC
    private ArrayList<Pair<Colours, Integer>> colourHeavyCards; // Colour, number of cards of this colour that require more than one mana of this colour

    public DeckStatistics(Deck deck) {
        initializeStatLists();
        populateStatLists(deck);
    }

    private void initializeStatLists() {
        initializeCMCLists();
        initializeColourHeavyCardsList();
    }

    private void initializeCMCLists() {
        spellCMCs = new ArrayList<Pair<Integer, Integer>>();
        creatureCMCs = new ArrayList<Pair<Integer, Integer>>();
        coalescedCMCs = new ArrayList<Pair<Integer, Integer>>();

        int cmc = 0;
        while (cmc <= 7) {
            spellCMCs.add(new Pair<Integer, Integer>(cmc, 0));
            creatureCMCs.add(new Pair<Integer, Integer>(cmc, 0));
            coalescedCMCs.add(new Pair<Integer, Integer>(cmc, 0));
            cmc++;
        }
    }

    private void initializeColourHeavyCardsList() {
        colourHeavyCards = new ArrayList<Pair<Colours, Integer>>();

        for (Colours colour : Colours.values()) {
            colourHeavyCards.add(new Pair<Colours, Integer>(colour, 0));
        }
    }

    private void populateStatLists(Deck deck) {
        for (Card card : deck.getCards()) {
            if (isCreature(card)) {
                Pair<Integer, Integer> cmcPair = getCMCPair(creatureCMCs, card);
                cmcPair.setSecond(cmcPair.getSecond() + card.getCount());
            } else if (isOtherSpell(card)) {
                Pair<Integer, Integer> cmcPair = getCMCPair(spellCMCs, card);
                cmcPair.setSecond(cmcPair.getSecond() + card.getCount());
            } else if (isLand(card)) {
                // Do nothing - here for clarity.
            }

            // Update coalesced list if the card is not a land (we don't want to count lands towards CMC 0):
            if (isLand(card)) {
                // Do nothing
            } else {
                Pair<Integer, Integer> cmcPair = getCMCPair(coalescedCMCs, card);
                cmcPair.setSecond(cmcPair.getSecond() + card.getCount());
            }

            updateColourHeavyList(card);
        }
    }

    private Pair<Integer, Integer> getCMCPair(ArrayList<Pair<Integer, Integer>> list, Card card) {
        Pair<Integer, Integer> result = null;
        if (getCMC(card) >= 7) {
            result = list.get(7);
        } else {
            result = list.get(getCMC(card));
        }

        return result;
    }

    private void updateColourHeavyList(Card card) {
        if (card.getManaCost().contains("B,B")) {
            Pair<Colours, Integer> pair = getColourHeavyPair(Colours.BLACK);
            pair.setSecond(pair.getSecond() + 1);
        }
        if (card.getManaCost().contains("U,U")) {
            Pair<Colours, Integer> pair = getColourHeavyPair(Colours.BLUE);
            pair.setSecond(pair.getSecond() + 1);
        }
        if (card.getManaCost().contains("G,G")) {
            Pair<Colours, Integer> pair = getColourHeavyPair(Colours.GREEN);
            pair.setSecond(pair.getSecond() + 1);
        }
        if (card.getManaCost().contains("R,R")) {
            Pair<Colours, Integer> pair = getColourHeavyPair(Colours.RED);
            pair.setSecond(pair.getSecond() + 1);
        }
        if (card.getManaCost().contains("W,W")) {
            Pair<Colours, Integer> pair = getColourHeavyPair(Colours.WHITE);
            pair.setSecond(pair.getSecond() + 1);
        }
    }

    private Pair<Colours, Integer> getColourHeavyPair(Colours colour) {
        for (Pair<Colours, Integer> pair : colourHeavyCards) {
            if (pair.getFirst() == colour) {
                return pair;
            }
        }

        //Should never happen:
        return null;
    }

    private int getCMC(Card card) {
        return Integer.parseInt(card.getConvertedManaCost());
    }

    private boolean isCreature(Card card) {
        return card.getTypes().contains("Creature");
    }

    private boolean isOtherSpell(Card card) {
        return card.getTypes().contains("Instant")
                || card.getTypes().contains("Sorcery")
                || card.getTypes().contains("Enchantment")
                || card.getTypes().contains("Artifact");
    }

    private boolean isLand(Card card) {
        return card.getTypes().contains("Land");
    }

    public ArrayList<Pair<Integer, Integer>> getSpellCMCs() {
        return spellCMCs;
    }

    public ArrayList<Pair<Integer, Integer>> getCreatureCMCs() {
        return creatureCMCs;
    }

    public ArrayList<Pair<Integer, Integer>> getCoalescedCMCs() {
        return coalescedCMCs;
    }

    public ArrayList<Pair<Colours, Integer>> getColourHeavyCards() {
        return colourHeavyCards;
    }
}
