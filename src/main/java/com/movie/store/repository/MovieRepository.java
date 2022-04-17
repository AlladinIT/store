package com.movie.store.repository;

import com.movie.store.dto.Category;
import com.movie.store.dto.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {


    //for getMovies
    @Query(value = "SELECT m FROM Movie m WHERE upper(m.movieTitle) LIKE %?1%")
    List<Movie> findByMovieTitleContainingIgnoreCase(String title);


    //for addMovie
    //@Query("SELECT m FROM Movie m WHERE m.movieTitle = ?1") //JPQL query based on the classes I created
    Optional<Movie> findByMovieTitle(String title);


    List<Movie> findMoviesByCategories(Category category);


    List<Movie> findMoviesByCategoriesOrderByMovieId(Category category);

    Movie findMovieByMovieId(Long movieId);

    boolean existsByMovieId(Long movieId);



}
