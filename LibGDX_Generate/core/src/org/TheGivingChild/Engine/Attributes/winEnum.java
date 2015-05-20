package org.TheGivingChild.Engine.Attributes;

public enum winEnum {
	
	TIMEOUT{
		private int winInfo;
		public void setWinInfo(String winInfo){this.winInfo=Integer.parseInt(winInfo);}
		public boolean checkWin(){
			//if gameclock > time, lose.
			return false;
		}
	},
		
	COLLISION{
		public boolean checkWin(){
			return false;
		}
	};
	
	
}
