package com.movie.store.repository;

import com.movie.store.dto.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
//@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void saveMovie(){
        Movie movie = Movie.builder()
                .movieTitle("Tom and Jerry")
                .actors("Tom, Jerry")
                .description("")
                .releaseDate(LocalDate.of(1995,12,27))
                .build();
        movieRepository.save(movie);

    }

    @Test
    public void printAllMovies(){
        List<Movie> movieList =movieRepository.findAll();

        System.out.println("movieList = " + movieList);
    }

}