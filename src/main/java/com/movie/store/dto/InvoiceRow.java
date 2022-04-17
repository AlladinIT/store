package com.movie.store.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceRow {
    private Movie movie;
    private Integer rentingTimeInWeeks;
    private BigDecimal pricePerMovieRental;
}
