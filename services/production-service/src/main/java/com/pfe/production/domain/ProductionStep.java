package com.pfe.production.domain;

public class ProductionStep {
    private String machineId;
    private String machineName;
    private String type;
    private int priority;
    private String status; // PENDING, COMPLETED

    public ProductionStep() {
    }

    public ProductionStep(String machineId, String machineName, String type, int priority) {
        this.machineId = machineId;
        this.machineName = machineName;
        this.type = type;
        this.priority = priority;
        this.status = "PENDING";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "ProductionStep{" +
                "machineId='" + machineId + '\'' +
                ", machineName='" + machineName + '\'' +
                ", type='" + type + '\'' +
                ", priority=" + priority +
                '}';
    }
}
