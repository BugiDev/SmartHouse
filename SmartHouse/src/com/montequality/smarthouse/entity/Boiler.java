package com.montequality.smarthouse.entity;

public class Boiler {

	private int id;
	private boolean power;
	private String room;
	
	public Boiler(int id, boolean power, String room) {
		this.setId(id);
		this.setPower(power);
		this.setRoom(room);
	}
	
	public Boiler() {
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
	
	
}
