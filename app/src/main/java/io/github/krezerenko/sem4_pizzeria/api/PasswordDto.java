package io.github.krezerenko.sem4_pizzeria.api;

public class PasswordDto
{
    private final String password;

    public PasswordDto(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }
}
