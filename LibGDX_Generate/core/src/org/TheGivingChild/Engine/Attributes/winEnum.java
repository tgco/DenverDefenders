package org.TheGivingChild.Engine.Attributes;

public enum winEnum {
	
	TIMEOUT{
		public boolean checkWin(){
			//if gameclock > time, lose.
			return false;
		}
	},
		
	COLLISION{
		public boolean checkWin(){
			return false;
		}
	}
	
	
}
