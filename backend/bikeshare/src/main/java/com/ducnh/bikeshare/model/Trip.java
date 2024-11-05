package com.ducnh.bikeshare.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    private String trip_id;
    private String subscriber_type;
    private String bike_id;
    private String bike_type;
    private LocalDateTime start_time;
//    private Integer start_station_id;
    private String start_station_name;
//    private String end_station_id;
    private String end_station_name;
    private Integer duration_minutes;
}
