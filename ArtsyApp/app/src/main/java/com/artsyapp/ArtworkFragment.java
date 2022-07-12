package com.hw9.artsyapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Objects;


public class ArtworkFragment extends Fragment {

    private static final String ARG_TEXT = "artworkJSON";

    private String artworkJSON;
    private View rootView;
    private ArtistArtwork[] artistArtworks;
    private ArtworkGene[] genes;
    private FrameLayout noArtwork;


    public ArtworkFragment() {
        // Required empty public constructor
    }

    public static ArtworkFragment newInstance(String param) {
        ArtworkFragment fragment = new ArtworkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            artworkJSON = getArguments().getString(ARG_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_artwork, container, false);
        }
        initView();
        return rootView;
    }

    // Change the elements in the fragments:
    private void initView() {
        // Get the no artwork frame layout:
        noArtwork = rootView.findViewById(R.id.no_artwork_frame);
        // Hide it in first place:
        noArtwork.setVisibility(View.GONE);

        // Parse JSON:
        Gson gson = new Gson();
        artistArtworks = gson.fromJson(artworkJSON, ArtistArtwork[].class);

        // If no artwork:
        if (Objects.equals(artworkJSON, "[]") || artistArtworks.length == 0) {
            noArtwork.setVisibility(View.VISIBLE);
        // There are artworks:
        } else {
            // Get recycler view:
            RecyclerView rv_artwork = rootView.findViewById(R.id.artwork_recycler_view);

            // Set recycler view layout:
            GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1);
            rv_artwork.setLayoutManager(layoutManager);

            // Set up adapter:
            ArtworkAdapter myAdapter = new ArtworkAdapter(artistArtworks, requireContext());
            rv_artwork.setAdapter(myAdapter);

            // Set on click event for the artwork card:
            myAdapter.setOnRecyclerItemClickListener(new ArtworkAdapter.OnRecyclerItemClickListener() {
                @Override
                public void onRecyclerItemClick(int position) {
                    // Show modal:
                    createModal(artistArtworks[position].getArtworkId());
                }
            });
        }
    }

    // When an artwork card is clicked, create and show modal:
    private void createModal(String artworkId) {
        // Fetch Data first:
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String geneURL = "https://artsy-server.wl.r.appspot.com/centaurus/gene/" + artworkId;
        StringRequest srGene = new StringRequest(Request.Method.GET, geneURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parse Data first:
                        Gson gson = new Gson();
                        genes = gson.fromJson(response, ArtworkGene[].class);
                        showModal();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ArtworkFragment", "Volley: Get Artwork Gene Failed.");
                    }
                });
        queue.add(srGene);
    }

    // Create the modal with information get:
    private void showModal() {
        // Create alert dialog builder:
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // Get the modal layout view, one for there is result, one for there are no result:
        View modalView = getLayoutInflater().inflate(R.layout.modal_layout, null);
        View modalNoGeneView = getLayoutInflater().inflate(R.layout.modal_no_category, null);

        if (genes.length == 0) {
            builder.setView(modalNoGeneView).create().show();
        } else {
            // Get some view elements from the view:
            TextView modalName = modalView.findViewById(R.id.gene_name);
            TextView modalDescription = modalView.findViewById(R.id.gene_description);
            ImageView modalImage = modalView.findViewById(R.id.gene_pic);

            // Fill in information:
            modalName.setText(genes[0].getGeneName());
            modalDescription.setText(genes[0].getGeneDescription());
            Picasso.get().load(genes[0].getGenePicURL()).into(modalImage);

            builder.setView(modalView).create().show();
        }
    }
}