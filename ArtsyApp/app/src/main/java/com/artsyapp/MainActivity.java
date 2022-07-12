package com.hw9.artsyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.TimeUnit;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set the splash screen:
        setTheme(R.style.Theme_ArtsyApp);


        // Set up the main screen:
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show the loading screen:
        FrameLayout beginning_loading = findViewById(R.id.loading_first_frame);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                beginning_loading.setVisibility(View.GONE);
            }
        }, 2500);

        // Display date on the main screen:
        TextView dateTime = findViewById(R.id.dateTime);
        String currDateTime = getDate();
        dateTime.setText(currDateTime);

        // Initial toolbar:
        Toolbar toolbar = findViewById(R.id.searchBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Get button to work:
        Button artsyButton = findViewById(R.id.artsyLinkButton);
        artsyButton.setOnClickListener(v -> {
            Intent openArtsy = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.artsy.net/"));
            startActivity(openArtsy);
        });

        // Generate the recycler view of favorite artists:
        initFav();
    }

    private void initFav() {
        // Get recycler view to work:
        RecyclerView rv_fav = findViewById(R.id.favorite_recycler_view);

        // Set recycler view layout:
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_fav.setLayoutManager(layoutManager);

        // Set up adapter:
        // At this point, we need to get data from shared preference:
        SharedPreferences sharedPreferences = getSharedPreferences("FavSharedPref", MODE_PRIVATE);
        Map<String, ?> allFavMap = sharedPreferences.getAll();
        List<FavoriteItem> favoriteItemList = new ArrayList<>();
        Gson gson = new Gson();
        for (String key : allFavMap.keySet()) {
            FavoriteItem favItem = gson.fromJson(allFavMap.get(key).toString(), FavoriteItem.class);
            favoriteItemList.add(0, favItem);
        }

        // Setup recycler view:
        FavoriteAdapter myAdapter = new FavoriteAdapter(favoriteItemList, this);
        rv_fav.setAdapter(myAdapter);

        // Set on click event for the button inside the favorite section:
        myAdapter.setButtonClickListener(new FavoriteAdapter.ButtonClickListener() {
            @Override
            public void buttonClick(int position) {
                Intent favToShowMore = new Intent(MainActivity.this, ShowMoreActivity.class);
                favToShowMore.putExtra("artistId", favoriteItemList.get(position).getFavId());
                favToShowMore.putExtra("artistName", favoriteItemList.get(position).getFavName());
                startActivity(favToShowMore);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFav();
    }

    @Override
    protected void onPause() {
        super.onPause();
        initFav();
    }

    @Override
    protected void onStop() {
        super.onStop();
        initFav();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        // Get SearchView:
        MenuItem searchItem = menu.findItem(R.id.artist_search);
        SearchView sv = (SearchView) searchItem.getActionView();
        // Modify a few things:
        sv.setQueryHint("Search...");

        // When user searches:
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sv.clearFocus();
                if (!TextUtils.isEmpty(query)) {
                    // Jump to next activity:
                    Intent toSearchPage = new Intent(MainActivity.this, SearchArtistActivity.class);
                    toSearchPage.putExtra("userInput", query);
                    startActivity(toSearchPage);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    // Get the date in day month year format:
    @NonNull
    private String getDate() {
        LocalDate date = LocalDate.now();
        return date.getDayOfMonth() + " " + date.getMonth() + " " + date.getYear();
    }
}