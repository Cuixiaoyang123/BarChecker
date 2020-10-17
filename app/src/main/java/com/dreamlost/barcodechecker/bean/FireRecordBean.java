package com.dreamlost.barcodechecker.bean;

import java.util.List;

/**
 * Created by DreamLost on 2020/9/26 at 9:56
 * Description:
 */
public class FireRecordBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 111111111111
         * barcode : 6921168509256
         * productionDate : 2020-10-10 10:10:10
         * validdateOfBody : 2022-10-10 10:10:10
         * validdateOfFire : 2022-10-10 10:10:10
         * manufacture : 农夫山泉灭火器有限公司
         * "finished": false,
         * comment : 无
         * positionId : {"id":122111111111,"code":"25452","name":"北邮教一2楼东侧","areaId":124972}
         */

        private long id;
        private String barcode;
        private String productionDate;
        private String validdateOfBody;
        private String validdateOfFire;
        private String manufacture;
        private boolean finished;
        private String comment;
        private PositionIdBean positionId;

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", barcode='" + barcode + '\'' +
                    ", productionDate='" + productionDate + '\'' +
                    ", validdateOfBody='" + validdateOfBody + '\'' +
                    ", validdateOfFire='" + validdateOfFire + '\'' +
                    ", manufacture='" + manufacture + '\'' +
                    ", finished=" + finished +
                    ", comment='" + comment + '\'' +
                    ", positionId=" + positionId +
                    '}';
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getProductionDate() {
            return productionDate;
        }

        public void setProductionDate(String productionDate) {
            this.productionDate = productionDate;
        }

        public String getValiddateOfBody() {
            return validdateOfBody;
        }

        public void setValiddateOfBody(String validdateOfBody) {
            this.validdateOfBody = validdateOfBody;
        }

        public String getValiddateOfFire() {
            return validdateOfFire;
        }

        public void setValiddateOfFire(String validdateOfFire) {
            this.validdateOfFire = validdateOfFire;
        }

        public String getManufacture() {
            return manufacture;
        }

        public void setManufacture(String manufacture) {
            this.manufacture = manufacture;
        }

        public boolean getFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public PositionIdBean getPositionId() {
            return positionId;
        }

        public void setPositionId(PositionIdBean positionId) {
            this.positionId = positionId;
        }

        public static class PositionIdBean {
            /**
             * id : 122111111111
             * code : 25452
             * name : 北邮教一2楼东侧
             * areaId : 124972
             */

            private long id;
            private String code;
            private String name;
            private long areaId;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public long getAreaId() {
                return areaId;
            }

            public void setAreaId(long areaId) {
                this.areaId = areaId;
            }
        }
    }
}
