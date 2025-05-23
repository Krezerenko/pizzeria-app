package io.github.krezerenko.sem4_pizzeria;

public class UserRequestDto
{
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String password;

    public UserRequestDto(String name, String email, String phoneNumber, String password)
    {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public String getPassword()
    {
        return password;
    }
}
