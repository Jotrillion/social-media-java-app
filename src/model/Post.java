package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

// Represents a single status update and the users who liked it.
public class Post {
    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("MMM dd, yyyy  HH:mm");

    private final String id;
    private final String authorUsername;
    private final String content;
    private final LocalDateTime createdAt;
    private final Set<String> likes;

    public Post(String authorUsername, String content) {
        // A short ID keeps the console output readable when liking posts by ID.
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.authorUsername = authorUsername;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.likes = new LinkedHashSet<>();
    }

    public String getId()             { return id; }
    public String getAuthorUsername() { return authorUsername; }
    public String getContent()        { return content; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
    public Set<String> getLikes()     { return likes; }

    public boolean like(String username) {
        return likes.add(username);
    }

    public boolean unlike(String username) {
        return likes.remove(username);
    }

    public String getFormattedTime() {
        return createdAt.format(FORMATTER);
    }
}
