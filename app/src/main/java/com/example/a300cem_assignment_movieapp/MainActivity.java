package com.example.a300cem_assignment_movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private EditText mMovieNameEdt;
    private ListView mMovieListLv;

    private RequestQueue queue;
    private String[][] searchedMovieArr = new String[0][0];
    private LayoutInflater inflater;
    private MovieListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieNameEdt = findViewById(R.id.edt_movieName);
        mMovieListLv = (ListView) findViewById(R.id.lv_movieList);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void search(View view) {

        String mName = mMovieNameEdt.getText().toString();
        if ( mName.isEmpty()) {
            mMovieNameEdt.setError("Please enter movie name!");
            return;
        }

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);
        String url = "https://www.omdbapi.com/?s=" + mName + "&plot=full&page=1&apikey=cac49838";
        Log.d("url: ", url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject res_jsonObj = new JSONObject(response);
                            JSONArray res_jsonArrOfSearch = res_jsonObj.getJSONArray("Search");
                            int jsonArrLength = res_jsonArrOfSearch.length();
                            int objNum = 2; // imdbID, title (year)
                            searchedMovieArr = new String[jsonArrLength][objNum];
                            for (int i = 0; i < jsonArrLength; i++) {
                                JSONObject jsonObjOfJsonArrOfSearch = res_jsonArrOfSearch.getJSONObject(i);

                                String title = jsonObjOfJsonArrOfSearch.getString("Title");
                                String year = jsonObjOfJsonArrOfSearch.getString("Year");
                                String imdbID = jsonObjOfJsonArrOfSearch.getString("imdbID");
                                String posterURL = jsonObjOfJsonArrOfSearch.getString("Poster");

                                searchedMovieArr[i][0]= imdbID;//your value
                                searchedMovieArr[i][1]= title + " | " + year;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("searchedMovieArr:", Arrays.deepToString(searchedMovieArr));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        // Do somethings after Volley sending the request is finished
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new MovieListAdapter(searchedMovieArr,inflater);
                        mMovieListLv.setAdapter(adapter);
                        mMovieListLv.setOnItemClickListener(onClickListView);
                    }
                });
                queue.getCache().clear();
            }
        });
    }

    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
            Toast.makeText(MainActivity.this,"Movie Name: "+searchedMovieArr[position][1] + "\nID: " + searchedMovieArr[position][0], Toast.LENGTH_SHORT).show();

            Intent myIntent = new Intent(view.getContext(), MovieDetailActivity.class);
            myIntent.putExtra("imdbID", searchedMovieArr[position][0]);
            startActivityForResult(myIntent, 0);
        }
    };

}
