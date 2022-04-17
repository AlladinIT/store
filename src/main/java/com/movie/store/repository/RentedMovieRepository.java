package com.movie.store.repository;

import com.movie.store.dto.Movie;
import com.movie.store.dto.RentedMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentedMovieRepository extends JpaRepository<RentedMovie,Long> {

    boolean existsByUserId(Long userId);

    List<RentedMovie> findByUserIdOrderByMovie(Long userId);


    List<RentedMovie> findByMovie(Movie movie);


    long countByUserIdAndMovie(Long userId, Movie movie);

    @Query(value = "SELECT r.movie.movieId, r.movie.movieTitle, COUNT(r.movie.movieId) AS purchases FROM RentedMovie r GROUP BY r.movie.movieId, r.movie.movieTitle ORDER BY purchases DESC")
    List<List> findMostPopularMovies();


}
