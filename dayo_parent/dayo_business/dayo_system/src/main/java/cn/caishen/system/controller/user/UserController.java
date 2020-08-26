package cn.caishen.system.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LB
 */
@RestController
public class UserController {

    @RequestMapping(value = "/test")
    public String test(){
        return "111";
    }
}
