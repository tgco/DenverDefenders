package org.TheGivingChild.Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

// Manages audio settings and allows playback.  Singleton pattern.
// Author: Walter Schlosser
public class AudioManager {
	// Singleton instance
	private static AudioManager instance;
	// Reference to game
	private TGC_Engine game;
	// Settings
	public float volume; // 0 to 1 inclusive range
	public boolean soundEnabled;
	public boolean musicEnabled;
	// Map of sound name to sound asset
	private ObjectMap<String, Sound> soundMap;
	// Reference to currently selected song
	private Music backgroundSoundToPlay;

	public static AudioManager getInstance() {
		if (instance == null)
			instance = new AudioManager();
		return instance;
	}

	public AudioManager() {
		soundMap = new ObjectMap<String, Sound>();
		// Load settings
		Preferences prefs = Gdx.app.getPreferences("save_data");
		soundEnabled = prefs.getBoolean("soundEnabled", true);
		musicEnabled = prefs.getBoolean("musicEnabled", true);
		volume = 1.0f;
	}

	// Sets up sound references
	public void initialize(TGC_Engine game) {
		this.game = game;
		this.addAvailableSound("sounds/click.wav");
	}

	// Adds the sound name to the map
	public void addAvailableSound(String soundFile) {
		//Load sound, this should be refactored, it is currently synchronous
		game.getAssetManager().load(soundFile, Sound.class);
		game.getAssetManager().finishLoadingAsset(soundFile);
		soundMap.put(soundFile, game.getAssetManager().get(soundFile, Sound.class));
	}

	// Begins looping background song
	public void playBackgroundMusic() {
		backgroundSoundToPlay = game.getAssetManager().get("sounds/backgroundMusic/03_Chibi_Ninja.wav", Music.class);
		backgroundSoundToPlay.setVolume(volume);
		backgroundSoundToPlay.setLooping(true);
		if (musicEnabled) {
			backgroundSoundToPlay.play();
		}
	}

	// Plays the sound file if sound is enabled
	public void play(String fileName) {
		if (soundEnabled)
			soundMap.get(fileName).play(volume);
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	// Sets music enabled bool and plays the music if enabled is true
	public void setMusicEnabled(boolean enabled) {
		musicEnabled = enabled;
		if (enabled && !backgroundSoundToPlay.isPlaying())
			backgroundSoundToPlay.play();
		else if (!enabled)
			backgroundSoundToPlay.pause();
	}

	public void setSoundEnabled(boolean enabled) {
		soundEnabled = enabled;
	}

	public void dispose() {
		backgroundSoundToPlay.dispose();
		for (Sound s : soundMap.values()) {
			s.dispose();
		}
	}

}
