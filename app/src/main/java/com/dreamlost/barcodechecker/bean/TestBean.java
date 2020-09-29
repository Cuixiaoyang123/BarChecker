package com.dreamlost.barcodechecker.bean;

import java.util.Date;
import java.util.List;

/**
 * Created by DreamLost on 2020/9/22 at 7:37
 * Description:
 */
public class TestBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1111
         * qianfeng : boolean,铅封
         * yali : boolean,压力
         * pingti : boolean,瓶体
         * penguan : boolean,喷管
         * penzui : boolean,喷嘴
         * operation : string,操作
         * status : string,状态
         * inspectTime : date-time,巡检时间
         * fireId : long,灭火器使用位置记录
         * missionId : long,巡检任务id
         */

        private int id;
        private boolean qianfeng;
        private boolean yali;
        private boolean pingti;
        private boolean penguan;
        private boolean penzui;
        private String operation;
        private String status;
        private String inspectTime;
        private long fireId;
        private long missionId;

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", qianfeng=" + qianfeng +
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
    }
}
