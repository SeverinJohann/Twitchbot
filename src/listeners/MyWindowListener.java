package listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import def.Main;

public class MyWindowListener implements WindowListener {
	
	private Main main;
	
	public MyWindowListener(Main main) {
		this.main = main;
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		main.getBot().save();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}
}
