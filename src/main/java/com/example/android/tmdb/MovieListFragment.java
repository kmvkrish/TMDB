package com.example.android.tmdb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MovieListFragment extends Fragment {

    MovieAdapter movieAdapter;
    ProgressDialog progressDialog;
    ArrayList<Movie> moviesList = new ArrayList<Movie>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public MovieListFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_refresh){
            updateMovieList();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieList();
    }

    private void updateMovieList() {
        Log.v("MovieListFragment", "Started Activity");
        FetchMovieListTask fetchMovieListTask = new FetchMovieListTask();
        fetchMovieListTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_movie_list_fragment, container, false);

        movieAdapter = new MovieAdapter(getActivity(), moviesList);

        ListView movieListView = (ListView) rootView.findViewById(R.id.movie_list_view);
        movieListView.setAdapter(movieAdapter);

        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = movieAdapter.getItem(i);
                //Toast.makeText(getActivity(), movie.getMovieTitle(), Toast.LENGTH_SHORT).show();
                Intent movieDetailIntent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra("movieId", movie.getMovieId())
                        .putExtra("movieTitle", movie.getMovieTitle());
                startActivity(movieDetailIntent);
            }
        });

        return rootView;
    }

    public class FetchMovieListTask extends AsyncTask<Void, Void, Movie[]>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            super.onPostExecute(movies);
            if(movies != null){
                if(movies.length > 0){
                    movieAdapter.clear();
                    for(Movie movie:movies){
                        movieAdapter.add(movie);
                    }
                }
            }
            progressDialog.dismiss();
        }

        @Override
        protected Movie[] doInBackground(Void... voids) {


            HttpURLConnection httpURLConnection = null;
            BufferedReader reader = null;

            String movieListJsonStr = null;
            try {
                URL url = new URL("http://api.themoviedb.org/3/movie/popular?api_key=<api_key>");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if(inputStream == null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }
                if(buffer.length() == 0){
                    return null;
                }
                movieListJsonStr = buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                if(httpURLConnection != null){
                    httpURLConnection.disconnect();
                }
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.v("JSON Response",movieListJsonStr);
            return getMovieListFromJson(movieListJsonStr);
        }

        private Movie[] getMovieListFromJson(String movieListJsonStr) {
            String TMDB_KEY = "results";
            String imageBaseUrl = "http://image.tmdb.org/t/p/original";
            Movie[] movies = null;
            JSONObject movieJson = null;

            Movie movie = null;

            String movieTitle = "";
            String moviePic = "";
            String movieReleaseDate = "";
            int movieId;
            String movieRating;
            String movieDescription;

            try {
                movieJson = new JSONObject(movieListJsonStr);
                JSONArray movieArray = movieJson.getJSONArray(TMDB_KEY);
                movies = new Movie[movieArray.length()];

                for(int i=0; i< movieArray.length(); i++){
                    JSONObject movieObject = movieArray.getJSONObject(i);

                    movieTitle = movieObject.getString("title");
                    movieReleaseDate = getSimpleDate(movieObject.getString("release_date"));
                    movieDescription = movieObject.getString("overview");
                    movieId = movieObject.getInt("id");
                    moviePic = imageBaseUrl + movieObject.getString("poster_path");
                    movieRating = movieObject.getString("vote_average");

                    movie = new Movie(movieId, movieTitle, movieDescription, moviePic, movieReleaseDate, movieRating);
                    movies[i] = movie;

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return movies;
        }

        private String getSimpleDate(String date){
            SimpleDateFormat sdfOut = new SimpleDateFormat("dd MMM, yyyy");
            SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
            String simpleDate = "";
            try {
                simpleDate = sdfOut.format(sdfIn.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return simpleDate;
        }
    }
}
