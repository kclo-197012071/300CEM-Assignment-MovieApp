package com.example.a300cem_assignment_movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MovieDetailActivity extends AppCompatActivity {

    private final String APIKEY = "cac49838";

    private String imdbID;
    private RequestQueue queue;
    private StringBuilder outputData = new StringBuilder();

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
        String url = "https://www.omdbapi.com/?i=" + id + "&plot=full&apikey=" + APIKEY;
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
                                mTitleTv.setText(getApplicationContext().getResources().getText(R.string.tv_title) + title);
                                String year = res_jsonObj.getString("Year");
                                mYearTv.setText(getApplicationContext().getResources().getText(R.string.tv_year) + year);
                                String genre = res_jsonObj.getString("Genre");
                                mGenreTv.setText(getApplicationContext().getResources().getText(R.string.tv_genre) + genre);
                                String director = res_jsonObj.getString("Director");
                                mDirectorTv.setText(getApplicationContext().getResources().getText(R.string.tv_director) + director);
                                String actors = res_jsonObj.getString("Actors");
                                mActorsTv.setText(getApplicationContext().getResources().getText(R.string.tv_actors) + actors);
                                String duration = res_jsonObj.getString("Runtime");
                                mDurationTv.setText(getApplicationContext().getResources().getText(R.string.tv_duration) + duration);
                                String imdbRating = res_jsonObj.getString("imdbRating");
                                mImdbRatingTv.setText(getApplicationContext().getResources().getText(R.string.tv_rating) + imdbRating);
                                String plot = res_jsonObj.getString("Plot");
                                mPlotTv.setText(getApplicationContext().getResources().getText(R.string.tv_plot) + plot);

                                String posterURL = res_jsonObj.getString("Poster");
                                if (!posterURL.equals("N/A")) {
                                    Picasso.get().load(posterURL).resize(600, 894).into(mPosterIv);
                                }

                                // Append text for output file
                                outputData.append(mTitleTv.getText().toString() + "\n");
                                outputData.append(mYearTv.getText().toString() + "\n");
                                outputData.append(mGenreTv.getText().toString() + "\n");
                                outputData.append(mDirectorTv.getText().toString() + "\n");
                                outputData.append(mActorsTv.getText().toString() + "\n");
                                outputData.append(mDurationTv.getText().toString() + "\n");
                                outputData.append(mImdbRatingTv.getText().toString() + "\n");
                                outputData.append(mPlotTv.getText().toString() + "\n");
                                outputData.append("Poster: " + posterURL);
                                Log.d("outputData: ", outputData.toString());

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

        // Do somethings after Volley sending the request is finished
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                writeDataToFile(outputData.toString());
            }
        });

    }

    private void writeDataToFile(String data) {
        /**
         * Default output path as /data/data/com.example.a300cem_assignment_movieapp/files
         */

        String filename = "Movie_Detail.txt";
        FileOutputStream outputStream = null;
        BufferedWriter writer = null;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(data);
            Toast.makeText(MovieDetailActivity.this, "Details of the selected movie were stored in local.", Toast.LENGTH_SHORT).show();
            Log.d("writeDataToFile: ", "writeDataToFile successful");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
