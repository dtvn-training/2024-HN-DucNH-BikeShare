package com.ducnh.bikeshare.service;

import com.ducnh.bikeshare.dto.StationParamDTO;
import com.ducnh.bikeshare.model.StationHolder;
import org.springframework.stereotype.Service;

@Service
public interface IStationService {
    public StationHolder getStations(StationParamDTO params);
}
