package model;

// Abstract base for profile-like objects that share display metadata.
public abstract class AbstractProfile {
    private String displayName;
    private String bio;

    protected AbstractProfile(String displayName, String bio) {
        this.displayName = displayName;
        this.bio = bio;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBio() {
        return bio;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    // Each concrete profile type decides how its unique identifier is exposed.
    public abstract String getProfileId();
}