/**
 * 
 */
package org.sugyan.counter.template;

import com.google.appengine.api.users.User;

/**
 * @author sugyan
 *
 */
public class BaseTemplate {
    
    private boolean isUserLoggedIn = false;
    private User currentUser = null;
    private String linkUrl = null;
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "<!DOCTYPE html\n" +
               "          PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" +
               "          \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
               "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"ja\" xml:lang=\"ja\">\n" +
               "  <head>\n" + 
               "    <title>GAE Counter</title>\n" + 
               "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" + 
               header() +
               "  </head>\n" +
               "  <body>\n" +
               "    <div align=\"right\">\n" +
               (currentUser != null ? 
               "      " + currentUser.getNickname() + "\n" :
               "") +
               "      <a href=\"" + linkUrl + "\">\n" + 
               (currentUser != null ?
               "        Sign out\n" :
               "        Sign in\n") +
               "      </a>\n" +
               "    </div>\n" + 
               content() + 
               "  </body>\n" +
               "</html>\n";
    }
    
    protected String content() {
        // TODO Auto-generated method stub
        return "";
    }

    protected String header() {
        return "";
    }

    /**
     * @param isUserLoggedIn the isUserLoggedIn to set
     */
    public void setUserLoggedIn(boolean isUserLoggedIn) {
        this.isUserLoggedIn = isUserLoggedIn;
    }

    /**
     * @return the isUserLoggedIn
     */
    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }

    /**
     * @param currentUser the currentUser to set
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * @return the currentUser
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * @param linkUrl the linkUrl to set
     */
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    /**
     * @return the linkUrl
     */
    public String getLinkUrl() {
        return linkUrl;
    }
}
