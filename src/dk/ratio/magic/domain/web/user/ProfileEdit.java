package dk.ratio.magic.domain.web.user;

import dk.ratio.magic.domain.db.user.User;

public class ProfileEdit
{
    private int id;
    private String name;
    private String email;

    public ProfileEdit()
    {
    }

    public ProfileEdit(User user)
    {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Override
    public String toString()
    {
        return "ProfileEdit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
