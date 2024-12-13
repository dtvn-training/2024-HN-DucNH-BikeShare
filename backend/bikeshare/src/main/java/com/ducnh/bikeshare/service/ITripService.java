package com.ducnh.bikeshare.service;

import com.ducnh.bikeshare.dto.TripParamDTO;
import com.ducnh.bikeshare.model.TripHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ITripService {
    public TripHolder getTrips(TripParamDTO params) throws IOException;
}