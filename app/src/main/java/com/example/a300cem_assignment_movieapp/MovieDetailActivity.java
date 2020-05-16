package com.example.a300cem_assignment_movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailActivity extends AppCompatActivity {

    private String imdbID;
    private RequestQueue queue;

    private TextView mTitleTv, mYearTv, mGenreTv, mDirectorTv, mActorsTv,  mDurationTv, mImdbRatingTv, mPlotTv;
    private ImageView mPosterIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitleTv = findViewById(R.id.tv_title);
        mYearTv = findViewById(R.id.tv_year);
        mGenreTv = findViewById(R.id.tv_genre);
        mDirectorTv = findViewById(R.id.tv_director);
        mActorsTv = findViewById(R.id.tv_actors);
        mDurationTv = findViewById(R.id.tv_duration);
        mImdbRatingTv = findViewById(R.id.tv_imdbRating);
        mPlotTv = findViewById(R.id.tv_plot);
        mPosterIv = findViewById(R.id.iv_poster);

        Intent intent = getIntent();
        imdbID = intent.getStringExtra("imdbID");
        Log.d("imdbID: ", imdbID);
        volley_newRequestQueue(imdbID);
    }

    public void volley_newRequestQueue(String id) {
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);
        String url = "https://www.omdbapi.com/?i=" + id + "&plot=full&apikey=cac49838";
        Log.d("url: ", url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("test_response:", response);
                        try {
                            JSONObject res_jsonObj = new JSONObject(response);
                            String resResult = res_jsonObj.getString("Response");

                            if(resResult.equals("True")) {

                                String title = res_jsonObj.getString("Title");
                                mTitleTv.setText("title: " + title);
                                String year = res_jsonObj.getString("Year");
                                mYearTv.setText("Year: " + year);
                                String genre = res_jsonObj.getString("Genre");
                                mGenreTv.setText("Genre: " + genre);
                                String director = res_jsonObj.getString("Director");
                                mDirectorTv.setText("Director: " + director);
                                String actors = res_jsonObj.getString("Actors");
                                mActorsTv.setText("Actors: " + actors);
                                String duration = res_jsonObj.getString("Runtime");
                                mDurationTv.setText("Duration: " + duration);
                                String imdbRating = res_jsonObj.getString("imdbRating");
                                mImdbRatingTv.setText("IMDB Rating: " + imdbRating);
                                String plot = res_jsonObj.getString("Plot");
                                mPlotTv.setText("Plot: " + plot);

                                String posterURL = res_jsonObj.getString("Poster");
                                if (!posterURL.equals("N/A")) {
                                    Picasso.get().load(posterURL).into(mPosterIv);
                                }

                            } else {
                                Log.d("response:", response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MovieDetailActivity.this, "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
