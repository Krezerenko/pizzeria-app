package io.github.krezerenko.sem4_pizzeria.api;

public class LoginRequest
{
    private final String name;
    private final String password;

    public LoginRequest(String name, String password)
    {
        this.name = name;
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
