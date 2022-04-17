package com.movie.store.dto;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.time.temporal.ChronoUnit.WEEKS;
import static javax.persistence.GenerationType.SEQUENCE;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movies")
@Builder
public class Movie {

    @Id
    @SequenceGenerator(
            name = "movie_sequence",
            sequenceName = "movie_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "movie_sequence"
    )

    @Column(name = "movie_id", updatable = false)
    private Long movieId;

    @Column(name = "movie_title", nullable = false)
    private String movieTitle;


    @Transient
    private BigDecimal pricePerWeek;

    @Transient
    private String currency;

    @Transient
    private String priceClass;


    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;


    @Column(name = "actors", nullable = false, columnDefinition = "TEXT")
    private String actors;


    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;



    @ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.EAGER) //owning side
    @JoinTable(
            name = "movie_categories",//join/link table(movie_categories)
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    Set<Category> categories = new HashSet<>();


    public Movie(String movieTitle,
                 LocalDate releaseDate,
                 String actors,
                 String description,
                 Set<Category> categories) {
        this.movieTitle = movieTitle;
        this.releaseDate = releaseDate;
        this.actors = actors;
        this.description = description;
        this.categories = categories;
    }

    public Movie(String movieTitle,
                 LocalDate releaseDate,
                 String actors,
                 String description) {
        this.movieTitle = movieTitle;
        this.releaseDate = releaseDate;
        this.actors = actors;
        this.description = description;
    }



    public BigDecimal getPricePerWeek(){
        long weeks = WEEKS.between(this.releaseDate,LocalDate.now());
        if(weeks <= 52){
            return BigDecimal.valueOf(5);
        }
        else if(weeks<156){
            return BigDecimal.valueOf(3.49);
        }
        else{
            return BigDecimal.valueOf(1.99);
        }

    }


    public String getCurrency() {
        return "EUR";
    }

    public String getPriceClass() {
        long weeks = WEEKS.between(this.releaseDate,LocalDate.now());
        if(weeks <= 52){
            return "New movie";
        }
        else if(weeks<156){
            return "Regular movie";
        }
        else{
            return "Old movie";
        }
    }
}


