/*
 * View class for GusList app.
 * Handles all GUI components and event callback mechanisms 
 */

package edu.gac.mcs270.hvidsten.guslistgae.client;

import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.Geocoder.Callback;

import edu.gac.mcs270.hvidsten.guslistgae.shared.PostData;

public class GusListView {
	private GusList control;
	// Popup panels need to be instantiated as final - just once
	final PopupPanel searchPopup = new PopupPanel(false);
	final PopupPanel mapPopup = new PopupPanel(false);

	public GusListView(){}
	
	// For wiring up the MVC architecture
	public void setController(GusList gusList) {
		control = gusList;
	}

	public GusList getController() {
		return control;
	}
	
	//  Welcome Page is main app page
	public void viewWelcomePage(){
		RootPanel rootPanel = RootPanel.get();
		rootPanel.clear();
		makeMenuBar(rootPanel);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		rootPanel.add(horizontalPanel, 10, 79);
		horizontalPanel.setSize("412px", "211px");
		
		makeSideBar(horizontalPanel);
	}

	// PostData page contains a list of Posts
	//  Created when user clicks "View Posts"
	public void viewPostData(List<PostData> posts) {
		if(posts==null) return;
		
		RootPanel rootPanel = RootPanel.get();
		rootPanel.clear();
		makeMenuBar(rootPanel);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		rootPanel.add(horizontalPanel, 10, 79);
		
		makeSideBar(horizontalPanel);
		
		VerticalPanel dataListPanel = new VerticalPanel();
		horizontalPanel.add(dataListPanel);
		
		FlowPanel flowPanel = new FlowPanel();
		dataListPanel.add(flowPanel);
		
		Label programTitlebar = new Label("GusList");
		programTitlebar.addStyleName("appTitleBar");
		programTitlebar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		flowPanel.add(programTitlebar);
		
		makePostTable(posts, flowPanel, dataListPanel);
	}
	
	// Post Create Page - page for input of post data and submitting it to 
	//  datastore.  Created when user clicks "Post Ad".
	public void viewPostAdForm() {
		RootPanel rootPanel = RootPanel.get();
		rootPanel.clear();
		makeMenuBar(rootPanel);
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		rootPanel.add(mainPanel, 10, 79);
		
		makeSideBar(mainPanel);
		
		FormPanel submitPanel = makeSubmitPostForm(null);
		mainPanel.add(submitPanel);
	}
	
	public void viewPostEditForm(PostData post){
		RootPanel rootPanel = RootPanel.get();
		rootPanel.clear();
		makeMenuBar(rootPanel);
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		rootPanel.add(mainPanel, 10, 79);
		
		makeSideBar(mainPanel);
		
		FormPanel submitPanel = makeSubmitPostForm(post);
		mainPanel.add(submitPanel);
	}
	
	// Helper method to create the internals of the HTML Form (FormPanel)
	//  Submit form may be for new post, or for editing existing post
	private FormPanel makeSubmitPostForm(final PostData post){
		final FormPanel submitFormPanel = new FormPanel();
		VerticalPanel postFormPanel = new VerticalPanel();
		postFormPanel.addStyleName("submitPostVertPanel");
		submitFormPanel.add(postFormPanel);
		
		// Name input
		HorizontalPanel nameRow = new HorizontalPanel();
		Label nameLabel = new Label("Name (First Last");
		final TextBox nameTextbox = new TextBox();
		nameRow.add(nameLabel);
		nameRow.add(nameTextbox);
		postFormPanel.add(nameRow);
				
		// Title input
		HorizontalPanel titleRow = new HorizontalPanel();
		Label titleLabel = new Label("Title (e.g. car, bike, etc)");
		final TextBox titleTextbox = new TextBox();
		titleRow.add(titleLabel);
		titleRow.add(titleTextbox);
		postFormPanel.add(titleRow);
				
		// Description input
		HorizontalPanel descrRow = new HorizontalPanel();
		Label descrLabel = new Label("Item Short description");
		final TextArea descrText = new TextArea();
		descrText.setCharacterWidth(40);
		descrText.setVisibleLines(10);
		descrRow.add(descrLabel);
		descrRow.add(descrText);
		postFormPanel.add(descrRow);
				
		// Price input
		HorizontalPanel priceRow = new HorizontalPanel();
		Label priceLabel = new Label("Price ($)");
		final TextBox priceTextbox = new TextBox();
		priceTextbox.setVisibleLength(6);
		priceRow.add(priceLabel);
		priceRow.add(priceTextbox);
		postFormPanel.add(priceRow);
		
		// Address input
		HorizontalPanel addressRow = new HorizontalPanel();
		Label addressLabel = new Label("Address");
		final TextArea addressText = new TextArea();
		addressText.setCharacterWidth(40);
		addressText.setVisibleLines(5);
		addressRow.add(addressLabel);
		addressRow.add(addressText);
		postFormPanel.add(addressRow);

		if(post!=null) {
			nameTextbox.setText(post.getSellerName());
			titleTextbox.setText(post.getTitle());
			descrText.setText(post.getDescription());
			priceTextbox.setText(""+post.getPrice());
			addressText.setText(post.getAddress());
		}
		
		// New widget for file upload
		HorizontalPanel fileRow = new HorizontalPanel();
		final FileUpload upload = new FileUpload();
		if(post!=null) {
			Anchor link = new Anchor("Stored File", post.getURL());
			link.setTarget("_blank");
			fileRow.add(link);
		    upload.setTitle("Change Uploaded File");
		}
		else upload.setTitle("Upload a File for Post");
		fileRow.add(upload);
		postFormPanel.add(fileRow);
		
		// Submit button
		Button submitButton;
		if(post!=null) {
			submitButton = new Button("Submit Changes");
			submitButton.setText("Submit Changes");
		}
		else {
			submitButton = new Button("Submit Ad");
			submitButton.setText("Submit Ad");
		}
		submitButton.setStyleName("sideBarButton");
		postFormPanel.add(submitButton);
		
		// The submitFormPanel, when submitted, will trigger an HTTP call to the
		// servlet.  The following parameters must be set
		submitFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		submitFormPanel.setMethod(FormPanel.METHOD_POST);

		// Set Names for the text boxes so that they can be retrieved from the
		// HTTP call as parameters
		nameTextbox.setName("name");
		titleTextbox.setName("title");
		descrText.setName("description");
		priceTextbox.setName("price");
		addressText.setName("address");
		upload.setName("upload");
		
		// Upon clicking "Submit" control goes to Controller
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(post == null)
					control.handlePostFromSubmitForm(submitFormPanel);
				else 
					control.handlePostEditFromSubmitForm(post, submitFormPanel);
			}});
		
		// This event handler is "fired" just before the Form causes a doPost 
		//  message to server
		submitFormPanel.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				// This event is fired just before the form is submitted.
				//  We can take this opportunity to perform validation.
				if (nameTextbox.getText().length() == 0) {
					Window.alert("The author is required.");
					event.cancel();
				}
				if (titleTextbox.getText().length() == 0) {
					Window.alert("The title is required.");
					event.cancel();
				}
				Double price = Double.parseDouble(priceTextbox.getText());
				if (priceTextbox.getText().length() == 0 || Double.isNaN(price)) {
					Window.alert("The price is required. It must be a valid number");
					event.cancel();
				}
				if (addressText.getText().length() == 0) {
					Window.alert("The address is required.");
					event.cancel();
				}
				if (upload.getFilename().length() == 0 && post == null) {
					Window.alert("The upload file is required.");
					event.cancel();
				}
			}
		});

		// The doPost message itself is sent from the Form and not intercepted
		//  by GWT.  

		// This event handler is "fired" just after the Form causes a doPost 
		//  message to server
		submitFormPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				if(post ==null) {
					submitFormPanel.reset();
					nameTextbox.setFocus(true);
				}
				else control.viewAdDataFromServer();
			}

		});
		return submitFormPanel;
	}
	
	// Next two methods are Helper methods to create list of posts
	private void makePostTable(List<PostData> posts, FlowPanel flowPanel, VerticalPanel mainPanel) {
		for(PostData post: posts){
			if(post !=null)
				flowPanel.add(makePostRow(post, mainPanel));
			else System.out.println("null post");
		}
	}

	private HorizontalPanel makePostRow(final PostData post, final VerticalPanel mainPanel) {
		HorizontalPanel row = new HorizontalPanel();
		Label titleLabel = new Label("Title: "+post.getTitle()+" ");
		titleLabel.addStyleName("postLabel");
		Label descrLabel = new Label(post.getDescription()+" ");
		descrLabel.addStyleName("postLabel");
		Label priceLabel = new Label("Price: $"+post.getPrice()+" ");
		priceLabel.addStyleName("postLabel");
		Label nameLabel = new Label("Seller Name: "+post.getSellerName()+" ");
		nameLabel.addStyleName("postLabel");
		Anchor link = new Anchor("Uploaded File", post.getURL());
		link.setTarget("_blank");
		Button addressButton = new Button("Address");
		addressButton.addStyleName("postInfoButton");
		addressButton.setText("Address");
		//add a clickListener to the button
		addressButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doMapPopup(post);
			}
	      });
		
		Button deleteButton = new Button("Delete");
		deleteButton.addStyleName("postInfoButton");
		deleteButton.setText("Delete");
		//add a clickListener to the button
		deleteButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				control.handleDeleteRequest(post);
			}
	      });
		Button editButton = new Button("Edit");
		editButton.addStyleName("postInfoButton");
		editButton.setText("Edit");
		//add a clickListener to the button
		editButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				viewPostEditForm(post);
			}
	      });
		
		row.add(titleLabel);
		row.add(descrLabel);
		row.add(priceLabel);
		row.add(nameLabel);
		row.add(link);
		row.add(new Label("  "));
		row.add(addressButton);
		row.add(new Label("  "));
		row.add(editButton);
		row.add(new Label("  "));
		row.add(deleteButton);
		return row;
	}

	// Helper method to make row of menuss on top of View
	public void makeMenuBar(RootPanel rp){
		MenuBar menuBar = new MenuBar(false);
		rp.add(menuBar, 94, 39);
		
		MenuItem menuHomeItem = new MenuItem("Home", false, new Command() {
			public void execute() {
				viewWelcomePage();
			}
		});
		menuHomeItem.setHTML("Home");
		menuBar.addItem(menuHomeItem);
		menuBar.addSeparator(new MenuItemSeparator());
		
		MenuItem menuSearchItem = new MenuItem("Search", false, new Command() {
			public void execute() {
				doPostSearch();
			}
		});
		menuSearchItem.setHTML("Search");
		menuBar.addItem(menuSearchItem);
		menuBar.addSeparator(new MenuItemSeparator());
		
		MenuItem menuSignOutItem = new MenuItem("Log Out", false, new Command() {
			public void execute() {
				 control.handleSignOutRequest();
			}
		});
		menuSignOutItem.setHTML("Log Out");
		menuBar.addItem(menuSignOutItem);
		menuBar.addSeparator(new MenuItemSeparator());
		
		MenuItem menuContactItem = new MenuItem("Contact", false, (Command) null);
		menuContactItem.setHTML("Contact");
		menuBar.addItem(menuContactItem);
		menuBar.addSeparator(new MenuItemSeparator());
		
		MenuItem menuHelpItem = new MenuItem("Help", false, (Command) null);
		menuHelpItem.setHTML("Help");
		menuBar.addItem(menuHelpItem);
	}

	// Helper method to make list of buttons on left side of View
	public void makeSideBar(HorizontalPanel hp){
		VerticalPanel sidePanel = new VerticalPanel();
		hp.add(sidePanel);
		sidePanel.setSize("72px", "98px");
		
		Button postAdButton = new Button("Post Ad");
		postAdButton.setStyleName("sideBarButton");
		postAdButton.setText("Post Ad");
		//add a clickListener to the button
		postAdButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				viewPostAdForm();
			}
	      });
		sidePanel.add(postAdButton);
		
		Button viewAdsButton = new Button("View Ads");
		viewAdsButton.setStyleName("sideBarButton");
		viewAdsButton.setText("View Ads");
		//add a clickListener to the button
		viewAdsButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				control.viewAdDataFromServer();
			}
	      });
		sidePanel.add(viewAdsButton);
		
		Hyperlink adminHyperlink = new Hyperlink("Admin Page", false, "newHistoryToken");
		sidePanel.add(adminHyperlink);
		
	}

	// Method response to user click of "Search" menu in top menubar.
	// Causes popup panel to appear
	protected void doPostSearch() {		
		VerticalPanel content = new VerticalPanel();
		content.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		HorizontalPanel inputRow = new HorizontalPanel();
		Label searchTermLabel = new Label("Search Title Term: ");
		final TextBox searchTermTextBox = new TextBox();
		inputRow.add(searchTermLabel);
		inputRow.add(searchTermTextBox);
		
		HorizontalPanel btnRow = new HorizontalPanel();
		btnRow.setStyleName("search-button-row");
		Button cancelBtn = new Button("Cancel");
		cancelBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchPopup.hide();
			}
	      });
		Button searchBtn = new Button("Search");
		searchBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				control.handleTitleSearchRequest(searchTermTextBox.getText());
				searchPopup.hide();
			}
	      });
		btnRow.add(cancelBtn);
		btnRow.add(new Label(""));
		btnRow.add(searchBtn);
		
		content.add(inputRow);
		content.add(btnRow);
		searchPopup.setWidget(content);
		searchPopup.center();
	}
	
	// Method response to user click of "Address" button in a post row
	// Causes popup panel to appear
	protected void doMapPopup(PostData post) {		
		VerticalPanel content = new VerticalPanel();
		content.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		final String address = post.getAddress();
		
		// Create a Panel to hold the map
		HorizontalPanel mapPanel = new HorizontalPanel();
		mapPanel.setSize("400px", "400px");
	    
	    // Create the Google Map object, with set options
	    MapOptions myOptions = MapOptions.create();
	    myOptions.setZoom(8.0);
	    myOptions.setCenter(LatLng.create(-34.397, 150.644));
	    myOptions.setMapTypeId(MapTypeId.ROADMAP);
	    final GoogleMap map= GoogleMap.create(mapPanel.getElement(), myOptions);
	    
	    // GeoCoder is Google Maps API for taking an address and finding 
	 	//  latitude-longitude. 
	 	Geocoder geocoder = Geocoder.create();
	 	GeocoderRequest request = GeocoderRequest.create();
	 	request.setAddress(address);
	    
	    geocoder.geocode(request, new Callback() {
	        public void handle(JsArray<GeocoderResult> results, GeocoderStatus status) {
	          if (status == GeocoderStatus.OK) {
	            GeocoderResult location = results.get(0);
	            // Bug Fix - Needed to make sure that map is re-sized to parent panel
	            map.triggerResize();
	            map.setCenter(location.getGeometry().getLocation());
	            // Create Marker (red) to show location
	            MarkerOptions markerOpts = MarkerOptions.create();
	            markerOpts.setMap(map);
	            // Title of Marker will be address
	            markerOpts.setTitle(address);
	            // Place Marker at address location
	            markerOpts.setPosition(location.getGeometry().getLocation());
	            Marker.create(markerOpts);
	          } else {
	            Window.alert("Geocode was not successful for the following reason: "
	                + status);
	          }
	        }
	      });
		
		HorizontalPanel btnRow = new HorizontalPanel();
		btnRow.setStyleName("search-button-row");
		Button cancelBtn = new Button("Cancel");
		cancelBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				searchPopup.hide();
			}
	      });
		btnRow.add(cancelBtn);
		
		content.add(mapPanel);
		content.add(btnRow);
		searchPopup.setWidget(content);
		searchPopup.center();
	}
	
	public void sendSuccessfulPostmessage() {
		Window.alert("Post was successfully stored.");
	}

	public void sendSuccessfulDeleteMessage() {
		Window.alert("Post was deleted successfully");
	}  

	public void sendErrorMessage(String msg) {
		Window.alert(msg);  
	}

	public void setWindow(String url) {
		Window.Location.replace(url);
	}
}
