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

public class UserPasswordValidator
{
    public void validate(UserManager userManager, UserDao userDao, PasswordChange passwordChange, Errors errors) {
        /*
         * This is an already-created user, so we check if this action is allowed.
         */
        if (!userManager.isAuthentic(passwordChange.getId(), passwordChange.getPassword())) {
            errors.rejectValue("password", "invalid", "invalid password");
        }

        /*
         * Sanity-check the new password
         */
        if (StringUtils.isBlank(passwordChange.getNewPassword())) {
            errors.rejectValue("newPassword", "required", "required");
        }
    }
}