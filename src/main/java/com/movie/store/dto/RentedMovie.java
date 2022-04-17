package com.movie.store.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static javax.persistence.GenerationType.SEQUENCE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "RentedMovie")
@Builder
@Table(name = "rentedmovie")
public class RentedMovie {
    @Id
    @SequenceGenerator(
            name = "rentedmovie_sequence",
            sequenceName = "rentedmovie_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "rentedmovie_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;


    @Column(name = "user_id", nullable = false)
    private Long userId;


    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;


    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;


    @Column(name = "rental_price", nullable = false)
    private BigDecimal rentalPrice;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(
            name = "movie_id"
    )
    private Movie movie;

    public RentedMovie(LocalDate startDate, LocalDate endDate, Movie movie) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.movie = movie;
    }

    public RentedMovie(Long userId, LocalDate startDate, LocalDate endDate, BigDecimal rentalPrice, Movie movie) {
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentalPrice = rentalPrice;
        this.movie = movie;
    }
}
