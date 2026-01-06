# SenpaiStreamingWorld - Anime & Manga Streaming Website

A comprehensive web application for streaming anime and reading manga, built with Java, JSP, CSS, and JavaScript.

## 🌟 Features

### User Authentication
- User registration with email validation
- Secure login system with password hashing (SHA-256)
- Session management with "Remember Me" functionality
- User profile management

### Content Management
- **Anime Section**
  - Browse anime by genre
  - Search functionality
  - Continue watching feature
  - Completed anime tracking
  - Custom video player with advanced controls

- **Manga Section**
  - Browse manga by genre and author
  - Search functionality
  - Continue reading feature
  - Completed manga tracking
  - Custom manga reader

### Video Player Features
- Play/Pause controls
- 10-second forward/backward skip
- Volume control with mute option
- Quality selector (480p, 720p, 1080p, Auto)
- Subtitle toggle
- Download option
- Zoom in/out functionality
- Fullscreen mode
- Picture-in-Picture support
- Playback speed control
- Progress bar with time display
- Keyboard shortcuts

### Manga Reader Features
- Chapter navigation
- Zoom in/out controls
- Fullscreen reading mode
- Page-by-page display
- Keyboard navigation

### User Profile
- Continue watching/reading lists
- Completed anime/manga lists
- User settings
- Logout functionality

## 🛠️ Technology Stack

- **Backend**: Java Servlets
- **Frontend**: JSP, CSS3, JavaScript (ES6)
- **Database**: MySQL
- **Server**: Apache Tomcat (recommended)

## 📋 Prerequisites

- JDK 8 or higher
- Apache Tomcat 9.x or higher
- MySQL 8.x or higher
- Maven (optional, for dependency management)

## 🚀 Installation & Setup

### 1. Database Setup

```sql
-- Run the database schema
mysql -u root -p < database/schema.sql
```

### 2. Configure Database Connection

Edit `src/main/java/com/senpaistream/util/DatabaseConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/senpai_streaming_world";
private static final String USER = "your_mysql_username";
private static final String PASSWORD = "your_mysql_password";
```

### 3. Add MySQL Connector

Download MySQL Connector/J and add it to your project's lib folder or add this Maven dependency:

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

### 4. Deploy to Tomcat

1. Build the project:
   ```bash
   # If using Maven
   mvn clean package
   ```

2. Copy the WAR file to Tomcat's webapps directory:
   ```bash
   cp target/SenpaiStreamingWorld.war $TOMCAT_HOME/webapps/
   ```

3. Start Tomcat:
   ```bash
   $TOMCAT_HOME/bin/startup.sh  # Linux/Mac
   $TOMCAT_HOME/bin/startup.bat  # Windows
   ```

4. Access the application:
   ```
   http://localhost:8080/SenpaiStreamingWorld
   ```

## 📁 Project Structure

```
SenpaiStreamingWorld/
├── database/
│   └── schema.sql
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── senpaistream/
│       │           ├── dao/
│       │           │   ├── AnimeDAO.java
│       │           │   ├── MangaDAO.java
│       │           │   └── UserDAO.java
│       │           ├── model/
│       │           │   ├── Anime.java
│       │           │   ├── Manga.java
│       │           │   └── User.java
│       │           ├── servlet/
│       │           │   ├── AnimeServlet.java
│       │           │   ├── LoginServlet.java
│       │           │   ├── LogoutServlet.java
│       │           │   ├── MangaServlet.java
│       │           │   ├── ProfileServlet.java
│       │           │   ├── ReadServlet.java
│       │           │   ├── RegisterServlet.java
│       │           │   └── WatchServlet.java
│       │           └── util/
│       │               └── DatabaseConnection.java
│       └── webapp/
│           ├── css/
│           │   ├── auth.css
│           │   ├── player.css
│           │   ├── profile.css
│           │   ├── reader.css
│           │   └── style.css
│           ├── js/
│           │   ├── main.js
│           │   └── player.js
│           ├── images/
│           ├── WEB-INF/
│           │   └── web.xml
│           ├── index.jsp
│           ├── login.jsp
│           ├── register.jsp
│           ├── profile.jsp
│           ├── watch.jsp
│           └── read.jsp
```

## 🎮 Usage

### Registration
1. Navigate to the registration page
2. Enter username, email, and password
3. Accept terms and conditions
4. Click "Register"

### Login
1. Enter your email and password
2. Optionally check "Remember Me"
3. Click "Login"

### Watching Anime
1. Browse or search for anime
2. Select an anime
3. Choose an episode
4. Use the video player controls:
   - Spacebar: Play/Pause
   - Arrow Keys: Navigate/Volume
   - F: Fullscreen
   - M: Mute

### Reading Manga
1. Browse or search for manga
2. Select a manga
3. Choose a chapter
4. Use keyboard arrows to navigate pages

## 🔧 Customization

### Adding Custom Genres
Edit `database/schema.sql` and add your genres to the `genres` table.

### Changing Theme Colors
Edit `src/main/webapp/css/style.css` and modify the CSS variables:

```css
:root {
    --primary-color: #ff6b6b;
    --secondary-color: #4ecdc4;
    --dark-bg: #1a1a2e;
    --light-bg: #16213e;
}
```

## 🔐 Security Features

- Password hashing using SHA-256
- SQL injection prevention using PreparedStatements
- Session management
- XSS protection
- CSRF token support (can be implemented)

## 📝 Future Enhancements

- Admin panel for content management
- User comments and ratings
- Social features (friends, sharing)
- Watchlist/Reading list
- Email verification
- Password reset functionality
- Multi-language support
- Recommendation system
- Mobile app version

## 🐛 Troubleshooting

### Database Connection Issues
- Verify MySQL is running
- Check database credentials
- Ensure MySQL Connector/J is in classpath

### Video/Manga Not Loading
- Check file paths in database
- Verify media files exist in correct directories
- Check file permissions

### Login/Registration Not Working
- Clear browser cache and cookies
- Check database table structure
- Verify servlet mappings in web.xml

## 📄 License

This project is open-source and available for educational purposes.

## 👨‍💻 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📧 Contact

For questions or support, please open an issue on the project repository.

---

**Built with ❤️ for anime and manga enthusiasts!**
