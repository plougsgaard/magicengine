package dk.ratio.magic.validation.user;

import org.springframework.validation.Errors;
import dk.ratio.magic.domain.web.user.Credentials;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.services.user.UserManager;

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