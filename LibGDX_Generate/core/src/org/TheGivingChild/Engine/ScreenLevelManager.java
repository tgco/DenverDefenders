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
	
	//constructor. Initialize the variables.
	public ScreenLevelManager(TGC_Engine game) {
		this.game = game;
		packets = new Array<LevelPacket>();
		//createPackets();
		packetTable = createLevelPacketButtons();
		
		
	}
	//fill packets with newly created packets for levels with matching level types.
	
	public void createPackets(){
		for(Level l: game.getLevels()){
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
		//textButtonStyle.down = game.getButtonAtlasSkin().getDrawable("Button_LevelPackIcon");
		//textButtonStyle.up = game.getButtonAtlasSkin().getDrawable("Button_LevelPackIcon");
		//textButtonStyle.checked = game.getButtonAtlasSkin().getDrawable("Button_LevelPackIcon");
		
		for(LevelPacket p: packets){
			String packetName = p.getPacketName();
			TextButton textButton = new TextButton(packetName, textButtonStyle);
			//Transition to the ScreenPacketLevels Screen.
			textButton.addListener(new ChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					game.setScreen(new ScreenAdapter());
					hidePacketTable();
				}
        	});
			table.add(textButton);
		}
		//position the table in the middle of the screen.
		table.setPosition(
				game.getWidth()/2,
				game.getHeight()/2);
		return table;
	}
	
	public void hidePacketTable(){
		game.removeTable(packetTable);
	}
	
	public void showPacketTable(){
		game.addTable(packetTable);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1,0,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
