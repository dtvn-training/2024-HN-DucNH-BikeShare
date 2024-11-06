package com.ducnh.bikeshare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StationParamDTO {
    private String name;
    private String status;
    private String address;
    private String power_type;
    private String property_type;
    private int min_docks;
    private int max_docks;
    private int min_length;
    private int max_length;
    private float min_width;
    private float max_width;
}
