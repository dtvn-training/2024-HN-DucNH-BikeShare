package com.ducnh.bikeshare.controller;

import com.ducnh.bikeshare.constant.Constant;
import com.ducnh.bikeshare.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/export")
@CrossOrigin(origins = Constant.FRONTEND_URL)
public class ExportController {
    @Autowired
    ExportService exportService;

    @PostMapping("/{table}")
    public void export(@RequestBody String json,
                       @PathVariable String table,
                       HttpServletResponse response) throws IOException {
        json = exportService.editJSONInput(json);
        exportService.exportQueryResult(json, response, table);
    }
}
