package com.openclassrooms.entrevoisins.ui.neighbour_list.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_list.OnNeighbourClickListener;
import com.openclassrooms.entrevoisins.ui.neighbour_list.adapters.MyNeighbourRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;


public class NeighbourFragment extends Fragment implements OnNeighbourClickListener {

    private NeighbourApiService mApiService;
    private RecyclerView mRecyclerView;

    private static final String MODE_FAVORITE = "MODE_FAVORITE";
    private OnNeighbourClickListener listener;

    /**
     * Create and return a new instance
     *
     * @return @{@link NeighbourFragment}
     */
    public static NeighbourFragment newInstance(boolean isFavoriteMode) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(MODE_FAVORITE, isFavoriteMode);
        NeighbourFragment fragment = new NeighbourFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnNeighbourClickListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neighbour_list, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        return view;
    }

    /**
     * Init the List of neighbours
     */
    private void initList() {
        List<Neighbour> neighbours;
        if (requireArguments().getBoolean(MODE_FAVORITE)) {
            neighbours = mApiService.getFavoritesNeighbours();
        } else {
            neighbours = mApiService.getNeighbours();
        }
        mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(neighbours, this));
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

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Fired if the user clicks on a delete button
     *
     * @param event
     */
    @Subscribe
    public void onDeleteNeighbour(DeleteNeighbourEvent event) {
        mApiService.deleteNeighbour(event.neighbour);
        initList();
    }

    @Override
    public void onNeighbourClick(long id) {
        listener.onNeighbourClick(id);
    }
}
