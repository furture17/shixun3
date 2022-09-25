package sx5.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sx5.service.DemoService;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Resource
    private DemoService demoService;

    @RequestMapping("/demo")
    public String demo() {
        demoService.demo();
        return "This is a demo!";
    }
}
