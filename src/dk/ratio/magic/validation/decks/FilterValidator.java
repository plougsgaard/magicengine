package dk.ratio.magic.validation.decks;

import dk.ratio.magic.domain.web.decks.DeckFilter;
import org.springframework.validation.Errors;

public class FilterValidator
{
    public void validate(DeckFilter deckFilter, Errors errors)
    {
        if (deckFilter.getAuthor() == null) {
            errors.rejectValue("author", "not null", "not null");
        }
        if (deckFilter.getTitle() == null) {
            errors.rejectValue("title", "not null", "not null");
        }
    }
}
