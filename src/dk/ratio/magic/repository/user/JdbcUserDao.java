package dk.ratio.magic.repository.user;

import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.domain.web.user.PasswordChange;
import dk.ratio.magic.domain.web.user.ProfileEdit;
import dk.ratio.magic.util.repository.Page;
import dk.ratio.magic.util.repository.Pagination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class JdbcUserDao implements UserDao
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private SimpleJdbcTemplate simpleJdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public User get(int id)
    {
        List<User> results = simpleJdbcTemplate.query(
                "SELECT id, email, password, password_salt, name FROM users " +
                "WHERE id = :id",
                new UserMapper(),
                new MapSqlParameterSource()
                    .addValue("id", id)
        );
        if (results.size() != 1)
        {
            logger.warn("Finding user failed. " +
                         "No user exists with id: " + id);
            return null;
        }
        return results.get(0);
    }

    public User get(String email)
    {
        List<User> results = simpleJdbcTemplate.query(
                "SELECT id, email, password, password_salt, name FROM users " +
                "WHERE email = :email",
                new UserMapper(),
                new MapSqlParameterSource()
                    .addValue("email", email)
        );
        if (results.size() != 1)
        {
            logger.warn("Finding user failed. " +
                         "No user exists with email: " + email);
            return null;
        }
        return results.get(0);
    }

    public Page<User> getUserPage(Integer pageNumber)
    {
        String selectClause =
                "SELECT id, email, password, password_salt, name ";
        String fromWhereClause =
                "FROM users ";
        return new Pagination<User>().fetchPage(
                pageNumber, simpleJdbcTemplate, selectClause, fromWhereClause,
                new MapSqlParameterSource(),
                new UserMapper()
        );
    }

    public User create(User user)
    {
        /*
         * Generate password salt first. It might be set, and then again
         * it might not. We don't really care.
         */
        String passwordSalt = String.valueOf(
                System.currentTimeMillis() * new Random().nextGaussian()
        );
        user.setPasswordSalt(passwordSalt);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int count = namedParameterJdbcTemplate.update(
            "INSERT INTO users (email, password, password_salt, name) " +
            "VALUES (:email, sha1(:password), :password_salt, :name)",
            new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword() + user.getPasswordSalt())
                .addValue("password_salt", user.getPasswordSalt())
                .addValue("name", user.getName()),
                keyHolder
        );
        if (keyHolder.getKeyList().size() == 0)
        {
            logger.error("Adding user " + user.getEmail() + " failed. " +
                         "Did not get any id back from the database.");
        }
        else
        {
            user.setId(keyHolder.getKey().intValue());
            logger.info("Added User: " + user + ". Affected rows: " + count);
        }
        return user;
    }

    public void update(ProfileEdit profileEdit)
    {
        int count = namedParameterJdbcTemplate.update(
                "UPDATE users SET name = :name, email = :email " +
                "WHERE id = :id",
                new MapSqlParameterSource()
                    .addValue("id", profileEdit.getId())
                    .addValue("name", profileEdit.getName())
                    .addValue("email", profileEdit.getEmail())
        );
        logger.info("Updated user. Updated " + count + " rows in `users`.");
    }

    public String SHA1(String input)
    {
        return simpleJdbcTemplate.queryForObject("SELECT sha1(?)", String.class, input);
    }

    private static class UserMapper implements RowMapper<User>
    {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            User user = new User(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setPasswordSalt(rs.getString("password_salt"));
            user.setName(rs.getString("name"));
            return user;
        }
    }
}
