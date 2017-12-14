package My2048;


import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.text.BadLocationException;

public class MainClass {
	public static void main(String [] args)  {
		 
		 My2048Game game= new My2048Game();
		 
		 MyJFrame window = new MyJFrame("2048( Meine Kursacho!!!)");
		 window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 window.setResizable(false);
		 window.add(game);
		 window.pack();
		 window.setLocationRelativeTo(null);
		 window.setVisible(true);
		 
		 game.start();
	 }
}
class MyJFrame extends JFrame implements ActionListener {
	
	
	MenuBar menuBar;
	 Menu mfile;
	 MenuItem newGame;
	 MenuItem exitGame;
	 
	 MyJFrame() {
		 super();
	 }
	 
	 MyJFrame(String s) {
		 super(s);
		 
		 menuBar = new MenuBar();
		 
		 mfile = new Menu("Менюшка");
		 newGame = new MenuItem("Новая игра");
		 exitGame = new MenuItem("Выйти из игры");
		 
		 mfile.add(newGame);
		 mfile.add(exitGame);
		 
		 menuBar.add(mfile);
		
		 newGame.addActionListener(this);
		 exitGame.addActionListener(this);
		 
		 setMenuBar(menuBar);
	 }

	@Override
	public void actionPerformed(ActionEvent e) {
		 if(e.getSource().equals(newGame)) {
			 new MainClass();
			 MyGameBoard.score=0;
			 MainClass.main(null);
			 
			 this.dispose();
			 
		 }
		 else if (e.getSource().equals(exitGame)) {
			 dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		 }
	}	 
}