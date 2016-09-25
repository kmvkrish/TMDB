package com.example.android.tmdb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

public class MovieDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent movieDetailIntent = getIntent();
        int movieId = movieDetailIntent.getIntExtra("movieId",0);
        String movieTitle = movieDetailIntent.getStringExtra("movieTitle");

        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(movieTitle);
        }catch(NullPointerException npe){
            npe.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_movie_detail, MovieDetailFragment.newInstance(movieId, movieTitle)).commit();
    }

    public static class MovieDetailFragment extends Fragment{

        TextView movieTitleView;
        MovieDetail movieDetailObject;
        ProgressDialog progressDialog;

        View rootView;

        int movieId;
        String movieTitle;

        public MovieDetailFragment(){
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            movieTitle = getArguments().getString("movieTitle");
            movieId = getArguments().getInt("movieId");
        }

        @Override
        public void onStart() {
            super.onStart();
            Toast.makeText(getActivity(), movieTitle, Toast.LENGTH_SHORT).show();
            getMovieDetails();
        }

        private void getMovieDetails() {
            MovieDetailsTask movieDetailsTask = new MovieDetailsTask();
            movieDetailsTask.execute();
        }

        public void updateView(MovieDetail movieDetailObj){
            movieTitleView.setText(movieDetailObj.getMovieTitle());
            if(movieDetailObj.getMovieDescription().length() > 0){
                ((TextView)rootView.findViewById(R.id.movie_detail_overview)).setText(movieDetailObj.getMovieDescription());
            }
            if(movieDetailObj.getMoviePic().length() > 0){
                ImageView imageView = (ImageView) rootView.findViewById(R.id.movie_poster);
                int width = imageView.getWidth();
                int height = imageView.getHeight();

                Picasso.with(getActivity()).load(movieDetailObj.getMoviePic()).resize(width, height).into(imageView);
            }else{
                ((ImageView)rootView.findViewById(R.id.movie_poster)).setImageResource(R.mipmap.ic_launcher);
            }
            if(movieDetailObj.getMovieReleaseDate().length() > 0){
                ((TextView)rootView.findViewById(R.id.movie_detail_releaseDate)).setText(movieDetailObj.getMovieReleaseDate());
            }
            if(movieDetailObj.getMovieRating().length() > 0){
                ((TextView)rootView.findViewById(R.id.movie_detail_runTime)).setText(movieDetailObj.getMovieRuntime());
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

            movieTitleView = (TextView) rootView.findViewById(R.id.movie_detail_title);

            movieDetailObject = new MovieDetail("","","","","",null,"", 0);

            updateView(movieDetailObject);

            return rootView;
        }

        public static MovieDetailFragment newInstance(int movieId, String movieTitle){
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            Bundle args = new Bundle();
            args.putString("movieTitle", movieTitle);
            args.putInt("movieId", movieId);
            movieDetailFragment.setArguments(args);
            return movieDetailFragment;
        }

        public class MovieDetailsTask extends AsyncTask<Void, Void, MovieDetail>{
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
            protected void onPostExecute(MovieDetail movieDetail) {
                super.onPostExecute(movieDetail);
                if(movieDetail != null){
                    updateView(movieDetail);
                }
                progressDialog.dismiss();

            }

            @Override
            protected MovieDetail doInBackground(Void... voids) {

                HttpURLConnection httpURLConnection = null;
                BufferedReader reader = null;

                String movieDetailsJsonStr = null;
                try {
                    URL url = new URL("http://api.themoviedb.org/3/movie/" + movieId + "?api_key=8499f26672ec5778fb71964387394e29");
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
                    movieDetailsJsonStr = buffer.toString();
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

                return getMovieDetailsFromJson(movieDetailsJsonStr);
            }

            private MovieDetail getMovieDetailsFromJson(String movieDetailsJsonStr) {
                MovieDetail movieDetail = null;

                JSONObject movieDetailJson = null;
                String imageBaseUrl = "http://image.tmdb.org/t/p/original";

                String movieDetailTitle = "";
                String movieDetailPic = "";
                String movieDetailReleaseDate = "";
                String movieDetailRating;
                String movieDetailDescription;
                String movieDetailRuntime;
                int movieDetailVoteCount;
                String movieGenres;

                try{
                    movieDetailJson = new JSONObject(movieDetailsJsonStr);

                    movieDetailTitle = movieDetailJson.getString("original_title");
                    movieDetailDescription = movieDetailJson.getString("overview");
                    movieDetailReleaseDate = getSimpleDate(movieDetailJson.getString("release_date"));
                    movieDetailVoteCount = movieDetailJson.getInt("vote_count");
                    movieDetailPic = imageBaseUrl + movieDetailJson.getString("poster_path");
                    movieDetailRating = movieDetailJson.getString("vote_average");
                    movieDetailRuntime = convertMinutesToHours(movieDetailJson.getInt("runtime"));
                    movieGenres = convertGenresToString(movieDetailJson.getJSONArray("genres"));

                    movieDetail = new MovieDetail(movieDetailPic, movieDetailTitle, movieDetailRating, movieDetailReleaseDate, movieDetailDescription, movieGenres, movieDetailRuntime, movieDetailVoteCount);
                    System.out.println(movieDetail);
                }catch(JSONException je){
                    je.printStackTrace();
                }

                return movieDetail;
            }

            private String convertGenresToString(JSONArray genres) {
                String genresStr = "";
                try{
                    JSONObject genreJsonObject = null;
                    for(int i=0;i<genres.length(); i++){
                        genreJsonObject = genres.getJSONObject(i);
                        genresStr += genreJsonObject.getString("name") + ",";
                    }
                    genresStr = genresStr.substring(0, genresStr.lastIndexOf(","));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return genresStr;
            }

            private String convertMinutesToHours(int runtime) {
                int hrs = runtime / 60;
                int minutes = runtime % 60;
                return hrs + "h " + minutes + "m";
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

}
