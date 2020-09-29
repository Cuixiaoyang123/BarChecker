package com.dreamlost.barcodechecker.bean;

/**
 * Created by DreamLost on 2020/9/26 at 8:51
 * Description:
 */
public class MissionDetailBean {

    /**
     * id : 2222
     * checker : string,巡检人
     * result : string,巡检结果
     * comment : string,备注
     * missionId : 11111
     * area : {"id":11111,"name":"string,区域名称","code":"string,区域编号","comment":"string,备注"}
     */

    private long id;
    private String checker;
    private String result;
    private String comment;
    private long missionId;
    private AreaBean area;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public AreaBean getArea() {
        return area;
    }

    public void setArea(AreaBean area) {
        this.area = area;
    }

    public static class AreaBean {
        /**
         * id : 11111
         * name : string,区域名称
         * code : string,区域编号
         * comment : string,备注
         */

        private long id;
        private String name;
        private String code;
        private String comment;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }
}

