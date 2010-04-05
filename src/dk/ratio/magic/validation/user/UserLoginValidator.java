package dk.ratio.magic.validation.user;

import org.springframework.validation.Errors;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.domain.db.user.Credentials;
import dk.ratio.magic.domain.db.user.PasswordChange;
import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.repository.deck.DeckDao;
import dk.ratio.magic.services.web.UserManager;

public class UserLoginValidator
{
    public void validate(UserManager userManager, UserDao userDao, Credentials credentials, Errors errors) {
        /*
         * This is an already-created user, so we check if this action is allowed.
         */
        if (!userManager.isAuthentic(credentials)) {
            errors.rejectValue("password", "invalid", "invalid password");
        }
    }
}