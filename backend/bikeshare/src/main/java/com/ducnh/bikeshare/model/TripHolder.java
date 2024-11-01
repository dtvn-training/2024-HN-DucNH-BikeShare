package com.ducnh.bikeshare.model;

import lombok.Data;

import java.util.List;

@Data
public class TripHolder {
    private Integer count;
    private String status;
    private List<Trip> trips;
}
