package com.hw9.artsyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShowMoreActivity extends AppCompatActivity {

    private String infoJSON;
    private String artworkJSON;

    // For favorite section:
    private String favName;
    private String favNationality;
    private String favBirthday;
    private String favId;
    private ArtistInfo artistInfo;

    private ArrayList<Fragment> fragments;
    private TabLayoutMediator mediator;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private boolean infoGetFinished;
    private boolean artworkGetFinished;
    private FrameLayout spinner;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_more);

        // Set shared preference:
        sharedPreferences = getSharedPreferences("FavSharedPref", MODE_PRIVATE);

        // Get the user input from the search bar:
        Intent userCard = getIntent();
        String artistId = userCard.getStringExtra("artistId");
        String artistName = userCard.getStringExtra("artistName");

        // Initial toolbar, set the action to go back:
        Toolbar topBar = findViewById(R.id.topBar_show_more);
        topBar.setTitle("");
        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });

        // Add support action to it: (for favorite section)
        setSupportActionBar(topBar);

        // Get TextView object:
        TextView tv = findViewById(R.id.topBarText_show_more);
        tv.setText(artistName);

        // fill in the favId
        favId = artistId;

        // Do http request:
        httpGetDetailArtwork(artistId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_show_more, menu);
        // Show different favorite icon depend on if the artist is a favorite artist:
        if (sharedPreferences.contains(favId)) {
            // We show filled star
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.star);
        } else {
            // We show outlined star
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.start_outline);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                // Setup shared preference:
                SharedPreferences.Editor favEdit = sharedPreferences.edit();
                if (sharedPreferences.contains(favId)) {
                    // This means this item is favorite, and if clicked again, we cancel the like:
                    // Set icon and show toast:
                    item.setIcon(R.drawable.start_outline);
                    String toast_text = favName + " is removed from favorites";
                    Toast.makeText(this, toast_text, Toast.LENGTH_SHORT).show();

                    // Remove preference:
                    favEdit.remove(favId);
                } else {
                    // This means this is not a favorite artist:
                    // Set icon and show toast:
                    item.setIcon(R.drawable.star);
                    String toast_text = favName + " is added to favorites";
                    Toast.makeText(this, toast_text, Toast.LENGTH_SHORT).show();

                    // Put the key, value pair into shared preference: {artistId : favoriteItem in JSON}
                    FavoriteItem favoriteItem = new FavoriteItem(favName, favNationality, favBirthday, favId);
                    Gson gson = new Gson();
                    String favJSON = gson.toJson(favoriteItem);
                    favEdit.putString(favId, favJSON);
                }
                favEdit.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Initialize the view pager:
    private void initPager() {
        viewPager = findViewById(R.id.viewpage);
        tabLayout = findViewById(R.id.two_tabs);
        viewPager.setOffscreenPageLimit(2);
        if (infoGetFinished && artworkGetFinished) {
            ArrayList<Fragment> fragments = new ArrayList<>();
            fragments.add(DetailsFragment.newInstance(infoJSON));
            fragments.add(ArtworkFragment.newInstance(artworkJSON));
            MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
            viewPager.setAdapter(pagerAdapter);

            // Setup mediator:
            mediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    if (position == 0) {
                        tab.setText(R.string.details);
                        tab.setIcon(R.drawable.detail_icon);
                    } else {
                        tab.setText(R.string.artwork);
                        tab.setIcon(R.drawable.artwork_icon);
                    }
                }
            });
            mediator.attach();
        }
    }

    // Get artist details and artworks:
    private void httpGetDetailArtwork(String artistId) {
        // Get the loading screen working:
        spinner = findViewById(R.id.loading_third_frame);
        spinner.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String infoURL = "https://artsy-server.wl.r.appspot.com/centaurus/artist_info/" + artistId;
        String artworkURL = "https://artsy-server.wl.r.appspot.com/centaurus/artwork_list/" + artistId;
        infoGetFinished = false;
        artworkGetFinished = false;

        StringRequest srInfo = new StringRequest(Request.Method.GET, infoURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Pass value:
                        infoJSON = response;
                        infoGetFinished = true;

                        // Side mission, fill favorite required parameters:
                        Gson gson = new Gson();
                        artistInfo = gson.fromJson(response, ArtistInfo.class);
                        favName = artistInfo.getName();
                        favNationality = artistInfo.getNationality();
                        favBirthday = artistInfo.getBirthday();

                        // Hide spinner:
                        hideSpinner();
                        // Setup the fragments:
                        initPager();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ShowMoreActivity", "Volley: Get Artist Info Failed.");
                    }
                });

        StringRequest srArtwork = new StringRequest(Request.Method.GET, artworkURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Pass value:
                        artworkJSON = response;
                        artworkGetFinished = true;
                        hideSpinner();
                        // Setup the fragments:
                        initPager();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ShowMoreActivity", "Volley: Get Artwork Failed.");
                    }
                });
        queue.add(srArtwork);
        queue.add(srInfo);
    }

    // If both GET request is done, then hide spinner:
    private void hideSpinner() {
        if (infoGetFinished && artworkGetFinished) {
            spinner.setVisibility(View.GONE);
        }
    }

    // Return to previous page:
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}