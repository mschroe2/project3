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

import edu.gac.mcs270.hvidsten.guslistgae.shared.PostData;

public class GusListModel {
	static final PersistenceManagerFactory pmf = PMF.get();
	static String appBaseURL="";
	static PostData editingPost =null;

	public static List<PostData> getPostData() {
		PersistenceManager pm = pmf.getPersistenceManager();
		Query query = pm.newQuery(PostData.class);
		List<PostData> posts = (List<PostData>) query.execute();
		// Child classes are loaded "lazily" - not until they are accessed.
		// To make sure they are loaded before the PersistenceManager closes,
		// we reference them here so they are forced to load.  
		for(PostData post: posts){
			post.getSeller();
			post.getBuyer();
		}
		return new ArrayList<PostData>(posts);
	}
  
	public static void storePost(PostData post) {
		PersistenceManager pm = pmf.getPersistenceManager();
		// Transactions lock all records in a datastore and keep them locked 
		//  until they are ready to commit their changes. This eliminates
		//  possibility of conflict of access
		try {
			pm.currentTransaction().begin();
			pm.makePersistent(post);
			pm.currentTransaction().commit();
		}
		finally {
		    if (pm.currentTransaction().isActive())
		      pm.currentTransaction().rollback();
		    if (!pm.isClosed())
		      pm.close();
		   }
	}
	
	public static void updateEditedPost(PostData post, String name,
			String title, String descr, String address, double price, String url) {
		
		// If we change a classes field values while the 
		//  Persistencemanager is open, the fields are automatically 
		//  updated in the Datastore
		PersistenceManager pm = pmf.getPersistenceManager();
		// Get the datastore object so that we can update it.  
		PostData storedPost = pm.getObjectById(PostData.class, post.getID());

		// Keep alterations in a Transaction, so records are locked until commit
		try {
			pm.currentTransaction().begin();
			storedPost.setSellerName(name);
			storedPost.setTitle(title);
			storedPost.setDescr(descr);
			storedPost.setAddress(address);
			storedPost.setPrice(price);
			storedPost.setURL(url);
			pm.currentTransaction().commit();
		}
		finally {
		    if (pm.currentTransaction().isActive())
		      pm.currentTransaction().rollback();
		    if (!pm.isClosed())
		      pm.close();
		   }
		
		// reset editing state
		GusListModel.setEditingPost(null);
	}

	public static List<PostData> getTitleMatchData(String titleStr) {
		PersistenceManager pm = pmf.getPersistenceManager();
		//Declare a Query to be for matches to the title field of a PostData object
		//  "titleVal" will be the parameter type for the field
		Query query = pm.newQuery(PostData.class,
				"this.title == titleVal");
		// declare the parameter type to be a String
		query.declareParameters("String titleVal");
		// Execute the query with the String given by titleStr
		// Query returns a list of PostData objects matching the quesry
		List<PostData> posts = (List<PostData>) query.execute(titleStr);
		return new ArrayList<PostData>(posts);
	}

	public static void deletePost(PostData post) {
		PersistenceManager pm = pmf.getPersistenceManager();
		// Keep alterations in a Transaction, so records are locked until done
		try {
			pm.currentTransaction().begin();
			// Find the object in the datastore that matches the 
			//  class and ID of the post. Then, delete it. 
			pm.deletePersistent( pm.getObjectById(PostData.class, post.getID() ) );
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
		GusListModel.appBaseURL = url;
	}
	
	public static String getAppBaseURL(){
		return GusListModel.appBaseURL;
	}
	
	public static void setEditingPost(PostData post){
		GusListModel.editingPost = post;
	}
	
	public static PostData getEditingPost() {
		return GusListModel.editingPost;
	}

}
