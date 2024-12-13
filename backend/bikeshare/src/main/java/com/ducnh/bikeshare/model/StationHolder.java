package com.ducnh.bikeshare.model;

import lombok.Data;

import java.util.List;

@Data
public class StationHolder {
    private Integer count;
    private String status;
    private List<Station> stations;
}
