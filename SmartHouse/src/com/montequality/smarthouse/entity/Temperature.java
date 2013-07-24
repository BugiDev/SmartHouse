package com.montequality.smarthouse.entity;

public class Temperature {
	

	private int id;
	private int temperature;
	private String room;
	
	public Temperature(int id, int temperature, String room) {
		this.setId(id);
		this.setTemperature(temperature);
		this.setRoom(room);
	}
	
	public Temperature() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}
	
	

}
