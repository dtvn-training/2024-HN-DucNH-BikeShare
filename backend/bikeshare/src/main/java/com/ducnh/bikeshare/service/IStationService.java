package com.ducnh.bikeshare.service;

import com.ducnh.bikeshare.dto.StationParamDTO;
import com.ducnh.bikeshare.model.StationHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface IStationService {
    StationHolder getStations(StationParamDTO params) throws IOException;
}
