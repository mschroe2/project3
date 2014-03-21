/*
 * Controller class for GusList app.
 * Handles communications between View (client side) and Model (server side) 
 */

package edu.gac.mcs270.hvidsten.guslistgae.client;

import java.util.List;

import edu.gac.mcs270.hvidsten.guslistgae.server.GusListModel;
import edu.gac.mcs270.hvidsten.guslistgae.shared.PostData;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;

public class GusList implements EntryPoint {
	private final GusListView glView = new GusListView();
	// Create the RPC services for client-server communication
	private final ClientModelServiceAsync clientModelService = GWT
			.create(ClientModelService.class);
	private final BlobServiceAsync blobService = GWT
			.create(BlobService.class);
	
	// Needed to keep track of the URL that is loaded when the app first loads
	private String homeURL ="";

	// Entry point for web app 
	public void onModuleLoad() {
		// Wire up the View-Controller interface
		glView.setController(GusList.this);
		// Store the url for where the app starts
		homeURL = Window.Location.getHref();
		
		// Tell the Model where the startup url is 
		// Note: Meat of implementation is in ClientModelServiceImpl.java
		clientModelService.setAppBaseURL(homeURL,
				new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						return;
					}
					public void onSuccess(String result) {
					}
				});
		
		//  Ask Model is a user is logged in 
		clientModelService.isUserLoggedIn(
				new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				return;
			}

			public void onSuccess(Boolean result) {
				if(result){ // If user is logged in 
					// Show welcome page
					glView.viewWelcomePage();
				}// Otherwise, set page to login page
				else glView.setWindow("../GusListWelcome.html");
			}
		});
	}
	
	
	public GusListView getView() {
		return glView;
	}
	
	// Client (View) request to view stored ad data
	public void viewAdDataFromServer(){
		clientModelService.getPostDataFromServer(
				new AsyncCallback<List<PostData>>() {
					public void onFailure(Throwable caught) {
						return;
					}

					@Override
					public void onSuccess(List<PostData> data) {
						glView.viewPostData(data);
					}
				});
	}

	// Client (View) request to do a search for ads matching a given title
	public void handleTitleSearchRequest(String title) {
		clientModelService.getTitleMatchDataFromServer(title,
				new AsyncCallback<List<PostData>>() {
			public void onFailure(Throwable caught) {
				return;
			}

			@Override
			public void onSuccess(List<PostData> data) {
				// If communication was successful, vie wthe list of matches 
				glView.viewPostData(data);
			}
		});
	}

	// Client (View) request to delete a post
	public void handleDeleteRequest(PostData post) {
		clientModelService.deletePostFromDB(post, 
				new AsyncCallback<List<PostData>>() {
			public void onFailure(Throwable caught) {
				return;  
			}
			public void onSuccess(List<PostData> data) {
				glView.sendSuccessfulDeleteMessage();
				glView.viewPostData(data);
			}
		});
	}

	// Client request to initiate a submittal of the Form data from View. 
	//  Note - this comes from clicking the "Submit" button.  
	// The actual submittal of the Form data through a doPost to server comes 
	// in that part of this method that is starrred ***
	public void handlePostFromSubmitForm(final FormPanel submitFormPanel) {
		blobService.getBlobStoreUploadUrl(
		  new AsyncCallback<String>() {
			public void onSuccess(String blobURL) {
				// Set the form action to the newly created
				// blobstore upload URL
				submitFormPanel.setAction(blobURL.toString());
				// *** Submit the form to complete the upload
				// This causes a doPost to sever from the HTML Form (FormPanel)
				submitFormPanel.submit();
				// This POST will be captured by SubmitpostHTTPServiceImpl.java
			}

			public void onFailure(Throwable caught) {
				glView.sendErrorMessage("Upload Failed");
			}
		});
	}

	// Client request to edit post by a re-submittal of Form data from View. 
	//  Note - this comes from clicking the "Submit" button.  
	// The actual submittal of the Form data through a doPost to server comes 
	// in that part of this method that is starrred ***
	public void handlePostEditFromSubmitForm(final PostData post, final FormPanel submitFormPanel) {
		// Store the current post under revision, so we can access it via 
		//  other server methods and classes
		clientModelService.setModelEditingPost(post, 
				new AsyncCallback<Void>() {
			public void onSuccess(Void result) {
			}
			public void onFailure(Throwable caught) {
			}
		});
		
		blobService.getBlobStoreUploadUrl(
		    new AsyncCallback<String>() {
				public void onSuccess(String blobURL) {
					// Set the form action to the newly created
					// blobstore upload URL
					System.out.println("edit blob url = "+blobURL.toString());
					submitFormPanel.setAction(blobURL.toString());
					// *** Submit the form to complete the upload
					// This causes a doPost to sever from the HTML Form (FormPanel)
					submitFormPanel.submit();
					// This POST will be captured by SubmitpostHTTPServiceImpl.java
				}

				public void onFailure(Throwable caught) {
					glView.sendErrorMessage("Upload Failed");
				}
			});
	}
	
	// Client (View) request to have Model sign the user out. 
	public void handleSignOutRequest() {
		clientModelService.getLogOutUrl(
				new AsyncCallback<String>() {
					public void onSuccess(String url) {
						// If logout was successful, we set the window
						//  to the logout url returned by clientModelService.
						//      (../GusListThanks.html)
						glView.setWindow(url);
					}
					public void onFailure(Throwable caught) {
						glView.sendErrorMessage("Cannot find LogOut URL");
					}
				});
	}

}
