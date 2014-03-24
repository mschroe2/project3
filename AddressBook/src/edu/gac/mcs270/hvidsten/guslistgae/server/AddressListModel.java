/*
 * Model class for GusList app. 
 * Note: Model is on server side. Messages passed to Model
 *  from Controller must be done through RPC methods. 
 *  Model has static methods to simplify RPC calls 
 *    (see AdDataServiceImpl.java)
 */
package edu.gac.mcs270.hvidsten.guslistgae.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import edu.gac.mcs270.hvidsten.guslistgae.shared.EntryData;

public class AddressListModel {
	static final PersistenceManagerFactory pmf = PMF.get();
	static String appBaseURL="";
	static EntryData editingEntry =null;

//	public static List<EntryData> getEntryData() {
	//	PersistenceManager pm = pmf.getPersistenceManager();
		//Query query = pm.newQuery(EntryData.class);
		//List<EntryData> entries = (List<EntryData>) query.execute();
		// Child classes are loaded "lazily" - not until they are accessed.
		// To make sure they are loaded before the PersistenceManager closes,
		// we reference them here so they are forced to load.
		// NOT SO SURE WE NEED ANY OF THIS
		//for(EntryData entry: entries){
			//entry.getSeller();
			//entry.getBuyer();
	//	}
		//return new ArrayList<EntryData>(entries); BLOP BLOP
	//}
  
	public static void storeEntry(EntryData entry) {
		PersistenceManager pm = pmf.getPersistenceManager();
		// Transactions lock all records in a datastore and keep them locked 
		//  until they are ready to commit their changes. This eliminates
		//  possibility of conflict of access
		try {
			pm.currentTransaction().begin();
			pm.makePersistent(entry);
			pm.currentTransaction().commit();
		}
		finally {
		    if (pm.currentTransaction().isActive())
		      pm.currentTransaction().rollback();
		    if (!pm.isClosed())
		      pm.close();
		   }
	}
	
	public static void updateEditedEntry(EntryData entry, String name, String address, String city,
			String state, int zip, String email, int phone) {
		
		// If we change a classes field values while the 
		//  Persistencemanager is open, the fields are automatically 
		//  updated in the Datastore
		PersistenceManager pm = pmf.getPersistenceManager();
		// Get the datastore object so that we can update it.  
		EntryData storedEntry = pm.getObjectById(EntryData.class, entry.getID());

		// Keep alterations in a Transaction, so records are locked until commit
		// GET STUFF FROM ENTRY
		try {
			pm.currentTransaction().begin();
			storedEntry.setName(name);
			storedEntry.setAddress(address);
			storedEntry.setCity(city);
			storedEntry.setState(state);
			storedEntry.setZip(zip);
			storedEntry.setEmail(email);
			storedEntry.setPhone(phone);
			pm.currentTransaction().commit();
		}
		finally {
		    if (pm.currentTransaction().isActive())
		      pm.currentTransaction().rollback();
		    if (!pm.isClosed())
		      pm.close();
		   }
		
		// reset editing state
		AddressListModel.setEditingEntry(null);
	}

	public static List<EntryData> getTitleMatchData(String titleStr) {
		PersistenceManager pm = pmf.getPersistenceManager();
		//Declare a Query to be for matches to the title field of a EntryData object
		//  "titleVal" will be the parameter type for the field
		Query query = pm.newQuery(EntryData.class, "this.title == titleVal");
		// declare the parameter type to be a String
		query.declareParameters("String titleVal");
		// Execute the query with the String given by titleStr
		// Query returns a list of EntryData objects matching the query
		List<EntryData> entries = (List<EntryData>) query.execute(titleStr);
		return new ArrayList<EntryData>(entries);
	}

	public static void deleteEntry(EntryData entry) {
		PersistenceManager pm = pmf.getPersistenceManager();
		// Keep alterations in a Transaction, so records are locked until done
		try {
			pm.currentTransaction().begin();
			// Find the object in the datastore that matches the 
			//  class and ID of the post. Then, delete it. 
			pm.deletePersistent( pm.getObjectById(EntryData.class, entry.getID() ) );
			pm.currentTransaction().commit();
		}
		finally {
		    if (pm.currentTransaction().isActive())
		      pm.currentTransaction().rollback();
		    if (!pm.isClosed())
		      pm.close();
		   }
	}
	
	// Getters and setters of globally needed values
	public static void setAppBaseURL(String url){
		AddressListModel.appBaseURL = url;
	}
	
	public static String getAppBaseURL(){
		return AddressListModel.appBaseURL;
	}
	
	public static void setEditingEntry(EntryData entry){
		AddressListModel.editingEntry = entry;
	}
	
	public static EntryData getEditingEntry() {
		return AddressListModel.editingEntry;
	}

}
