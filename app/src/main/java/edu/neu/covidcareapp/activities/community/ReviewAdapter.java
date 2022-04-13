package edu.neu.covidcareapp.activities.community;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.covidcareapp.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder>{


    private final ArrayList<PostCard> itemList;
    private ItemClickListener listener;

    public ReviewAdapter(ArrayList<PostCard> itemList) {
        this.itemList = itemList;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ReviewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        PostCard currentItem = itemList.get(position);

        holder.postTitle.setText(currentItem.getPostTitle());
        holder.postDesc.setText(currentItem.getPostDesc());


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
