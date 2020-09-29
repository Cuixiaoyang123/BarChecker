package com.dreamlost.barcodechecker.bean;

/**
 * Created by DreamLost on 2020/9/26 at 8:37
 * Description:
 */
public class RecordBean {
    /**
     * qianfeng : true
     * yali : true
     * pingti : true
     * penguan : true
     * penzui : true
     * operation : 无
     * status : 正常
     * inspectTime : 2020-10-10 10:10:10
     * fireId : 111111111111
     * missionId : 111111111131
     */

    private boolean qianfeng = true;
    private boolean yali = true;
    private boolean pingti = true;
    private boolean penguan = true;
    private boolean penzui = true;
    private String operation = "无";
    private String status = "正常";
    private String inspectTime = "2020-10-10 10:10:10";
    private long fireId;
    private long missionId;
    private String comment = "无";

    @Override
    public String toString() {
        return "RecordBean{" +
                "qianfeng=" + qianfeng +
                ", yali=" + yali +
                ", pingti=" + pingti +
                ", penguan=" + penguan +
                ", penzui=" + penzui +
                ", operation='" + operation + '\'' +
                ", status='" + status + '\'' +
                ", inspectTime='" + inspectTime + '\'' +
                ", fireId=" + fireId +
                ", missionId=" + missionId +
                '}';
    }

    public boolean isQianfeng() {
        return qianfeng;
    }

    public void setQianfeng(boolean qianfeng) {
        this.qianfeng = qianfeng;
    }

    public boolean isYali() {
        return yali;
    }

    public void setYali(boolean yali) {
        this.yali = yali;
    }

    public boolean isPingti() {
        return pingti;
    }

    public void setPingti(boolean pingti) {
        this.pingti = pingti;
    }

    public boolean isPenguan() {
        return penguan;
    }

    public void setPenguan(boolean penguan) {
        this.penguan = penguan;
    }

    public boolean isPenzui() {
        return penzui;
    }

    public void setPenzui(boolean penzui) {
        this.penzui = penzui;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInspectTime() {
        return inspectTime;
    }

    public void setInspectTime(String inspectTime) {
        this.inspectTime = inspectTime;
    }

    public long getFireId() {
        return fireId;
    }

    public void setFireId(long fireId) {
        this.fireId = fireId;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
