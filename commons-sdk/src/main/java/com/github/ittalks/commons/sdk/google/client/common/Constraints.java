package com.github.ittalks.commons.sdk.google.client.common;

import com.google.api.services.calendar.CalendarScopes;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 刘春龙 on 2017/2/20.
 */
public class Constraints {

    /**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    public static final String APPLICATION_NAME = "";

    /** Directory to store user credentials. */
//    public static final java.io.File DATA_STORE_DIR =
//            new java.io.File(System.getProperty("user.home"), Configration.DATA_STORE_DIR);

    /** OAuth 2.0 scopes. */
    public static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/userinfo.profile",
//            "https://www.googleapis.com/auth/userinfo.email",
            CalendarScopes.CALENDAR);
}
