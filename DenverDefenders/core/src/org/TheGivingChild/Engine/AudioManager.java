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
		volume = .75f;
	}
	
	// Sets up sound references
	public void initialize(TGC_Engine game) {
		this.game = game;
		
		backgroundSounds.add(game.getAssetManager().get("sounds/backgroundMusic/01_A_Night_Of_Dizzy_Spells.wav", Music.class));
		backgroundSounds.add(game.getAssetManager().get("sounds/backgroundMusic/02_Underclocked_underunderclocked_mix_.wav", Music.class));
		backgroundSounds.add(game.getAssetManager().get("sounds/backgroundMusic/03_Chibi_Ninja.wav", Music.class));
		backgroundSounds.add(game.getAssetManager().get("sounds/backgroundMusic/04_All_of_Us.wav", Music.class));
		backgroundSounds.add(game.getAssetManager().get("sounds/backgroundMusic/05_Come_and_Find_Me.wav", Music.class));
		backgroundSounds.add(game.getAssetManager().get("sounds/backgroundMusic/06_Searching.wav", Music.class));
		backgroundSounds.add(game.getAssetManager().get("sounds/backgroundMusic/07_We_39_re_the_Resistors.wav", Music.class));
		backgroundSounds.add(game.getAssetManager().get("sounds/backgroundMusic/08_Ascending.wav", Music.class));
		backgroundSounds.add(game.getAssetManager().get("sounds/backgroundMusic/09_Come_and_Find_Me.wav", Music.class));
		backgroundSounds.add(game.getAssetManager().get("sounds/backgroundMusic/10_Arpanauts.wav", Music.class));
		
		soundMap.put("sounds/click.wav", game.getAssetManager().get("sounds/click.wav", Sound.class));
		soundMap.put("sounds/bounce.wav", game.getAssetManager().get("sounds/bounce.wav", Sound.class));
	}
	
	// Selects a random background song and plays it
	public void playBackgroundMusic() {
		backgroundSoundToPlay = backgroundSounds.random();
		backgroundSoundToPlay.setVolume(volume);
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
