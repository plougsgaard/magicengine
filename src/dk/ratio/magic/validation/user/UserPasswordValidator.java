package dk.ratio.magic.validation.user;

import org.springframework.validation.Errors;
import org.apache.commons.lang.StringUtils;
import dk.ratio.magic.domain.web.user.PasswordChange;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.services.user.UserManager;

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