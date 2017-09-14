package com.example.android.developerinfo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

/**
 * Created by USER on 8/21/2017.
 */

public class DeveloperDetailsActivity extends AppCompatActivity {
    //global variable to hold the developer url
    private String developerUrl;
    //global variable to hold the developer name
    private String developerName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_details);
        developerName = getIntent().getStringExtra("name");
        developerUrl = getIntent().getStringExtra("url");
        String developerImageUrl = getIntent().getStringExtra("image");
        //below calls get references to the different views in the details activity and sets their data appropriately
        TextView nameTextView = (TextView) findViewById(R.id.name_text_view);
        nameTextView.setText(developerName);
        TextView urlTextView = (TextView) findViewById(R.id.url_text_view);
        urlTextView.setText(developerUrl);
        urlTextView.setPaintFlags(urlTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        ImageView developerImageView = (ImageView) findViewById(R.id.developer_image);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.details_progress_bar);
        Button shareButton = (Button) findViewById(R.id.share_button);
        shareButton.setBackgroundColor(Color.RED);
        loadOvalImageWithGlide(developerImageUrl, progressBar, developerImageView);

    }

    // method uses glide library to load an oval image and has a listener interface with methods implemented to ensure a progress bar is shown while loading
    private void loadOvalImageWithGlide(String url, ProgressBar bar, ImageView imageView) {
        final ProgressBar progressBar = bar;
        Glide.with(this).load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);

                        return false;
                    }
                })
                .apply(RequestOptions.circleCropTransform()).into(imageView);
    }

    //method called directly from xml when the developer url textview is clicked to launch an internet intent
    public void openDeveloperUrl(View view) {
        Uri currentUri = Uri.parse(developerUrl);
        Intent internetIntent = new Intent(Intent.ACTION_VIEW, currentUri);
        startActivity(internetIntent);
    }

    //method called directly from xml when the share button is clicked
    public void launchShareIntent(View view) {
        String intentContent = R.string.check_developer_string + developerName + ", " + developerUrl + ".";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/html");
        shareIntent.putExtra(Intent.EXTRA_TEXT, intentContent);
        startActivity(Intent.createChooser(shareIntent, "Select APP to Share WIth"));
    }
}
