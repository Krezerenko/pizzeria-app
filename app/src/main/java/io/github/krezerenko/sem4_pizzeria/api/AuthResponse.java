package io.github.krezerenko.sem4_pizzeria.api;

public class AuthResponse
{
    private final String accessToken;
    private final String refreshToken;

    public AuthResponse(String accessToken, String refreshToken)
    {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken()
    {
        return refreshToken;
    }

    public String getAccessToken()
    {
        return accessToken;
    }
}
