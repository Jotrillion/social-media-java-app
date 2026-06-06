package model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// Stores profile details plus the relationship and post references for one user.
public class User extends AbstractProfile {
    private final String username;
    private final Set<String> followers;
    private final Set<String> following;
    private final List<String> postIds;

    public User(String username, String displayName, String bio) {
        super(displayName, bio);
        this.username = username;
        this.followers = new LinkedHashSet<>();
        this.following = new LinkedHashSet<>();
        this.postIds = new ArrayList<>();
    }

    public String getUsername()              { return username; }
    public Set<String> getFollowers()        { return followers; }
    public Set<String> getFollowing()        { return following; }
    public List<String> getPostIds()         { return postIds; }

    @Override
    public String getProfileId() {
        return username;
    }

    // Sets prevent duplicate followers/following entries while preserving order.
    public void addFollower(String username)    { followers.add(username); }
    public void removeFollower(String username) { followers.remove(username); }
    public void addFollowing(String username)   { following.add(username); }
    public void removeFollowing(String username){ following.remove(username); }

    // Posts are tracked by ID so post data can live in the post service.
    public void addPostId(String postId)        { postIds.add(postId); }
}
