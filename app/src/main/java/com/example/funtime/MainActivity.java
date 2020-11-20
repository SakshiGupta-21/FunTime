package com.example.funtime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    String currentUrl= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("myactivity", "loadmemecalling");

        loadMeme();

    }

         private void loadMeme(){
             final ProgressBar progressBar= (ProgressBar)findViewById(R.id.progress_bar);
             progressBar.setVisibility(View.VISIBLE);

             Log.d("myactivity", "loadmemecalled");
            String url = "https://meme-api.herokuapp.com/gimme";
             Log.d("myac", url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        ImageView imageView;

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("myactivity", "gotresponse");
                            try {
                                currentUrl = response.getString("url");
                                Log.d("myactivity",currentUrl);
                                imageView = (ImageView) findViewById(R.id.imageView);
                                Glide.with(MainActivity.this).load(currentUrl).
                                        listener(new RequestListener<Drawable>(){

                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                               progressBar.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                progressBar.setVisibility(View.GONE);
                                                return false;
                                            }
                                        }).into(imageView);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("myactivity","failed");
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            Log.e("myactivity","failed");
                            //VolleyLog.d(TAG, "Error: " + error.getMessage());



                        }
                    });
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
             // Access the RequestQueue through your singleton class.
             //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        }




        public void nextmeme (View view){
        loadMeme();
        }

    public void sharememe(View view) {
        Intent intent= new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Hi,See this funny meme "+currentUrl);
        Intent chooser = Intent.createChooser(intent, "Share this meme using..");
        startActivity(chooser);


    }
}