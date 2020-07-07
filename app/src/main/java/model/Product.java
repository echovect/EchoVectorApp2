package model;

import com.google.firebase.Timestamp;

public class Product {

    private String title;

    private String description;
    private String imageUrl;
//    private String userId;
    private Timestamp timeAdded;
    private boolean expanded;

    public Product() { //Must for firestore to work
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public Product(String title, String description, String imageUrl, /*String userId,  String userName,*/ Timestamp timeAdded) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
//        this.userId = userId;
        this.timeAdded = timeAdded;
        this.expanded=false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
}
