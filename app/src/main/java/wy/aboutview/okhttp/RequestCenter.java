package wy.aboutview.okhttp;

import android.content.Context;

import java.net.HttpURLConnection;

import wy.aboutview.callback.BaseCallback;

/**
 * Created by xiebaoxin on 2016/2/1.
 */
public class RequestCenter {
    private static final String TAG = RequestCenter.class.getSimpleName();
    private static final String BASE_URL = "http://120.24.239.158:599/";
    private static RequestCenter requestCenter;
    //private static Context mContext;
    private HttpURLConnection urlConn;
    private static OkHttpHelper okHttpHelper;

    public static RequestCenter getRequestCenter(Context context) {
        //mContext = context.getApplicationContext();
        if (requestCenter == null) {
            synchronized (RequestCenter.class) {
                if (requestCenter == null) {
                    requestCenter = new RequestCenter();
                    okHttpHelper = OkHttpHelper.getInstance();
                }
            }
        }
        return requestCenter;
    }

    public void getAboutUs(Class clazz, BaseCallback callback) {
        String url = BASE_URL + "index.php?m=api&c=ServerInfo&a=aboutUs&time=" + System.currentTimeMillis() / 1000;
        okHttpHelper.get(url, clazz, callback);
    }
}
