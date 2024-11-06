package com.ducnh.bikeshare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripParamDTO {
    private int offset;
    private String trip_id;
    private String subscriber_type;
    private String bike_id;
    private String bike_type;
    private String start_station_name;
    private String end_station_name;
    private int min_duration;
    private int max_duration;
    private String min_start_time;
    private String max_start_time;
}
