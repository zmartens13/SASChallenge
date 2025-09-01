package com.Nash.Packages.domain;

import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String title;
	
	private String desciption;
	
	private LocalDate dueDate;
	
	private Status status;

	public Task(String title, String desciption, LocalDate date) {
		super();
		this.setTitle(title);
		this.setDesciption(desciption);
		// Could validate date format
		this.setDate(date);
		this.setStatus(Status.PENDING);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

	public LocalDate getDate() {
		return dueDate;
	}

	public void setDate(LocalDate date) {
		this.dueDate = date;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status pending) {
		this.status = pending;
	}

	@Override
	public String toString() {
		return "title=" + title + ", desciption=" + desciption + ", dueDate=" + dueDate + ", status=" + status;
	}
		
}
