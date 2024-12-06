package com.ducnh.bikeshare.service;

import com.ducnh.bikeshare.dto.StationParamDTO;
import com.ducnh.bikeshare.dto.TripParamDTO;
import com.ducnh.bikeshare.model.Station;
import com.ducnh.bikeshare.model.StationHolder;
import com.ducnh.bikeshare.model.Trip;
import com.ducnh.bikeshare.model.TripHolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {
    @InjectMocks
    TripService tripService;

    @Test
    void whenGetAll_Pass() {
        TripHolder tripHolder = new TripHolder();
        tripHolder.setCount(1);
        tripHolder.setStatus("Success");
        List<Trip> trips = new ArrayList<>();
        trips.add(new Trip("", "", "", "", null, "", "", 0));
        tripHolder.setTrips(trips);

        TripParamDTO params = new TripParamDTO(0, 0, "26357247", "", "", "", "", "", 0, 0, "", "");

        TripHolder actualHolder = tripService.getTrips(params);

        Assertions.assertThat(actualHolder.getCount()).isEqualTo(tripHolder.getCount());
    }
}
