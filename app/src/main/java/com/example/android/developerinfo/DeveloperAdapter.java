package com.example.android.developerinfo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 * Created by USER on 8/20/2017.
 */

public class DeveloperAdapter extends RecyclerView.Adapter<DeveloperAdapter.DeveloperAdapterViewHolder> {
    //meant to hold arrarlist of developers as a global variable
    private List<Developer> mDeveloperList;
    //holds the context(developer activity) as a global variable to enable multiple method calls on it
    private Context mainActivityContext;
    // holds the DeveloperAdapterOnClickHandler as a global variable
    private DeveloperAdapterOnClickHandler developerAdapterOnClickHandler;

    public DeveloperAdapter(Context context, DeveloperAdapterOnClickHandler clickHandler) {
        mainActivityContext = context;
        developerAdapterOnClickHandler = clickHandler;
    }

    //creates the viewholder object
    @Override
    public DeveloperAdapter.DeveloperAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        boolean shouldAttachImmediatelyToParent = false;
        View view = inflater.inflate(R.layout.list_item, parent, shouldAttachImmediatelyToParent);
        return new DeveloperAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeveloperAdapter.DeveloperAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    //meant to retuen size of developer arraylist
    @Override
    public int getItemCount() {
        if (null == mDeveloperList) return 0;
        return mDeveloperList.size();
    }

    public void setDeveloperList(List<Developer> developers) {
        mDeveloperList = developers;
        notifyDataSetChanged();
    }

    public interface DeveloperAdapterOnClickHandler {
        public void onClick(int position);
    }

    public class DeveloperAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mDeveloperTextView;
        ImageView mDeveloperImageView;
        ProgressBar mProgressBar;

        public DeveloperAdapterViewHolder(View itemView) {
            super(itemView);
            mDeveloperTextView = (TextView) itemView.findViewById(R.id.list_view_text);
            mDeveloperImageView = (ImageView) itemView.findViewById(R.id.developer_image_view);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.list_item_progress_bar);
            itemView.setOnClickListener(this);
        }

        //method called to attach data to each of the views in the viewholder
        void bind(int listIndex) {
            Developer currentDeveloper = mDeveloperList.get(listIndex);
            mDeveloperTextView.setText(currentDeveloper.getCurrentUserName());
            //  uses glide library to load a circular image and has a listener interface with methods implemented to ensure a progress bar is shown while loading
            Glide.with(mainActivityContext).load(currentDeveloper.getCurrentUrlImageString())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);

                            return false;
                        }
                    })
                    .apply(RequestOptions.circleCropTransform()).into(mDeveloperImageView);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            developerAdapterOnClickHandler.onClick(clickedPosition);

        }
    }
}
