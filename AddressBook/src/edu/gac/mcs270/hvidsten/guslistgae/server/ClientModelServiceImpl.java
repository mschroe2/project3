/**
 * The server side implementation of the RPC service.
 * This is mainly a conduit for communication messages to GusListModel
 * 
 */

package edu.gac.mcs270.hvidsten.guslistgae.server;

import java.util.List;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.gac.mcs270.hvidsten.guslistgae.client.ClientModelService;
import edu.gac.mcs270.hvidsten.guslistgae.shared.EntryData;


@SuppressWarnings("serial")
public class ClientModelServiceImpl extends RemoteServiceServlet implements
                                   ClientModelService {
	public List<EntryData> getPostDataFromServer() { 
		return AddressListModel.getPostData();
	}
	
	public List<EntryData> getTitleMatchDataFromServer(String title){
		return AddressListModel.getTitleMatchData(title);
	}

	public String submitPostToServer(EntryData post) {
		AddressListModel.storePost(post);
		return "post submitted okay";
	}

	public List<EntryData> deletePostFromDB(EntryData post){
		AddressListModel.deletePost(post);
		return AddressListModel.getPostData();
	}
	
	public String getLogOutUrl(){
		UserService userService = UserServiceFactory.getUserService();
		return userService.createLogoutURL("../GusListThanks.html");
	}

	public String setAppBaseURL(String homeURL){
		AddressListModel.setAppBaseURL(homeURL);
		return null;
	}
	
	public Boolean isUserLoggedIn() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		return new Boolean(user!=null);
	}

	public void setModelEditingPost(EntryData post) {
		AddressListModel.setEditingPost(post);
	}
}
