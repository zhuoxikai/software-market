package com.sicau.controller;

import com.sicau.constants.CommonConstants;
import com.sicau.entity.dto.Delay;
import com.sicau.entity.pojo.vo.ResultVO;
import com.sicau.service.TestService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * TestController
 * @author chenshihang on 2019/1/21
 */
@RestController
public class TestController {

    @Autowired
    private TestService userService;

    @RequestMapping(CommonConstants.PUB_PREFIX+"/test")
    public ResultVO test(){
        ResultVO resultVO = userService.login();
        return resultVO;
    }

    @RequestMapping("/xt_test")
    public ResultVO test2(){




        return  userService.xt_test();
    }


    public static void main(String[] args) {
        List<String> listA = new ArrayList<String>();        listA.add("A");  listA.add("v");
        List<String> listB = new ArrayList<String>();        listB.add("B");
        List<String> listFinal = new ArrayList<String>();
        listFinal.addAll(listA);
        listFinal.addAll(listB);
        System.out.println(listFinal.toString());
    }



}
