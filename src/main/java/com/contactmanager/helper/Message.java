package com.contactmanager.helper;

public class Message {
	
	private String contact;
	private String type;
	public Message(String contact, String type) {
		super();
		this.contact = contact;
		this.type = type;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
