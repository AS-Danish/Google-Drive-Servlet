package com.googledrive;

import java.sql.Timestamp;

public class File {
    private int id;
    private String name;
    private String uploadedOn; // Change to Timestamp if preferred
    private String owner;
    private String location;

    // Constructor with parameters
    public File(int id, String name, String uploadedOn, String owner, String location) {
        this.id = id;
        this.name = name;
        this.uploadedOn = uploadedOn;
        this.owner = owner;
        this.location = location;
    }

    // No-argument constructor (optional)
    public File() {
        // Initialize with default values if needed
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public void setUploadedOn(Timestamp uploadedOn) {
        this.uploadedOn = uploadedOn.toString(); // Convert Timestamp to String if needed
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
