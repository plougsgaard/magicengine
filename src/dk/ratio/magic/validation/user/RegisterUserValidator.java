package dk.ratio.magic.validation.user;

import org.springframework.validation.Errors;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.services.web.UserManager;

public class RegisterUserValidator
{
    public void validate(UserManager userManager, UserDao userDao, User user, Errors errors) {
        /*
         * Validate against the general user validator first.
         */
        new UserValidator().validate(userManager, userDao, user, errors);

        /*
         * We don't want the same email address to be registered more than once
         * because it's a key in the database. Also it's inappropriate on
         * many other levels.
         */
        if (userDao.getUser(user.getEmail()) != null) {
            errors.rejectValue("email", "exists", "that email already exists");
        }
    }
}
