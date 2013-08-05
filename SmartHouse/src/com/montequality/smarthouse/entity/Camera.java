package com.montequality.smarthouse.entity;

public class Camera {

    private int id;
    private String path;
    private String room;
    private boolean alarmOnOff;

    public Camera(int id, String path, String room, boolean alarmOnOff) {
        this.setId(id);
        this.setRoom(room);
        this.setPath(path);
        this.setAlarmOnOff(alarmOnOff);

    }

    public Camera() {
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isAlarmOnOff() {
        return alarmOnOff;
    }

    public void setAlarmOnOff(boolean alarmOnOff) {
        this.alarmOnOff = alarmOnOff;
    }

}
