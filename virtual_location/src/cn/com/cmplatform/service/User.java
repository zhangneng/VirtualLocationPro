package cn.com.cmplatform.service;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {

	private int id;
	private String username;
	private String password;

	public User() {
		super();
	}

	public User(String name, String password) {
		this.username = name;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [id = " + id + ",username=" + username + ",password="
				+ password + "]";
	}
}
