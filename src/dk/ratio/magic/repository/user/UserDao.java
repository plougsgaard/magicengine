package dk.ratio.magic.repository.user;

import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.domain.web.user.PasswordChange;
import dk.ratio.magic.util.repository.Page;

import java.sql.SQLException;

public interface UserDao
{
    public User getUser(int id);
    public User getUser(String email);
    public Page<User> getUserPage(Integer pageNumber);

    public User addUser(User user);
    public void saveUser(User user);
    public void changePassword(User user);

    public String SHA1(String password);
}
