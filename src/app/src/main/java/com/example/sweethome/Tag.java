package com.example.sweethome;

public class Tag {

    private String tagName;
    private String tagColor;

    // Constructor
    public Tag(String tagName, String tagColor) {
        this.tagName = tagName;
        this.tagColor = tagColor;
    }

    // Getter for tagName
    public String getTagName() {
        return tagName;
    }

    // Getter for tagColor
    public String getTagColor() {
        return tagColor;
    }

    // (Optional) Setter for tagName
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    // (Optional) Setter for tagColor
    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }
}
