package com.ducnh.bikeshare.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Station {
    private Integer station_id;
    private String name;
    private String status;
    private String location;
    private String address;
    private String alternate_name;
    private Integer city_asset_number;
    private String property_type;
    private Integer number_of_docks;
    private String power_type;
    private Integer footprint_length;
    private Float footprint_width;
    private String notes;
    private Integer council_district;
    private LocalDateTime modified_date;
}
