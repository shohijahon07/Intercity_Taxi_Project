package org.example.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping(value = {
            "/{path:^(?!api|assets).*$}",
            "/{path:^(?!api|assets).*$}/**"
    })
    public String forward() {
        return "forward:/index.html";
    }
}
