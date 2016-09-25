package com.example.android.tmdb;

/**
 * Created by kmvkrish on 24-09-2016.
 */
public class MovieDetail {
    public String moviePic;
    public String movieTitle;
    public String movieRating;
    public String movieReleaseDate;
    public String movieDescription;
    public String movieGenres;
    public String movieRuntime;
    public int movieVoteCount;

    public MovieDetail(String moviePic, String movieTitle, String movieRating, String movieReleaseDate, String movieDescription, String movieGenres, String movieRuntime, int movieVoteCount) {
        this.moviePic = moviePic;
        this.movieTitle = movieTitle;
        this.movieRating = movieRating;
        this.movieReleaseDate = movieReleaseDate;
        this.movieDescription = movieDescription;
        this.movieGenres = movieGenres;
        this.movieRuntime = movieRuntime;
        this.movieVoteCount = movieVoteCount;
    }

    public String getMoviePic() {
        return moviePic;
    }

    public void setMoviePic(String moviePic) {
        this.moviePic = moviePic;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getMovieGenres() {
        return movieGenres;
    }

    public void setMovieGenres(String movieGenres) {
        this.movieGenres = movieGenres;
    }

    public String getMovieRuntime() {
        return movieRuntime;
    }

    public void setMovieRuntime(String movieRuntime) {
        this.movieRuntime = movieRuntime;
    }

    public int getMovieVoteCount() {
        return movieVoteCount;
    }

    public void setMovieVoteCount(int movieVoteCount) {
        this.movieVoteCount = movieVoteCount;
    }

    @Override
    public String toString() {
        return "MovieDetail{" +
                "moviePic='" + moviePic + '\'' +
                ", movieTitle='" + movieTitle + '\'' +
                '}';
    }
}
