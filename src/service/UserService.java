package service;

import model.User;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

// Centralizes all user and relationship operations for the app.
public class UserService {

    private final Map<String, User> users = new LinkedHashMap<>();

    // ── Registration ──────────────────────────────────────────────────────────

    public boolean register(String username, String displayName, String bio) {
        if (username == null || username.isBlank()) return false;
        // Usernames are normalized so lookups are case-insensitive everywhere.
        if (users.containsKey(username.toLowerCase())) return false;
        users.put(username.toLowerCase(), new User(username.toLowerCase(), displayName, bio));
        return true;
    }

    // ── Lookup ────────────────────────────────────────────────────────────────

    public User getUser(String username) {
        return users.get(username.toLowerCase());
    }

    public boolean exists(String username) {
        return users.containsKey(username.toLowerCase());
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    // ── Profile update ────────────────────────────────────────────────────────

    public boolean updateProfile(String username, String displayName, String bio) {
        User user = getUser(username);
        if (user == null) return false;
        if (displayName != null && !displayName.isBlank()) user.setDisplayName(displayName);
        if (bio != null) user.setBio(bio);
        return true;
    }

    // ── Follow / Unfollow ─────────────────────────────────────────────────────

    public String follow(String followerUsername, String targetUsername) {
        if (followerUsername.equalsIgnoreCase(targetUsername))
            return "You cannot follow yourself.";
        User follower = getUser(followerUsername);
        User target   = getUser(targetUsername);
        if (follower == null) return "User '" + followerUsername + "' not found.";
        if (target   == null) return "User '" + targetUsername   + "' not found.";
        if (follower.getFollowing().contains(targetUsername.toLowerCase()))
            return "You already follow @" + targetUsername + ".";

        // Update both sides so follower/following counts stay consistent.
        follower.addFollowing(targetUsername.toLowerCase());
        target.addFollower(followerUsername.toLowerCase());
        return "You are now following @" + targetUsername + ".";
    }

    public String unfollow(String followerUsername, String targetUsername) {
        User follower = getUser(followerUsername);
        User target   = getUser(targetUsername);
        if (follower == null) return "User '" + followerUsername + "' not found.";
        if (target   == null) return "User '" + targetUsername   + "' not found.";
        if (!follower.getFollowing().contains(targetUsername.toLowerCase()))
            return "You are not following @" + targetUsername + ".";

        follower.removeFollowing(targetUsername.toLowerCase());
        target.removeFollower(followerUsername.toLowerCase());
        return "You unfollowed @" + targetUsername + ".";
    }
}
