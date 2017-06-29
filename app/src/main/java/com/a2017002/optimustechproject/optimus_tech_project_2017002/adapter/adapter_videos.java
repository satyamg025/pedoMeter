package com.a2017002.optimustechproject.optimus_tech_project_2017002.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.Activity.VideoActivity;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.R;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.fragments.VideoFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satyam on 28/6/17.
 */

public class adapter_videos extends RecyclerView.Adapter<adapter_videos.view_holder> {

    Context context;
    public List<String> name=new ArrayList<String>();
    public List<String> videoBy=new ArrayList<String>();
    public List<String> background=new ArrayList<String>();
    public List<String> fetchUrl=new ArrayList<String>();


    public adapter_videos(Context context, List<String> name, List<String> videoBy, List<String> background, List<String> fetchUrl) {
        this.context=context;
        this.name=name;
        this.videoBy=videoBy;
        this.background=background;
        this.fetchUrl=fetchUrl;
    }

    @Override
    public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_video,parent,false);
        return new view_holder(view);
    }

    @Override
    public void onBindViewHolder(final view_holder holder, int position) {
        holder.setIsRecyclable(false);

        holder.video_name.setText(name.get(position));
        holder.video_by.setText(videoBy.get(position));
        Picasso.with(context).load(background.get(position)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.relativeLayout.setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class view_holder extends RecyclerView.ViewHolder {

        public  RelativeLayout relativeLayout;
        public TextView video_name,video_by;

        public view_holder(View itemView) {

            super(itemView);

            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.rl_background);
            video_name=(TextView)itemView.findViewById(R.id.video_name);
            video_by=(TextView)itemView.findViewById(R.id.video_by);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("videoFetchUrl",fetchUrl.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }

    }
}