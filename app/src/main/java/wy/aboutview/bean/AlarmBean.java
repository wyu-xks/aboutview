package wy.aboutview.bean;

/**
 * Created by Xie on 2016/11/30.
 */
public class AlarmBean {
    public String targeTime;
    public String wakeUpTime;
    public float startTime;
    public float endDegree;
    public float sleepTime;

    public AlarmBean(float startTime, String targeTime, String wakeUpTime, float endDegree, float sleepTime) {
        this.startTime = startTime;
        this.targeTime = targeTime;
        this.wakeUpTime = wakeUpTime;
        this.endDegree = endDegree;
        this.sleepTime = sleepTime;
    }
}
