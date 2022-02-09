package com.openclassrooms.entrevoisins.ui.neighbour_list.adapters;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.events.FavoriteNeighbourEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_list.OnAdapterItemClickListener;
import com.openclassrooms.entrevoisins.ui.neighbour_list.activities.NeighbourDetailsActivity;
import com.openclassrooms.entrevoisins.ui.neighbour_list.fragments.FavoriteFragment;
import com.openclassrooms.entrevoisins.ui.neighbour_list.fragments.NeighbourFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyNeighbourRecyclerViewAdapter extends RecyclerView.Adapter<MyNeighbourRecyclerViewAdapter.ViewHolder> {

    private final List<Neighbour> mNeighbours;
    private final Activity activity;
    private Fragment fragment;

    private OnAdapterItemClickListener mCallBack;

    public MyNeighbourRecyclerViewAdapter(List<Neighbour> items, Activity activity, Fragment fragment, OnAdapterItemClickListener mCallBack) {
        mNeighbours = items;
        this.activity = activity;
        this.fragment = fragment;
        this.mCallBack = mCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemview_neighbour, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Neighbour neighbour = mNeighbours.get(position);
        holder.mNeighbourName.setText(neighbour.getName());
        Glide.with(holder.mNeighbourAvatar.getContext())
                .load(neighbour.getAvatarUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mNeighbourAvatar);

        holder.root.setOnClickListener(v -> {
            holder.root.isEnabled();
            mCallBack.onAdapterItemClickListener(neighbour.getId());
        });

        if (fragment instanceof FavoriteFragment) {
            holder.mDeleteButton.setOnClickListener(v -> EventBus.getDefault().post(new FavoriteNeighbourEvent(neighbour)));
        } else {
            holder.mDeleteButton.setOnClickListener(v -> EventBus.getDefault().post(new DeleteNeighbourEvent(neighbour)));
        }
    }

    @Override
    public int getItemCount() {
        return mNeighbours.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root)
        public ConstraintLayout root;
        @BindView(R.id.item_list_avatar)
        public ImageView mNeighbourAvatar;
        @BindView(R.id.item_list_name)
        public TextView mNeighbourName;
        @BindView(R.id.item_list_delete_button)
        public ImageButton mDeleteButton;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
