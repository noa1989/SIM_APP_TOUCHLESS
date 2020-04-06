package cl.genesys.appchofer.layout;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cl.genesys.appchofer.R;
import cl.genesys.appchofer.utilities.Utility;

public class SINC_API extends AppCompatActivity {

    //public TextView TV_retorno;
    public Button bt_genera, bt_consulta, bt_status;
    public Context context;
    public String x = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinc_api);

        context = this;

        //TV_retorno.findViewById(R.id.TV_retorno);
        bt_genera = findViewById(R.id.bt_genera);
        bt_consulta = findViewById(R.id.bt_consulta);
        bt_status =findViewById(R.id.bt_status);

        bt_status.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                status();
            }
        });
        
        bt_genera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getJsonResponsePost();
                //obtenerToken();
            }
        });

        bt_consulta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getJsonCuestomers();
            }
        });

    }

    private void status() {
        StringRequest request = new StringRequest(Request.Method.GET, "http://desarrollocmpc.genesys.cl:112/webapisegura/api/login/echouser", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response!= null) {
                    Utility.ShowMessage(context, response);
                } else {

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                return params;
            }

            //Pass Your Parameters here
           /* @Override
            protected Map<String, String> getParams() {
               *//* Map<String, String> params = new HashMap<String, String>();
                params.put("Username", "Admin");
                params.put("Password", "123456");
                return params;*//*
            }*/
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    /*
    public void llenarInfo() {
        StringRequest request = new StringRequest(Request.Method.POST, "http://desarrollocmpc.genesys.cl:112/webapisegura/api/customers", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    Utility.ShowMessage(context, "llegur");
                } else {

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("token", x); // Genera token
                return params;
            }

            //Pass Your Parameters here
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Username", "Admin");
                params.put("Password", "123456");
                params.put("Authorization: Bearer", "123456");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    */

    /*
    public void obtenerToken() {
        JSONObject json = new JSONObject();
        try {
            json.put("Username","admin");
            json.put("Password","123456");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(Request.Method.POST, "http://desarrollocmpc.genesys.cl:112/webapisegura/api/login/authenticate", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    //Utility.ShowMessage(context, response.toString());
                } else {

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                String credentials = "admin" + ":" + "123456";
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                //params.put("token", ""); // Genera token
                return headers;
            }

            //Pass Your Parameters here
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Username", "admin");
//                params.put("Password", "123456");
//                return params;
//            }

//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Username", "admin");
//                params.put("Password", "123456");
//
//                return super.getBody();
//            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

     */

    public void  getJsonResponsePost(){

        JSONObject json = new JSONObject();
        try {
            json.put("Username","admin");
            json.put("Password","123456");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url ="http://desarrollocmpc.genesys.cl:112/webapisegura/api/login/authenticate";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Gson gson = new GsonBuilder().serializeNulls().create();
                String json = gson.toJson(response);
                cl.genesys.appchofer.entities.Response r = new cl.genesys.appchofer.entities.Response();

              /*  JSONArray jsonArray = null;
                JSONObject jsonConfig = jsonArray.getJSONObject(i);
                r.setToken(response.isNull("cancha") ? null : response.getInt("cancha"));
                        //JSONObject jsonConfig = jsonArray.getJSONObject(i);

                new Gson().fromJson(response, DataWrapper.class);

                r.Token = response.getString("");
                Utility.ShowMessage(context, "String Response : "+ json);*/
                x = cl.genesys.appchofer.entities.Response.fromJson(response.toString()).Token;
                Utility.ShowMessage(context, "String Response : "+ x);
            }

            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.ShowMessage(context, "String Response : "+ error.getMessage());
            }
        });
        //jsonObjectRequest.setTag(REQ_TAG);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(jsonObjectRequest);
        //requestQueue.add(jsonObjectRequest);
    }


    public void  getJsonCuestomers(){

        JSONObject json = new JSONObject();
        try {
            json.put("Content-Type", "application/json");
                    json.put("Authorization: Bearer",x);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url ="http://desarrollocmpc.genesys.cl:112/webapisegura/api/customers";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {



                Utility.ShowMessage(context, "String Response : "+ response.toString());
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.ShowMessage(context, "String Response : "+ error.getMessage());
            }
        }){

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {

            HashMap<String, String> headers = new HashMap<String, String> ();
            //Map<String,String> headers = Constants.getHeaders(context);
            // add headers <key,value>
            //String credentials = USERNAME+":"+PASSWORD;
            String auth = "Bearer "   + x;
            headers.put("Authorization", auth);
            return headers;
        }};
        /*@Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String> ();
            TokenService tokenservice = new TokenService(ctx);
            String accesstoken = tokenservice.getToken(ApiHelper.ACCESS_TOKEN_SHARED_PREF);
            headers.put("Authorization", "Bearer " + accesstoken);

            return headers;
        }*/

       /* {*/

          /*  @Override
        protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization: Bearer", x);
                return params;
            }*/

        /*}*/


        //jsonObjectRequest.setTag(REQ_TAG);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(jsonObjectRequest);
        //requestQueue.add(jsonObjectRequest);
    }
}