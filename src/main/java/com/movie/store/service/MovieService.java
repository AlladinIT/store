package com.movie.store.service;

import com.movie.store.dto.Category;
import com.movie.store.dto.Movie;
import com.movie.store.exception.CommonException;
import com.movie.store.repository.CategoryRepository;
import com.movie.store.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * This class is a service layer
 */
@Service
public class MovieService {
    private final CategoryRepository categoryRepository;

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository,CategoryRepository categoryRepository) {
        this.movieRepository = movieRepository;
        this.categoryRepository = categoryRepository;
    }


    /**
     * This method returns a movie with a specified title. If a title is not specified it will return all movies.
     * @param movieTitle specifies the title of the movie customer wants to find (works with a part of a title).
     * @return all movies or movies with specified title if title is specified.
     * @throws CommonException if Movie list is empty (no movies in the database).
     * @throws CommonException if there is no such movie with a given title (if title is specified).
     */
    @Transactional
    public List<Movie> getMovies(String movieTitle)throws CommonException {
        List<Movie> movies = new ArrayList<>();
        int flag = 0;
        if(movieTitle == null){
            movieRepository.findAll(Sort.by(Sort.Direction.ASC, "movieId"))
                    .forEach(movies::add);
            flag = 1;
        }
        else{
            movieTitle = movieTitle.toUpperCase();
            movieRepository.findByMovieTitleContainingIgnoreCase(movieTitle).forEach(movies::add);
            flag = 2;
        }

        if (movies.isEmpty() && flag==1){
            throw new CommonException("Movie list is empty");
        }
        else if(movies.isEmpty() && flag==2){
            throw new CommonException("No such movie with title: "+movieTitle);
        }
        else{
            return movies;
        }

    }


    /**
     * This method returns movie available in the library by its ID.
     * @param movieId specifies movie ID (required).
     * @return movie with a specified ID.
     * @throws CommonException if Movie with given ID does not exist.
     */
    @Transactional
    public Movie getMovieById(Long movieId)throws CommonException {
        return movieRepository.findById(movieId)
               .orElseThrow(() -> new CommonException(
                    "Movie with id " + movieId + " does not exist"));
    }


    /**
     * This method returns categories assigned to a movie with given ID.
     * @param movieId specifies movie ID (required).
     * @return categories assigned to a movie.
     * @throws CommonException if movie with given ID was not found.
     * @throws CommonException if there were no categories assigned to a movie.
     */
    @Transactional
    public List<Category> getCategoriesByMovieId(Long movieId)throws CommonException {
        if (!movieRepository.existsById(movieId)) {
            throw new CommonException("Not found Movie with id = " + movieId);
        }
        Movie movie = movieRepository.findMovieByMovieId(movieId);

        List<Category> categories = categoryRepository.findCategoriesByMovies(movie);

        if (categories.isEmpty()){
            throw new CommonException("No category with movieId: "+movieId);
        }
        else{
            return categories;
        }
    }


    /**
     * This method adds a new movie to the database.
     * @param movie is a movie object that is sent through request body (required).
     * @throws CommonException if movie with given title already exists.
     */
    @Transactional
    public void addMovie(Movie movie)throws CommonException {
        Optional<Movie> movieByTitle = movieRepository
                .findByMovieTitle(movie.getMovieTitle());
        if (movieByTitle.isPresent()){
            throw new CommonException("Movie with given title already exists");
        }
        else{
            movieRepository.save(movie);
        }
    }


    /**
     * This method deletes a movie from the database by its ID.
     * @param movieId is movie ID (required).
     * @throws CommonException if movie with given id does not exist.
     */
    @Transactional
    public void deleteMovie(Long movieId)throws CommonException {
        boolean exists = movieRepository.existsById(movieId);
        if(!exists){
            throw new CommonException("Movie with id " + movieId + " does not exist");
        }
        movieRepository.deleteById(movieId);
    }


    /**
     * This method updates a movie in the database.
     * @param movieId ID of a movie that needs to be updated (required).
     * @param movieTitle is a title of a movie.
     * @param releaseDate is a release date of a movie.
     * @param actors are actors in the movie.
     * @param description is a description of the movie.
     *
     * @throws CommonException if movie with given ID does not exist.
     */
    @Transactional
    public void manageMovie(Long movieId,
                            String movieTitle,
                            LocalDate releaseDate,
                            String actors,
                            String description) throws CommonException{
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new CommonException(
                        "Movie with id " + movieId + " does not exist"));


        if (!ObjectUtils.isEmpty(movieTitle)){
            movie.setMovieTitle(movieTitle);
        }

        if(releaseDate != null && releaseDate.isBefore(LocalDate.now())){
            movie.setReleaseDate(releaseDate);
        }

        if (!ObjectUtils.isEmpty(actors)){
            movie.setActors(actors);
        }

        if (!ObjectUtils.isEmpty(description)){
            movie.setDescription(description);
        }

    }



}
