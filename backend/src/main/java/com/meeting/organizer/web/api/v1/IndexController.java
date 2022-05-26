package com.meeting.organizer.web.api.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class IndexController {

    @GetMapping({"", "/", "/account/login", "/account/registration/register"})
    public String index() {
        return "forward:/index.html";
    }

}