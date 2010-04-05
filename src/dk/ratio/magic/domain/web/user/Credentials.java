package dk.ratio.magic.domain.user;

/**
 * Used for form binding.
 *
 * The 'remember' boolean is used to decide whether a session
 * should be remembered (i.e. user will not be logged out when
 * its session ends).
 *
 * Note: the default 'remember' value is set to false, as it is
 * the semantics - if it is not checked it will not be present
 * when the binding takes place.
 */
public class Credentials
{
    private String email;
    private String password;
    private boolean remember = false;

    public Credentials()
    {
    }

    public Credentials(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean isRemember()
    {
        return remember;
    }

    public void setRemember(boolean remember)
    {
        this.remember = remember;
    }

    public String toString()
    {
        return "Credentials{" +
               "email='" + email + '\'' +
               ", password='******'" +
               ", remember=" + remember +
               '}';
    }
}
