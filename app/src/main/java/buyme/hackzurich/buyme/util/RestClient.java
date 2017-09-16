package buyme.hackzurich.buyme.util;

/**
 * Created by cecibloom on 16/09/2017.
 */

import android.content.Context;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RestClient {

    public static void getImageAttributes(byte[] image, String url, RequestQueue ctx){
        Map<String, String> params = new HashMap<String, String>();
        params.put("url", url);
        params.put("image", "");
        call(Constant.FASHWELL_ATTR, params, ctx);
    }

    public static void getProductsInStores(String url, String image, String shop, int min, int max, int limit, RequestQueue ctx){
        Map<String, String> params = new HashMap<String, String>();
        params.put("url", url);
        params.put("image", image);
        params.put("shop_name", shop);
        if (min > 0) params.put("min_price", String.valueOf(min));
        if (max > 0) params.put("max_price", String.valueOf(max));
        if (limit > 0) params.put("max_products_per_detection", String.valueOf(limit));
        call(Constant.FASHWELL_POSTS, params, ctx);
    }

    public static void getProductBySKU(String sku, RequestQueue ctx){
        Map<String, String> params = new HashMap<String, String>();
        params.put("sku", sku);
        call(Constant.FASHWELL_SKU + sku + "/", params, ctx);
    }

    public static void getSimilarProductsBySKU(String sku, String shop, String min, String max, RequestQueue ctx){
        String query = sku + "/?shop_name=" + shop + "&min_price=" + min + "&max_price=" + max;
        call(Constant.FASHWELL_SKU + query, null, ctx);
    }

    public static void getProductByEAN(String ean, RequestQueue ctx){
        call(Constant.SIROOP_EAN + ean + "/?apikey=" + Constant.SIROOP_TOKEN, null, ctx);
    }

    public static void getProductBySKUinSiroop(String sku, RequestQueue ctx){
        call(Constant.SIROOP_SKU + sku + "?apikey=" + Constant.SIROOP_TOKEN, null, ctx);
    }

    public static void searchInSiroop(String query, String limit, String category, RequestQueue ctx){
        call(Constant.SIROOP_SEARCH + "/?query" + query + "&limit=" + limit + "&category=" + category + "&apikey=" + Constant.SIROOP_TOKEN, null, ctx);
    }

    public static void recommend(String sku,RequestQueue ctx){
        call(Constant.SIROOP_RECOMMEND + "?abstract_sku" + sku + "&locale=de_ch&apikey=" + Constant.SIROOP_TOKEN, null, ctx);
    }

    private static void call(String endpoint, final Map<String,String> parameters, RequestQueue queue){

        //RequestQueue queue = Volley.newRequestQueue(ctx);
        int op = (endpoint == Constant.FASHWELL_ATTR || endpoint == Constant.FASHWELL_POSTS) ? 1 : 0;
        final boolean isFashWell = endpoint.contains("/www.fashwell.com");
        StringRequest sr = new StringRequest(Request.Method.POST,"http://www.fashwell.com/api/hackzurich/v1/posts/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    }},
                new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("ERROR","error => "+error.toString());
                            }
                        })
            {
            @Override
            protected Map<String,String> getParams(){
                return parameters;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                if (isFashWell) {
                    params.put("Authorization",Constant.FASHWELL_TOKEN);
                } else {
                    params.put("Authorization",Constant.SIROOP_TOKEN);
                }
                return params;
            }
        };
        queue.add(sr);
    }

}


