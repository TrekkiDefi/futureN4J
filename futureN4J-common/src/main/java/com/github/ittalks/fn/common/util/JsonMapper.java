package com.github.ittalks.fn.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.github.ittalks.fn.common.advice.exception.NestedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by 刘春龙 on 2017/5/2.
 * <p>
 * 简单封装Jackson，实现JSON String <-> Java Object的Mapper.
 * <p>
 * 封装不同的输出风格, 使用不同的builder函数创建实例.
 */
public class JsonMapper {

    public static Logger logger = LoggerFactory.getLogger(JsonMapper.class);

    private ObjectMapper mapper;

    public JsonMapper() {
        this(null);
    }

    public JsonMapper(JsonInclude.Include include) {
        mapper = new ObjectMapper();
        //设置输出时包含属性的风格
        if (include != null) {
            mapper.setSerializationInclusion(include);
        }
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        //“FAIL_ON_UNKNOWN_PROPERTIES”表示遇到未知属性时失败。false表示禁用该策略。
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //TODO 禁止使用int代表Enum的order()來反序列化Enum,非常危險
//        mapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
    }

    /**
     * 创建输出“全部属性”到Json字符串的Mapper.
     *
     * @return
     */
    public static JsonMapper buildNormalMapper() {
        return new JsonMapper(JsonInclude.Include.ALWAYS);
    }

    /**
     * 创建只输出“非空属性”到Json字符串的Mapper.
     *
     * @return
     */
    public static JsonMapper buildNonNullMapper() {
        return new JsonMapper(JsonInclude.Include.NON_NULL);
    }

    /**
     * 创建只输出“非Null且非Empty(如List.isEmpty)的属性”到Json字符串的Mapper.
     *
     * @return
     */
    public static JsonMapper buildNonEmptyMapper() {
        return new JsonMapper(JsonInclude.Include.NON_EMPTY);
    }

    /**
     * 创建只输出“初始值被改变的属性”到Json字符串的Mapper.
     *
     * @return
     */
    public static JsonMapper buildNonDefaultMapper() {
        return new JsonMapper(JsonInclude.Include.NON_DEFAULT);
    }

    /**
     * 反序列化POJO或“简单”Collection如List<String>.
     * <p>
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * <p>
     * 如需反序列化“复杂”Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     *
     * @param jsonString
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new NestedException(e);
        }
    }

    /**
     * 反序列化“复杂”Collection如List<Bean>, 先使用函数“createCollectionType”构造类型,然后调用本函数.
     * <p>
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     *
     * @param jsonString
     * @param javaType
     * @param <T>
     * @return
     * @see #constructParametricType(Class, Class...)
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            throw new NestedException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, Class<?> parametrized, Class<?>... parameterClasses) {
        return (T) this.fromJson(jsonString, constructParametricType(parametrized, parameterClasses));
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> fromJsonToList(String jsonString, Class<T> classMeta) {
        return (List<T>) this.fromJson(jsonString, constructParametricType(List.class, classMeta));
    }

    @SuppressWarnings("unchecked")
    public <T> T fromJson(JsonNode node, Class<?> parametrized, Class<?>... parameterClasses) {
        JavaType javaType = constructParametricType(parametrized, parameterClasses);
        try {
            return (T) mapper.readValue(node.toString(), javaType);
        } catch (IOException e) {
            throw new NestedException(e);
        }
    }

    /**
     * 反序列化为JsonNode对象
     *
     * @param json
     * @return
     */
    public JsonNode parseNode(String json) {
        try {
            return mapper.readValue(json, JsonNode.class);
        } catch (IOException e) {
            throw new NestedException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T pathAtRoot(String json, String path, Class<?> parametrized, Class<?>... parameterClasses) {
        JsonNode rootNode = parseNode(json);
        JsonNode node = rootNode.path(path);
        return (T) fromJson(node, parametrized, parameterClasses);
    }

    @SuppressWarnings("unchecked")
    public <T> T pathAtRoot(String json, String path, Class<T> clazz) {
        JsonNode rootNode = parseNode(json);
        JsonNode node = rootNode.path(path);
        return (T) fromJson(node, clazz);
    }

    /**
     * 当JSON里只含有Bean的部分属性时，更新一個已存在Bean，只覆盖該部分的属性.
     *
     * @param object
     * @param jsonString
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T update(T object, String jsonString) {
        try {
            return (T) mapper.readerForUpdating(object).readValue(jsonString);
        } catch (JsonProcessingException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        } catch (IOException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
        return null;
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。
     * <p>
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     *
     * @param object
     * @return
     */
    public String toJson(Object object) {

        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new NestedException(e);
        }
    }

    /**
     * 输出全部属性
     *
     * @param object
     * @return
     */
    public static String toNormalJson(Object object) {
        return new JsonMapper(JsonInclude.Include.ALWAYS).toJson(object);
    }

    /**
     * 输出非空属性
     *
     * @param object
     * @return
     */
    public static String toNonNullJson(Object object) {
        return new JsonMapper(JsonInclude.Include.NON_NULL).toJson(object);
    }

    /**
     * 输出非Null且非Empty(如List.isEmpty)的属性
     *
     * @param object
     * @return
     */
    public static String toNonEmptyJson(Object object) {
        return new JsonMapper(JsonInclude.Include.NON_EMPTY).toJson(object);
    }

    /**
     * 输出初始值被改变部分的属性
     *
     * @param object
     * @return
     */
    public static String toNonDefaultJson(Object object) {
        return new JsonMapper(JsonInclude.Include.NON_DEFAULT).toJson(object);
    }

    /**
     * 输出JSONP格式数据.
     *
     * @param functionName
     * @param object
     * @return
     */
    public String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * 以“UTC时间字符串(含有T/Z)”序列化Object对象
     *
     * @param object
     * @return
     */
    public static String toLogJson(Object object) {
        JsonMapper jsonMapper = new JsonMapper(JsonInclude.Include.NON_EMPTY);
        jsonMapper.setDateFormat(JsonMapper.DateUtil.yyyy_MM_dd_HH_mm_ss);
        return jsonMapper.toJson(object);
    }

    /**
     * 构造泛型的Collection Type如:
     * <p>
     * ArrayList<MyBean>, 则调用constructCollectionType(ArrayList.class,MyBean.class)
     * HashMap<String,MyBean>, 则调用constructCollectionType(HashMap.class,String.class, MyBean.class)
     *
     * @param parametrized
     * @param parameterClasses
     * @return
     */
    public JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    /**
     * 设置日期输出格式
     *
     * @param dateFormat
     */
    public void setDateFormat(String dateFormat) {
        mapper.setDateFormat(new SimpleDateFormat(dateFormat));
    }

    /**
     * TODO 设定是否使用Enum的toString函数来读写Enum
     * <p>
     * 为False时使用Enum的name()函数来读写Enum, 默认为False.
     * 注意本函数一定要在Mapper创建后, 所有的读写动作之前调用.
     */
    public void setEnumUseToString(boolean value) {
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, value);
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, value);
    }

    /**
     * TODO 支持使用Jaxb的Annotation，使得POJO上的annotation不用与Jackson耦合。
     * 默认会先查找jaxb的annotation，如果找不到再找jackson的。
     */
    public void enableJaxbAnnotation() {
        JaxbAnnotationModule module = new JaxbAnnotationModule();
        mapper.registerModule(module);
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

    private static class DateUtil {
        private static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    }
}
