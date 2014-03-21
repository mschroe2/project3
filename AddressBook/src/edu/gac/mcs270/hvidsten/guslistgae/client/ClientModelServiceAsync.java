package edu.gac.mcs270.hvidsten.guslistgae.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.gac.mcs270.hvidsten.guslistgae.shared.PostData;

public interface ClientModelServiceAsync {

	void deletePostFromDB(PostData post, AsyncCallback<List<PostData>> callback);

	void getPostDataFromServer(AsyncCallback<List<PostData>> callback);

	void getTitleMatchDataFromServer(String title,
			AsyncCallback<List<PostData>> callback);

	void submitPostToServer(PostData post, AsyncCallback<String> callback);

	void isUserLoggedIn(AsyncCallback<Boolean> callback);

	void getLogOutUrl(AsyncCallback<String> callback);

	void setAppBaseURL(String homeURL, AsyncCallback<String> asyncCallback);

	void setModelEditingPost(PostData post, AsyncCallback<Void> callback);

}
