package com.openclassrooms.entrevoisins.ui.neighbour_list.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.events.FavoriteNeighbourEvent;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_list.OnAdapterItemClickListener;
import com.openclassrooms.entrevoisins.ui.neighbour_list.activities.NeighbourDetailsActivity;
import com.openclassrooms.entrevoisins.ui.neighbour_list.adapters.MyNeighbourRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class FavoriteFragment extends Fragment implements OnAdapterItemClickListener {

    private AppCompatImageView iconEmpty;
    private AppCompatTextView textEmpty;
    private RecyclerView recyclerView;

    private NeighbourApiService mApiService;
    private Context context;

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        context = view.getContext();

        recyclerView = view.findViewById(R.id.list_neighbours_favorites);
        iconEmpty = view.findViewById(R.id.icon_empty);
        textEmpty = view.findViewById(R.id.text_empty);
        initList();
        return view;
    }

    /**
     * Init the List of neighbours
     */
    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        if (mApiService.getFavoritesNeighbours().size() != 0) {
            recyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(mApiService.getFavoritesNeighbours(), getActivity(), this, this));
            iconEmpty.setVisibility(View.INVISIBLE);
            textEmpty.setVisibility(View.INVISIBLE);
        } else {
            iconEmpty.setVisibility(View.VISIBLE);
            textEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    /**
     * Fired if the user clicks on a delete button
     *
     * @param event
     */
    @Subscribe
    public void onFavoriteNeighbour(FavoriteNeighbourEvent event) {
        mApiService.neighbourChangeFavorites(event.neighbour);
        initList();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAdapterItemClickListener(long id) {
        Intent intent = new Intent(getActivity(), NeighbourDetailsActivity.class);
        intent.putExtra("id", id);
        getActivity().startActivity(intent);
    }
}