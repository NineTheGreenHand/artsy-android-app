package com.hw9.artsyapp;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Objects;

public class DetailsFragment extends Fragment {

    private static final String ARG_TEXT = "infoJSON";

    private View rootView;
    private String infoJSON;
    private ArtistInfo info;

    public DetailsFragment() {

    }

    public static DetailsFragment newInstance(String param) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            infoJSON = getArguments().getString(ARG_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_details, container, false);
        }
        initView();
        return rootView;
    }

    // Change the elements in the fragments:
    private void initView() {
        // Parse the infoJSON value:
        Gson gson = new Gson();
        info = gson.fromJson(infoJSON, ArtistInfo.class);
        String name = info.getName();
        String nationality = info.getNationality();
        String birthday = info.getBirthday();
        String deathday = info.getDeathday();
        String bio = info.getBio();

        // Define text views:
        TextView tv_name = rootView.findViewById(R.id.name_text);
        TextView tv_nationality = rootView.findViewById(R.id.nationality_text);
        TextView tv_birthday = rootView.findViewById(R.id.birthday_text);
        TextView tv_deathday = rootView.findViewById(R.id.deathday_text);
        TextView tv_bio = rootView.findViewById(R.id.bio_text);

        // Define corresponding part:
        ConstraintLayout name_frag = rootView.findViewById(R.id.name_frag);
        ConstraintLayout nationality_frag = rootView.findViewById(R.id.nationality_frag);
        ConstraintLayout birthday_frag = rootView.findViewById(R.id.birthday_frag);
        ConstraintLayout deathday_frag = rootView.findViewById(R.id.deathday_frag);
        ConstraintLayout bio_frag = rootView.findViewById(R.id.bio_frag);

        // Just in case, initially, all frag should be visible:
        name_frag.setVisibility(View.VISIBLE);
        nationality_frag.setVisibility(View.VISIBLE);
        birthday_frag.setVisibility(View.VISIBLE);
        deathday_frag.setVisibility(View.VISIBLE);
        bio_frag.setVisibility(View.VISIBLE);

        // Then, depending weather corresponding part is empty, show/hide frags:
        // name will always be there:
        tv_name.setText(name);
        if (nationality == null || Objects.equals(nationality, "")) {
            nationality_frag.setVisibility(View.GONE);
        } else {
            tv_nationality.setText(nationality);
        }
        if (birthday == null || Objects.equals(birthday, "")) {
            birthday_frag.setVisibility(View.GONE);
        } else {
            tv_birthday.setText(birthday);
        }
        if (deathday == null || Objects.equals(deathday, "")) {
            deathday_frag.setVisibility(View.GONE);
        } else {
            tv_deathday.setText(deathday);
        }
        if (bio == null || Objects.equals(bio, "")) {
            bio_frag.setVisibility(View.GONE);
        } else {
            tv_bio.setText(bio);
        }
    }
}