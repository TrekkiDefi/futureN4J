package com.github.ittalks.fn.web.controller;

import com.github.ittalks.commons.example.ws.cxf.restful.server.ILogisticsApi;
import com.github.ittalks.fn.common.result.APIResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 刘春龙 on 2017/11/1.
 */
@Controller
@RequestMapping("/webservice/rs")
public class JaxRsController {

    private final ILogisticsApi logisticsApi;

    @Autowired
    public JaxRsController(@Qualifier("logisticsApi") ILogisticsApi logisticsApi) {
        this.logisticsApi = logisticsApi;
    }

    @ResponseBody
    @RequestMapping(value = "/doGet", method = RequestMethod.GET)
    public Object doGet() {
        return APIResult.Y(logisticsApi.doGet("fnpac", "凡派,"));
    }
}
