package com.example.android.tmdb;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kmvkrish on 20-09-2016.
 */
public class MovieAdapter extends BaseAdapter {

    Context context;
    ArrayList<Movie> movieList;

    MovieAdapter(Context context, ArrayList<Movie> movieList){
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(view == null){
            view = inflater.inflate(R.layout.movie_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.movieNameView = (TextView) view.findViewById(R.id.movieTitle);
            viewHolder.movieDateView = (TextView) view.findViewById(R.id.movieDate);
            viewHolder.movieRatingView = (TextView) view.findViewById(R.id.movieRating);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.moviePic);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        Movie movie = movieList.get(i);
        //viewHolder.imageView.setImageResource(movie.getMoviePicId());
        viewHolder.movieNameView.setText(movie.getMovieTitle());
        viewHolder.movieDateView.setText(movie.getMovieReleaseDate());
        viewHolder.movieRatingView.setText(movie.getMovieRating());
        Picasso.with(context).load(movie.getMoviePic()).resize(120, 120).into(viewHolder.imageView);

        return view;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Movie getItem(int i) {
        return movieList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return movieList.indexOf(getItem(i));
    }

    public void add(Movie movie) {
        movieList.add(movie);
        notifyDataSetChanged();
    }

    public void remove(int i){
        movieList.remove(i);
        notifyDataSetChanged();
    }

    public void remove(Movie movie){
        movieList.remove(movie);
        notifyDataSetChanged();
    }

    private class ViewHolder{
        ImageView imageView;
        TextView movieNameView;
        TextView movieDateView;
        TextView movieRatingView;
    }

    public void clear(){
        movieList.clear();
        notifyDataSetChanged();
    }
}
