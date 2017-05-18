package wy.aboutview.image;


import android.widget.ImageView;

import com.bumptech.glide.Glide;

import wy.aboutview.R;

/**
 * Author : xks
 * Date : 2017/5/9
 * Des : @Des 头像
 */

public class ImgLoadUtil {

    private static ImgLoadUtil instance;

    private ImgLoadUtil() {
    }

    public static ImgLoadUtil getInstance() {
        if (instance == null) {
            instance = new ImgLoadUtil();
        }
        return instance;
    }

    /**
     * 加载圆角图
     */
    public static void displayCircle(ImageView imageView, Object imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .error(R.drawable.ic_avatar)
                .transform(new GlideCircleTransform(imageView.getContext()))
                .into(imageView);
    }

}
