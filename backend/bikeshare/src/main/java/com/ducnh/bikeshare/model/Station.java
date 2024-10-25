package com.ducnh.bikeshare.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
//    private LocalDateTime modified_date;

//    public Station(Integer station_id, String name, String status, String location, String address, String alternate_name, Integer city_asset_number, String property_type, Integer number_of_docks, String power_type, Integer footprint_length, Float footprint_width, String notes, Integer council_district) {
//        this.station_id = station_id;
//        this.name = name;
//        this.status = status;
//        this.location = location;
//        this.address = address;
//        this.alternate_name = alternate_name;
//        this.city_asset_number = city_asset_number;
//        this.property_type = property_type;
//        this.number_of_docks = number_of_docks;
//        this.power_type = power_type;
//        this.footprint_length = footprint_length;
//        this.footprint_width = footprint_width;
//        this.notes = notes;
//        this.council_district = council_district;
////        this.modified_date = modified_date;
//    }
}
