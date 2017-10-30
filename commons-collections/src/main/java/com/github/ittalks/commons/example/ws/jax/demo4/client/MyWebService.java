package com.github.ittalks.commons.example.ws.jax.demo4.client;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.Map;

/**
 * Created by 刘春龙 on 2017/10/30.
 */
public interface MyWebService {

    Map<String, List<Role>> getRoles();
}
