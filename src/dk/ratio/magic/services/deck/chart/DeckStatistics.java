package dk.ratio.magic.services.deck.chart;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.deck.Deck;

import java.util.ArrayList;

public class DeckStatistics {
    private ArrayList<Pair<Integer, Integer>> spellCMCs; // CMC, number of cards of this CMC
    private ArrayList<Pair<Integer, Integer>> creatureCMCs; // CMC, number of cards of this CMC
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


        int cmc = 0;
        while (cmc <= 15) {
            spellCMCs.add(new Pair<Integer, Integer>(cmc, 0));
            creatureCMCs.add(new Pair<Integer, Integer>(cmc, 0));
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
                Pair<Integer, Integer> cmcPair = creatureCMCs.get(getCMC(card));
                incrementCardsInPair(cmcPair);
            } else if (isOtherSpell(card)) {
                Pair<Integer, Integer> cmcPair = spellCMCs.get(getCMC(card));
                incrementCardsInPair(cmcPair);
            } else if (isLand(card)) {
                // Do nothing - here for clarity.
            }

            updateColourHeavyList(card);
        }
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

    // TODO: I am not impressed by this - might be it should be removed... 
    private void incrementCardsInPair(Pair<Integer, Integer> cmcPair) {
        int numberWithThisCMC = cmcPair.getSecond() + 1;
        cmcPair.setSecond(numberWithThisCMC);
    }

    private boolean isCreature(Card card) {
        return getLowerCaseCardTypes(card).startsWith("creature")
                || getLowerCaseCardTypes(card).startsWith("legendary creature");
    }

    private boolean isOtherSpell(Card card) {
        return getLowerCaseCardTypes(card).startsWith("instant")
                || getLowerCaseCardTypes(card).startsWith("sorcery")
                || getLowerCaseCardTypes(card).startsWith("enchantment")
                || getLowerCaseCardTypes(card).startsWith("artifact")
                || getLowerCaseCardTypes(card).startsWith("legendary artifact");
    }

    private boolean isLand(Card card) {
        return getLowerCaseCardTypes(card).startsWith("land")
                || getLowerCaseCardTypes(card).startsWith("legendary land");
    }

    private String getLowerCaseCardTypes(Card card) {
        return card.getTypes().toLowerCase();
    }

    public ArrayList<Pair<Integer, Integer>> getSpellCMCs() {
        return spellCMCs;
    }

    public ArrayList<Pair<Integer, Integer>> getCreatureCMCs() {
        return creatureCMCs;
    }

    public ArrayList<Pair<Colours, Integer>> getColourHeavyCards() {
        return colourHeavyCards;
    }
}
