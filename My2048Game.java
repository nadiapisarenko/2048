package My2048;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class My2048Game extends JPanel implements KeyListener, Runnable{
	private static final long serialVersionUID = 1L;
    public static final int GameBoardWidth = 500;
    public static final int GameBoardHeight = 560;
    public static final Font mainFont = new Font("Calibri", Font.PLAIN, 28); 
	private Thread game;
	private boolean gameRunning;
	private BufferedImage gameImage = new BufferedImage(GameBoardWidth,GameBoardHeight,BufferedImage.TYPE_INT_RGB);
    private MyGameBoard myBoard;
	
	public My2048Game() {
		setFocusable(true);
		setPreferredSize(new Dimension(GameBoardWidth,GameBoardHeight));
		addKeyListener(this);
		
		myBoard=new MyGameBoard(GameBoardWidth/2-MyGameBoard.gameBoardWidth/2, GameBoardHeight-MyGameBoard.gameBoardHeight-10);
	 }
	
	private void updateGame() {
		myBoard.update();
		 KeyboardButtons.updateKeyboard();
	 }
	 private void paintGame() {
			Graphics2D g = (Graphics2D) gameImage.getGraphics() ;
			g.setColor(Color.white);
			g.fillRect(0, 0, GameBoardWidth, GameBoardHeight);
			//render board
			myBoard.paintBoard(g);
			g.dispose();
			
			Graphics2D g2d = (Graphics2D) getGraphics();
			g2d.drawImage(gameImage, 0,0,null);
			g2d.dispose();
		 }
	 @Override
		public void run() {
	    double nsPerUpdate = 1000000000.0/60;
		 //last update time in nanoseconds
		double then = System.nanoTime();
		double unprocessed = 0;
	 while(gameRunning) {
			boolean shouldRepaint = false;
			 double now = System.nanoTime();
			 unprocessed+=(now-then)/nsPerUpdate;
			then=now;
		 while (unprocessed>=1) {
			updateGame();
			unprocessed--;
			shouldRepaint = true;
		 }
		 if (shouldRepaint) {
			paintGame();
			shouldRepaint=false;
		 }
		 else {
			 try {
				 Thread.sleep(1); 
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
		 }
		
	}
}
	 public synchronized void start() {
		   if(gameRunning)return;
		   gameRunning=true;
		   game = new Thread(this,"game");
		   game.start();
	   }
	   
	   public synchronized void stop() {
		   if(!gameRunning)return;
		   gameRunning=false;
		   System.exit(0);
	   }

		@Override
		public void keyPressed(KeyEvent e) {
	      KeyboardButtons.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
		KeyboardButtons.keyReleased(e);	
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}   
}
