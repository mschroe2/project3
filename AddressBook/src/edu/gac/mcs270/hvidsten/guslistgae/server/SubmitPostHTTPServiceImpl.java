/*
 * Implementation of HTTP request from client for form submittal (doPost) 
 * and url request (doGet).
 *  
 */
package edu.gac.mcs270.hvidsten.guslistgae.server;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import edu.gac.mcs270.hvidsten.guslistgae.shared.PostData;
import edu.gac.mcs270.hvidsten.guslistgae.shared.Seller;

//The FormPanel must submit to a servlet that extends HttpServlet  
//  so HttpServlet must be used
@SuppressWarnings("serial")
public class SubmitPostHTTPServiceImpl extends HttpServlet {
	//Start Blobstore 
	BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	//Override the doPost method to store the Blob's meta-data
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		@SuppressWarnings("deprecation")
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
		BlobKey blobKey = blobs.get("upload");

		//Get the parameters from the request to post the ad
		String name = req.getParameter("name");
		String title = req.getParameter("title");
		String descr = req.getParameter("description");
		String address = req.getParameter("address");
		double price = Double.valueOf(req.getParameter("price"));
		
		String url=null;
		
		// Check to see if we are editing a post or creating a new post. 
		PostData editingPost = GusListModel.getEditingPost();
		if(editingPost !=null) {
			if(blobKey != null)
				//Map the ImageURL to the blobservice servlet, which will serve the image
				url = "/guslist/blobservice?blob-key=" + blobKey.getKeyString();
			else url = editingPost.getURL();
			GusListModel.updateEditedPost(editingPost, name, title, descr, 
					                            address, price, url);
		}
		else {
			//Map the ImageURL to the blobservice servlet, which will serve the image
			url = "/guslist/blobservice?blob-key=" + blobKey.getKeyString();
			PostData post = new PostData(title,descr,price, address,
						url, new Seller(name), null);
			GusListModel.storePost(post);
		}
	}

}
