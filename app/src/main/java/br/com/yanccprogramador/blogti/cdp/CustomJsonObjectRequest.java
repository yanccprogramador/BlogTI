package br.com.yanccprogramador.blogti.cdp;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by yanccprogramador on 15/06/2017.
 */

public class CustomJsonObjectRequest extends Request<JSONObject> {
    Response.Listener<JSONObject> response;
    Map<String, String> params;
    public CustomJsonObjectRequest(int method, String url, Map<String, String> params, Response.Listener<JSONObject> response, Response.ErrorListener listener) {
        super(method, url, listener);
        this.params=params;
        this.response=response;
    }
    public CustomJsonObjectRequest(String url, Map<String, String> params, Response.Listener<JSONObject> response, Response.ErrorListener listener) {
        super(Method.GET, url, listener);
        this.params=params;
        this.response=response;
    }
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String js=new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            return (Response.success(new JSONObject(js),HttpHeaderParser.parseCacheHeaders(networkResponse)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
       this.response.onResponse(response);
    }
}
