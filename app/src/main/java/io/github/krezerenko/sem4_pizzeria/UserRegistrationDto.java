package io.github.krezerenko.sem4_pizzeria;

public class UserRegistrationDto
{
    private final String name;
    private final String password;

    public UserRegistrationDto(String username, String password)
    {
        this.name = username;
        this.password = password;
    }

    public String getName()
    {
        return name;
    }

    public String getPassword()
    {
        return password;
    }
}
