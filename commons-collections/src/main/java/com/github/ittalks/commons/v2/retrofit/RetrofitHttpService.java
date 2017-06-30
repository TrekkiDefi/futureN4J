package com.github.ittalks.commons.v2.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import rx.Observable;

import java.util.Map;

/**
 * Created by 刘春龙 on 2017/6/7.
 */
public interface RetrofitHttpService {

    /**
     * GET请求。
     *
     * @param headers 请求头
     * @param url 请求url
     * @param params 请求参数
     * @return
     */
    @GET()
    Call<String> get(@HeaderMap Map<String, String> headers, @Url String url, @QueryMap Map<String, String> params);

    @GET()
    Observable<String> obGet(@HeaderMap Map<String, String> headers, @Url String url, @QueryMap Map<String, String> params);

    /**
     * POST表单请求。<br>
     *      这里有几点需要说明的：<br>
     *          <ul>
     *          <li>1. @{@link FormUrlEncoded}将会自动将请求参数的类型调整为{@code application/x-www-form-urlencoded}，
     *          假如{@code content}传递的参数为{@code Good Luck}，那么最后得到的请求体就是{@code content=Good+Luck}<br>
     *          <li>2. @{@link Field}注解将每一个请求参数都存放至请求体中，还可以添加encoded参数，该参数为boolean型，默认false。
     *          </ul>具体的用法为{@code @Field(value = "book", encoded = true) String book}，encoded参数为true的话，key-value-pair将会被编码，即将中文和特殊字符进行编码转换<br>
     *  对于{@code application/x-www-form-urlencoded}，根据{@code http://www.w3.org/TR/html401/interact/forms.html#form-content-typehttp://www.w3.org/TR/html401/interact/forms.html#form-content-type}，
     *  空格将被'+'替换，所以人们可能希望使用"+"附加替换"％20"来执行encodeURIComponent替换。
     * @param headers 请求头
     * @param url 请求url
     * @param params 请求参数
     * @return
     */
    @FormUrlEncoded
    @POST()
    Call<String> post(@HeaderMap Map<String, String> headers, @Url String url, @FieldMap(encoded = false) Map<String, String> params);

    @FormUrlEncoded
    @POST()
    Observable<String> obPost(@HeaderMap Map<String, String> headers, @Url String url, @FieldMap Map<String, String> params);

    @Streaming
    @GET()
    Call<ResponseBody> download(@HeaderMap Map<String, String> headers, @Url String url, @QueryMap Map<String, String> params);

    @Streaming
    @GET()
    Observable<ResponseBody> obDownload(@HeaderMap Map<String, String> headers, @Url String url, @QueryMap Map<String, String> params);
}
