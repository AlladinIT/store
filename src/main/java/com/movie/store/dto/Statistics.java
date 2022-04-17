package com.movie.store.dto;

import lombok.Data;

@Data
public class Statistics {
    private String movieTitle;
    private Long movieId;
    private Long purchases;
}
