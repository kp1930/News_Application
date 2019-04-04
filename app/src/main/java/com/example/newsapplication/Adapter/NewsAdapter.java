package com.example.newsapplication.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.newsapplication.Model.Article;
import com.example.newsapplication.R;
import com.example.newsapplication.Utility.Utils;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private List<Article> articles;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public NewsAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_news, viewGroup, false);
        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        final MyViewHolder myViewHolder = holder;
        Article article = articles.get(i);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context).load(article.getUrlToImage()).apply(requestOptions).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                myViewHolder.pbNews.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                myViewHolder.pbNews.setVisibility(View.GONE);
                return false;
            }
        }).transition(DrawableTransitionOptions.withCrossFade()).into(myViewHolder.ivNews);

        myViewHolder.tvTitle.setText(article.getTitle());
        myViewHolder.tvDesc.setText(article.getDescription());
        myViewHolder.tvSource.setText(article.getSource().getName());
        myViewHolder.tvAuthor.setText(article.getAuthor());
        myViewHolder.tvPublishAt.setText(article.getPublishedAt());
        myViewHolder.tvTime.setText("\u2022" + Utils.DateToTimeFormat(article.getPublishedAt()));
    }

    @Override
    public int getItemCount() { return articles.size(); }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) { this.onItemClickListener = onItemClickListener; }

    public interface OnItemClickListener { void onItemClick(View view, int position);}

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle, tvDesc, tvAuthor, tvPublishAt, tvTime, tvSource;
        ImageView ivNews;
        ProgressBar pbNews;
        OnItemClickListener onItemClickListener;

        public MyViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);
            tvTitle = itemView.findViewById(R.id.textViewTitle);
            tvDesc = itemView.findViewById(R.id.textViewDesc);
            tvAuthor = itemView.findViewById(R.id.textViewAuthor);
            tvPublishAt = itemView.findViewById(R.id.textViewPublishAt);
            tvSource = itemView.findViewById(R.id.textViewSource);
            tvTime = itemView.findViewById(R.id.textViewTime);
            ivNews = itemView.findViewById(R.id.imageViewNews);
            pbNews = itemView.findViewById(R.id.progressBarNews);

            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) { onItemClickListener.onItemClick(v, getAdapterPosition()); }
    }
}