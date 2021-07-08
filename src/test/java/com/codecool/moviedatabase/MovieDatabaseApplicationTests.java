package com.codecool.moviedatabase;

import com.codecool.moviedatabase.testmodel.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MovieDatabaseApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private Integer port;

    private static final String DIRECTOR_1 = "Director One";
    private static final String DIRECTOR_2 = "Director Two";
    private static final List<Movie> MOVIES_1 = List.of(
            new Movie("Title 1", 5.0, DIRECTOR_1),
            new Movie("Title 2", 5.7, DIRECTOR_1),
            new Movie("Title 3", 4.2, DIRECTOR_1),
            new Movie("Title 4", 8.0, DIRECTOR_1),
            new Movie("Title 5", 7.8, DIRECTOR_1)
    );
    private static final List<Movie> MOVIES_2 = List.of(
            new Movie("Title 6", 9.0, DIRECTOR_2),
            new Movie("Title 7", 3.7, DIRECTOR_2),
            new Movie("Title 8", 6.2, DIRECTOR_2),
            new Movie("Title 9", 5.0, DIRECTOR_2),
            new Movie("Title 0", 6.8, DIRECTOR_2)
    );

    private void putMovie(Movie movie) {
        String url = "http://localhost:" + port + "/movie/add";
        restTemplate.put(url, movie);
    }

    @Test
    void noData_moviesByDirector_emptyListReturned() {
        String url = "http://localhost:" + port + "/movie/list?director=Does Not Matter Who";

        String result = restTemplate.getForObject(url, String.class);

        assertEquals("[]", result);
    }

    @Test
    void noData_allDirectors_expectedMessageReturned() {
        String url = "http://localhost:" + port + "/director/list/all";

        String result = restTemplate.getForObject(url, String.class);

        assertEquals("[\"There are no directors yet in the database\"]", result);
    }

    @Test
    void oneMovieAdded_allDirectors_directorReturned() {
        putMovie(new Movie("Movie Title", 7.0, DIRECTOR_1));
        String url = "http://localhost:" + port + "/director/list/all";

        String[] result = restTemplate.getForObject(url, String[].class);

        assertEquals(1, result.length);
        assertEquals(DIRECTOR_1, result[0]);
    }

    @Test
    void oneMovieAdded_moviesBySameDirector_movieReturned() {
        Movie movie = new Movie("Movie Title", 7.0, DIRECTOR_1);
        putMovie(movie);
        String url = "http://localhost:" + port + "/movie/list?director=" + DIRECTOR_1;

        Movie[] result = restTemplate.getForObject(url, Movie[].class);

        assertEquals(1, result.length);
        assertEquals(movie, result[0]);
    }

    @Test
    void oneMovieAdded_moviesByOtherDirector_emptyArrayReturned() {
        Movie movie = new Movie("Movie Title", 7.0, DIRECTOR_1);
        putMovie(movie);
        String url = "http://localhost:" + port + "/movie/list?director=" + DIRECTOR_2;

        Movie[] result = restTemplate.getForObject(url, Movie[].class);

        assertEquals(0, result.length);
    }

    @Test
    void sameMovieAddedTwice_moviesByDirector_movieReturnedOnce() {
        Movie movie = new Movie("Movie Title", 7.0, DIRECTOR_1);
        putMovie(movie);
        putMovie(movie);
        String url = "http://localhost:" + port + "/movie/list?director=" + DIRECTOR_1;

        Movie[] result = restTemplate.getForObject(url, Movie[].class);

        assertEquals(1, result.length);
        assertEquals(movie, result[0]);
    }

    @Test
    void manyMoviesAdded_allDirectors_eachReturnedOnce() {
        MOVIES_1.forEach(this::putMovie);
        MOVIES_2.forEach(this::putMovie);
        String url = "http://localhost:" + port + "/director/list/all";

        String[] result = restTemplate.getForObject(url, String[].class);
        List<String> resultAsList = Arrays.asList(result);

        assertEquals(2, result.length);
        assertTrue(resultAsList.contains(DIRECTOR_1) && resultAsList.contains(DIRECTOR_2));
    }

    @Test
    void manyMoviesAdded_moviesByDirector_moviesInCorrectOrder() {
        MOVIES_1.forEach(this::putMovie);
        String url = "http://localhost:" + port + "/movie/list?director=" + DIRECTOR_1;
        List<Movie> sortedMovies = MOVIES_1.stream().sorted().collect(Collectors.toList());

        Movie[] result = restTemplate.getForObject(url, Movie[].class);
        List<Movie> resultAsList = Arrays.asList(result);

        for(int i = 0; i < sortedMovies.size(); i++) {
            assertEquals(resultAsList.get(i), sortedMovies.get(i));
        }
    }

    @Test
    void manyMoviesAddedTwice_moviesByDirector_eachMovieReturnedOnce() {
        MOVIES_1.forEach(this::putMovie);
        MOVIES_1.forEach(this::putMovie);
        String url = "http://localhost:" + port + "/movie/list?director=" + DIRECTOR_1;

        Movie[] result = restTemplate.getForObject(url, Movie[].class);
        List<Movie> resultAsList = Arrays.asList(result);

        assertEquals(MOVIES_1.size(), resultAsList.size());
        for (Movie movie : MOVIES_1) {
            assertTrue(resultAsList.contains(movie));
        }
    }
}
