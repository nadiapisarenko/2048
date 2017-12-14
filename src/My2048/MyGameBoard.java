package My2048;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class MyGameBoard {
	public static final int LINE=4;
	  public static final int COLUMNS=4;
	  
	  private final int startingBars=2;
	  private Bar[][] barArray;
	  private boolean dead;
	  private boolean won,bet;
	  private BufferedImage gameBoardIm;
	  private BufferedImage finalGameBoardIm;
	  private int x;
	  private int y;
	  public  static int score=0;
	  private int highScore=0;
	  private Font scoreFont;
	  
	  public static int SpaceBetween=10;
	  public static int gameBoardWidth = (COLUMNS+1)* SpaceBetween + COLUMNS*Bar.BarWidth;
	  public static int gameBoardHeight = (LINE+1)* SpaceBetween + LINE*Bar.BarHeight;
	 
	  private boolean gameHasStarted;
	  private String saveDataPath;
	  private String fileName="SaveData";
	  
	  public MyGameBoard(int x,int y) {
		  try {
			  saveDataPath=MyGameBoard.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(); 
		  } catch(Exception e) {
			e.printStackTrace();  
		  }
		  scoreFont=My2048Game.mainFont.deriveFont(24f);
		  this.x=x;
		  this.y=y;
		  barArray = new Bar[LINE][COLUMNS];
		  gameBoardIm = new BufferedImage(gameBoardWidth,gameBoardHeight,BufferedImage.TYPE_INT_RGB);
		  finalGameBoardIm = new BufferedImage(gameBoardWidth,gameBoardHeight,BufferedImage.TYPE_INT_RGB);
	      
		  loadHighScore();
		  createBoardImage();
		  start();
	  }
	  
	  private void createSaveData() {
		try {
			File file=new File(saveDataPath,fileName);
			FileWriter output = new FileWriter(file);
			BufferedWriter writer=new BufferedWriter(output);
			writer.write(""+ 0);
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	  }
	  private void loadHighScore() {
		  try {
				File f=new File(saveDataPath,fileName);
				if(!f.isFile()) {
				 createSaveData();	
				}
				BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
				highScore=Integer.parseInt(reader.readLine());
				reader.close();
			} catch(Exception e) {
				e.printStackTrace();
			}  
	  }
	  private void setHighScore() {
		FileWriter output=null;
		 try {
			File f = new File(saveDataPath,fileName); 
			output =  new FileWriter(f);
			BufferedWriter writer=new BufferedWriter(output);
			
				 writer.write(""+ highScore);
			 writer.close();
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
	  }
	  
	  private void createBoardImage() {
		Graphics2D g = (Graphics2D)gameBoardIm.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, gameBoardWidth, gameBoardHeight);
		g.setColor(Color.lightGray);
		
		for(int row=0;row<LINE;row++) {
			for(int col=0;col<COLUMNS;col++) {
				int x = SpaceBetween +SpaceBetween*col+Bar.BarWidth*col;
				int y = SpaceBetween +SpaceBetween*row+Bar.BarWidth*row;
				g.fillRoundRect(x, y, Bar.BarWidth, Bar.BarHeight, Bar.ARCWidth, Bar.ARCHeight);
			}
		}
	  }
	  private void start() {
		for(int i=0;i<startingBars;i++) {
			spawnRandom(); //random number in random spot
		 }
	  }	  
	  private void spawnRandom() {//pick a spot and a side where 2 or 4 is gonna be 
		Random random=new Random();
		boolean notValid = true;
		while(notValid) {
		   int location = random.nextInt(LINE*COLUMNS);
		   int row = location/LINE;
		   int col= location%COLUMNS;
		   Bar current = barArray[row][col];
		   if (current==null){
			int value = random.nextInt(10) < 9 ? 2:4; 
			Bar bar = new Bar(value,getTileX(col),getTileY(row));
			barArray[row][col]=bar;
			notValid=false;
		   }
		}
		}
	 
	  public void paintBoard(Graphics2D g) {
		Graphics2D g2d=(Graphics2D) finalGameBoardIm.getGraphics();
		g2d.drawImage(gameBoardIm, 0, 0, null);
		
		for(int row=0;row<LINE;row++) {//draw tiles
			for(int col=0;col<COLUMNS;col++) {
			Bar current = barArray[row][col];
			if(current==null) continue;
			current.paintBar(g2d);
			}	 
		}
		
		g.drawImage(finalGameBoardIm, x, y, null);
		g2d.dispose();
		
		g.setColor(Color.lightGray);
		g.setFont(scoreFont);
		g.drawString(""+ score, 30, 40);
		g.setColor(Color.red);
		g.drawString("Best: "+highScore, My2048Game.GameBoardWidth-DrawUtilsGame.getMessageWidth("Best: "+highScore, scoreFont, g)-20, 40);
	  }
	  public void update() {
		  
		  checkKeys();
		  
		  if(score>=highScore) {
			highScore=score;  
		  }
		  for(int row=0;row<LINE;row++) {
			  for (int col=0;col<COLUMNS;col++) {
				  Bar current=barArray[row][col];
				  if(current==null)continue;
				   current.update();
				   //reset postion
				   animation(current,row,col);
				   if((current.getBarValue()==2048) & (won != true)) {
					  // GameWon endGame = new GameWon();
					   won=true;
				   }
			  }
		  }
	  }
	  public int getTileX(int col) {
		  return SpaceBetween+col*Bar.BarWidth+col*SpaceBetween;
	  }
	  public int getTileY(int row) {
		  return SpaceBetween+row*Bar.BarHeight+row*SpaceBetween;
	  }
	  public void animation(Bar current, int row, int col) {
		  if (current==null) return;
		  int x= getTileX(col);
		  int y=getTileY(row);
		  int distX=current.getBarX()-x;
		  int distY=current.getBarY()-y;
		  
		  if (Math.abs(distX)<Bar.MovingSpeed) {
			  current.setBarX(current.getBarX()-distX);
		  }
		  if (Math.abs(distY)<Bar.MovingSpeed) {
			  current.setBarY(current.getBarY()-distY);
		  }
		 if (distX<0) {
			 current.setBarX(current.getBarX()+Bar.MovingSpeed);
		 }
		 if(distY<0) {
			 current.setBarY(current.getBarY()+Bar.MovingSpeed);
		 }
		if (distX>0) {
			current.setBarX(current.getBarX()-Bar.MovingSpeed);
		}
		if (distY>0) {
			current.setBarY(current.getBarY()-Bar.MovingSpeed);
		}
	  }
	  
	 private boolean move(int row,int col,int horizontalDirection, int verticalDirection, KeyboardDirection dir){
	 	 boolean canMoveBar=false;
	 	  Bar current=barArray[row][col];
	 	   if(current==null)return false;
	 	   boolean move=true;
	 	   int newCol=col;
	 	   int newRow=row;
	 	    while(move) {
	 	    	newCol+=horizontalDirection;
	 	    	newRow+=verticalDirection;
	 	    	 if(checkOutOfBounds(dir,newRow,newCol)) break;
	 	    	  if(barArray[newRow][newCol]==null) {
	 	    		 barArray[newRow][newCol]=current; 
	 	    		 barArray[newRow - verticalDirection][newCol - horizontalDirection]= null;
	 	    	     barArray[newRow][newCol].setMoveTo(new MyPoint(newRow,newCol));
	 	    	     canMoveBar=true;
	 	    	  }
	 	    	  else if(barArray[newRow][newCol].getBarValue()==current.getBarValue()&& barArray[newRow][newCol].isShouldCombine()) {
	                barArray[newRow][newCol].setShouldCombine(false);
	                barArray[newRow][newCol].setBarValue(barArray[newRow][newCol].getBarValue()*2);
	                canMoveBar=true;
	                barArray[newRow - verticalDirection][newCol - horizontalDirection]= null;
	                barArray[newRow][newCol].setMoveTo(new MyPoint(newRow,newCol));
	             //   barArray[newRow][newCol].setCombineAnimation(true);
	 	    	 //add to score
	                score=(score+barArray[newRow][newCol].getBarValue());
	 	    	  }
	 	    	  else {
	 	    		move=false;  
	 	    	  }
	 	    }
	 	 return canMoveBar;
	 }
	  private boolean checkOutOfBounds(KeyboardDirection dir, int row,int col) {
		  if(dir==KeyboardDirection.LEFT) {
			  return col<0;
		  }
		  else if(dir==KeyboardDirection.RIGHT) {
			  return col>COLUMNS-1;
		  }
		  else if(dir==KeyboardDirection.UP) {
			  return row<0;
		  }
		  else if(dir==KeyboardDirection.DOWN) {
			  return row>LINE-1;
		  }
		  return false;
	  }
	  
	  private void moveTiles(KeyboardDirection dir) {
		 boolean canMoveBar=false;
		 int horizontalDirection = 0;
		 int verticalDirection = 0;
		   if(dir==KeyboardDirection.LEFT) {
			   horizontalDirection = -1;
			    for(int row=0;row<LINE;row++) {
			    	for(int col=0;col<COLUMNS;col++) {
			    		if (!canMoveBar) {
			    			canMoveBar=move(row,col,horizontalDirection,verticalDirection,dir);
			    		}
			    		else move(row,col,horizontalDirection,verticalDirection,dir);
			    	}
			    }
		   }
		   else if(dir==KeyboardDirection.RIGHT) {
			   horizontalDirection = 1;
			    for(int row=0;row<LINE;row++) {
			    	for(int col=COLUMNS-1;col>=0;col--) {
			    		if (!canMoveBar) {
			    			canMoveBar=move(row,col,horizontalDirection,verticalDirection,dir);
			    		}
			    		else move(row,col,horizontalDirection,verticalDirection,dir);
			    	}
			    }
		   }
		   //2 2 4 8(update8)---->0 4 4 8 update right
		   // 2 2 4 8 ------> 0 0 0 16
		   else  if(dir==KeyboardDirection.UP) {
			   verticalDirection = -1;
			    for(int row=0;row<LINE;row++) {
			    	for(int col=0;col<COLUMNS;col++) {
			    		if (!canMoveBar) {
			    			canMoveBar=move(row,col,horizontalDirection,verticalDirection,dir);
			    		}
			    		else move(row,col,horizontalDirection,verticalDirection,dir);
			    	}
			    }
		   }
		   else if(dir==KeyboardDirection.DOWN) {
			   verticalDirection = 1;
			    for(int row=LINE-1;row>=0;row--) {
			    	for(int col=0;col<COLUMNS;col++) {
			    		if (!canMoveBar) {
			    			canMoveBar=move(row,col,horizontalDirection,verticalDirection,dir);
			    		}
			    		else move(row,col,horizontalDirection,verticalDirection,dir);
			    	}
			    }
		   }
		   else {
			   System.out.println(dir+"is not a valid direction");
		   }
		   for(int row=0;row<LINE;row++) {
		    	for(int col=0;col<COLUMNS;col++) {
		    	 Bar current=barArray[row][col];
		    	  if(current==null)continue;
		    	  current.setShouldCombine(true);
		    	}
		    }
		   if(canMoveBar) {
			   spawnRandom();
			   //check dead
			  checkDead(); 
		   }
	  }
	    private void checkDead() {
	    	if((score > highScore) & (bet != true) & (highScore != 0)) {
				   bet=true;
				   highScore = score;
				   setHighScore();
				   //YouBetRecord endGame = new YouBetRecord();
				   return;
			  }
	    	for (int row=0; row<LINE;row++) {
	    		for (int col=0;col<COLUMNS;col++){
	    			if(barArray[row][col]==null) return;
	    			if (checkSurroundingBars(row,col,barArray[row][col])) {
	    				return;
	    			}
	    		}
	    	}
	    	dead=true;
	    	//GameOver gmovr = new GameOver();
	    	setHighScore();
	    }
	    private boolean checkSurroundingBars(int row, int col, Bar current) {
	    	if(row>0){
	    	 Bar check = barArray[row-1][col];
	    	 if(check==null)return true;
	    	 if(current.getBarValue()==check.getBarValue()) return true;
	    	}
	    	if (row<LINE-1) {
	    		Bar check=barArray[row+1][col];
	    		if(check==null) return true;
	    		if(current.getBarValue()==check.getBarValue()) return true;
	    	}
	    	if (col>0) {
	    		Bar check=barArray[row][col-1];
	    		if(check==null) return true;
	    		if(current.getBarValue()==check.getBarValue()) return true;
	    	}
	    	if (col<COLUMNS-1) {
	    		Bar check=barArray[row][col+1];
	    		if(check==null) return true;
	    		if(current.getBarValue()==check.getBarValue()) return true;
	    	}
	    	return false;
	    }
	  private void checkKeys() {
		if(KeyboardButtons.typed(KeyEvent.VK_LEFT)) {
			//move tiles left
			moveTiles(KeyboardDirection.LEFT);
		if(gameHasStarted) gameHasStarted=true;
		}
		if(KeyboardButtons.typed(KeyEvent.VK_RIGHT)) {
			//move tiles right
			moveTiles(KeyboardDirection.RIGHT);
		if(gameHasStarted) gameHasStarted=true;
		}
		if(KeyboardButtons.typed(KeyEvent.VK_UP)) {
			//move tiles up
			moveTiles(KeyboardDirection.UP);
		if(gameHasStarted) gameHasStarted=true;
		}
		if(KeyboardButtons.typed(KeyEvent.VK_DOWN)) {
			//move tiles down
			moveTiles(KeyboardDirection.DOWN);
		if(gameHasStarted) gameHasStarted=true;
		}
	  }

}


