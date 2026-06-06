# Overview

This project is a command-line social media platform written in Java. It lets users
create profiles, log in, publish posts, browse user profiles, follow and unfollow
other users, like and unlike posts, and view a personalized feed based on the
accounts they follow.

The application runs entirely in the console and uses in-memory data structures to
manage users and posts during execution. It also seeds demo accounts and sample posts
at startup so the platform can be explored immediately without manual setup.

# Assignment Requirements Covered

The program demonstrates all required basics:

- Variables
- Expressions
- Conditionals (`if`, `else`, `switch`)
- Loops (`while`, `for`)
- Functions (methods for menus, actions, and helpers)
- Classes (`User`, `Post`, `UserService`, `PostService`, `SocialMediaApp`)
- Java collections (`List`, `Set`, `Map`)

And it demonstrates additional requirements:

- Object-oriented design with multiple cooperating classes
- User relationship management with followers and following collections
- Feed generation by combining and sorting posts from followed users
- Demo seed data for immediate testing and interaction

# Build and Run

From the project folder, compile the source files into the `out` folder and then run
the application with:

```powershell
New-Item -ItemType Directory -Force -Path out | Out-Null
javac -d out -sourcepath src src/Main.java src/SocialMediaApp.java src/model/User.java src/model/Post.java src/service/UserService.java src/service/PostService.java
java -cp out Main
```

# Example Usage

1. Start the app and log in with one of the demo accounts: `alice`, `bob`, or `charlie`.
2. Create a post to publish a new status update.
3. Follow another user to add their posts to your feed.
4. View your feed to see followed users' updates in newest-first order.
5. Like or unlike posts and inspect user profiles to see follower counts and post history.

# Software Demo Video

- Software Demo Video: not added yet

# Development Environment

- Language: Java
- Runtime/Compiler: JDK (`javac`, `java`)
- OS: Windows
- Interface: Command-line application using `Scanner`

# Useful Websites

- [Java Documentation](https://docs.oracle.com/en/java/)
- [Scanner Class](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Scanner.html)
- [LocalDateTime Class](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDateTime.html)
- [Collections Framework Overview](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/doc-files/coll-overview.html)

# Future Work

{Make a list of things that you need to fix, improve, and add in the future.}

- Add persistent storage so users, posts, and follows survive after the app closes.
- Build a web frontend and REST API so the project becomes a true full-stack application.
- Add authentication with passwords, session handling, and better account security.
- Improve the feed with pagination, search, and sorting filters.
- Add profile images, post editing, and post deletion.