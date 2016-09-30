package wy.aboutview.bean;

/**
 * Created by Xie on 2016/7/28.
 */
public class Histogram {
    public int type;
    public float value;

    public Histogram(int key, float value) {
        this.type = key;
        this.value = value;
    }
}
