package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.XML.Level;
import org.TheGivingChild.Engine.XML.LevelPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

class ScreenLevelPackets extends ScreenAdapter{
	//list of packets for each level set.
	private Array<LevelPacket> packets;
	//current index to select packet from packets
	private int currentPacketLevelIndex = 0;
	//table to add the scrollTable of packets to.
	private Table packetTable;
	//array of levels to fill the packets with
	private Array<Level> levels;
	//skin for using textures in the asset manager
	private Skin skin = new Skin();
	//reference to the game for adding to stage, etc.
	private TGC_Engine game;
	
	private AssetManager manager;
	private boolean isRendered = false;
	private Batch batch = new SpriteBatch();
	
	//constructor. Initialize the variables.
	public ScreenLevelPackets() {
		//get the game from the manager instance
		game = ScreenAdapterManager.getInstance().game;
		manager = game.getAssetManager();
		//initialize the packets
		packets = new Array<LevelPacket>();
		//initialize and fill levels array from the games level
		levels = new Array<Level>(game.getLevels());
		//group the levels into different packets.
		createPackets();
		//get the UI elements that represent the packets
		packetTable = createLevelPacketButtons();
	}
	
	public Table createLevelPacketButtons(){
		//initialize a new table
		Table table = new Table();
		//Table to store the packets in a row
		Table packetsRow = new Table();
		//padding width between the packets
		float padWidth = game.getWidth()/24;
		
		//add regions from the asset manager to skin
		skin.addRegions((TextureAtlas) manager.get("Packs/Buttons.pack"));
		//create a font for the buttons
        BitmapFont font = game.getBitmapFontButton();
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.down = skin.getDrawable("ButtonChecked_LevelPackIcon");
		textButtonStyle.up = skin.getDrawable("Button_LevelPackIcon");
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
					ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.LEVEL);
				}
        	});
			//increment the packets index
			i++;
			//add the button to the row, with padding
			packetsRow.add(textButton).width(game.getWidth()/3 - padWidth).height(game.getHeight()/2).padLeft(padWidth).padRight(padWidth);
		}
		//ScrollPane to scroll through packets
		ScrollPaneStyle sps = new ScrollPaneStyle();
		ScrollPane buttonScrollPane = new ScrollPane(packetsRow, sps);
		//set the scroll pane to size to the parent table
		table.add(buttonScrollPane).fill().expand();
		//set the size of the table to the size of the screen
		table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//set the table to position itself in the bottom left corner
		table.align(Align.bottomLeft);
		//return the table
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
		//game.removeTable(packetTable);
		packetTable.remove();
	}
	
	@Override
	public void render(float delta) {
		ScreenAdapterManager.getInstance().screenTransitionOutComplete = ScreenAdapterManager.getInstance().screenTransitionOut();
		if(manager.update()) {
			if(ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT <= 0 && ScreenAdapterManager.getInstance().screenTransitionOutComplete) {
				Gdx.gl.glClearColor(.5f,0,.5f,.5f);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				isRendered = true;
				show();
				game.getStage().act();
			}
		}
		
		if(ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT >= 0)
			ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT -= Gdx.graphics.getDeltaTime();
	}
	
	@Override
	public void show() {
		//game.addTable(packetTable);
		if(isRendered){
			game.getStage().addActor(packetTable);
			isRendered = false;
		}
		
	}
}
