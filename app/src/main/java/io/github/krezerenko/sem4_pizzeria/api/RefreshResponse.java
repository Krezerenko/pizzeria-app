package io.github.krezerenko.sem4_pizzeria.api;

public class RefreshResponse
{
    private final String accessToken;

    public RefreshResponse(String accessToken)
    {
        this.accessToken = accessToken;
    }

    public String getAccessToken()
    {
        return accessToken;
    }
}
