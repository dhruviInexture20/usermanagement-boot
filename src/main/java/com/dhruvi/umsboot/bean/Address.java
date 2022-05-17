package com.dhruvi.umsboot.bean;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.google.gson.annotations.Expose;

@Entity
@Table(name="userAddress")
public class Address implements Serializable {

	private static final long serialVersionUID = 1L;

	@Expose
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int addressid;
	
	@NotBlank(message = "Street Address cannot be empty")
	@Expose
	private String street_address;
	@NotBlank(message = "City cannot be empty")
	@Expose
	private String city;
	@NotBlank(message = "Country cannot be empty")
	@Expose
	private String country;
	@NotBlank(message = "State cannot be empty")
	@Expose
	private String state;
	@NotBlank(message = "Postal code cannot be empty")
	@Expose
	private String postal_code;
	
	@ManyToOne
	private User user;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getAddressid() {
		return addressid;
	}
	public void setAddressid(int addressid) {
		this.addressid = addressid;
	}
	public String getStreet_address() {
		return street_address;
	}
	public void setStreet_address(String street_address) {
		this.street_address = street_address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPostal_code() {
		return postal_code;
	}
	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}
	
	
	
}