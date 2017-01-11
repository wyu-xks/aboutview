package wy.aboutview.callback;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Xie on 2017/1/6.
 * 基本的回调
 */
public interface  BaseCallback<T> {


    /**
     * 请求之前调用
     */
    void onRequestBefore();

    /**
     * 请求失败调用（网络问题）
     *
     * @param request
     * @param e
     */
     void onFailure(Request request, Exception e);

    /**
     * 请求成功而且没有错误的时候调用
     *
     * @param response
     * @param t
     */
    void onSuccess(Response response, T t);

    /**
     * 请求成功但是有错误的时候调用，例如Gson解析错误等
     *
     * @param response
     * @param errorCode
     * @param e
     */
     void onError(Response response, int errorCode, Exception e);

}