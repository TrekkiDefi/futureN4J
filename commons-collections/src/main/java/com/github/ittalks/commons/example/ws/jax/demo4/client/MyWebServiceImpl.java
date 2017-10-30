package com.github.ittalks.commons.example.ws.jax.demo4.client;

import javax.jws.WebService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 刘春龙 on 2017/10/30.
 */
@WebService
public class MyWebServiceImpl implements MyWebService{

    @XmlJavaTypeAdapter(MapAdapter.class)
    public Map<String, List<Role>> getRoles() {
        Map<String, List<Role>> map = new HashMap<>();

        List<Role> roleList = new ArrayList<Role>();
        roleList.add(new Role(1, "技术总监"));
        roleList.add(new Role(2, "架构师"));
        map.put("admin", roleList);

        List<Role> roleList2 = new ArrayList<Role>();
        roleList2.add(new Role(1, "java菜鸟"));
        map.put("eson15", roleList2);

        return map;
    }

}
