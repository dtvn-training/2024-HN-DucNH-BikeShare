package com.ducnh.bikeshare.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface IExportService {
    void exportQueryResult(String json, HttpServletResponse response, String table) throws IOException;
    <P> void exportToExcel(List<P> data, HttpServletResponse response) throws IOException;
}
