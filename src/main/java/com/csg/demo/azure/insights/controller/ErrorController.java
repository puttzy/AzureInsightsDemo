package com.csg.demo.azure.insights.controller;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.EventTelemetry;
import com.microsoft.applicationinsights.telemetry.ExceptionTelemetry;
import com.microsoft.applicationinsights.telemetry.Telemetry;
import com.microsoft.applicationinsights.telemetry.TelemetryContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Dan on 3/10/2015.
 */

@Controller
@RequestMapping("/error")
public class ErrorController {

    private static TelemetryClient client = new TelemetryClient();

    @RequestMapping(value="/404")
    public String handle404(ModelMap model) {
        model.addAttribute("message", "404  Page not found");
        return "/static/errors/404";
    }

}
