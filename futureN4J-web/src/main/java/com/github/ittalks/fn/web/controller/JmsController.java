package com.github.ittalks.fn.web.controller;

import com.github.ittalks.commons.example.jms.model.Msg;
import com.github.ittalks.fn.common.result.APIResult;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public JmsController(JmsOperations template) {
        this.template = template;
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public Object sendMsg(Msg msg) {
        template.convertAndSend("barrage.queue", msg);
        return APIResult.Y();
    }
}
