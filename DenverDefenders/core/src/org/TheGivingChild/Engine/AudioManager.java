package org.TheGivingChild.Engine;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

// Manages audio settings and allows playback
public class AudioManager {
	// Singleton instance
	private static AudioManager instance;
	// Reference to game
	private TGC_Engine game;
	// Settings
	public float volume;
	public boolean soundEnabled;
	public boolean musicEnabled;
	public boolean muteAll;
	// Array of background music
	private Array<Music> backgroundSounds;
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
		backgroundSounds = new Array<Music>();
		soundMap = new ObjectMap<String, Sound>();
		soundEnabled = true;
		musicEnabled = true;
		muteAll = false;
		volume = 1.0f;
	}
	
	// Sets up sound references
	public void initialize(TGC_Engine game) {
		this.game = game;
	
		backgroundSounds.add(game.getAssetManager().get("sounds/backgroundMusic/03_Chibi_Ninja.wav", Music.class));
		
		addAvailableSound("sounds/click.wav");
	}
	
	// Adds the sound name to the map
	public void addAvailableSound(String soundFile) {
		//Load sound, this should be refactored
		game.getAssetManager().load(soundFile, Sound.class);
		game.getAssetManager().finishLoadingAsset(soundFile);
		soundMap.put(soundFile, game.getAssetManager().get(soundFile, Sound.class));
	}
	
	// Selects a random background song and plays it
	public void playBackgroundMusic() {
		backgroundSoundToPlay = backgroundSounds.random();
		backgroundSoundToPlay.setVolume(volume);
		backgroundSoundToPlay.setLooping(true);
		backgroundSoundToPlay.play();
	}
	
	// Plays the sound file
	public void play(String fileName) {
		if (soundEnabled && !muteAll)
			soundMap.get(fileName).play(volume);
	}
	
	public void setMuteAll(boolean muteAll) {
		this.muteAll = muteAll;
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
	}
	
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
		for (Music m : backgroundSounds) {
			m.dispose();
		}
		for (Sound s : soundMap.values()) {
			s.dispose();
		}
	}

}
