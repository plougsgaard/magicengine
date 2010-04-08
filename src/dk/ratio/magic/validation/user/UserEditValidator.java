package dk.ratio.magic.validation.user;

import dk.ratio.magic.domain.web.user.ProfileEdit;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.springframework.validation.Errors;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.services.user.UserManager;

public class UserEditValidator
{
    public void validate(UserManager userManager, UserDao userDao, ProfileEdit profileEdit, Errors errors) {
        if (StringUtils.isBlank(profileEdit.getName())) {
            errors.rejectValue("name", "required", "required");
        }

        if (StringUtils.isBlank(profileEdit.getEmail())) {
            errors.rejectValue("email", "required", "required");
        }
        /*
         * Besides the blank-check we also perform a simple email
         * validity check in the else-branch.
         */
        else if (!EmailValidator.getInstance().isValid(profileEdit.getEmail())) {
            errors.rejectValue("email", "invalid", "not a valid email");
        }
    }
}