package edu.gac.mcs270.hvidsten.guslistgae.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.gac.mcs270.hvidsten.guslistgae.shared.EntryData;

public interface ClientModelServiceAsync {

	void deletePostFromDB(EntryData post, AsyncCallback<List<EntryData>> callback);

	void getPostDataFromServer(AsyncCallback<List<EntryData>> callback);

	void getTitleMatchDataFromServer(String title,
			AsyncCallback<List<EntryData>> callback);

	void submitPostToServer(EntryData post, AsyncCallback<String> callback);

	void isUserLoggedIn(AsyncCallback<Boolean> callback);

	void getLogOutUrl(AsyncCallback<String> callback);

	void setAppBaseURL(String homeURL, AsyncCallback<String> asyncCallback);

	void setModelEditingPost(EntryData post, AsyncCallback<Void> callback);

}
