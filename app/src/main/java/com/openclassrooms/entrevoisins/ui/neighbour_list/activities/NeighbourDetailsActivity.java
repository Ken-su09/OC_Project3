package com.openclassrooms.entrevoisins.ui.neighbour_list.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NeighbourDetailsActivity extends AppCompatActivity {

    @BindView(R.id.add_to_favorites)
    FloatingActionButton addToFavorites;

    @BindView(R.id.details_toolbar)
    MaterialToolbar mToolbar;

    //region ======================================== Neighbour data ========================================

    @BindView(R.id.neighbour_image)
    AppCompatImageView neighbourImage;
    @BindView(R.id.neighbour_name)
    AppCompatTextView neighbourName;
    @BindView(R.id.neighbour_name_1)
    AppCompatTextView neighbourName1;
    @BindView(R.id.location_content)
    AppCompatTextView locationContent;
    @BindView(R.id.phone_content)
    AppCompatTextView phoneContent;
    @BindView(R.id.social_network_content)
    AppCompatTextView socialNetworkContent;
    @BindView(R.id.neighbour_description_content)
    AppCompatTextView neighbourDescriptionContent;

    private void fillNeighbourData() {
        runOnUiThread(() -> Glide.with(activity)
                .load(convertImageLink(neighbour.getAvatarUrl()))
                .into(neighbourImage));

        neighbourName.setText(neighbour.getName());
        neighbourName1.setText(neighbour.getName());
        locationContent.setText(neighbour.getAddress());
        phoneContent.setText(neighbour.getPhoneNumber());
        socialNetworkContent.setText("www.facebook.fr/" + neighbour.getName().toLowerCase(Locale.ROOT));
        neighbourDescriptionContent.setText(neighbour.getAboutMe());

        isFavorite = neighbour.getIsFavorite();
        checkIfFavorite();
    }

    private String convertImageLink(String link) {
        return link.replace("150", "400");
    }

    private void getNeighbourForService() {
        NeighbourApiService mApiService = DI.getNeighbourApiService();

        for (int i = 0; i <= mApiService.getNeighbours().size(); i++) {
            if (mApiService.getNeighbours().get(i).getId() == getIntent().getLongExtra("id", 0)) {
                neighbour = mApiService.getNeighbours().get(i);
                break;
            } else {
                neighbour = mApiService.getNeighbours().get(i);
            }
        }
    }

    //endregion

    private Neighbour neighbour;
    private final Activity activity = this;
    private Boolean isFavorite = false;

    private NeighbourApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();

        setContentView(R.layout.activity_neighbour_details);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getNeighbourForService();
        fillNeighbourData();

        addToFavorites.setOnClickListener(view -> {
            addToFavorites();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkIfFavorite() {
        if (isFavorite) {
            addToFavorites.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_white_24dp));
        } else {
            addToFavorites.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_border_white_24dp));
        }
    }

    private void addToFavorites() {
        mApiService.neighbourChangeFavorites(neighbour);
        if (isFavorite) {
            addToFavorites.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_border_white_24dp));
            isFavorite = false;
        } else {
            addToFavorites.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_white_24dp));
            isFavorite = true;
        }
    }
}