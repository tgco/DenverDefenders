package org.TheGivingChild.Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
	//fill packets with newly created packets for levels with matching level types.
	
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
	
	public Table createLevelPacketButtons(){
		//create a table to add buttons to.
		Table table = new Table();
		//create a font for the buttons
        BitmapFont font = game.getBitmapFontButton();
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.down = game.getButtonAtlasSkin().getDrawable("Buttons/ButtonChecked_LevelPackIcon");
		textButtonStyle.up = game.getButtonAtlasSkin().getDrawable("Buttons/Button_LevelPackIcon");
		textButtonStyle.checked = game.getButtonAtlasSkin().getDrawable("Buttons/ButtonChecked_LevelPackIcon");
		//indexer for finding which packet to play when button is clicked.
		int i = 0;
		for(LevelPacket p: packets){
			String packetName = p.getPacketName();
			TextButton textButton = new TextButton(packetName, textButtonStyle);
			textButton.setSize(textButton.getWidth()/packets.size,textButton.getHeight()/packets.size);
			//final variable to access within change listener
			final int j = i;
			//Transition to the ScreenPacketLevels Screen
			textButton.addListener(new ChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					hide();
					game.playLevels(packets.get(j));
				}
        	});
			i++;
			table.add(textButton);
			if(i%2 == 0 && i != 0) table.row();
		}
		//position the table in the middle of the screen.
		table.setPosition(game.getWidth()/2,game.getHeight());
		return table;
	}
	
	@Override
	public void show() {
		game.addTable(packetTable);
	};
	
	@Override
	public void hide() {
		game.removeTable(packetTable);
	};
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1,0,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
