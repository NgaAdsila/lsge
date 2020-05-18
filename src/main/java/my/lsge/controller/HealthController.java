package my.lsge.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @RequestMapping(
            value = "",
            method = RequestMethod.GET
    )
    public String check() {
        return "Hello World!";
    }
}
