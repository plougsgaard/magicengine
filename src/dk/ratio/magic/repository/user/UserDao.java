package dk.ratio.magic.repository.user;

import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.domain.web.user.PasswordChange;
import dk.ratio.magic.domain.web.user.ProfileEdit;
import dk.ratio.magic.util.repository.Page;

import java.sql.SQLException;

public interface UserDao
{
    public User create(User user);
    public User get(int id);
    public User get(String email);
    public void update(ProfileEdit profileEdit);

    public Page<User> getUserPage(Integer pageNumber);

    public String SHA1(String password);
}
