package dk.ratio.magic.validation.user;

import org.springframework.validation.Errors;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.services.web.UserManager;

/**
 * A general user validator. Is never called directly. Rather it is called
 * as via its delegates, which would be the validator for user registration
 * or user details editing.
 *
 * Notice: We use the else-if if there are several checks on the same field to
 * avoid flooding the unsuspecting user with millions of errors. Seems scary.
 */
public class UserValidator
{
    public void validate(UserManager userManager, UserDao userDao, User user, Errors errors) {
        if (StringUtils.isBlank(user.getName())) {
            errors.rejectValue("name", "required", "required");
        }

        if (StringUtils.isBlank(user.getEmail())) {
            errors.rejectValue("email", "required", "required");
        }
        /*
         * Besides the blank-check we also perform a simple email
         * validity check.
         */
        else if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            errors.rejectValue("email", "invalid", "not a valid email");
        }

        if (StringUtils.isBlank(user.getPassword())) {
            errors.rejectValue("password", "required", "required");
        }
    }
}