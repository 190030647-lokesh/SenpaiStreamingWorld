-- SenpaiStreamingWorld Database Schema
-- Create Database
CREATE DATABASE IF NOT EXISTS senpai_streaming_world;
USE senpai_streaming_world;

-- Users Table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
);

-- Genres Table (for both anime and manga)
CREATE TABLE genres (
    genre_id INT AUTO_INCREMENT PRIMARY KEY,
    genre_name VARCHAR(50) UNIQUE NOT NULL
);

-- Anime Table
CREATE TABLE anime (
    anime_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    genre VARCHAR(100),
    release_year INT,
    total_episodes INT,
    poster_url VARCHAR(500),
    rating DECIMAL(3,1) DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Anime Episodes Table
CREATE TABLE anime_episodes (
    episode_id INT AUTO_INCREMENT PRIMARY KEY,
    anime_id INT,
    episode_number INT NOT NULL,
    episode_title VARCHAR(255),
    video_url VARCHAR(500),
    video_quality_720p VARCHAR(500),
    video_quality_1080p VARCHAR(500),
    subtitle_url VARCHAR(500),
    duration INT, -- in seconds
    release_date DATE,
    FOREIGN KEY (anime_id) REFERENCES anime(anime_id) ON DELETE CASCADE
);

-- Manga Table
CREATE TABLE manga (
    manga_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    genre VARCHAR(100),
    author VARCHAR(100),
    total_chapters INT,
    cover_url VARCHAR(500),
    rating DECIMAL(3,1) DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Manga Chapters Table
CREATE TABLE manga_chapters (
    chapter_id INT AUTO_INCREMENT PRIMARY KEY,
    manga_id INT,
    chapter_number INT NOT NULL,
    chapter_title VARCHAR(255),
    pages_count INT,
    release_date DATE,
    FOREIGN KEY (manga_id) REFERENCES manga(manga_id) ON DELETE CASCADE
);

-- Manga Pages Table
CREATE TABLE manga_pages (
    page_id INT AUTO_INCREMENT PRIMARY KEY,
    chapter_id INT,
    page_number INT NOT NULL,
    image_url VARCHAR(500),
    FOREIGN KEY (chapter_id) REFERENCES manga_chapters(chapter_id) ON DELETE CASCADE
);

-- User Watch History (for anime)
CREATE TABLE watch_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    anime_id INT,
    episode_id INT,
    watch_timestamp DECIMAL(10,2), -- current time in seconds
    total_duration DECIMAL(10,2), -- total video duration
    last_watched TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status ENUM('watching', 'completed', 'dropped', 'plan_to_watch') DEFAULT 'watching',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (anime_id) REFERENCES anime(anime_id) ON DELETE CASCADE,
    FOREIGN KEY (episode_id) REFERENCES anime_episodes(episode_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_episode (user_id, episode_id)
);

-- User Read History (for manga)
CREATE TABLE read_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    manga_id INT,
    chapter_id INT,
    page_number INT DEFAULT 1,
    last_read TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status ENUM('reading', 'completed', 'dropped', 'plan_to_read') DEFAULT 'reading',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (manga_id) REFERENCES manga(manga_id) ON DELETE CASCADE,
    FOREIGN KEY (chapter_id) REFERENCES manga_chapters(chapter_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_chapter (user_id, chapter_id)
);

-- Insert Sample Genres
INSERT INTO genres (genre_name) VALUES 
('Action'), ('Adventure'), ('Comedy'), ('Drama'), ('Fantasy'), 
('Horror'), ('Mystery'), ('Romance'), ('Sci-Fi'), ('Slice of Life'),
('Sports'), ('Supernatural'), ('Thriller'), ('Shounen'), ('Seinen');

-- Insert Sample Anime (2-3 per genre)
-- ACTION
INSERT INTO anime (title, description, genre, release_year, total_episodes, poster_url, rating) VALUES
('Demon Slayer', 'A boy becomes a demon slayer after his family is killed by demons.', 'Action, Fantasy', 2019, 26, '/images/anime/demon-slayer.jpg', 8.7),
('My Hero Academia', 'A world where people have superpowers and a boy without power dreams of becoming a hero.', 'Action, Shounen', 2016, 113, '/images/anime/my-hero-academia.jpg', 8.5),
('Attack on Titan', 'Humanity fights for survival against giant humanoid creatures.', 'Action, Drama', 2013, 87, '/images/anime/attack-on-titan.jpg', 9.0),

-- ADVENTURE
('One Piece', 'A young pirate searches for the greatest treasure and gathers an eccentric crew.', 'Adventure, Action', 2000, 1000, '/images/anime/one-piece.jpg', 9.1),
('Fullmetal Alchemist', 'Two brothers seek a philosopher stone to restore their bodies after a failed alchemical experiment.', 'Adventure, Fantasy', 2005, 64, '/images/anime/fullmetal-alchemist.jpg', 9.1),

-- COMEDY
('The Devil is a Part-Timer!', 'A demon lord and hero work at a fast food restaurant in modern Japan.', 'Comedy, Fantasy', 2013, 13, '/images/anime/devil-part-timer.jpg', 7.6),
('Nichijou', 'A comedy about the everyday lives of high school girls with absurd twists.', 'Comedy, Slice of Life', 2011, 26, '/images/anime/nichijou.jpg', 7.7),
('KonoSuba', 'An adventurer is sent to a fantasy world with a goddess and gather party members.', 'Comedy, Fantasy', 2016, 20, '/images/anime/konosuba.jpg', 8.1),

-- DRAMA
('Your Name', 'Two teenagers mysteriously swap bodies across time and space.', 'Drama, Romance', 2016, 1, '/images/anime/your-name.jpg', 8.8),
('A Silent Voice', 'A former bully seeks redemption from his deaf classmate.', 'Drama, Romance', 2016, 1, '/images/anime/silent-voice.jpg', 8.9),
('Steins;Gate', 'A group of scientists discovers time travel through a microwave and must prevent catastrophe.', 'Drama, Sci-Fi', 2011, 24, '/images/anime/steins-gate.jpg', 9.0),

-- FANTASY
('Re:Zero', 'A man is transported to a fantasy world and discovers he can turn back time.', 'Fantasy, Drama', 2016, 25, '/images/anime/rezero.jpg', 8.4),
('That Time I Got Reincarnated as a Spider', 'A girl is reincarnated as a spider in a dungeon.', 'Fantasy, Adventure', 2021, 24, '/images/anime/reincarnated-spider.jpg', 7.6),
('Sword Art Online', 'Players get trapped in a virtual reality MMORPG.', 'Fantasy, Sci-Fi', 2012, 25, '/images/anime/sword-art-online.jpg', 7.5),

-- HORROR
('Another', 'A mysterious death curse plagues a class and students must uncover its origin.', 'Horror, Thriller', 2012, 12, '/images/anime/another.jpg', 7.5),
('Corpse Party', 'Students trapped in a haunted school must escape vengeful spirits.', 'Horror, Supernatural', 2021, 4, '/images/anime/corpse-party.jpg', 6.8),

-- MYSTERY
('Monster', 'A doctor pursues a mysterious former patient turned serial killer.', 'Mystery, Thriller', 2004, 74, '/images/anime/monster.jpg', 8.6),
('Ergo Proxy', 'A girl and android explore a post-apocalyptic world seeking answers.', 'Mystery, Sci-Fi', 2006, 23, '/images/anime/ergo-proxy.jpg', 7.7),

-- ROMANCE
('Toradora!', 'Two students strike a deal: help each other win their crushes.', 'Romance, Comedy', 2008, 25, '/images/anime/toradora.jpg', 8.2),
('Fruit Basket', 'A girl learns her classmates transform into zodiac animals when hugged.', 'Romance, Drama', 2019, 63, '/images/anime/fruit-basket.jpg', 8.8),

-- SCI-FI
('Ghost in the Shell', 'A cyborg special ops agent questions her humanity.', 'Sci-Fi, Action', 1995, 1, '/images/anime/ghost-in-shell.jpg', 8.6),
('Neon Genesis Evangelion', 'Teenagers pilot giant robots to fight alien invaders.', 'Sci-Fi, Drama', 1995, 26, '/images/anime/evangelion.jpg', 8.0),

-- SLICE OF LIFE
('Laid-Back Camp', 'Girls go on relaxing camping trips.', 'Slice of Life, Comedy', 2018, 13, '/images/anime/laid-back-camp.jpg', 8.4),
('A Place Further Than the Universe', 'Four girls travel to Antarctica to discover themselves.', 'Slice of Life, Adventure', 2018, 13, '/images/anime/place-further.jpg', 8.7),

-- SPORTS
('Haikyuu!!', 'A short volleyball player aims to become the best in Japan.', 'Sports, School', 2014, 85, '/images/anime/haikyuu.jpg', 8.8),
('Ping Pong the Animation', 'Two childhood friends compete in professional ping pong.', 'Sports, Drama', 2014, 11, '/images/anime/ping-pong.jpg', 8.4),

-- SUPERNATURAL
('Jujutsu Kaisen', 'A boy swallows a cursed finger and becomes a sorcerer.', 'Supernatural, Action', 2020, 24, '/images/anime/jujutsu-kaisen.jpg', 8.8),
('Tokyo Ghoul', 'A human becomes a half-ghoul and must hide his identity.', 'Supernatural, Horror', 2014, 48, '/images/anime/tokyo-ghoul.jpg', 7.8),

-- THRILLER
('Psycho-Pass', 'Inspectors hunt criminals using a system that detects criminal potential.', 'Thriller, Sci-Fi', 2012, 22, '/images/anime/psycho-pass.jpg', 8.3),
('Code Geass', 'A student gains the power to command anyone to obey him.', 'Thriller, Mecha', 2006, 50, '/images/anime/code-geass.jpg', 8.4);

-- Insert Sample Episodes (for multiple anime)
INSERT INTO anime_episodes (anime_id, episode_number, episode_title, video_url, video_quality_720p, video_quality_1080p, subtitle_url, duration) VALUES
-- Demon Slayer
(1, 1, 'Cruelty', '/videos/demon-slayer/ep1.mp4', '/videos/demon-slayer/ep1-720p.mp4', '/videos/demon-slayer/ep1-1080p.mp4', '/subtitles/demon-slayer/ep1.vtt', 1440),
(1, 2, 'Trainer Sakonji Urokodaki', '/videos/demon-slayer/ep2.mp4', '/videos/demon-slayer/ep2-720p.mp4', '/videos/demon-slayer/ep2-1080p.mp4', '/subtitles/demon-slayer/ep2.vtt', 1440),
-- My Hero Academia
(2, 1, 'Izuku Midoriya: Origin', '/videos/mha/ep1.mp4', '/videos/mha/ep1-720p.mp4', '/videos/mha/ep1-1080p.mp4', '/subtitles/mha/ep1.vtt', 1380),
(2, 2, 'Quirks', '/videos/mha/ep2.mp4', '/videos/mha/ep2-720p.mp4', '/videos/mha/ep2-1080p.mp4', '/subtitles/mha/ep2.vtt', 1380),
-- Attack on Titan
(3, 1, 'To Your 2000 Years in the Future', '/videos/aot/ep1.mp4', '/videos/aot/ep1-720p.mp4', '/videos/aot/ep1-1080p.mp4', '/subtitles/aot/ep1.vtt', 1440),
(3, 2, 'The Fake Aristocrats', '/videos/aot/ep2.mp4', '/videos/aot/ep2-720p.mp4', '/videos/aot/ep2-1080p.mp4', '/subtitles/aot/ep2.vtt', 1440);

-- Insert Sample Manga (2-3 per genre)
-- ACTION
INSERT INTO manga (title, description, genre, author, total_chapters, cover_url, rating) VALUES
('One Piece', 'A young pirate searches for the greatest treasure.', 'Action, Adventure', 'Eiichiro Oda', 1100, '/images/manga/one-piece.jpg', 9.0),
('Naruto', 'A young ninja seeks recognition and dreams of becoming Hokage.', 'Action, Shounen', 'Masashi Kishimoto', 700, '/images/manga/naruto.jpg', 8.5),
('Jujutsu Kaisen', 'A boy swallows a cursed finger and becomes a sorcerer.', 'Action, Supernatural', 'Gege Akutami', 275, '/images/manga/jujutsu-kaisen.jpg', 8.9),

-- ADVENTURE
('The Legend of Zelda: A Link to the Past', 'A hero awakens to save the kingdom from darkness.', 'Adventure, Fantasy', 'Kazuhiro Kazami', 32, '/images/manga/zelda.jpg', 8.0),

-- COMEDY
('Asobi Asobase', 'Three girls play everyday games with competitive intensity.', 'Comedy, Slice of Life', 'Ousama Yuuji', 92, '/images/manga/asobi-asobase.jpg', 7.9),
('Dyan-Ma!', 'Girls at school get into silly misadventures.', 'Comedy, School', 'Mami Sekiya', 45, '/images/manga/dyan-ma.jpg', 7.5),

-- DRAMA
('A Silent Voice', 'A former bully seeks redemption from a deaf girl.', 'Drama, Romance', 'Yoshitoki Oima', 64, '/images/manga/a-silent-voice.jpg', 8.9),
('Fruits Basket', 'A girl learns classmates transform into zodiac animals.', 'Drama, Romance', 'Natsuki Takaya', 186, '/images/manga/fruits-basket.jpg', 8.8),

-- FANTASY
('My Hero Academia', 'A powerless boy dreams of becoming a hero in a superpowered world.', 'Fantasy, Action', 'Kohei Horikoshi', 426, '/images/manga/mha.jpg', 8.8),
('The Promised Neverland', 'Orphans discover their orphanage is actually a farm for demons.', 'Fantasy, Thriller', 'Kaiu Shirai', 181, '/images/manga/promised-neverland.jpg', 8.7),
('Attack on Titan', 'Humanity fights for survival against giant humanoid creatures.', 'Fantasy, Action', 'Hajime Isayama', 141, '/images/manga/attack-on-titan.jpg', 9.0),

-- HORROR
('Junji Ito Collection', 'Collection of horrifying short horror stories.', 'Horror, Supernatural', 'Junji Ito', 100, '/images/manga/junji-ito.jpg', 9.1),
('Hellbound', 'People are suddenly condemned to Hell with no explanation.', 'Horror, Thriller', 'Yeon Sang-ho', 6, '/images/manga/hellbound.jpg', 8.2),

-- MYSTERY
('Monster', 'A doctor pursues a mysterious former patient turned criminal.', 'Mystery, Thriller', 'Naoki Urasawa', 162, '/images/manga/monster.jpg', 8.7),
('Ergo Proxy', 'A girl and android explore a post-apocalyptic world.', 'Mystery, Sci-Fi', 'Kou Yaginuma', 47, '/images/manga/ergo-proxy.jpg', 7.8),

-- ROMANCE
('Kaguya-sama: Love is War', 'Two geniuses play mind games in their romance battle.', 'Romance, Comedy', 'Aka Akasaka', 290, '/images/manga/kaguya-sama.jpg', 8.9),
('Horimiya', 'A perfect student and a boy with piercings fall in love.', 'Romance, School', 'CLAMP', 128, '/images/manga/horimiya.jpg', 8.6),

-- SCI-FI
('Steins;Gate', 'Scientists discover how to send messages to the past.', 'Sci-Fi, Thriller', 'Anonymous Code', 24, '/images/manga/steins-gate.jpg', 8.8),
('Akira', 'A biker gang member discovers psychic powers in Neo-Tokyo.', 'Sci-Fi, Action', 'Katsuhiro Otomo', 120, '/images/manga/akira.jpg', 8.7),

-- SLICE OF LIFE
('Yotsuba&!', 'The daily adventures of a cheerful green-haired girl.', 'Slice of Life, Comedy', 'Kiyohiko Azuki', 212, '/images/manga/yotsuba.jpg', 8.8),
('Nichijou', 'Everyday school life with absurd twists.', 'Slice of Life, Comedy', 'Masao Otake', 177, '/images/manga/nichijou.jpg', 8.2),

-- SPORTS
('Haikyuu!!', 'A short volleyball player aims for nationals.', 'Sports, School', 'Haruichi Furudate', 402, '/images/manga/haikyuu.jpg', 8.9),
('Slam Dunk', 'A street brawler turns into a basketball star.', 'Sports, School', 'Takehiko Inoue', 276, '/images/manga/slam-dunk.jpg', 8.8),
('Kuroko Basketball', 'A team dreams of winning the national basketball championship.', 'Sports, School', 'Tadatoshi Fujimaki', 271, '/images/manga/kuroko.jpg', 8.5),

-- SUPERNATURAL
('Tokyo Ghoul', 'A human becomes a half-ghoul in a world of ghouls.', 'Supernatural, Action', 'Sui Ishida', 143, '/images/manga/tokyo-ghoul.jpg', 8.1),
('Blue Exorcist', 'A boy discovers his father is Satan and becomes an exorcist.', 'Supernatural, Action', 'Kazue Kato', 133, '/images/manga/blue-exorcist.jpg', 8.0),

-- THRILLER
('Psycho-Pass', 'Inspectors hunt criminals using a system that predicts crime.', 'Thriller, Sci-Fi', 'Natsuyuki Nakanishi', 32, '/images/manga/psycho-pass.jpg', 8.4),
('Code Geass', 'A student gains the power to command anyone to obey.', 'Thriller, Mecha', 'Ichirou Ohkouchi', 80, '/images/manga/code-geass.jpg', 8.5);

-- Insert Sample Chapters for Multiple Manga
INSERT INTO manga_chapters (manga_id, chapter_number, chapter_title, pages_count, release_date) VALUES
-- One Piece
(1, 1, 'Romance Dawn', 52, '1997-07-22'),
(1, 2, 'They Call Him Straw Hat Luffy', 20, '1997-08-04'),
-- Naruto
(2, 1, 'Naruto Uzumaki!!', 45, '1999-09-21'),
(2, 2, 'The Worst Student in the Academy', 18, '1999-10-05'),
-- Jujutsu Kaisen
(3, 1, 'Ryomen Sukuna', 38, '2018-03-05'),
(3, 2, 'Curse Womb Must Die', 20, '2018-03-26');


