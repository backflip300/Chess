package clock;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class clock extends JFrame implements Runnable {

	JPanel panel;
	JLabel timeLabel;

	private int playerLost = 0;
	private boolean whiteTurn = true;
	private double blackTime, whiteTime;
	private int whiteMin;
	private int whiteSec;
	private int blackMin;
	private int blackSec;
	private double previousTime;

	// private int blackDisplay,whiteDisplay;
	public clock(int whiteTime, int blackTime) {

		super();
		this.whiteTime = whiteTime;
		this.blackTime = blackTime;
		this.setVisible(true);
		this.setSize(200, 200);

		panel = new JPanel();

		panel.setLayout(new MigLayout());

		timeLabel = new JLabel("", SwingConstants.CENTER);

		panel.add(timeLabel, "cell 1 0");
		previousTime = System.currentTimeMillis();
		update();

		this.add(panel);
	}

	public void getTime() {
		whiteMin = (int) whiteTime / 60;
		whiteSec = (int) whiteTime % 60;
		blackMin = (int) blackTime / 60;
		blackSec = (int) blackTime % 60;
	}
	
	public int getWhiteTime() {
		int time = whiteMin*60 + whiteSec;
		return time;
	}
	
	public int getBlackTime() {
		int time = blackMin*60 + blackSec;
		return time;
	}
	
	public void setWhiteTime(int whiteTime) {
		whiteMin = whiteTime/60;
		whiteSec = whiteTime%60;
	}
	
	public void setBlackTime(int blackTime) {
		blackMin = blackTime/60;
		blackSec = blackTime%60;
	}
	
	public void changeclock() {
		whiteTurn = (whiteTurn == true) ? false : true;
	}

	public void update() {

		if (whiteTurn == true) {
			whiteTime += (previousTime - System.currentTimeMillis())/1000;
			if (whiteTime <= 0) {
				playerLost = 1;
			}
		} else {
			blackTime += (previousTime - System.currentTimeMillis())/1000;
			if (blackTime <= 0) {
				playerLost = 2;
			}
		}
		previousTime = System.currentTimeMillis();
		getTime();
		timeLabel.setText(whiteMin + ":" + whiteSec + " - " + blackMin + ":"
				+ blackSec);
	}

	public int playerLost() {
		return playerLost;
	}

	@Override
	public void run() {
		while (true) {
			update();
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
