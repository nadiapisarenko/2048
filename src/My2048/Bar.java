package My2048;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Bar {
	public static final int BarWidth=100;
	 public static final int BarHeight=100;
	 public static final int MovingSpeed=30;
	 public static final int ARCWidth=30;
	 public static final int ARCHeight=30;
	 
	 private int barValue;
	 private BufferedImage barImage;
	 private Color barBackground;
	 private Color barText;
	 private Font barFont;
	 private MyPoint moveTo;
	 private int barX;
	 private int barY;
	 private boolean shouldCombine=true;
	 
	 public Bar(int barValue,int barX, int barY) {
		 this.barValue=barValue;
		 this.barX=barX;
		 this.barY=barY;
		 moveTo=new MyPoint(barX,barY);
		 barImage = new BufferedImage(BarWidth,BarHeight,BufferedImage.TYPE_INT_RGB);
		 drawBarImage();
	 }
	 
	 private void drawBarImage() {
		  Graphics2D g=(Graphics2D)barImage.getGraphics();
		  if (barValue==2) {
			  barBackground= Color.gray;
			  barText= Color.black;
		  }
		  else if(barValue==4) {
			  barBackground= Color.orange;
			  barText= Color.black;  
		  }
		  else if(barValue==8) {
			  barBackground=Color.magenta;
			  barText=Color.white;  
		  }
		  else if(barValue==16) {
			  barBackground=Color.pink;
			  barText=Color.white; 
		  }
		  else if(barValue==32) {
			  barBackground=Color.cyan;
			  barText=Color.white; 
		  }
		  else if(barValue==64) {
			  barBackground= Color.red;
			  barText=Color.white; 
		  }
		  else if(barValue==128) {
			  barBackground=Color.blue;
			  barText=Color.white;
		  }
		  else if(barValue==256) {
			  barBackground=Color.yellow;
			  barText=Color.white;  
		  }
		  else if(barValue==512) {
			  barBackground=Color.lightGray;
			  barText=Color.white;  
		  }
		  else if(barValue==1024) {
			  barBackground=Color.darkGray;
			  barText=Color.white; 
		  }
		  else if(barValue==2048) {
			  barBackground=Color.white;
			  barText=Color.black;  
		  }
		  else {
			  barBackground=Color.black;
			  barText=Color.white;
		  }
		  
		  g.setColor(new Color(0,0,0,0));
		  g.fillRect(0, 0, BarWidth, BarHeight);
		  g.setColor(barBackground);
		  g.fillRoundRect(0, 0, BarWidth, BarHeight, ARCWidth, ARCHeight);
		  
		  g.setColor(barText);
		  if (barValue<=64) {
			  barFont=My2048Game.mainFont.deriveFont(36f);
		  }
		  else {
			  barFont=My2048Game.mainFont;
		  }
			   g.setFont(barFont);
			   
			   int drawX = BarWidth/2 - DrawUtilsGame.getMessageWidth("" + barValue, barFont, g)/2;
			   int drawY = BarWidth/2 + DrawUtilsGame.getMessageHeight("" + barValue, barFont, g)/2;
			   g.drawString("" +barValue, drawX, drawY);
			   g.dispose();
		 }
	 public void update() {}
	
	public void paintBar(Graphics2D g) {
		g.drawImage(barImage,barX, barY, null);  
	  }
	public int getBarValue() {
		return barValue;
	}

	public void setBarValue(int barValue) {
		this.barValue = barValue;
		drawBarImage();
	}

	public boolean isShouldCombine() {
		return shouldCombine;
	}

	public void setShouldCombine(boolean shouldCombine) {
		this.shouldCombine = shouldCombine;
	}

	public int getBarX() {
		return barX;
	}

	public void setBarX(int barX) {
		this.barX = barX;
	}

	public int getBarY() {
		return barY;
	}

	public void setBarY(int barY) {
		this.barY = barY;
	}

	public MyPoint getMoveTo() {
		return moveTo;
	}

	public void setMoveTo(MyPoint moveTo) {
		this.moveTo = moveTo;
	}

}
