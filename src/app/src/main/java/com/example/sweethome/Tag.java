package com.example.sweethome;

import java.util.ArrayList;

/**
 * The Tag class represents a tag with a name and array of usernames.
 * Tags can be associated with items to provide additional categorization or information.
 *
 * November 10, 2023
 *
 */
public class Tag {
    /* attributes for this class */
    private String id;
    private String name;
    private ArrayList<String> usernames;

    // Constructor
    public Tag(String id, String name, ArrayList<String> usernames) {
        this.id = id;
        this.name = name;
        this.usernames = usernames;
    }

    // Getter for id
    public String getTagId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setUsernames(ArrayList<String> usernames) {
        this.usernames = usernames;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Getter for tagName
    public String getTagName() {
        return name;
    }

    // Getter for users who have this tag
    public ArrayList<String> getTagUsernames() {
        return usernames;
    }
}
