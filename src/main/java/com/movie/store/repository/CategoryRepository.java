package com.movie.store.repository;

import com.movie.store.dto.Category;
import com.movie.store.dto.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Category findCategoryByCategoryId(Long categoryId);


    List <Category> findCategoriesByMovies(Movie movie);



    Optional<Category> findByCategoryName(String categoryName);


}
