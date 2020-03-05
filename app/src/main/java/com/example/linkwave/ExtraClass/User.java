package com.example.linkwave.ExtraClass;

import androidx.annotation.NonNull;

public class User {

    private String UserId;
    private String Firstname;
    private String Lastname;
    private String Mobilenumber;
    private String ProfilePicLink;
   // private UserNotification notification;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        UserId = "";
        Firstname = "";
        Lastname = "";
        Mobilenumber = "";
        ProfilePicLink = "https://firebasestorage.googleapis.com/v0/b/androidlinkwave.appspot.com/o/profile_pic%2Fdefault-avatar.png?alt=media&token=15781da5-31b7-452e-948d-429e52c57fad";
        //notification = new UserNotification("0","0","");
    }


    public User(String userId, String firstname, String lastname, String mobilenumber ,String profilepiclink) {
        UserId = userId;
        Firstname = firstname;
        Lastname = lastname;
        Mobilenumber = mobilenumber;
        //this.notification = notification;
        if (profilepiclink == null || profilepiclink == ""){
            ProfilePicLink = "https://firebasestorage.googleapis.com/v0/b/androidlinkwave.appspot.com/o/profile_pic%2Fdefault-avatar.png?alt=media&token=15781da5-31b7-452e-948d-429e52c57fad";
        }
        else{
            ProfilePicLink = profilepiclink;
        }

    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getMobilenumber() {
        return Mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        Mobilenumber = mobilenumber;
    }

    public String getProfilePicLink() {
        return ProfilePicLink;
    }


    public void setProfilePicLink(String profilePicLink) {
        ProfilePicLink = profilePicLink;
    }

   /*
    public UserNotification getNotification() {
        return notification;
    }

    public void setNotification(UserNotification notification) {
        this.notification = notification;
    }
    */
}
