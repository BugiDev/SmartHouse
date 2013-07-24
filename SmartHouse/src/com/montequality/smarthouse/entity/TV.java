package com.montequality.smarthouse.entity;

public class TV {


	private int id;
	private boolean power;
	private String room;
	private int chanell;
	private int volume;
	
	public TV(int id, boolean power, String room, int chanell, int volume) {
		this.setId(id);
		this.setPower(power);
		this.setRoom(room);
		this.setChanell(chanell);
		this.setVolume(volume);
	}
	
	public TV() {
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

	public int getChanell() {
		return chanell;
	}

	public void setChanell(int chanell) {
		this.chanell = chanell;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	
}
