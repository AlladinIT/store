package com.movie.store.controller;


import com.movie.store.dto.Statistics;
import com.movie.store.exception.CommonException;
import com.movie.store.service.RentedMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is rest controller for rented movies
 */
@RestController
@RequestMapping(value = "api", produces = MediaType.APPLICATION_JSON_VALUE)
public class RentedMovieController {

    private final RentedMovieService rentedMovieService;


    @Autowired
    public RentedMovieController(RentedMovieService rentedMovieService) {
        this.rentedMovieService = rentedMovieService;
    }


    /**
     * This method returns all movies that were rented.
     * @return list of rented movies in ascending order(ordered by user ID).
     * If the exception was caught, method returns exception message from service layer.
     */
    @GetMapping("rented")
    public Object getRentedMovies() {
        try {
            return rentedMovieService.getRentedMovies();
        }
        catch (CommonException ex){
            return ex.getMessage();
        }
    }


    /**
     * This method returns movies that are rented by a specified user.
     * @param userId is a user ID (required).
     * @return List of movies that a specified user has rented.
     * If the exception was caught, method returns exception message from service layer.
     */
    @GetMapping("rentedbyuser/{userId}")
    public Object getRentedMoviesByUserId(@PathVariable("userId") Long userId){
        try {
            return rentedMovieService.getRentedMoviesByUserId(userId);
        }
        catch (CommonException ex){
            return ex.getMessage();
        }
    }


    /**
     * This method returns rented movies by movie ID.
     * @param movieId is a movie ID (required).
     * @return list of rented movies by movie ID.
     * If the exception was caught, method returns exception message from service layer.
     */
    @GetMapping("rented/{movieId}")
    public Object getRentedMoviesByMovieId(@PathVariable("movieId") Long movieId){
        try {
            return rentedMovieService.getRentedMoviesByMovieId(movieId);
        }
        catch (CommonException ex){
            return ex.getMessage();
        }
    }


    /**
     * This method returns a calculated invoice with the price per movie rental and the total amount to pay.
     * @param movieIDs List of movie IDs (required).
     * @param timesInWeeks List of weeks that movies are rented (amount of weeks per each movie) (required).
     * @return a calculated invoice with the price per movie rental and the total amount to pay.
     * If the exception was caught, method returns exception message from service layer.
     *
     */
    @GetMapping("invoicecalculation")
    public Object getInvoiceCalculation(
            @RequestParam List<Long> movieIDs,
            @RequestParam List<Integer> timesInWeeks){
        try {
            return rentedMovieService.calculate(movieIDs, timesInWeeks);
        }
        catch (CommonException ex){
            return ex.getMessage();
        }
    }


    /**
     * This method adds movies that a specified user has rented.
     * @param movieIDs List of movie IDs (required).
     * @param timesInWeeks List of weeks that movies are rented (amount of weeks per each movie) (required).
     * @param userId ID of a user that rents movies (required).
     * @return a message if movies were successfully rented (added to the table rentedmovie).
     * If the exception was caught, method returns exception message from service layer.
     */
    @PostMapping("rentmovies")
    public String rentMovie(
            @RequestParam List<Long> movieIDs,
            @RequestParam List<Integer> timesInWeeks,
            @RequestParam Long userId){
        try {
            rentedMovieService.rentMovie(movieIDs, timesInWeeks, userId);
            return "Movies with IDs: " + movieIDs + " were rented for " + timesInWeeks + " weeks by user " + userId;
        }
        catch (CommonException ex){
            return ex.getMessage();
        }
    }


    /**
     * This method returns movies in descending order (from popular to unpopular).
     * @return returns movies in descending order (from the biggest amount of purchases to the smallest amount).
     */
    @GetMapping("rented/popular")
    public List<Statistics> getPopularMovies(){
        return rentedMovieService.getPopularMovies();
    }

}
