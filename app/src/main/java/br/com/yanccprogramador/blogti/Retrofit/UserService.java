package br.com.yanccprogramador.blogti.Retrofit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by yanccprogramador on 27/06/2017.
 */

public interface UserService {
    @Headers( "Content-Type: application/json" )
    @POST("usuario/")
    Call<JsonElement> createUser(@Body JsonObject user);
}
