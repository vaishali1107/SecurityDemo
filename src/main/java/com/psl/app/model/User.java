package com.psl.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_data")
public class User {

	@Id
	private int id;
	
	private String username;
	private String password;
	private int age;
	private double salary;
	private String role;
	
	
	public User() {
		super();
	}
	public User(int id, String username, String password, int age, double salary, String role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.age = age;
		this.salary = salary;
		this.role = role;
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", age=" + age + ", salary="
				+ salary + ", role=" + role + "]";
	}
	
	
}
