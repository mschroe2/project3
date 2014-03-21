/*
 * RPC Servlet to handle getting the BlobStore URL address for the Google
 *   web site that will handle uploading the Blob. 
 */

package edu.gac.mcs270.hvidsten.guslistgae.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/* This is extracted from code in the BlobStore Tutorial written by the blogger "fishbone":
 *  http://www.fishbonecloud.com/2010/12/tutorial-gwt-application-for-storing.html 
 *  Note: March 2014 - this link is no longer active. 
 *  */

@RemoteServiceRelativePath("blobservice")
public interface BlobService extends RemoteService {
  String getBlobStoreUploadUrl();

}