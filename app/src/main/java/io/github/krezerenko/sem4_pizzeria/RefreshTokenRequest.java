package io.github.krezerenko.sem4_pizzeria;

public class RefreshTokenRequest
{
    private final String refreshToken;

    public RefreshTokenRequest(String refreshToken)
    {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken()
    {
        return refreshToken;
    }
}
