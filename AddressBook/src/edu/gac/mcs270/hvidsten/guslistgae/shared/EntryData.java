/*
 * PostData class.  Represents a posted ad. 
 */
package edu.gac.mcs270.hvidsten.guslistgae.shared;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
public class EntryData implements Serializable {
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	// Need to define the key this way so that a Seller can be passed
	// as data through RPC services.   
	// See https://developers.google.com/appengine/docs/java/datastore/jdo/creatinggettinganddeletingdata#Keys
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;

	@Persistent
	private String name="no name";
	@Persistent
	private String address="empty";
	@Persistent
	private String city="empty";
	@Persistent
	private String state="empty";
	@Persistent
	private int zip= 0;
	@Persistent
	private String email="empty";
	@Persistent
	private int phone= 0;

	// GWT serializable Objects need a no=argument constructor
	public EntryData() {}

	public EntryData(String n, String add, String c, String s, int z, String em, int ph){
		name = n;
		address = add;
		city = c;
		state = s;
		zip = z;
		email = em;
		phone = ph;

	}

	// Getters and Setters
	public String getName(){
		return name;
	}
	public String getAddress(){
		return address;
	}
	public String getCity(){
		return city;
	}
	public String getState(){
		return state;
	}
	public int getZip(){
		return zip;
	}
	public String getEmail(){
		return email;
	}
	public int getPhone(){
		return phone;
	}

	public void setName(String n){
		name = n;
	}
	public void setAddress(String add){
		address = add;
	}
	public void setCity(String c){
		city = c;
	}
	public void setState(String s){
		state = s;
	}
	public void setZip(int z){
		zip = z;
	}
	public void setEmail(String e){
		email = e;
	}
	public void setPhone(int ph){
		phone = ph;
	}

	public String getID() {
		return id;
	}

	public String stringRep() {	
		return "Name: " + name +
				"\n Address: " + address +
				"\n          " + city+", " + state + " " + zip +
				"\n E-mail: " + email +
				"\n Phone: " + phone;
	}

}