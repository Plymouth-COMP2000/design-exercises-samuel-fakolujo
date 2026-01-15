package com.example.restaurantmanagement.data.api;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import com.android.volley.VolleyError;
import java.nio.charset.StandardCharsets;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;


public class UserApiService {

    public interface JsonCallback {
        void onSuccess(JSONObject res);
        void onError(String msg);
    }

    public static void createUser(Context ctx, JSONObject body, JsonCallback cb) {
        String url = ApiConfig.BASE_URL + "create_user/" + ApiConfig.STUDENT_ID;

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST, url, body,
                cb::onSuccess,
                err -> cb.onError(parseVolleyError(err))

        );

        VolleySingleton.getInstance(ctx).addToRequestQueue(req);
    }

    public static void readUser(Context ctx, String username, JsonCallback cb) {
        String url = ApiConfig.BASE_URL + "read_user/" + ApiConfig.STUDENT_ID + "/" + username;

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET, url, null,
                cb::onSuccess,
                err -> cb.onError(parseVolleyError(err))

        );

        VolleySingleton.getInstance(ctx).addToRequestQueue(req);
    }


    // Get all users (returns a JSONObject response)
    public static void readAllUsers(Context ctx, JsonCallback cb) {

        String url = ApiConfig.BASE_URL + "read_all_users/" + ApiConfig.STUDENT_ID;

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                cb::onSuccess,
                err -> cb.onError(parseVolleyError(err))
        );

        // Optional: helps on slow VPN
        req.setRetryPolicy(new DefaultRetryPolicy(
                20000, // 20 seconds timeout
                0,     // no retries
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        VolleySingleton.getInstance(ctx).addToRequestQueue(req);
    }

    // Add a user (equivalent to "add employee")
    public static void addUser(Context ctx, JSONObject userJson, JsonCallback cb) {

        String url = ApiConfig.BASE_URL + "create_user/" + ApiConfig.STUDENT_ID;

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                url,
                userJson,
                cb::onSuccess,
                err -> cb.onError(parseVolleyError(err))
        );

        // Optional: helps avoid TimeoutError on slow VPN
        req.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        VolleySingleton.getInstance(ctx).addToRequestQueue(req);
    }

    // Delete a user (equivalent to "delete employee")
    public static void deleteUser(Context ctx, String username, JsonCallback cb) {

        String url = ApiConfig.BASE_URL + "delete_user/" + ApiConfig.STUDENT_ID + "/" + username;

        StringRequest req = new StringRequest(
                Request.Method.DELETE,
                url,
                response -> {
                    try {
                        // Wrap response string into JSON so we can reuse JsonCallback(JSONObject)
                        JSONObject wrapper = new JSONObject();
                        wrapper.put("message", response);
                        cb.onSuccess(wrapper);
                    } catch (Exception e) {
                        cb.onError("Delete succeeded but parse error: " + e.getMessage());
                    }
                },
                err -> cb.onError(parseVolleyError(err))
        );

        // Optional: helps avoid TimeoutError on slow VPN
        req.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        VolleySingleton.getInstance(ctx).addToRequestQueue(req);
    }


    private static String parseVolleyError(VolleyError err) {
        try {
            if (err.networkResponse != null) {
                int code = err.networkResponse.statusCode;
                String body = "";
                if (err.networkResponse.data != null) {
                    body = new String(err.networkResponse.data, StandardCharsets.UTF_8);
                }
                return "HTTP " + code + (body.isEmpty() ? "" : " | " + body);
            }
            if (err.getMessage() != null) return err.getMessage();
            return err.toString();
        } catch (Exception e) {
            return err.toString();
        }
    }



}
