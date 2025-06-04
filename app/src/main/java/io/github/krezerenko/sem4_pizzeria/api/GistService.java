package io.github.krezerenko.sem4_pizzeria.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GistService
{
    String GIST_ID = "a125d51a2841204464a5316e30989acc";
    String HEADER_ACCEPT = "application/vnd.github+json";
    String HEADER_VERSION = "2022-11-28";

    @GET("/gists/" + GIST_ID)
    Call<ResponseBody> getTunnelGist(@Header("Accept") String acceptHeader, @Header("X-GitHub-Api-Version") String headerVersion);
}
