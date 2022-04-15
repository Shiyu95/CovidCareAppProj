package edu.neu.covidcareapp.activities.community;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.neu.covidcareapp.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private Context commentContext;
    private List<Comment> commentList;


    public CommentAdapter(Context commentContext, List<Comment> commentList) {
        this.commentContext = commentContext;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(commentContext).inflate(R.layout.row_comment_display,parent,false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Glide.with(commentContext).load(commentList.get(position).getUserImg()).into(holder.userImg);
        holder.commentDisplayName.setText(commentList.get(position).getUserName());
        holder.commentDisplayContent.setText(commentList.get(position).getContent());
        holder.commentDisplayDate.setText(commentList.get(position).getTimestamp());

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        ImageView userImg;
        TextView commentDisplayName;
        TextView commentDisplayContent;
        TextView commentDisplayDate;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userImg = itemView.findViewById(R.id.comment_user_img);
            commentDisplayName = itemView.findViewById(R.id.comment_username);
            commentDisplayContent = itemView.findViewById(R.id.comment_content);
            commentDisplayDate = itemView.findViewById(R.id.comment_date);
        }
    }


}
