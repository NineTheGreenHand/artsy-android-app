package com.hw9.artsyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Objects;

public class SearchArtistActivity extends AppCompatActivity {

    private ArtistCard[] cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_artist);


        // Get the user input from the search bar:
        Intent userQuery = getIntent();
        String userInput = userQuery.getStringExtra("userInput");


        // Initial toolbar, set the action to go back:
        Toolbar topBar = findViewById(R.id.topBar);
        topBar.setTitle("");
        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });

        // Get TextView object:
        TextView tv = findViewById(R.id.topBarText);
        tv.setText(userInput.toUpperCase());
        // Do http request:
        httpGetArtist(userInput);
    }

    // Do Volley call on user input for artists:
    private void httpGetArtist(String userInput) {
        // Hide the no result screen: (Just in case)
        FrameLayout no_result = findViewById(R.id.no_result_frame);
        no_result.setVisibility(View.GONE);

        // Get the loading screen working:
        FrameLayout spinner = findViewById(R.id.loading_second_frame);
        spinner.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://artsy-server.wl.r.appspot.com/centaurus/artist_list/" + userInput;

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something:
                        // If no result:
                        if (Objects.equals(response, "[]")) {
                            no_result.setVisibility(View.VISIBLE);
                        // There are results:
                        } else {
                            // Parse JSON into ArtistCards objects:
                            Gson gson = new Gson();
                            cards = gson.fromJson(response, ArtistCard[].class);

                            // Get recyclerview:
                            RecyclerView rv_artist = findViewById(R.id.artist_recycler_view);

                            // Set recycler view layout:
                            GridLayoutManager layoutManager = new GridLayoutManager(SearchArtistActivity.this, 1);
                            rv_artist.setLayoutManager(layoutManager);

                            // Set up adapter:
                            ArtistAdapter myAdapter = new ArtistAdapter(cards, SearchArtistActivity.this);
                            rv_artist.setAdapter(myAdapter);

                            // Set on click event for the artist card:
                            myAdapter.setOnRecyclerItemClickListener(new ArtistAdapter.OnRecyclerItemClickListener() {
                                @Override
                                public void onRecyclerItemClick(int position) {
                                    // do something here, jump to next activity
                                    Intent toArtistDetailPage = new Intent(SearchArtistActivity.this, ShowMoreActivity.class);
                                    toArtistDetailPage.putExtra("artistId", cards[position].getId());
                                    toArtistDetailPage.putExtra("artistName", cards[position].getName());
                                    startActivity(toArtistDetailPage);
                                }
                            });
                        }
                        // After everything, set the loading sign off:
                        spinner.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SearchArtistActivity", "Volley: Get Artist Failed.");
                    }
                });
        queue.add(sr);
    }

    // Return to previous page:
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}