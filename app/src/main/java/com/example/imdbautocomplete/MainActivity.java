package com.example.imdbautocomplete;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String TAG = "IMDB APP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mainTextView = findViewById(R.id.tv_main_text_view);
        EditText mainEditText = findViewById(R.id.edit_text);
        Button makeRequestButton = findViewById(R.id.btn_main_make_request);

        makeRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = createURL(mainEditText.getText().toString());
                getResponse(url, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) throws JSONException {
                        JSONObject jsonResponse =  result;
                        String topResultString = jsonResponse.getJSONArray("d").getJSONObject(0).getString("l");
                        mainTextView.setText(topResultString);
                    }
                });
            }
        });



    }

    public void getResponse(String url, final VolleyCallback callback){

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // do error stuff here
                    }
                }) {
            @Override
            public Map<String, String> getHeaders(){
                HashMap<String, String> customHeaders = new HashMap<>();
                customHeaders.put("x-rapidapi-key", "");
                customHeaders.put("x-rapidapi-host", "imdb8.p.rapidapi.com");
                return customHeaders;
            }
        };
        queue.add(request);
    }




    public String createURL(String searchTerm){
        String newSearchTerm = searchTerm.replace(" ", "%20");
        String firstPart = "https://imdb8.p.rapidapi.com/auto-complete?q=";
        return firstPart + newSearchTerm;
    }










}