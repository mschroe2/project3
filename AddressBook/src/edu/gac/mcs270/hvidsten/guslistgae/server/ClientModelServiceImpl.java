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
import edu.gac.mcs270.hvidsten.guslistgae.shared.PostData;


@SuppressWarnings("serial")
public class ClientModelServiceImpl extends RemoteServiceServlet implements
                                   ClientModelService {
	public List<PostData> getPostDataFromServer() { 
		return GusListModel.getPostData();
	}
	
	public List<PostData> getTitleMatchDataFromServer(String title){
		return GusListModel.getTitleMatchData(title);
	}

	public String submitPostToServer(PostData post) {
		GusListModel.storePost(post);
		return "post submitted okay";
	}

	public List<PostData> deletePostFromDB(PostData post){
		GusListModel.deletePost(post);
		return GusListModel.getPostData();
	}
	
	public String getLogOutUrl(){
		UserService userService = UserServiceFactory.getUserService();
		return userService.createLogoutURL("../GusListThanks.html");
	}

	public String setAppBaseURL(String homeURL){
		GusListModel.setAppBaseURL(homeURL);
		return null;
	}
	
	public Boolean isUserLoggedIn() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		return new Boolean(user!=null);
	}

	public void setModelEditingPost(PostData post) {
		GusListModel.setEditingPost(post);
	}
}
