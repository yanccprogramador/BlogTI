package br.com.yanccprogramador.blogti.Retrofit;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by yanccprogramador on 27/06/2017.
 */

public interface ArticleService {
    @POST("users/new")
    Call<JSONObject> createArticle(@Body JSONObject article);
}
