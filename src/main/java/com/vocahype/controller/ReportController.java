package com.vocahype.controller;


import com.vocahype.dto.ResponseEntityJsonApi;
import com.vocahype.service.ReportService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping(Routing.REPORT)
    ResponseEntityJsonApi getReport(@RequestParam(name = "dateStart") Long dateStart,
                                     @RequestParam(name = "dateEnd") Long dateEnd) {
        return new ResponseEntityJsonApi(reportService.getReport(dateStart, dateEnd));
    }
}
