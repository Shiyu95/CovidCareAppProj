package edu.neu.covidcareapp.activities.community;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.neu.covidcareapp.R;
import edu.neu.covidcareapp.activities.community.ItemClickListener;

public class ReviewHolder extends RecyclerView.ViewHolder{
    public TextView postTitle;
    public TextView postDesc;

    public ReviewHolder(View itemView, final ItemClickListener listener) {
        super(itemView);
        postTitle = itemView.findViewById(R.id.post_title);
        postDesc = itemView.findViewById(R.id.post_description);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            }
        });



    }
}
