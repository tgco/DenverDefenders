package org.TheGivingChild.Engine;

import java.util.Random;

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
	
	// Random generator
	private Random rand;

	public static AudioManager getInstance() {
		if (instance == null)
			instance = new AudioManager();
		return instance;
	}

	public AudioManager() {
		soundMap = new ObjectMap<String, Sound>();
		rand = new Random();
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
		this.addAvailableSound("sounds/punch1.mp3");
		this.addAvailableSound("sounds/punch2.mp3");
		this.addAvailableSound("sounds/punch3.mp3");
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
		backgroundSoundToPlay = game.getAssetManager().get("sounds/backgroundMusic/adamWestBatman.mp3", Music.class);
		backgroundSoundToPlay.setVolume(.75f);
		backgroundSoundToPlay.setLooping(true);
		if (musicEnabled) {
			backgroundSoundToPlay.play();
		}
	}

	// Plays the sound file if sound is enabled
	public void play(String fileName) {
		if (soundEnabled) {
			soundMap.get(fileName).play(volume);
		}
	}
	
	// Plays a random "punch" sound
	public void playPunchSound() {
		if (soundEnabled) {
			int num = rand.nextInt(3) + 1;
			String name = "sounds/punch" + num + ".mp3";
			play(name);
		}
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
