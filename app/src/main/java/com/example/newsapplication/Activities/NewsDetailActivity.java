package com.example.newsapplication.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.newsapplication.R;
import com.example.newsapplication.Utility.Utils;

public class NewsDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    ImageView ivND;
    TextView tvNDNTitle, tvNDSNubTitle, tvNDTime, tvNDTitle, tvNDDate;
    private boolean isHideToolbarView = false;
    private FrameLayout fLayoutND;
    private LinearLayout lLayoutND;
    private AppBarLayout abLayoutND;
    Toolbar tbND;
    String url, image, title, date, source, author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        tbND = findViewById(R.id.toolBarND);
        setSupportActionBar(tbND);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolBarLayoutND);
        collapsingToolbarLayout.setTitle("");

        abLayoutND = findViewById(R.id.appBarLayoutND);
        abLayoutND.addOnOffsetChangedListener(this);
        fLayoutND = findViewById(R.id.frameLayoutND);
        lLayoutND = findViewById(R.id.linerLayoutND);
        ivND = findViewById(R.id.imageViewND);
        tvNDNTitle = findViewById(R.id.textViewNDNTitle);
        tvNDSNubTitle = findViewById(R.id.textViewNDNSubtitle);
        tvNDTime = findViewById(R.id.textViewNDTime);
        tvNDDate = findViewById(R.id.textViewNDDate);
        tvNDTitle = findViewById(R.id.textViewNDTitle);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        author = intent.getStringExtra("author");
        title = intent.getStringExtra("title");
        image = intent.getStringExtra("image");
        date = intent.getStringExtra("date");
        source = intent.getStringExtra("source");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());
        Glide.with(this).load(image).apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(ivND);

        tvNDNTitle.setText(source);
        tvNDSNubTitle.setText(url);
        tvNDDate.setText(date);
        tvNDTitle.setText(title);

        String mAuthor = null;
        if (author != null || author != "") author = "\u2022" + author;
        else mAuthor = "";

        tvNDTime.setText(source + mAuthor + "\u2022" + Utils.DateToTimeFormat(date));
        initWebView(url);
    }

    private void initWebView (String url) {
        WebView webView = findViewById(R.id.webViewND);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

        int maxScroll = abLayoutND.getTotalScrollRange();
        float percentage = (float) Math.abs(i) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            fLayoutND.setVisibility(View.GONE);
            lLayoutND.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;
        } else if (percentage < 1f && isHideToolbarView) {
            fLayoutND.setVisibility(View.VISIBLE);
            lLayoutND.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_browser) {
          Intent intent = new Intent(Intent.ACTION_SEND);
          intent.setData(Uri.parse(url));
          startActivity(intent);
          return true;
        } else if (id == R.id.action_share) {
             try {
                 Intent intent = new Intent(Intent.ACTION_SEND);
                 intent.setType("text/plan");
                 intent.putExtra(Intent.EXTRA_SUBJECT, source);
                 String body = title + "\n" + url + "\n" + "Share from the News Application" + "\n";
                 intent.putExtra(Intent.EXTRA_TEXT, body);
                 startActivity(Intent.createChooser(intent, "Share with : "));
             } catch (Exception e) {
                 Toast.makeText(NewsDetailActivity.this, "Hmm... Sorry, \nCannot be share",Toast.LENGTH_SHORT).show();
             }
        }
        return super.onOptionsItemSelected(item);
    }
}