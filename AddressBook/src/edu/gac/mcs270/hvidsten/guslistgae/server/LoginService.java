/*
 * Implementation of HTTP request from client for login service.
 * Login service is through Google User Accounts 
 */

package edu.gac.mcs270.hvidsten.guslistgae.server;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
 
@SuppressWarnings("serial")
public class LoginService extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
 
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		if (user != null) {  
			// If there is a User currently logged in to their Google account,
			//  re-direct the returned url to the main GusList page. 
			resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
		    resp.setHeader("Location", "../GusList.html");  
		} else {
			//If there is no user currently logged in,
			//  serve back to client the url for the Google login service. 
			String logInLink = userService.createLoginURL("../GusList.html");
			resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
		    resp.setHeader("Location", logInLink); 
		}
	}
}
