package com.movie.store.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @SequenceGenerator(
            name = "category_sequence",
            sequenceName = "category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "category_sequence"
    )
    @Column(name = "category_id", updatable = false)
    private Long categoryId;


    @Column(name = "category_name", nullable = false, columnDefinition = "TEXT")
    private String categoryName;


    @ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.EAGER,mappedBy = "categories")//inverse side
    @JsonIgnore
    private Set<Movie> movies = new HashSet<>();


    public Category(String categoryName, Set<Movie> movies) {
        this.categoryName = categoryName;
        this.movies = movies;
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }


}
