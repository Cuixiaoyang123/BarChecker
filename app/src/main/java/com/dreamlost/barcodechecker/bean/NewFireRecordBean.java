package com.dreamlost.barcodechecker.bean;

/**
 * Created by DreamLost on 2020/10/17 at 15:54
 * Description:
 */
public class NewFireRecordBean {
    private long id;
    private String barcode;
    private String productionDate;
    private String validdateOfBody;
    private String validdateOfFire;
    private String manufacture;
    private boolean finished;
    private String deployer;
    private String deployDate;
    private String comment;

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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getDeployer() {
        return deployer;
    }

    public void setDeployer(String deployer) {
        this.deployer = deployer;
    }

    public String getDeployDate() {
        return deployDate;
    }

    public void setDeployDate(String deployDate) {
        this.deployDate = deployDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public FireRecordBean.DataBean.PositionIdBean getPositionId() {
        return positionId;
    }

    public void setPositionId(FireRecordBean.DataBean.PositionIdBean positionId) {
        this.positionId = positionId;
    }

    private FireRecordBean.DataBean.PositionIdBean positionId;




}
