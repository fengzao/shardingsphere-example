package org.weexps.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @date 2020/6/16 13:44
 */
@RestController
public class IndexController {
    @RequestMapping("/index")
    public HashMap index() {
        return new HashMap();
    }

}
