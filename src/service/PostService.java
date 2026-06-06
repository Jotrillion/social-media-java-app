package service;

import model.Post;
import model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Manages post creation, profile timelines, feeds, and likes.
public class PostService {

    private final Map<String, Post> posts = new LinkedHashMap<>();
    private final UserService userService;

    public PostService(UserService userService) {
        this.userService = userService;
    }

    // ── Create ────────────────────────────────────────────────────────────────

    public Post createPost(String username, String content) {
        if (content == null || content.isBlank()) return null;
        User user = userService.getUser(username);
        if (user == null) return null;

        Post post = new Post(username.toLowerCase(), content.trim());
        posts.put(post.getId(), post);
        // Keep the user's timeline lightweight by storing only post IDs there.
        user.addPostId(post.getId());
        return post;
    }

    // ── Retrieve ──────────────────────────────────────────────────────────────

    public Post getPost(String postId) {
        return posts.get(postId);
    }

    /** All posts by a specific user, newest first. */
    public List<Post> getUserPosts(String username) {
        User user = userService.getUser(username);
        if (user == null) return List.of();
        List<Post> result = new ArrayList<>();
        for (String id : user.getPostIds()) {
            Post p = posts.get(id);
            if (p != null) result.add(p);
        }
        result.sort(Comparator.comparing(Post::getCreatedAt).reversed());
        return result;
    }

    /** Feed: posts from users the given user follows, newest first. */
    public List<Post> getFeed(String username) {
        User user = userService.getUser(username);
        if (user == null) return List.of();
        List<Post> feed = new ArrayList<>();

        // Build the feed from followed accounts, then sort globally by timestamp.
        for (String followedUsername : user.getFollowing()) {
            feed.addAll(getUserPosts(followedUsername));
        }
        feed.sort(Comparator.comparing(Post::getCreatedAt).reversed());
        return feed;
    }

    // ── Like / Unlike ─────────────────────────────────────────────────────────

    public String likePost(String postId, String username) {
        Post post = posts.get(postId);
        if (post == null) return "Post not found.";
        if (post.getAuthorUsername().equals(username.toLowerCase()))
            return "You cannot like your own post.";
        boolean added = post.like(username.toLowerCase());
        return added ? "Liked post [" + postId + "]." : "You already liked this post.";
    }

    public String unlikePost(String postId, String username) {
        Post post = posts.get(postId);
        if (post == null) return "Post not found.";
        boolean removed = post.unlike(username.toLowerCase());
        return removed ? "Removed like from post [" + postId + "]."
                       : "You have not liked this post.";
    }
}
