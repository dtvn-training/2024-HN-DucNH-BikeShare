package com.ducnh.bikeshare.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface IExportService {
    void exportQueryResult(String json, HttpServletResponse response) throws IOException;
    void setResponseHeader(HttpServletResponse response, String table);
}