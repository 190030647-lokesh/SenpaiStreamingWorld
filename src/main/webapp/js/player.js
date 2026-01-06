// Video Player Controls JavaScript

document.addEventListener('DOMContentLoaded', function() {
    const video = document.getElementById('videoPlayer');
    const playPauseBtn = document.getElementById('playPauseBtn');
    const backwardBtn = document.getElementById('backwardBtn');
    const forwardBtn = document.getElementById('forwardBtn');
    const volumeBtn = document.getElementById('volumeBtn');
    const volumeSlider = document.getElementById('volumeSlider');
    const timeDisplay = document.getElementById('timeDisplay');
    const progressBar = document.getElementById('progressBar');
    const progressFilled = document.getElementById('progressFilled');
    const subtitleBtn = document.getElementById('subtitleBtn');
    const qualitySelector = document.getElementById('qualitySelector');
    const downloadBtn = document.getElementById('downloadBtn');
    const zoomInBtn = document.getElementById('zoomInBtn');
    const zoomOutBtn = document.getElementById('zoomOutBtn');
    const fullscreenBtn = document.getElementById('fullscreenBtn');
    const videoControls = document.getElementById('videoControls');
    const videoWrapper = document.querySelector('.video-player-wrapper');

    let zoomLevel = 1;
    let hideControlsTimeout;

    // Play/Pause functionality
    playPauseBtn.addEventListener('click', togglePlayPause);
    video.addEventListener('click', togglePlayPause);

    function togglePlayPause() {
        if (video.paused) {
            video.play();
            playPauseBtn.innerHTML = '<i class="fas fa-pause"></i>';
        } else {
            video.pause();
            playPauseBtn.innerHTML = '<i class="fas fa-play"></i>';
        }
    }

    // Keyboard shortcuts
    document.addEventListener('keydown', function(e) {
        if (e.code === 'Space') {
            e.preventDefault();
            togglePlayPause();
        } else if (e.code === 'ArrowLeft') {
            video.currentTime -= 10;
        } else if (e.code === 'ArrowRight') {
            video.currentTime += 10;
        } else if (e.code === 'ArrowUp') {
            e.preventDefault();
            video.volume = Math.min(video.volume + 0.1, 1);
            volumeSlider.value = video.volume * 100;
        } else if (e.code === 'ArrowDown') {
            e.preventDefault();
            video.volume = Math.max(video.volume - 0.1, 0);
            volumeSlider.value = video.volume * 100;
        } else if (e.code === 'KeyF') {
            toggleFullscreen();
        } else if (e.code === 'KeyM') {
            toggleMute();
        }
    });

    // Forward and Backward buttons
    backwardBtn.addEventListener('click', function() {
        video.currentTime -= 10;
    });

    forwardBtn.addEventListener('click', function() {
        video.currentTime += 10;
    });

    // Volume control
    volumeSlider.addEventListener('input', function() {
        video.volume = this.value / 100;
        updateVolumeIcon();
    });

    volumeBtn.addEventListener('click', toggleMute);

    function toggleMute() {
        video.muted = !video.muted;
        updateVolumeIcon();
    }

    function updateVolumeIcon() {
        if (video.muted || video.volume === 0) {
            volumeBtn.innerHTML = '<i class="fas fa-volume-mute"></i>';
        } else if (video.volume < 0.5) {
            volumeBtn.innerHTML = '<i class="fas fa-volume-down"></i>';
        } else {
            volumeBtn.innerHTML = '<i class="fas fa-volume-up"></i>';
        }
    }

    // Progress bar
    video.addEventListener('timeupdate', updateProgress);

    function updateProgress() {
        const percent = (video.currentTime / video.duration) * 100;
        progressFilled.style.width = percent + '%';
        updateTimeDisplay();
    }

    progressBar.addEventListener('click', function(e) {
        const rect = progressBar.getBoundingClientRect();
        const percent = (e.clientX - rect.left) / rect.width;
        video.currentTime = percent * video.duration;
    });

    // Time display
    function updateTimeDisplay() {
        const currentTime = formatTime(video.currentTime);
        const duration = formatTime(video.duration);
        timeDisplay.textContent = `${currentTime} / ${duration}`;
    }

    function formatTime(seconds) {
        if (isNaN(seconds)) return '0:00';
        const mins = Math.floor(seconds / 60);
        const secs = Math.floor(seconds % 60);
        return `${mins}:${secs.toString().padStart(2, '0')}`;
    }

    // Subtitle toggle
    subtitleBtn.addEventListener('click', function() {
        const track = video.textTracks[0];
        if (track) {
            track.mode = track.mode === 'showing' ? 'hidden' : 'showing';
            this.style.color = track.mode === 'showing' ? 'var(--primary-color)' : 'white';
        }
    });

    // Quality selector
    qualitySelector.addEventListener('change', function() {
        const currentTime = video.currentTime;
        const wasPlaying = !video.paused;
        
        // In a real application, you would change the video source here
        // based on the selected quality
        const quality = this.value;
        console.log('Quality changed to:', quality);
        
        // Example: video.src = `/videos/episode-${quality}.mp4`;
        // video.currentTime = currentTime;
        // if (wasPlaying) video.play();
    });

    // Download button
    downloadBtn.addEventListener('click', function() {
        const videoSrc = video.currentSrc;
        const link = document.createElement('a');
        link.href = videoSrc;
        link.download = 'video.mp4';
        link.click();
    });

    // Zoom controls
    zoomInBtn.addEventListener('click', function() {
        zoomLevel += 0.1;
        video.style.transform = `scale(${zoomLevel})`;
    });

    zoomOutBtn.addEventListener('click', function() {
        if (zoomLevel > 0.5) {
            zoomLevel -= 0.1;
            video.style.transform = `scale(${zoomLevel})`;
        }
    });

    // Fullscreen
    fullscreenBtn.addEventListener('click', toggleFullscreen);

    function toggleFullscreen() {
        if (!document.fullscreenElement) {
            if (videoWrapper.requestFullscreen) {
                videoWrapper.requestFullscreen();
            } else if (videoWrapper.webkitRequestFullscreen) {
                videoWrapper.webkitRequestFullscreen();
            } else if (videoWrapper.msRequestFullscreen) {
                videoWrapper.msRequestFullscreen();
            }
            fullscreenBtn.innerHTML = '<i class="fas fa-compress"></i>';
        } else {
            if (document.exitFullscreen) {
                document.exitFullscreen();
            } else if (document.webkitExitFullscreen) {
                document.webkitExitFullscreen();
            } else if (document.msExitFullscreen) {
                document.msExitFullscreen();
            }
            fullscreenBtn.innerHTML = '<i class="fas fa-expand"></i>';
        }
    }

    // Auto-hide controls
    videoWrapper.addEventListener('mousemove', function() {
        videoControls.style.opacity = '1';
        clearTimeout(hideControlsTimeout);
        
        if (!video.paused) {
            hideControlsTimeout = setTimeout(function() {
                videoControls.style.opacity = '0';
            }, 3000);
        }
    });

    videoWrapper.addEventListener('mouseleave', function() {
        if (!video.paused) {
            videoControls.style.opacity = '0';
        }
    });

    // Save watch progress
    video.addEventListener('timeupdate', function() {
        if (video.currentTime > 0) {
            localStorage.setItem('watchProgress_' + window.location.search, video.currentTime);
        }
    });

    // Load saved progress
    const savedProgress = localStorage.getItem('watchProgress_' + window.location.search);
    if (savedProgress) {
        video.currentTime = parseFloat(savedProgress);
    }

    // Auto-play next episode when current ends
    video.addEventListener('ended', function() {
        const nextEpisodeBtn = document.querySelector('.nav-btn[href*="episode"]');
        if (nextEpisodeBtn && confirm('Play next episode?')) {
            window.location.href = nextEpisodeBtn.href;
        }
    });

    // Picture-in-Picture
    if (document.pictureInPictureEnabled) {
        const pipBtn = document.createElement('button');
        pipBtn.className = 'control-btn';
        pipBtn.title = 'Picture-in-Picture';
        pipBtn.innerHTML = '<i class="fas fa-clone"></i>';
        pipBtn.addEventListener('click', async function() {
            try {
                if (document.pictureInPictureElement) {
                    await document.exitPictureInPicture();
                } else {
                    await video.requestPictureInPicture();
                }
            } catch (error) {
                console.error('PiP error:', error);
            }
        });
        
        document.querySelector('.controls-right').insertBefore(
            pipBtn, 
            fullscreenBtn
        );
    }

    // Playback speed control
    const speedBtn = document.createElement('select');
    speedBtn.className = 'quality-selector';
    speedBtn.title = 'Playback Speed';
    speedBtn.innerHTML = `
        <option value="0.5">0.5x</option>
        <option value="0.75">0.75x</option>
        <option value="1" selected>1x</option>
        <option value="1.25">1.25x</option>
        <option value="1.5">1.5x</option>
        <option value="2">2x</option>
    `;
    
    speedBtn.addEventListener('change', function() {
        video.playbackRate = parseFloat(this.value);
    });
    
    document.querySelector('.controls-right').insertBefore(
        speedBtn,
        qualitySelector.nextSibling
    );

    // Loading indicator
    video.addEventListener('waiting', function() {
        videoWrapper.classList.add('loading');
    });

    video.addEventListener('canplay', function() {
        videoWrapper.classList.remove('loading');
    });
});
