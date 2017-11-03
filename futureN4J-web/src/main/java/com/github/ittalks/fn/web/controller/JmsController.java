package com.github.ittalks.fn.web.controller;

import com.github.ittalks.commons.example.jms.model.Msg;
import com.github.ittalks.commons.example.jms.remoting.AlertService;
import com.github.ittalks.fn.common.result.APIResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 刘春龙 on 2017/11/2.
 */
@RestController
@RequestMapping("/jms")
public class JmsController {

    private final JmsOperations template;
    private final AlertService alertService;

    /**
     *
     * @param template
     * @param alertService 基于{@link org.springframework.jms.remoting.JmsInvokerProxyFactoryBean}，参见spring-jms.xml
     */
    @Autowired
    public JmsController(JmsOperations template, @Qualifier("alertService") AlertService alertService) {
        this.template = template;
        this.alertService = alertService;
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public Object sendMsg(Msg msg) {
//        template.convertAndSend("barrage.queue", msg);
        alertService.alertMsg(msg);
        return APIResult.Y();
    }
}
