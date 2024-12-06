package com.ducnh.bikeshare.service;

import com.ducnh.bikeshare.dto.StationParamDTO;
import com.ducnh.bikeshare.dto.TripParamDTO;
import com.ducnh.bikeshare.model.Station;
import com.ducnh.bikeshare.model.StationHolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class StationServiceTest {
    @InjectMocks
    StationService stationService;

    @Test
    void whenGetAll_Pass() {
        StationHolder stationHolder = new StationHolder();
        stationHolder.setCount(101);
        stationHolder.setStatus("Success");
        List<Station> stations = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            stations.add(new Station(i, "", "", "", "", "", 0, "", 0, "", 0, 0f, "", 0, null));
        }
        stationHolder.setStations(stations);

        StationParamDTO params = new StationParamDTO("", "", "", "", "", 0, 0, 0, 0, 0f, 0f);

        StationHolder actualHolder = stationService.getStations(params);

        Assertions.assertThat(actualHolder.getCount()).isEqualTo(stationHolder.getCount());
    }
}
