package com.montequality.smarthouse.entity;

public class PotentiometerLight {

    private int id;
    private double powerMeter;
    private String room;
    
    public PotentiometerLight(int id, double powerMeter, String room) {
            this.setId(id);
            this.setPowerMeter(powerMeter);
            this.setRoom(room);
    }
    
    public PotentiometerLight() {
            // TODO Auto-generated constructor stub
    }

    public int getId() {
            return id;
    }

    public void setId(int id) {
            this.id = id;
    }

    public String getRoom() {
            return room;
    }

    public void setRoom(String room) {
            this.room = room;
    }

    public double getPowerMeter() {
        return powerMeter;
    }

    public void setPowerMeter(double powerMeter) {
        this.powerMeter = powerMeter;
    }
    
}
