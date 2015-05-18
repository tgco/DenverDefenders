package org.TheGivingChild.Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
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
		//width and height placeholders
		float textButtonWidth = 0;
		float textButtonHeight = 0;
		//get the number of rows to make, when the number of packets in each column = 3;
		int rows = packets.size/3 + 1;
		//create an empty array of groups for the rows
		Array<HorizontalGroup> buttonsRows = new Array<HorizontalGroup>();
		//create the needed groups for the arrays base on the number of rows
		for(int k = 0; k < rows; k++){
			buttonsRows.add(new HorizontalGroup());
		}
		//set the size to be a ratio of number of packets to be displayed each page.
		float sizeRatio = (packets.size);
		//iterate over each packet
		for(LevelPacket p: packets){
			//get the name, create a button, fetch the correct sizes.
			String packetName = p.getPacketName();
			TextButton textButton = new TextButton(packetName, textButtonStyle);
			textButtonWidth = textButton.getWidth()/sizeRatio;
			textButtonHeight = textButton.getHeight()/sizeRatio;
			//set the button size
			//textButton.setSize(textButtonWidth, textButtonHeight);
			textButton.setHeight(textButtonHeight);
			textButton.setWidth(textButtonWidth);
			//textButton.pad(400);
			//final variable to access within change listener
			final int j = i;
			//Transition to the ScreenPacketLevels Screen
			textButton.addListener(new ChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					//hide the current screen
					hide();
					//call play levels on the packet called.
					game.playLevels(packets.get(j));
				}
        	});
			//increment the packets index
			i++;
			//add the button to the row
			buttonsRows.get(buttonsRows.size-1 - i%rows).addActor(textButton);
			//create a new row if 3 packets are on the current row.
			//if(i%rows == 0 && i != 0) table.row();
		}
		//add each row to a vertical container
		VerticalGroup rowContainer = new VerticalGroup();
		for(HorizontalGroup h: buttonsRows){
			rowContainer.addActor(h);
		}
		table.add(rowContainer);
		//set the size of the table
		//table.setSize(textButtonWidth*3, textButtonHeight*rows);
		//position the table in the middle of the screen.
		//table.setPosition(game.getWidth()/2,0);
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
