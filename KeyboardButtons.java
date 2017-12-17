package My2048;

import java.awt.event.KeyEvent;

public class KeyboardButtons {
	public static boolean[] pressed = new boolean[256];
	public static boolean[] previous=new boolean[256];
 private KeyboardButtons() { }
    public static void updateKeyboard() {
    	for(int i=0;i<4;i++ ) {
    		if(i==0)previous[KeyEvent.VK_LEFT]=pressed[KeyEvent.VK_LEFT];
    		if(i==1)previous[KeyEvent.VK_RIGHT]=pressed[KeyEvent.VK_RIGHT];
    		if(i==2)previous[KeyEvent.VK_UP]=pressed[KeyEvent.VK_UP];
    		if(i==3)previous[KeyEvent.VK_DOWN]=pressed[KeyEvent.VK_DOWN];
    	}
    }
    public static void keyPressed(KeyEvent e) {
    	pressed[e.getKeyCode()]=true;
    }
    public static void keyReleased(KeyEvent e) {
    	pressed[e.getKeyCode()]=false;
    }
    public static boolean typed (int keyEvent) {
    	return !pressed[keyEvent]&& previous[keyEvent];
    }
}
