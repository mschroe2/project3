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


public class AddressListView {
	private AddressList control;
	final PopupPanel searchPopup = new PopupPanel(false);
	final PopupPanel mapPopup = new PopupPanel(false);

	public AddressListView(){}

	public void setController(AddressList addressList) {
			control = addressList;
		}

		public AddressList getController() {
			return control;
		}


		// PostData page contains a list of Posts
		//  Created when user clicks "View Posts"
		public void viewAddressData(List<EntryData> entries) {
			if(entries==null) return;

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

			Label programTitlebar = new Label("Address Book");
			programTitlebar.addStyleName("appTitleBar");
			programTitlebar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			flowPanel.add(programTitlebar);

			makePostTable(entries, flowPanel, dataListPanel);
		}

		// Post Create Page - page for input of post data and submitting it to 
		//  datastore.  Created when user clicks "Post Ad".
		public void viewAddressForm() {
			RootPanel rootPanel = RootPanel.get();
			rootPanel.clear();
			makeMenuBar(rootPanel);

			HorizontalPanel mainPanel = new HorizontalPanel();
			rootPanel.add(mainPanel, 10, 79);

			makeSideBar(mainPanel);

			FormPanel submitPanel = makeSubmitAddressForm(null);
			mainPanel.add(submitPanel);
		}

		public void viewAddressEditForm(EntryData entries){
			RootPanel rootPanel = RootPanel.get();
			rootPanel.clear();
			makeMenuBar(rootPanel);

			HorizontalPanel mainPanel = new HorizontalPanel();
			rootPanel.add(mainPanel, 10, 79);

			makeSideBar(mainPanel);

			FormPanel submitPanel = makeSubmitAddressForm(entries);
			mainPanel.add(submitPanel);
		}

		// Helper method to create the internals of the HTML Form (FormPanel)
		//  Submit form may be for new post, or for editing existing post
		private FormPanel makeSubmitAddressForm(final EntryData entries){
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

			// Address input
			HorizontalPanel addressRow = new HorizontalPanel();
			Label addressLabel = new Label("Address");
			final TextBox addressTextbox = new TextBox();
			addressRow.add(addressLabel);
			addressRow.add(addressTextbox);
			postFormPanel.add(addressRow);

			// City input
			HorizontalPanel cityRow = new HorizontalPanel();
			Label cityLabel = new Label("City");
			final TextBox cityTextbox = new TextBox();
			cityRow.add(cityLabel);
			cityRow.add(cityTextbox);
			postFormPanel.add(cityRow);

			// State input
			HorizontalPanel stateRow = new HorizontalPanel();
			Label stateLabel = new Label("State");
			final TextBox stateTextbox = new TextBox();
			stateRow.add(stateLabel);
			stateRow.add(stateTextbox);
			postFormPanel.add(stateRow);

			// Zip input
			HorizontalPanel zipRow = new HorizontalPanel();
			Label zipLabel = new Label("Zip");
			final TextBox zipTextbox = new TextBox();
			zipTextbox.setVisibleLength(5);
			zipRow.add(zipLabel);
			zipRow.add(zipTextbox);
			postFormPanel.add(zipRow);

			// email input
			HorizontalPanel emailRow = new HorizontalPanel();
			Label emailLabel = new Label("email");
			final TextBox emailTextbox = new TextBox();
			emailRow.add(emailLabel);
			emailRow.add(emailTextbox);
			postFormPanel.add(emailRow);

			// Phone input
			HorizontalPanel phoneRow = new HorizontalPanel();
			Label priceLabel = new Label("Phone");
			final TextBox phoneTextbox = new TextBox();
			phoneTextbox.setVisibleLength(10);
			phoneRow.add(priceLabel);
			phoneRow.add(phoneTextbox);
			postFormPanel.add(phoneRow);
/**
			// Address input EXTRA CREDIT!
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

			// New widget for file upload EXTRA CREDIT - EDIT
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
*/
			// add button
			Button addContactButton;
			if(post!=null) {
				addContactButton = new Button("Edit Contact");
				addContactButton.setText("Edit Contact");
			}
			else {
				addContactButton = new Button("Add Contact");
				addContactButton.setText("Add Contact");
			}
			addContactButton.setStyleName("sideBarButton");
			postFormPanel.add(addContactButton);

			// The submitFormPanel, when submitted, will trigger an HTTP call to the
			// servlet.  The following parameters must be set
			submitFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
			submitFormPanel.setMethod(FormPanel.METHOD_POST);

			// Set Names for the text boxes so that they can be retrieved from the
			// HTTP call as parameters
			nameTextbox.setName("name");
			addressTextbox.setName("address");
			cityTextbox.setName("city");
			stateTextbox.setName("state");
			zipTextbox.setName("zip");
			addressTextbox.setName("email");
			phoneTextbox.setName("phone");
			upload.setName("upload");

			// Upon clicking "Submit" control goes to Controller
			addContactButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(post == null)
						control.handlePostFromSubmitForm(submitFormPanel);
					else 
						control.handlePostEditFromSubmitForm(post, submitFormPanel);
				}});

			// This event handler is "fired" just before the Form causes a doPost 
			//  message to server
			submitFormPanel.addSubmitHandler(new FormPanel.SubmitHandler() {
				public void onSubmit(SubmitAddress event) {
					// This event is fired just before the form is submitted.
					//  We can take this opportunity to perform validation.
					if (nameTextbox.getText().length() == 0) {
						Window.alert("The name is required.");
						event.cancel();
					}
					if (addressTextbox.getText().length() == 0) {
						Window.alert("The address is required.");
						event.cancel();
					}
					if (cityTextbox.getText().length() == 0) {
						Window.alert("The city is required.");
						event.cancel();
					}
					if (stateTextbox.getText().length() == 0) {
						Window.alert("The state is required.");
						event.cancel();
					}
					if (zipTextbox.getText().length() == 0 || Double.isNaN(price)) {
						Window.alert("The zip is required. It must be a valid number");
						event.cancel();
					}
					if (emailTextbox.getText().length() == 0) {
						Window.alert("The email is required.");
						event.cancel();
					}
					if (phoneTextbox.getText().length() == 0 || Double.isNaN(price)) {
						Window.alert("A phone number is required. It must be a valid number");
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
		private void makePostTable(List<EntryData> entries, FlowPanel flowPanel, VerticalPanel mainPanel) {
			for(EntryData post: entries){
				if(post !=null)
					flowPanel.add(makePostRow(post, mainPanel));
				else System.out.println("null post");
			}
		}

		private HorizontalPanel makePostRow(final EntryData entries, final VerticalPanel mainPanel) {
			HorizontalPanel row = new HorizontalPanel();
			Label nameLabel = new Label("Name: "+entries.getName()+" ");
			nameLabel.addStyleName("postLabel");
			Label addressLabel = new Label(entries.getAddress()+" ");
			addressLabel.addStyleName("postLabel");
			Label cityLabel = new Label(entries.getCity()+" ");
			cityLabel.addStyleName("postLabel");
			Label stateLabel = new Label(entries.getState()+" ");
			stateLabel.addStyleName("postLabel");
			Label zipLabel = new Label("Zip: "+entries.getZip()+" ");
			zipLabel.addStyleName("postLabel");
			Label emailLabel = new Label(entries.getEmail()+" ");
			emailLabel.addStyleName("postLabel");
			Label phoneLabel = new Label("Phone: "+entries.getPhone()+" ");
			phoneLabel.addStyleName("postLabel");
			Anchor link = new Anchor("Uploaded File", entries.getURL());
			link.setTarget("_blank");
			Button addressButton = new Button("Address");
			addressButton.addStyleName("postInfoButton");
			addressButton.setText("Address");
			//add a clickListener to the button
			addressButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					doMapPopup(entries);
				}
		      });

			Button deleteButton = new Button("Delete");
			deleteButton.addStyleName("postInfoButton");
			deleteButton.setText("Delete");
			//add a clickListener to the button
			deleteButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					control.handleDeleteRequest(entries);
				}
		      });
			Button editButton = new Button("Edit");
			editButton.addStyleName("postInfoButton");
			editButton.setText("Edit");
			//add a clickListener to the button
			editButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					viewPostEditForm(entries);
				}
		      });

			row.add(nameLabel);
			row.add(addressLabel);
			row.add(cityLabel);
			row.add(stateLabel);
			row.add(zipLabel);
			row.add(emailLabel);
			row.add(phoneLabel);
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
/*
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
		protected void doMapPopup(EntryData post) {		
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
**/
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