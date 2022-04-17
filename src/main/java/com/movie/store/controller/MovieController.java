package com.movie.store.controller;


import com.movie.store.dto.Movie;
import com.movie.store.exception.CommonException;
import com.movie.store.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * This class is rest controller for movies.
 */
@RestController
@RequestMapping(value="api", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }


    /**
     * This method returns a movie with a specified title. If a title is not specified it will return all movies.
     * @param title specifies the title of the movie customer wants to find (works with a part of a title).
     * @return all movies or movies with specified title if title is specified.
     * If the exception was caught, method returns exception message from service layer.
     */

    @GetMapping("movies")
    public Object getMovies(@RequestParam(required = false) String title){
        try {
            return movieService.getMovies(title);
        }
        catch (CommonException ex){
            return ex.getMessage();
        }

    }

    /**
     * This method returns movie available in the library by its ID.
     * @param movieId specifies movie ID (required).
     * @return movie with a specified ID.
     * If the exception was caught, method returns exception message from service layer.
     */
    @GetMapping("movies/{movieId}")
    public Object getMovieById(@PathVariable("movieId") Long movieId){
        try{
            return movieService.getMovieById(movieId);
        }
        catch (CommonException ex){
            return ex.getMessage();
        }
    }

    /**
     * This method returns categories assigned to a movie with given ID.
     * @param movieId specifies movie ID (required).
     * @return categories assigned to a movie.
     * If the exception was caught, method returns exception message from service layer.
     */
    @GetMapping("movies/{movieId}/categories")
    public Object getCategoriesByMovieId(@PathVariable("movieId") Long movieId){
        try {
            return movieService.getCategoriesByMovieId(movieId);
        }
        catch (CommonException ex){
            return ex.getMessage();
        }
    }

    /**
     * This method adds a new movie to the database.
     * @param movie is a movie object that is sent through request body (required).
     * @return a message if a new movie is added.
     * If the exception was caught, method returns exception message from service layer.
     */
    @PostMapping("addmovie")
    public String addMovie(@RequestBody Movie movie){
        try {
            movieService.addMovie(movie);
            return "New movie was added to the database";
        }
        catch (CommonException ex){
            return ex.getMessage();
        }
    }

    /**
     * This method deletes a movie from the database by its ID.
     * @param movieId is movie ID (required).
     * @return a message if a movie is deleted.
     * If the exception was caught, method returns exception message from service layer.
     */
    @DeleteMapping("deletemovie/{movieId}")
    public String deleteMovie(@PathVariable("movieId") Long movieId){
        try {
            movieService.deleteMovie(movieId);
            return "Movie with ID: "+movieId+" was deleted from the database";
        }
        catch (CommonException ex){
            return ex.getMessage();
        }

    }


    /**
     * This method updates a movie in the database.
     * @param movieId ID of a movie that needs to be updated (required).
     * @param movieTitle is title of a movie.
     * @param releaseDate is a release date of a movie.
     * @param actors actors in the movie.
     * @param description is a description of the movie.
     *
     * @return a message if a movie was changed.
     * If the exception was caught, method returns exception message from service layer.
     */
    @PutMapping(path = "managemovie/{movieId}")
    public String manageMovie(
            @PathVariable("movieId") Long movieId,
            @RequestParam(required = false) String movieTitle,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate releaseDate,
            @RequestParam(required = false) String actors,
            @RequestParam(required = false) String description) {

        try {
            movieService.manageMovie(movieId,movieTitle,releaseDate,actors,description);
            return "Movie with ID: "+movieId+" was changed in the database";
        }
        catch (CommonException ex){
            return ex.getMessage();
        }

    }


}
