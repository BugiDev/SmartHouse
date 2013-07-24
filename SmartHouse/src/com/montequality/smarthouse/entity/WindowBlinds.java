package com.montequality.smarthouse.entity;

public class WindowBlinds {
	

	private int id;
	private String direction;
	private String room;
	
	public WindowBlinds(int id, String direction, String room) {
		this.setId(id);
		this.setRoom(room);
		this.setDirection(direction);
	}
	
	public WindowBlinds() {
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

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

}
