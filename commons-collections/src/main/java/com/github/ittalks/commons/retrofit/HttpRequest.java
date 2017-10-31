package com.github.ittalks.commons.retrofit;

import com.github.ittalks.commons.retrofit.interfaces.Error;
import com.github.ittalks.commons.retrofit.interfaces.ParamsInterceptor;
import com.github.ittalks.commons.retrofit.interfaces.Progress;
import com.github.ittalks.commons.retrofit.interfaces.Success;
import com.github.ittalks.commons.retrofit.interfaces.HeadersInterceptor;
import com.github.ittalks.commons.retrofit.utils.WriteFileUtil;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 刘春龙 on 2017/7/19.
 */
public class HttpRequest {

    public static class Builder {
        private static final Logger logger = LoggerFactory.getLogger(Builder.class);

        /**
         * 请求"公共参数"
         */
        private RetrofitHttpService mService;
        private String mVersionApi;// API版本，eg：https://accounts.google.com/o/oauth2/v2/auth，其中的v2
        private ParamsInterceptor mParamsInterceptor;// 自定义参数拦截器
        private HeadersInterceptor mHeadersInterceptor;// 自定义请求头拦截器

        /**
         * 请求"私有参数"
         */
        Object tag;// 用于取消请求的请求标记
        boolean hasVersion = false;// 是否追加接口版本号，即mVersionApi
        String path;// 下载文件保存路径

        String url;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        Error mErrorCallBack;
        Success mSuccessCallBack;
        Progress mProgressCallBack;

        /**
         * 构造方法，使用{@link HttpClient}构造请求，并指定请求地址
         *
         * @param httpClient HttpClient
         */
        public Builder(HttpClient httpClient, String url) {
            this.initParams(httpClient, url);
        }

        /**
         * 构造方法，使用{@link HttpClient}构造请求
         *
         * @param httpClient HttpClient
         */
        public Builder(HttpClient httpClient) {
            this.initParams(httpClient, null);
        }

        private void initParams(HttpClient httpClient, String url) {
            Assert.notNull(httpClient, "httpclient has not be initialized");

            this.mService = httpClient.getService();
            this.mVersionApi = httpClient.getVersionApi();
            this.mParamsInterceptor = httpClient.getParamsInterceptor();
            this.mHeadersInterceptor = httpClient.getHeadersInterceptor();

            this.url = url;
            this.params = new HashMap<>();
            this.headers = new HashMap<>();

            this.mErrorCallBack = c -> {
            };
            this.mSuccessCallBack = c -> {
            };
            this.mProgressCallBack = c -> {
            };
        }

        public HttpRequest.Builder Tag(Object tag) {
            this.tag = tag;
            return this;
        }
        public HttpRequest.Builder Version() {
            this.hasVersion = true;
            return this;
        }
        public HttpRequest.Builder Path(String path) {
            this.path = path;
            return this;
        }
        public HttpRequest.Builder Url(String url) {
            this.url = url;
            return this;
        }
        public HttpRequest.Builder Params(Map<String, String> params) {
            this.params.putAll(params);
            return this;
        }
        public HttpRequest.Builder Param(String key, String value) {
            this.params.put(key, value);
            return this;
        }
        public HttpRequest.Builder Headers(Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }
        public HttpRequest.Builder Header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }
        public HttpRequest.Builder CacheTime(String time) {
            this.headers.put("Cache-Time", time);
            return this;
        }
        public HttpRequest.Builder success(Success success) {
            this.mSuccessCallBack = success;
            return this;
        }
        public HttpRequest.Builder Progress(Progress progress) {
            this.mProgressCallBack = progress;
            return this;
        }
        public HttpRequest.Builder Error(Error error) {
            this.mErrorCallBack = error;
            return this;
        }

        public void cGet() {
            Call<String> call = mService.cGet(checkHeaders(this.headers), checkUrl(this.url), checkParams(this.params));
            HttpUtil.putCall(this.tag, this.url, call);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() >= 200 && response.code() < 300) {
                        mSuccessCallBack.Success(response.body().toString());
                    } else {
                        mErrorCallBack.Error(response.code(), response.message(), null);
                    }
                    if (tag != null)
                        HttpUtil.removeCall(tag.toString() + url);
                }

                @Override
                public void onFailure(Call<String> call, Throwable throwable) {
                    if (call.isCanceled()) {
                        logger.info("request was cancelled");
                    } else {
                        mErrorCallBack.Error(500, throwable.getMessage(), throwable);
                        if (tag != null)
                            HttpUtil.removeCall(tag.toString() + url);
                    }
                }
            });
        }

        public void oGet() {
            mService.oGet(checkHeaders(this.headers), checkUrl(this.url), checkParams(this.params))
                    .observeOn(Schedulers.io())
                    .subscribe(new Subscriber<String>() {

                        @Override
                        public void onCompleted() {
                            logger.info("request was completed");
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            mErrorCallBack.Error(500, throwable.getMessage(), throwable);
                        }

                        @Override
                        public void onNext(String s) {
                            mSuccessCallBack.Success(s);
                        }
                    });
        }

        public void cPost() {
            Call<String> call = mService.cPost(checkHeaders(this.headers), checkUrl(this.url), checkParams(this.params));
            HttpUtil.putCall(this.tag, this.url, call);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() >= 200 && response.code() < 300) {
                        mSuccessCallBack.Success(response.body().toString());
                    } else {
                        mErrorCallBack.Error(response.code(), response.message(), null);
                    }
                    if (tag != null)
                        HttpUtil.removeCall(tag.toString() + url);
                }

                @Override
                public void onFailure(Call<String> call, Throwable throwable) {
                    if (call.isCanceled()) {
                        logger.info("request was cancelled");
                    } else {
                        mErrorCallBack.Error(500, throwable.getMessage(), throwable);
                        if (tag != null)
                            HttpUtil.removeCall(tag.toString() + url);
                    }
                }
            });
        }

        public void oPost() {
            mService.oPost(checkHeaders(this.headers), checkUrl(this.url), checkParams(this.params))
                    .observeOn(Schedulers.io())
                    .subscribe(new Subscriber<String>() {

                        @Override
                        public void onCompleted() {
                            logger.info("request was completed");
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            mErrorCallBack.Error(500, throwable.getMessage(), throwable);
                        }

                        @Override
                        public void onNext(String s) {
                            mSuccessCallBack.Success(s);
                        }
                    });
        }

        public void cDownload() {
            Call<ResponseBody> call = mService.cDownload(checkHeaders(this.headers), checkUrl(this.url), checkParams(this.params));
            HttpUtil.putCall(this.tag, this.url, call);
            Observable<ResponseBody> observable = Observable.create(subscriber -> {
                call.enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() >= 200 && response.code() < 300) {
                            subscriber.onNext(response.body());
                        } else {
                            mErrorCallBack.Error(response.code(), response.message(), null);
                        }
                        if (tag != null)
                            HttpUtil.removeCall(tag.toString() + url);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        if (call.isCanceled()) {
                            logger.info("request was cancelled");
                        } else {
                            mErrorCallBack.Error(500, throwable.getMessage(), throwable);
                            if (tag != null)
                                HttpUtil.removeCall(tag.toString() + url);
                        }
                    }
                });
            });
            observable.observeOn(Schedulers.io())
                    .subscribe(body -> WriteFileUtil.writeFile(body, path, mProgressCallBack, mSuccessCallBack, mErrorCallBack), throwable -> {
                        mErrorCallBack.Error(throwable);
                    });
        }

        public void obDownload() {
            mService.oDownload(checkHeaders(this.headers), checkUrl(this.url), checkParams(this.params))
                    .observeOn(Schedulers.io())
                    .subscribe(body -> WriteFileUtil.writeFile(body, path, mProgressCallBack, mSuccessCallBack, mErrorCallBack), t -> {
                        mErrorCallBack.Error(t);
                    });
        }

        /**
         * 请求头校验
         *
         * @param headers 请求头key、value集合
         * @return
         */
        private Map<String, String> checkHeaders(Map<String, String> headers) {
            if (headers == null) {
                headers = new HashMap<>();
            }
            if (mHeadersInterceptor != null) {
                headers = mHeadersInterceptor.checkHeaders(headers);
            }
            // retrofit2的headers的值不能为null，此处做下校验，防止出错
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if (entry.getValue() == null) {
                    headers.put(entry.getKey(), "");
                }
            }
            return headers;
        }

        /**
         * url校验，如果设置了API接口版本号，则添加接口版本号
         *
         * @param url API接口版本号
         * @return
         */
        private String checkUrl(String url) {
            Assert.notNull(url, "relative url can not be empty");
            // 因为baseUrl以"/"结束，故url必须保证不能以"/"开始
            if (hasVersion) {
                url = url.startsWith("/") ? url : "/" + url;
                Assert.notNull(mVersionApi, "can not add VersionApi ,because of VersionApi is null");
                return mVersionApi + url;
            }
            url = url.startsWith("/") ? url.substring(1) : url;
            return url;
        }

        /**
         * 请求参数校验
         * @param params 请求参数
         * @return
         */
        private Map<String, String> checkParams(Map<String, String> params) {
            if (params == null) {
                params = new HashMap<>();
            }
            if (mParamsInterceptor != null) {
                params = mParamsInterceptor.checkParams(params);
            }
            // retrofit2的params的值不能为null，此处做下校验，防止出错
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() == null) {
                    params.put(entry.getKey(), "");
                }
            }
            return params;
        }
    }
}
