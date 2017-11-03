package com.github.ittalks.fn.web.controller;

import com.github.ittalks.commons.example.ws.cxf.integration.server.ComplexUserService;
import com.github.ittalks.commons.example.ws.cxf.integration.server.IComplexUserService;
import com.github.ittalks.fn.common.result.APIResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 刘春龙 on 2017/11/1.
 */
@Controller
@RequestMapping("/webservice/ws")
public class JaxWsController {

    private final IComplexUserService complexUserService;

    /**
     * 基于{@code jaxws:client}，参见ws-cxf-client.xml
     * @param complexUserService
     */
    @Autowired
    public JaxWsController(@Qualifier("userServiceWsClient") IComplexUserService complexUserService) {
        this.complexUserService = complexUserService;
    }

    @ResponseBody
    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public Object getUser(@RequestParam("name") String name) {
        return APIResult.Y(complexUserService.getUserByName(name));
    }
}
