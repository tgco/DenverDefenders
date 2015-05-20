package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.XML.Level;
import org.TheGivingChild.Engine.XML.LevelPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

class ScreenLevelPackets extends ScreenAdapter{
	//list of packets for each level set.
	private Array<LevelPacket> packets;
	private int currentPacketLevelIndex = 0;
	private Table packetTable;
	private Array<Level> levels;
	
	private TGC_Engine game;
	
	//constructor. Initialize the variables.
	public ScreenLevelPackets() {
		game = ScreenAdapterManager.getInstance().game;
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
		
		ScrollPaneStyle sps = new ScrollPaneStyle();
		sps.background = game.getButtonAtlasSkin().getDrawable("SliderBackground");
		ScrollPane buttonScrollPane = new ScrollPane(null, sps);
		
		//slide that ranges from 0 to size-1 index of packets.
		Slider slider = new Slider(0, packets.size-1, 1, false, sliderStyle);
		//row for the packet buttons
		Table packetsRow = new Table();
		//padding dimensions for the packets
		float padWidth = game.getWidth()/24;
		float padHeight = game.getHeight()/3;
		
		//create a font for the buttons
        BitmapFont font = game.getBitmapFontButton();
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.down = game.getButtonAtlasSkin().getDrawable("ButtonChecked_LevelPackIcon");
		textButtonStyle.up = game.getButtonAtlasSkin().getDrawable("Button_LevelPackIcon");
		//textButtonStyle.checked = game.getButtonAtlasSkin().getDrawable("ButtonChecked_LevelPackIcon");
		
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
					currentPacketLevelIndex = j;
					//hide the current screen, load the main screen
					ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
				}
        	});
			//increment the packets index
			i++;
			//add the button to the row, with padding
			buttonScrollPane.addActor(textButton);
		}
		
		//add the row of buttons and slider to the screen
		table.add(buttonScrollPane).expandX().expandY();
		
		return table;
	}
	
	public void createPackets(){
		for(Level l: levels){
			//packet name to add to
			String packetName = l.getPackageName();
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
	
	public LevelPacket getPacketToPlay(){
		return packets.get(currentPacketLevelIndex);
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
