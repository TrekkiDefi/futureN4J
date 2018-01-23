package com.github.ittalks.commons.sdk.google.calendar.proxy;

import com.github.ittalks.commons.sdk.google.client.common.Constraints;
import com.github.ittalks.commons.sdk.google.oauth2.proxy.AuthorizationCodeFlowProxy;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.calendar.Calendar;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/2/20.
 */
public class CalendarProxy {

    private static final Logger logger = Logger.getLogger(CalendarProxy.class.getName());

    private static final Lock lock = new ReentrantLock();
    private static AuthorizationCodeFlow flow;

    public static Credential loadCredential(String userId) {
        lock.lock();

        try {
            if(flow == null) {
                flow = AuthorizationCodeFlowProxy.initializeFlow();
            }

            Credential credential = flow.loadCredential(userId);
            if(credential != null && credential.getAccessToken() != null) {
                return credential;
            }
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return null;
    }

    public static Calendar buildCalendar(String userId) {
        //load credential
        Credential credential = loadCredential(userId);

        if(credential != null && credential.getAccessToken() != null) {
            // set up global Calendar instance
            Calendar client = new com.google.api.services.calendar.Calendar.Builder(
                    AuthorizationCodeFlowProxy.httpTransport, AuthorizationCodeFlowProxy.JSON_FACTORY, credential).setApplicationName(Constraints.APPLICATION_NAME).build();

            return client;
        }

        return null;
    }
}
