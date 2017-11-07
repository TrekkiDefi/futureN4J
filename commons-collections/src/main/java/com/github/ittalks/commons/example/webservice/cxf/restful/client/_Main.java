package com.github.ittalks.commons.example.webservice.cxf.restful.client;

import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;

/**
 * Created by 刘春龙 on 2017/10/31.
 */
public class _Main {

    private static String baseAddress = "http://localhost:9090/futureN4J/webservice/rest/logisticsApi";

    public static void main(String[] args) {
        WebClient client = WebClient.create(baseAddress)
                .header("charset", "UTF-8")
                .encoding("UTF-8")
                .acceptEncoding("UTF-8");

        Object xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<itemName>诺基亚</itemName>";
        String responseMessage = client.path("itemConfirm")
                .accept(MediaType.APPLICATION_XML)
                .post(xml, String.class);
        System.out.println("responseMessage : " + responseMessage);

        client = WebClient.create(baseAddress)
                .header("charset", "UTF-8")
                .encoding("UTF-8")
                .acceptEncoding("UTF-8");
        responseMessage = client.path("doGet/{first}/{last}", "fnpac", "凡派,")
                .accept(MediaType.APPLICATION_XML)
                .get(String.class);
        System.out.println("responseMessage : " + responseMessage);
    }
}
