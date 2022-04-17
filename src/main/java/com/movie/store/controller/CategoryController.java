package com.movie.store.controller;


import com.movie.store.dto.Category;
import com.movie.store.exception.CommonException;
import com.movie.store.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


/**
 * This class is Rest Controller for categories
 */
@RestController
@RequestMapping(value = "api", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * This method returns all categories from the database.
     * @return a list of categories in ascending order (ordered by category ID).
     * If the exception was caught, method returns exception message from service layer.
     */
    @GetMapping("categories")
    public Object getCategories(){
        try {
            return categoryService.getCategories();
        }
        catch (CommonException ex){
            return ex.getMessage();
        }
    }

    /**
     * This method returns list of movies assigned to a specified category by category ID.
     * @param categoryId is a category ID(required).
     * @return List of movies assigned to a specified category.
     * If the exception was caught, method returns exception message from service layer.
     */
    @GetMapping("categories/{categoryId}/movies")
    public Object getMoviesByCategoryId(@PathVariable("categoryId") Long categoryId){
        try {
            return categoryService.getMoviesByCategoryId(categoryId);
        }
        catch (CommonException ex){
            return ex.getMessage();
        }
    }


    /**
     * This method adds new category to the database.
     * @param category is a name of the category (required).
     * @return a message if a category was added.
     * If the exception was caught, method returns exception message from service layer.
     */
    @PostMapping("addcategory")
    public String addCategory(@RequestBody Category category){
        try {
            categoryService.addCategory(category);
            return "New category was added to the database";
        }
        catch (CommonException ex){
            return ex.getMessage();
        }
    }


    /**
     * This method deletes category from the database by its ID.
     * @param categoryId is a category ID (required).
     * @return a message if a category was deleted.
     * If the exception was caught, method returns exception message from service layer.
     */
    @DeleteMapping("deletecategory/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Long categoryId){
        try {
            categoryService.deleteCategory(categoryId);
            return "Category with ID: " + categoryId + " was deleted from the database";
        }
        catch (CommonException ex){
            return ex.getMessage();
        }
    }


    /**
     * This method changes category name in the database by ID
     * @param categoryId is a category ID (required).
     * @param categoryName is a new category name (required).
     * @return a message if a category name was changed.
     * If the exception was caught, method returns exception message from service layer.
     */
    @PutMapping(path = "managecategory/{categoryId}")
    public String manageCategory(
            @PathVariable("categoryId") Long categoryId,
            @RequestParam String categoryName){
        try {
            categoryService.manageCategory(categoryId, categoryName);
            return "Category with ID: " + categoryId + " was changed in the database";
        }
        catch (CommonException ex){
            return ex.getMessage();
        }
    }



    /**
     * This method assigns new category to a movie.
     * @param categoryId is a category ID (required).
     * @param movieId is a movie ID (required).
     * @return a message if a category was assigned to a movie.
     * If the exception was caught, method returns exception message from service layer.
     */
    @PostMapping("/addcategory/{categoryId}/tomovie/{movieId}")
    public String addCategoryToMovie(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("movieId") Long movieId) {
        try {
            categoryService.addCategoryToMovie(categoryId, movieId);
            return "Category with ID: " + categoryId + " was added to movie with ID " + movieId;
        }
        catch (CommonException ex){
            return ex.getMessage();
        }
    }
}
