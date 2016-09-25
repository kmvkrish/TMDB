package com.example.android.tmdb;

/**
 * Created by kmvkrish on 20-09-2016.
 */
public class Movie {
    public int movieId;
    public String movieTitle;
    public String movieDescription;
    public String moviePic;
    public String movieReleaseDate;
    public String movieRating;

    /**
     * @param movieId
     * @param movieTitle
     * @param movieDescription
     * @param moviePic
     * @param movieReleaseDate
     */
    /**
     * @param movieId
     * @param movieTitle
     * @param movieDescription
     * @param moviePic
     * @param movieReleaseDate
     */

    public int getMovieId() {
        return movieId;
    }
    /**
     * @param movieId
     * @param movieTitle
     * @param movieDescription
     * @param moviePic
     * @param movieReleaseDate
     * @param movieRating
     */
    public Movie(int movieId, String movieTitle, String movieDescription, String moviePic, String movieReleaseDate,
                 String movieRating) {
        super();
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieDescription = movieDescription;
        this.moviePic = moviePic;
        this.movieReleaseDate = movieReleaseDate;
        this.movieRating = movieRating;
    }
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
    public String getMovieTitle() {
        return movieTitle;
    }
    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }
    public String getMovieDescription() {
        return movieDescription;
    }
    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }
    public String getMoviePic() {
        return moviePic;
    }
    public void setMoviePic(String moviePic) {
        this.moviePic = moviePic;
    }
    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }
    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieReleaseDate='" + movieReleaseDate + '\'' +
                ", movieTitle='" + movieTitle + '\'' +
                '}';
    }
}
