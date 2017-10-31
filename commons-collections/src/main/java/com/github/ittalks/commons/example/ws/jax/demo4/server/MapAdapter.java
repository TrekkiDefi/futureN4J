package com.github.ittalks.commons.example.ws.jax.demo4.server;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 刘春龙 on 2017/10/30.
 *
 * 将Map转为类对象
 */
public class MapAdapter extends XmlAdapter<MyRole[], Map<String, List<Role>>> {

    /**
     * 适配转换 MyRole[]  ->  Map<String, List<Role>>
     * @param v
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, List<Role>> unmarshal(MyRole[] v) throws Exception {
        Map<String, List<Role>> map = new HashMap<>();
        for(int i = 0; i < v.length; i++) {
            MyRole role = v[i];
            map.put(role.getKey(), role.getValue());
        }
        return map;
    }

    /**
     * 适配转换 Map<String, List<Role>>  ->  MyRole[]
     * @param v
     * @return
     * @throws Exception
     */
    @Override
    public MyRole[] marshal(Map<String, List<Role>> v) throws Exception {
        MyRole[] roles = new MyRole[v.size()];

        int i = 0;
        for(String key : v.keySet()) {
            List<Role> rolesList = v.get(key);
            roles[i] = new MyRole();
            roles[i].setKey(key);
            roles[i].setValue(rolesList);
            i++;
        }
        return roles;
    }
}
