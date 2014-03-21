/*
 * RPC Servlet to handle client<->server communication 
 */

package edu.gac.mcs270.hvidsten.guslistgae.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.gac.mcs270.hvidsten.guslistgae.shared.PostData;

// Note: Name needs to match url-pattern in web.xml
@RemoteServiceRelativePath("clientmodelservice") 
public interface ClientModelService extends RemoteService {
	public List<PostData> getPostDataFromServer();
	public List<PostData> getTitleMatchDataFromServer(String title);
	public String submitPostToServer(PostData post);
	public List<PostData> deletePostFromDB(PostData post);
	public Boolean isUserLoggedIn();
	public String getLogOutUrl();
	public String setAppBaseURL(String homeURL);
	public void setModelEditingPost(PostData post);
}

