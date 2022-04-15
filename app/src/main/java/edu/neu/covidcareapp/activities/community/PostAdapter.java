package edu.neu.covidcareapp.activities.community;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.neu.covidcareapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ReviewHolder>{


    private final ArrayList<PostCard> itemList;
    private ItemClickListener listener;

    public PostAdapter(ArrayList<PostCard> itemList) {
        this.itemList = itemList;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        PostCard currentItem = itemList.get(position);

        holder.postTitle.setText(currentItem.getPostTitle());
        holder.postDesc.setText(currentItem.getPostDesc());
        holder.postDate.setText(currentItem.getTimeStamp());



    }

    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("mm:ss",calendar).toString();
        return date;


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }




    public class ReviewHolder extends RecyclerView.ViewHolder {

        public TextView postTitle;
        public TextView postDesc;
        public TextView postDate;

        public ReviewHolder(View itemView) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.post_title);
            postDesc = itemView.findViewById(R.id.post_description);
            postDate = itemView.findViewById(R.id.post_date);



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
    }
