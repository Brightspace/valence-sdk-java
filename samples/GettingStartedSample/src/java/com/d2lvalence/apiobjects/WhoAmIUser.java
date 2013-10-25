package com.d2lvalence.apiobjects;

import com.google.gson.annotations.SerializedName;

public class WhoAmIUser {
    @SerializedName("Identifier") private String identifier;
    @SerializedName("FirstName") private String firstName;
    @SerializedName("LastName") private String lastName;
    @SerializedName("UniqueName") private String uniqueName;
    @SerializedName("ProfileIdentifier") private String profileIdentifier;

    public WhoAmIUser() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileIdentifier() {
        return profileIdentifier;
    }

    public void setProfileIdentifier(String profileIdentifier) {
        this.profileIdentifier = profileIdentifier;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

}
