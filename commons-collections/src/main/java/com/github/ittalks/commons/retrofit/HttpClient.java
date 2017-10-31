package com.github.ittalks.commons.retrofit;

import com.github.ittalks.commons.retrofit.converter.StringConverterFactory;
import com.github.ittalks.commons.retrofit.interfaces.HeadersInterceptor;
import com.github.ittalks.commons.retrofit.interfaces.ParamsInterceptor;
import com.github.ittalks.commons.retrofit.utils.OkhttpProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.tools.javac.util.Assert;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchunlong on 2017/7/15.
 */
public class HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    /*
        请求"公共参数"
     */
    private RetrofitHttpService mService;
    private String mVersionApi;//API版本，eg：https://accounts.google.com/o/oauth2/v2/auth，其中的v2
    private ParamsInterceptor mParamsInterceptor;//自定义参数拦截器
    private HeadersInterceptor mHeadersInterceptor;//自定义请求头拦截器

    private HttpClient(String mVersionApi, RetrofitHttpService mService, ParamsInterceptor mParamsInterceptor, HeadersInterceptor mHeadersInterceptor) {
        this.mService = mService;
        this.mVersionApi = mVersionApi;
        this.mParamsInterceptor = mParamsInterceptor;
        this.mHeadersInterceptor = mHeadersInterceptor;
    }

    public RetrofitHttpService getService() {
        return mService;
    }

    public String getVersionApi() {
        return mVersionApi;
    }

    public ParamsInterceptor getParamsInterceptor() {
        return mParamsInterceptor;
    }

    public HeadersInterceptor getHeadersInterceptor() {
        return mHeadersInterceptor;
    }

    /**
     * 构造OkHttpClient，并初始化请求"公共参数"
     */
    protected static class Builder {
        private String baseUrl;//URL请求前缀地址。必传
        OkHttpClient client;

        /**
         * cachePath、servers的设置仅当未设置自定义的client时才会生效
         */
        private String cachePath;//cachePath用于CacheInterceptor拦截器，指定缓存的路径
        private List<String> servers = new ArrayList<>();//servers用于RetryAndChangeIpInterceptor拦截器，指定请求重试域名

        private List<Converter.Factory> converterFactories = new ArrayList<>();
        private List<CallAdapter.Factory> adapterFactories = new ArrayList<>();

        private String versionApi;//API版本，eg：https://accounts.google.com/o/oauth2/v2/auth，其中的v2
        private ParamsInterceptor paramsInterceptor;//自定义参数拦截器
        private HeadersInterceptor headersInterceptor;//自定义请求头拦截器

        /**
         * 构造方法，并传入{@code baseUrl}URL请求前缀地址
         *
         * @param baseUrl URL请求前缀地址。必传
         */
        public Builder(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        /**
         * {@code cachePath}、{@code servers}的设置仅当未设置自定义的client时才会生效
         *
         * @param client 自定义client
         */
        public Builder client(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public Builder cachePath(String cachePath) {
            this.cachePath = cachePath;
            return this;
        }

        /**
         * 添加请求失败时重试的域名
         *
         * @param server 格式：[protocol]://[ip]:[port]，eg，http://127.0.0.1:8080，https://square.github.io/
         * @return
         */
        public Builder addServer(String server) {
            this.servers.add(server);
            return this;
        }

        public Builder addServers(List<String> servers) {
            this.servers.addAll(servers);
            return this;
        }

        /**
         * 未指定自定义的{@link Converter.Factory}时，会默认使用{@link StringConverterFactory}、{@link GsonConverterFactory}
         *
         * @param factory 自定义的Converter.Factory
         * @return
         */
        public Builder addConverterFactory(Converter.Factory factory) {
            this.converterFactories.add(factory);
            return this;
        }

        public Builder addCallFactory(CallAdapter.Factory factory) {
            this.adapterFactories.add(factory);
            return this;
        }

        public Builder versionApi(String versionApi) {
            Assert.checkNonNull(versionApi, "versionApi can not null");
            // 删除首尾路径分隔符"/"
            if (versionApi.startsWith("/")) {
                versionApi = versionApi.substring(1);
            }
            if (versionApi.endsWith("/")) {
                versionApi = versionApi.substring(0, versionApi.length() - 1);
            }

            this.versionApi = versionApi;
            return this;
        }

        public Builder paramsInterceptor(ParamsInterceptor interceptor) {
            this.paramsInterceptor = interceptor;
            return this;
        }

        public Builder headersInterceptor(HeadersInterceptor headersInterceptor) {
            this.headersInterceptor = headersInterceptor;
            return this;
        }

        public HttpClient build() {
            Assert.checkNonNull(this.baseUrl, "baseUrl can not be null");
            if (converterFactories.size() == 0) {
                converterFactories.add(StringConverterFactory.create());

                Gson gson = new GsonBuilder()
                        //配置Gson
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .create();
                converterFactories.add(GsonConverterFactory.create(gson));
            }
            if (adapterFactories.size() == 0) {
                adapterFactories.add(RxJavaCallAdapterFactory.create());
            }
            if (client == null) {
                if (StringUtils.isEmpty(cachePath)) {//如果不指定cachePath，默认是用户主目录
                    cachePath = System.getProperty("user.home");
                }
                client = OkhttpProvider.okHttpClient(cachePath, servers);
            }
            Retrofit.Builder builder = new Retrofit.Builder();
            for (Converter.Factory converterFactory : converterFactories) {
                builder.addConverterFactory(converterFactory);
            }
            for (CallAdapter.Factory adapterFactory : adapterFactories) {
                builder.addCallAdapterFactory(adapterFactory);
            }
            Retrofit retrofit = builder
                    .baseUrl(baseUrl.endsWith("/") ? baseUrl : baseUrl + "/")
                    .client(client).build();
            RetrofitHttpService retrofitHttpService =
                    retrofit.create(RetrofitHttpService.class);
            HttpClient mInstance = new HttpClient(versionApi, retrofitHttpService, paramsInterceptor, headersInterceptor);
            return mInstance;
        }
    }
}
