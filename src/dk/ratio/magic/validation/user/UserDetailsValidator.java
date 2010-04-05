package dk.ratio.magic.validation.user;

import org.springframework.validation.Errors;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.services.user.UserManager;

public class UserDetailsValidator
{
    public void validate(UserManager userManager, UserDao userDao, User user, Errors errors) {
        /*
         * Validate against the general user validator first.
         */
        new UserValidator().validate(userManager, userDao, user, errors);

        /*
         * This is an already-created user, so we check if this action is allowed.
         */
        if (!userManager.isAuthentic(user.getId(), user.getPassword())) {
            errors.rejectValue("password", "invalid", "invalid password");
        }
    }
}