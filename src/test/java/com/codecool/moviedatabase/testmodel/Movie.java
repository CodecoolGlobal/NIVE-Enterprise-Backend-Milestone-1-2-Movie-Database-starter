package com.codecool.moviedatabase.testmodel;

import java.util.Objects;

public class Movie implements Comparable<Movie> {
    private String title;
    private Double rating;
    private String director;

    public Movie(String title, Double rating, String director) {
        this.title = title;
        this.rating = rating;
        this.director = director;
    }

    public String getDirector() {
        return director;
    }

    public Double getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int compareTo(Movie movie) {
        return Double.compare(movie.rating, this.rating);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return title.equals(movie.title) && director.equals(movie.director);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, director);
    }
}
