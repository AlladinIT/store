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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * This class is a service layer
 */
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final MovieRepository movieRepository;

    @Autowired
    public CategoryService(MovieRepository movieRepository,CategoryRepository categoryRepository) {
        this.movieRepository = movieRepository;
        this.categoryRepository = categoryRepository;
    }


    /**
     * This method returns all categories from the database.
     * @return a list of categories in ascending order (ordered by category ID).
     * @throws CommonException if category list is empty.
     *
     */
    @Transactional
    public List<Category> getCategories()throws CommonException{
        List<Category> categories = new ArrayList<>();
        categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "categoryId"))
                .forEach(categories::add);
        if (categories.isEmpty()){
            throw new CommonException("Category list is empty");
        }
        else
        return categories;
    }


    /**
     * This method returns list of movies assigned to a specified category by category ID.
     * @param categoryId is a category ID(required).
     * @return List of movies assigned to a specified category.
     * @throws CommonException if category with given ID does not exist.
     * @throws CommonException if there was no movie with given category ID.
     */
    public List<Movie> getMoviesByCategoryId(Long categoryId)throws CommonException {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CommonException("Not found Category with id = " + categoryId);
        }
        Category category = categoryRepository.findCategoryByCategoryId(categoryId);

        List<Movie> movies = movieRepository.findMoviesByCategoriesOrderByMovieId(category);
        if (movies.isEmpty()){
            throw new CommonException("No movie with categoryId: "+categoryId);
        }
        else{
            return movies;
        }

    }


    /**
     * This method adds new category to the database.
     * @param category is a name of the category (required).
     * @throws CommonException if category with given ID already exists.
     * @throws CommonException if category with given name already exists.
     */
    @Transactional
    public void addCategory(Category category)throws CommonException {
        Optional<Category> categoryByName = categoryRepository.findByCategoryName(category.getCategoryName());

        if(category.getCategoryId() != null){
            Optional<Category> categoryById = categoryRepository.findById(category.getCategoryId());
            if (categoryById.isPresent()){
                throw new CommonException("Category with ID: "+category.getCategoryId()+" already exists");
            }

        }

        if (categoryByName.isPresent()){
            throw new CommonException("Category with name: "+category.getCategoryName()+" already exists");
        }
        else{
            categoryRepository.save(category);
        }
    }


    /**
     * This method deletes category from the database by its ID.
     * @param categoryId is a category ID (required).
     * @throws CommonException if category with given ID does not exist.
     * @throws CommonException if given category is connected to a movie
     */
    @Transactional
    public void deleteCategory(Long categoryId)throws CommonException {
        boolean exists = categoryRepository.existsById(categoryId);

        if(!exists){
            throw new CommonException("Category with id " +categoryId+ " does not exist");
        }

        Category category = categoryRepository.findCategoryByCategoryId(categoryId);

        List<Movie> movieListByCategoryId = movieRepository.findMoviesByCategories(category);
        if(!movieListByCategoryId.isEmpty()){
            throw new CommonException("You can not delete category that is connected to a movie");
        }
        categoryRepository.deleteById(categoryId);

    }


    /**
     * This method changes category name in the database by ID
     * @param categoryId is a category ID (required).
     * @param categoryName is a new category name (required).
     * @throws CommonException if category with given ID does not exist.
     * @throws CommonException if category name is not specified
     * @throws CommonException if category name is the same as it was before
     */
    @Transactional
    public void manageCategory(Long categoryId, String categoryName)throws CommonException {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CommonException(
                        "Category with id " +categoryId+ " does not exist"));


        if (ObjectUtils.isEmpty(categoryName)){
            throw new CommonException("Please specify categoryName for further managing");
        }

        if(category.getCategoryName().equals(categoryName)){
            throw new CommonException("The category name is the same as it was before");
        }

        else
            category.setCategoryName(categoryName);

    }


    /**
     * This method assigns new category to a movie
     * @param categoryId is a category ID (required).
     * @param movieId is a movie ID (required).
     * @throws CommonException if category with given ID does not exist.
     * @throws CommonException if Movie with given ID does not exist.
     * @throws CommonException if Movie with given ID already has category with given ID.
     */
    public void addCategoryToMovie(Long categoryId, Long movieId)throws CommonException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CommonException(
                        "Category with id " +categoryId+ " does not exist"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new CommonException(
                        "Movie with id "+movieId+" does not exist"
                ));

        boolean alreadyExists = movie.getCategories().stream().
                anyMatch(cat -> cat.getCategoryId().equals(categoryId));
        if(alreadyExists){
            throw new CommonException("Movie with ID: "+movieId+ " already has category with ID: "+categoryId);
        }
        movie.getCategories().add(category);
        movieRepository.save(movie);



    }
}
