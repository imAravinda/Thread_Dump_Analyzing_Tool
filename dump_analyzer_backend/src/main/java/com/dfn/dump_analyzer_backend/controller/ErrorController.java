package com.dfn.dump_analyzer_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class ErrorController {
    @RequestMapping(value = "/**/{path:[^\\.]*}",method = RequestMethod.GET)
    public void forward(HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
    }
}
