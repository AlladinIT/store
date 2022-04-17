package com.movie.store.config;


import com.movie.store.dto.Category;
import com.movie.store.dto.Movie;
import com.movie.store.repository.CategoryRepository;
import com.movie.store.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

/**
 * This class is for configuration
 *
 * It automatically fills columns in category table, movie table and movie_categories table with data
 * If there is already some data in the columns, it will not save anything to the tables
 *
 */
@Configuration
public class MovieConfig {

    @Bean
    CommandLineRunner commandLineRunner1(MovieRepository movieRepository,CategoryRepository categoryRepository){
        return args -> {

            Category category1 = new Category(
                    "Action"
            );
            Category category2 = new Category(
                    "Adventure"
            );
            Category category3 = new Category(
                    "Comedy"
            );
            Category category4 = new Category(
                    "Fantasy"
            );
            Category category5 = new Category(
                    "Drama"
            );
            Category category6 = new Category(
                    "Sci-fi"
            );
            Category category7 = new Category(
                    "Mystery"
            );


            if (categoryRepository.count() == 0){
                categoryRepository.saveAll(
                        List.of(category1,category2,category3,category4,category5,category6,category7)
                );
            }




            Movie movie1 = new Movie(
                    "Guardians of the Galaxy Vol. 2",
                    LocalDate.of(2017, 5, 5),
                    "Chris Pratt, Zoe Saldana, Dave Bautista",
                    "The Guardians struggle to keep together as a team while dealing with their personal family issues, notably Star-Lord's encounter with his father the ambitious celestial being Ego."
            );
            movie1.getCategories().add(category1);
            movie1.getCategories().add(category2);
            movie1.getCategories().add(category3);

            Movie movie2 = new Movie(
                    "Star Wars",
                    LocalDate.of(1977,5,25),
                    "Mark Hamill, Harrison Ford, Carrie Fisher",
                    "Luke Skywalker joins forces with a Jedi Knight, a cocky pilot, a Wookiee and two droids to save the galaxy from the Empire's world-destroying battle station, while also attempting to rescue Princess Leia from the mysterious Darth Vader"
            );
            movie2.getCategories().add(category1);
            movie2.getCategories().add(category2);
            movie2.getCategories().add(category4);

            Movie movie3 = new Movie(
                    "Pirates of the Caribbean: The Curse of the Black Pearl",
                    LocalDate.of(2003,7,9),
                    "Johnny Depp, Geoffrey Rush, Orlando Bloom",
                    "Blacksmith Will Turner teams up with eccentric pirate \"Captain\" Jack Sparrow to save his love, the governor's daughter, from Jack's former pirate allies, who are now undead."
            );

            movie3.getCategories().add(category1);
            movie3.getCategories().add(category2);
            movie3.getCategories().add(category3);

            Movie movie4 = new Movie(
                    "The Lord of the Rings: The Fellowship of the Ring",
                    LocalDate.of(2001,12,19),
                    "Elijah Wood, Ian McKellen, Orlando Bloom",
                    "A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron."
            );
            movie4.getCategories().add(category1);
            movie4.getCategories().add(category2);
            movie4.getCategories().add(category4);

            Movie movie5 = new Movie(
                    "I, Robot",
                    LocalDate.of(2004, 7,16),
                    "Will Smith, Bridget Moynahan, Bruce Greenwood",
                    "In 2035, a technophobic cop investigates a crime that may have been perpetrated by a robot, which leads to a larger threat to humanity."
            );
            movie5.getCategories().add(category1);
            movie5.getCategories().add(category6);
            movie5.getCategories().add(category7);



            if (movieRepository.count() == 0){
                movieRepository.saveAll(
                        List.of(movie1,movie2,movie3,movie4,movie5)
                );
            }
        };
    }



}
