package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.Level;
import org.TheGivingChild.Engine.LevelPacket;
import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

public class ScreenLevelManager extends ScreenAdapter{
	
	//reference to the main engine
	private TGC_Engine game;
	//list of packets for each level set.
	private Array<LevelPacket> packets;
	private Table packetTable;
	private Array<Level> levels;
	//constructor. Initialize the variables.
	public ScreenLevelManager(TGC_Engine game) {
		this.game = game;
		packets = new Array<LevelPacket>();
		levels = new Array<Level>(game.getLevels());
		createPackets();
		packetTable = createLevelPacketButtons();
		
	}
	
	public Table createLevelPacketButtons(){
		Table table = new Table();
		//TODO: create slider to move between packets on screen
		//display 3 packets on the screen at once, with spacing.
	
		SliderStyle sliderStyle = new SliderStyle();
		sliderStyle.background = game.getButtonAtlasSkin().getDrawable("SliderBackground");
		sliderStyle.knob = game.getButtonAtlasSkin().getDrawable("SliderKnob");
		
		//slide that ranges from 0 to size-1 index of packets.
		Slider slider = new Slider(0, packets.size-1, 1, false, sliderStyle);
		
		HorizontalGroup packetsRow = new HorizontalGroup();
		
		//create a font for the buttons
        BitmapFont font = game.getBitmapFontButton();
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.down = game.getButtonAtlasSkin().getDrawable("ButtonChecked_LevelPackIcon");
		textButtonStyle.up = game.getButtonAtlasSkin().getDrawable("Button_LevelPackIcon");
		textButtonStyle.checked = game.getButtonAtlasSkin().getDrawable("ButtonChecked_LevelPackIcon");
		
		//indexer for finding which packet to play when button is clicked.
		int i = 0;
		for(LevelPacket p: packets){
			System.out.println(p.getPacketName());
			//get the name, create a button, fetch the correct sizes.
			String packetName = p.getPacketName();
			TextButton textButton = new TextButton(packetName, textButtonStyle);
			//final variable to access within change listener
			final int j = i;
			//Transition to the ScreenPacketLevels Screen
			textButton.addListener(new ChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					//hide the current screen
					hide();
					//call play levels on the packet called.
					game.setScreen(game.screens[3]);
				}
        	});
			//increment the packets index
			i++;
			//add the button to the correct row
			packetsRow.addActor(textButton);
		}
		//create a vertical group to add the slider and horizontal group to.
		//VerticalGroup verticalGroup = new VerticalGroup();
		//add the elements
		//verticalGroup.addActor(packetsRow);
		//verticalGroup.addActor(slider);
		
		//table.add(verticalGroup);
		table.add(packetsRow).height(100);
		table.row();
		table.add(slider).width(game.getWidth());
		
		
		return table;
	}
	
	public void createPackets(){
		for(Level l: levels){
			//packet name to add to
			String packetName = l.getPacketName();
			boolean packetFound = false;
			//keep track of array variable accessed when searching for packet
			int i = 0;
			//iterate over packets
			for(LevelPacket p: packets){
				//check equality of packetName to levels packetName
				if(p.getPacketName().equals(packetName)){
					//packetName matched, let us know, break
					packetFound = true;
					break;
				}
				//increment array index
				i++;
			}
			//packet found, so add the current level to the packet
			if(packetFound){
				packets.get(i).addLevel(l);
			}
			//packet not found
			else{
				//create new packet with name
				LevelPacket packet = new LevelPacket(packetName);
				//add level to the packet
				packet.addLevel(l);
				//add packet to packets.
				packets.add(packet);
			}
		}
	}
	
	@Override
	public void hide() {
		game.removeTable(packetTable);
	};
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1,0,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	};
	
	@Override
	public void show() {
		game.addTable(packetTable);
	}
}