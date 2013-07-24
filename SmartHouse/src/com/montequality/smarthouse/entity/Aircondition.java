package com.montequality.smarthouse.entity;

public class Aircondition {
	
	private int id;
	private boolean power;
	private String room;
	private String mode;
	private int temperature;
	
	public Aircondition(int id, boolean power, String room, String mode, int temperature) {
		this.setId(id);
		this.setPower(power);
		this.setRoom(room);
		this.setMode(mode);
		this.setTemperature(temperature);
	}
	
	public Aircondition() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isPower() {
		return power;
	}

	public void setPower(boolean power) {
		this.power = power;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

}
