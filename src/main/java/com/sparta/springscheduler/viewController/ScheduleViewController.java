package com.sparta.springscheduler.viewController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ScheduleViewController {

    @GetMapping("/view")
    public String view(Model model) {
        return "viewSchedule";
    }

}
