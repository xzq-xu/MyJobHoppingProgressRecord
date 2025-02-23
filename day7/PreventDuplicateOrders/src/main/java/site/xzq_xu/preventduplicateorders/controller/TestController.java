package site.xzq_xu.preventduplicateorders.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.xzq_xu.preventduplicateorders.annotation.AntiRepeatOrder;


@RequestMapping("/test")
@RestController
public class TestController {

    @AntiRepeatOrder(lockTime = 1000)
    @GetMapping("/hello")
    public String hello() throws InterruptedException {
        Thread.sleep(1000*5);
        return "hello world";
    }

}
