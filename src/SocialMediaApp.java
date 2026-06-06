import model.Post;
import model.User;
import service.PostService;
import service.UserService;

import java.util.List;
import java.util.Scanner;

// Drives the menu-based user experience for the social media platform.
public class SocialMediaApp {

    private static final String DIVIDER = "─".repeat(55);
    private static final String THIN    = "·".repeat(55);

    private final UserService userService = new UserService();
    private final PostService postService = new PostService(userService);
    private final Scanner scanner         = new Scanner(System.in);

    private User currentUser = null;

    // ── Entry point ───────────────────────────────────────────────────────────

    public void run() {
        printBanner();
        // Seed demo data every run so the app is immediately usable.
        seedDemoData();

        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = handleAuthMenu();
            } else {
                running = handleMainMenu();
            }
        }
        System.out.println("\nGoodbye!");
    }

    // ── Auth menu ─────────────────────────────────────────────────────────────

    private boolean handleAuthMenu() {
        System.out.println("\n" + DIVIDER);
        System.out.println("  MAIN MENU");
        System.out.println(DIVIDER);
        System.out.println("  1. Register");
        System.out.println("  2. Log in");
        System.out.println("  3. Browse all users");
        System.out.println("  0. Exit");
        System.out.print("\n  Choice: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> doRegister();
            case "2" -> doLogin();
            case "3" -> doBrowseUsers();
            case "0" -> { return false; }
            default  -> System.out.println("  Invalid option.");
        }
        return true;
    }

    // ── Main (logged-in) menu ─────────────────────────────────────────────────

    private boolean handleMainMenu() {
        System.out.println("\n" + DIVIDER);
        System.out.printf("  Logged in as @%s%n", currentUser.getUsername());
        System.out.println(DIVIDER);
        System.out.println("  1. Create post");
        System.out.println("  2. View my feed");
        System.out.println("  3. View my profile");
        System.out.println("  4. View another user's profile");
        System.out.println("  5. Follow a user");
        System.out.println("  6. Unfollow a user");
        System.out.println("  7. Like a post");
        System.out.println("  8. Unlike a post");
        System.out.println("  9. Edit my profile");
        System.out.println("  10. Browse all users");
        System.out.println("  11. Log out");
        System.out.println("  0. Exit");
        System.out.print("\n  Choice: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1"  -> doCreatePost();
            case "2"  -> doViewFeed();
            case "3"  -> doViewOwnProfile();
            case "4"  -> doViewUserProfile();
            case "5"  -> doFollow();
            case "6"  -> doUnfollow();
            case "7"  -> doLikePost();
            case "8"  -> doUnlikePost();
            case "9"  -> doEditProfile();
            case "10" -> doBrowseUsers();
            case "11" -> { currentUser = null; System.out.println("  Logged out."); }
            case "0"  -> { return false; }
            default   -> System.out.println("  Invalid option.");
        }
        return true;
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    private void doRegister() {
        System.out.print("  Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("  Display name: ");
        String displayName = scanner.nextLine().trim();
        System.out.print("  Bio (optional): ");
        String bio = scanner.nextLine().trim();

        if (userService.register(username, displayName, bio)) {
            System.out.println("  Account created! You can now log in.");
        } else {
            System.out.println("  Registration failed. Username may already be taken or invalid.");
        }
    }

    private void doLogin() {
        System.out.print("  Username: ");
        String username = scanner.nextLine().trim();
        User user = userService.getUser(username);
        if (user == null) {
            System.out.println("  User not found.");
        } else {
            currentUser = user;
            System.out.printf("  Welcome back, %s!%n", user.getDisplayName());
        }
    }

    private void doCreatePost() {
        System.out.print("  What's on your mind? ");
        String content = scanner.nextLine();
        Post post = postService.createPost(currentUser.getUsername(), content);
        if (post != null) {
            System.out.printf("  Post published! [ID: %s]%n", post.getId());
        } else {
            System.out.println("  Could not publish post (content must not be empty).");
        }
    }

    private void doViewFeed() {
        List<Post> feed = postService.getFeed(currentUser.getUsername());
        System.out.println("\n" + DIVIDER);
        System.out.printf("  FEED for @%s%n", currentUser.getUsername());
        System.out.println(DIVIDER);
        if (feed.isEmpty()) {
            System.out.println("  No posts yet. Follow some users to see their updates.");
        } else {
            printPosts(feed);
        }
    }

    private void doViewOwnProfile() {
        printProfile(currentUser);
        List<Post> myPosts = postService.getUserPosts(currentUser.getUsername());
        System.out.println("\n  --- Posts ---");
        if (myPosts.isEmpty()) {
            System.out.println("  No posts yet.");
        } else {
            printPosts(myPosts);
        }
    }

    private void doViewUserProfile() {
        System.out.print("  Enter username: ");
        String username = scanner.nextLine().trim();
        User user = userService.getUser(username);
        if (user == null) {
            System.out.println("  User not found.");
            return;
        }
        printProfile(user);
        List<Post> theirPosts = postService.getUserPosts(user.getUsername());
        System.out.println("\n  --- Posts ---");
        if (theirPosts.isEmpty()) {
            System.out.println("  No posts yet.");
        } else {
            printPosts(theirPosts);
        }
    }

    private void doFollow() {
        System.out.print("  Username to follow: ");
        String target = scanner.nextLine().trim();
        System.out.println("  " + userService.follow(currentUser.getUsername(), target));
    }

    private void doUnfollow() {
        System.out.print("  Username to unfollow: ");
        String target = scanner.nextLine().trim();
        System.out.println("  " + userService.unfollow(currentUser.getUsername(), target));
    }

    private void doLikePost() {
        System.out.print("  Post ID to like: ");
        String postId = scanner.nextLine().trim();
        System.out.println("  " + postService.likePost(postId, currentUser.getUsername()));
    }

    private void doUnlikePost() {
        System.out.print("  Post ID to unlike: ");
        String postId = scanner.nextLine().trim();
        System.out.println("  " + postService.unlikePost(postId, currentUser.getUsername()));
    }

    private void doEditProfile() {
        System.out.printf("  Current display name: %s%n", currentUser.getDisplayName());
        System.out.print("  New display name (leave blank to keep): ");
        String name = scanner.nextLine().trim();
        System.out.printf("  Current bio: %s%n", currentUser.getBio());
        System.out.print("  New bio (leave blank to keep): ");
        String bio = scanner.nextLine().trim();

        // Blank input means "no change" for that field.
        boolean updated = userService.updateProfile(
            currentUser.getUsername(),
            name.isEmpty()  ? null : name,
            bio.isEmpty()   ? null : bio
        );
        System.out.println(updated ? "  Profile updated." : "  Update failed.");
    }

    private void doBrowseUsers() {
        System.out.println("\n" + DIVIDER);
        System.out.println("  ALL USERS");
        System.out.println(DIVIDER);
        for (User u : userService.getAllUsers()) {
            System.out.printf("  @%-15s  %s  |  %d followers  %d following%n",
                u.getUsername(), u.getDisplayName(),
                u.getFollowers().size(), u.getFollowing().size());
        }
    }

    // ── Print helpers ─────────────────────────────────────────────────────────

    private void printProfile(User user) {
        System.out.println("\n" + DIVIDER);
        System.out.printf("  %s  (@%s)%n", user.getDisplayName(), user.getUsername());
        System.out.println(THIN);
        System.out.printf("  Bio      : %s%n", user.getBio().isEmpty() ? "—" : user.getBio());
        System.out.printf("  Posts    : %d%n", user.getPostIds().size());
        System.out.printf("  Followers: %d  |  Following: %d%n",
            user.getFollowers().size(), user.getFollowing().size());
        if (!user.getFollowers().isEmpty()) {
            System.out.printf("  Followers: %s%n", String.join(", ", user.getFollowers()));
        }
        System.out.println(DIVIDER);
    }

    private void printPosts(List<Post> posts) {
        for (Post p : posts) {
            System.out.println("\n  " + THIN);
            System.out.printf("  @%s  ·  %s  ·  [%s]%n",
                p.getAuthorUsername(), p.getFormattedTime(), p.getId());
            System.out.println("  " + p.getContent());
            System.out.printf("  ♥  %d like(s)%n", p.getLikes().size());
        }
        System.out.println("  " + THIN);
    }

    // ── Demo seed data ────────────────────────────────────────────────────────

    private void seedDemoData() {
        // Seeded users make it easy to test login, follows, and feed behavior.
        userService.register("alice",   "Alice Johnson", "Coffee addict & code lover");
        userService.register("bob",     "Bob Smith",     "Into hiking and photography");
        userService.register("charlie", "Charlie Lee",   "Tech enthusiast");

        // Seed posts populate each profile and demonstrate feed sorting.
        postService.createPost("alice",   "Hello world! First post on this platform.");
        postService.createPost("bob",     "Just got back from an amazing hike!");
        postService.createPost("alice",   "Java is awesome. Building cool things today.");
        postService.createPost("charlie", "Excited to try out this new social app!");
        postService.createPost("bob",     "Sunset photos from last weekend were stunning.");

        // Seed follow relationships give the demo accounts non-empty feeds.
        userService.follow("alice", "bob");
        userService.follow("alice", "charlie");
        userService.follow("bob",   "alice");
    }

    // ── Banner ────────────────────────────────────────────────────────────────

    private void printBanner() {
        System.out.println("""
        ╔═══════════════════════════════════════════════════════╗
        ║         JAVA SOCIAL  —  Connect & Share               ║
        ╚═══════════════════════════════════════════════════════╝
        Demo accounts ready: alice, bob, charlie  (no password needed)
        """);
    }
}
