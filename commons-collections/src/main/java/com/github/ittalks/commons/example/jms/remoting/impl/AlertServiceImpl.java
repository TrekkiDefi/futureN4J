package com.github.ittalks.commons.example.jms.remoting.impl;

import com.github.ittalks.commons.example.jms.model.Msg;
import com.github.ittalks.commons.example.jms.remoting.AlertService;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/11/2.
 */
@Service
public class AlertServiceImpl implements AlertService {

    private static final Logger logger = Logger.getLogger(AlertServiceImpl.class.getName());

    @Override
    public void alertMsg(Msg msg) {
        logger.info("[" + Thread.currentThread().getName() + "], RPC Received Msg：[" + msg + "]");
    }
}
