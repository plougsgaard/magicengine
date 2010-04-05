package dk.ratio.magic.domain.db.user;

public class User
{
    private int id;
    private String email;
    private String password;
    private String passwordSalt;
    private String name;

    public User()
    {
    }

    public User(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPasswordSalt()
    {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt)
    {
        this.passwordSalt = passwordSalt;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return "User{" +
               "id=" + id +
               ", email='" + email + '\'' +
               ", password='*************'" +
               ", passwordSalt='" + passwordSalt + '\'' +
               ", name='" + name + '\'' +
               '}';
    }
}