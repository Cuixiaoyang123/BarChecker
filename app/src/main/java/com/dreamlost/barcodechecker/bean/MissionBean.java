package com.dreamlost.barcodechecker.bean;

/**
 * Created by DreamLost on 2020/9/26 at 8:54
 * Description:
 */
public class MissionBean {

    /**
     * id : 1111199999999
     * maker : 制定人
     * makeTime : date-time
     * carryTime : date-time,任务执行时间
     * deadline : date-time,任务截止时间
     * comment : string
     */

    private long id;
    private String maker;
    private String makeTime;
    private String carryTime;
    private String deadline;
    private String comment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getMakeTime() {
        return makeTime;
    }

    public void setMakeTime(String makeTime) {
        this.makeTime = makeTime;
    }

    public String getCarryTime() {
        return carryTime;
    }

    public void setCarryTime(String carryTime) {
        this.carryTime = carryTime;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
