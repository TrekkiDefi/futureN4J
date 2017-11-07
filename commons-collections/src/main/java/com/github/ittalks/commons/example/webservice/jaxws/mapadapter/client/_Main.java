package com.github.ittalks.commons.example.webservice.jaxws.mapadapter.client;

import java.util.List;

/**
 * Created by 刘春龙 on 2017/10/30.
 */
public class _Main {

    public static void main(String[] args) {
        FnWebServiceImplService service = new FnWebServiceImplService();
        FnWebServiceImpl port = service.getFnWebServiceImplPort();

        // 省去不相关代码

        List<MyRole> roles = port.getRoles().getItem();
        for(MyRole myRole : roles) {
            System.out.print("key:" + myRole.getKey() + ",");
            System.out.print("role:");
            for(Role role : myRole.getValue()) {
                System.out.print(role.getRoleName() + " ");
            }
            System.out.println();
        }
    }
}
