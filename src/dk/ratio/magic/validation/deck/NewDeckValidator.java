package dk.ratio.magic.validation.deck;

import org.springframework.validation.Errors;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.repository.deck.DeckDao;

public class NewDeckValidator
{
    public void validate(DeckDao deckDao, Deck deck, Errors errors) {
        if (StringUtils.isBlank(deck.getTitle())) {
            errors.rejectValue("title", "required", "required");
        }
    }
}