package main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import board.checkersBoard;
import board.chessBoard;
import clock.clock;
import net.miginfocom.swing.MigLayout;

public class Main extends JPanel {

	private static final long serialVersionUID = 1L;

	JFrame gui;
	JPanel panel1 = new JPanel();

	BufferedReader reader;
	FileInputStream fStream = null;
	String gameType;

	checkersBoard checkersBoard;
	chessBoard chessBoard;
	ImageIcon icon = new ImageIcon("images/icon.png");

	int menuValue; // holds value returned by menu buttons
	int time = 0;

	boolean gameLoaded = false; // true if game is started from 'Load', false if
								// game is started from elsewhere

	String gameID = "";

	public Main() {

		menuValue = 0;
		createFrame();

		do {
			try {
				Thread.sleep(12);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (menuValue == 1) { // if 'Checkers' button is pressed
				gameCheckers(gameLoaded);
				panel1.removeAll();
				createMenu();
				gui.add(panel1);
				gui.revalidate();
				gameLoaded = false;
			}
			if (menuValue == 2) { // if 'Chess' button is pressed
				gameChess(gameLoaded);
				panel1.removeAll();
				createMenu();
				gui.add(panel1);
				gui.revalidate();
				gameLoaded = false;
			}
			if (menuValue == 3) { // if 'Load' button is pressed
				gameID = loadGameMenu();
				if (gameID != null) {
					Object[] options = { "Load", "Delete", "Cancel" };
					int load = JOptionPane.showOptionDialog(null,
							"What would you like to do with " + gameID + "?",
							"Load or Delete?",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE, null, options,
							options[0]);
					if (load == 0) {
						try {
							fStream = new FileInputStream("GameSave/" + gameID);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						reader = new BufferedReader(new InputStreamReader(
								fStream));

						try {
							gameType = reader.readLine();
							reader.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (gameType.equals("Checkers")) {
							menuValue = 1;
						} else {
							menuValue = 2;
						}
						gameLoaded = true;
					} else if (load == 1) {
						Path path = Paths.get("GameSave/" + gameID);
						try {
							Files.deleteIfExists(path);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {

					}
				} else {
					menuValue = 0;
				}
			}
			if (menuValue == 4) {
				try{
				time = settingsMenu();
				}catch(NullPointerException e){
					
				}
				menuValue = 0;
			}
			if (menuValue == 5) { // if 'Exit' button is pressed
				menuExit();
			}
		} while (menuValue != 22); // returns to menu after a game is closed

	}

	void createFrame() { // creates basic frame for the program and adds menu

		gui = new JFrame("Board Games");
		createMenu();
		gui.add(panel1);
		gui.revalidate();
		gui.setSize(435, 523);
		gui.setIconImage(icon.getImage());
		gui.getContentPane().setLayout(new MigLayout());
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setVisible(true);
		gui.setResizable(false);

	}

	public void createMenu() { // creates multiple buttons and JPanel to be
								// added to JFrame

		menuValue = 0;
		panel1.setLayout(new MigLayout());
		JButton menuCheckers = new JButton("Checkers");
		menuCheckers.setPreferredSize(new Dimension(139, 26));
		menuCheckers.setVerticalTextPosition(AbstractButton.CENTER);
		menuCheckers.setHorizontalTextPosition(AbstractButton.CENTER);
		panel1.add(menuCheckers, "cell 1 0, gapleft 128, gaptop 160");
		menuCheckers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				menuValue = 1;
			}
		});

		JButton menuChess = new JButton("Chess");
		menuChess.setPreferredSize(menuCheckers.getPreferredSize());
		menuChess.setVerticalTextPosition(AbstractButton.CENTER);
		menuChess.setHorizontalTextPosition(AbstractButton.CENTER);
		panel1.add(menuChess, "cell 1 1, gapleft 128");
		menuChess.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				menuValue = 2;
			}
		});

		JButton menuLoad = new JButton("Load/Delete");
		menuLoad.setPreferredSize(menuCheckers.getPreferredSize());
		menuLoad.setVerticalTextPosition(AbstractButton.CENTER);
		menuLoad.setHorizontalTextPosition(AbstractButton.CENTER);
		panel1.add(menuLoad, "cell 1 2, gapleft 128");
		menuLoad.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				menuValue = 3;
			}

		});

		JButton menuSettings = new JButton("Timer Settings");
		menuSettings.setPreferredSize(menuCheckers.getPreferredSize());
		menuSettings.setVerticalTextPosition(AbstractButton.CENTER);
		menuSettings.setHorizontalTextPosition(AbstractButton.CENTER);
		panel1.add(menuSettings, "cell 1 3, gapleft 128");
		menuSettings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				menuValue = 4;
			}
		});

		JButton menuExit = new JButton("Exit");
		menuExit.setPreferredSize(menuCheckers.getPreferredSize());
		menuExit.setVerticalTextPosition(AbstractButton.CENTER);
		menuExit.setHorizontalTextPosition(AbstractButton.CENTER);
		panel1.add(menuExit, "cell 1 4, gapleft 128");
		menuExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				menuValue = 5;
			}
		});

	}

	void drawBoard() { // adds board to gui
		if (menuValue == 1) { // add checkersBoard to gui if 'Checkers' button
								// is pressed
			checkersBoard = new checkersBoard();
			checkersBoard.setPreferredSize(new Dimension(1000, 1000));
			panel1.removeAll();
			panel1.add(checkersBoard);

		} else if (menuValue == 2) { // add chessBoard to gui if 'Checkers'
										// button is pressed
			chessBoard = new chessBoard();
			chessBoard.setPreferredSize(new Dimension(1000, 1000));
			panel1.removeAll();
			panel1.add(chessBoard);
		}
		gui.revalidate();
	}

	void gameCheckers(boolean gameLoaded) { // contains some game functionality
											// of checkers
		int blackPieces;
		int redPieces;
		int repeatedBoard;
		int startPieces;
		int endPieces;
		
		clock clock = null;

		drawBoard();
		checkersBoard.gameType = "Checkers";
		checkersBoard.playerTurn = 'B';

		if (gameLoaded) { // if a game save is loaded, runs 'readBoard' method
							// to set piecePosition and playerTurn to the
							// correct values
			checkersBoard.readBoard(gameID);
			if (checkersBoard.p1Time != 0) {
				(new Thread(clock = new clock(checkersBoard.p1Time, checkersBoard.p2Time))).start();
			}
		} else if (time != 0) {
			(new Thread(clock = new clock(time, time))).start();
		}
		

		do {
			String currentBoard = checkersBoard
					.writeBoard(checkersBoard.piecePosition);
			checkersBoard.previousBoards.add(currentBoard);

			checkersBoard.moveMade = false;

			startPieces = 0;

			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (checkersBoard.piecePosition[x][y] != "  ") {
						startPieces++;
					}
				}
			}

			try {
				Thread.sleep(12);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			while (!checkersBoard.moveMade && !checkersBoard.gameOver) {
				try {
					if (clock.playerLost() != 0) {
						checkersBoard.gameOver = true;
					}
					checkersBoard.p1Time = clock.getWhiteTime();
					checkersBoard.p2Time = clock.getBlackTime();
				} catch (NullPointerException e) {

				}
				try {
					Thread.sleep(12);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			checkersBoard.repaint();

			endPieces = 0;

			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (checkersBoard.piecePosition[x][y] != "  ") {
						endPieces++;
					}
				}
			}

			if (checkersBoard.piecePosition[checkersBoard.endRow][checkersBoard.endCol]
					.charAt(1) == 'M' || startPieces > endPieces) {
				checkersBoard.previousBoards.clear();
			}

			repeatedBoard = 0;

			for (int x = 0; x < checkersBoard.previousBoards.size(); x++) { 				
				if (checkersBoard.writeBoard(checkersBoard.piecePosition)
						.equals(checkersBoard.previousBoards.get(x))) {
					repeatedBoard++;
				}
			}

			blackPieces = 0;
			redPieces = 0;
			for (int rowNo = 0; rowNo < 8; rowNo++) { // counts each players
														// remaining pieces
				for (int colNo = 0; colNo < 8; colNo++) {
					if (checkersBoard.piecePosition[rowNo][colNo].charAt(0) == 'B') {
						blackPieces++;
					} else if (checkersBoard.piecePosition[rowNo][colNo]
							.charAt(0) == 'R') {
						redPieces++;
					}
				}
			}

			if (repeatedBoard == 2) { // if the board position has been repeated
										// 3 times the game is ended in a draw
				checkersBoard.gameOver = true;
			}

			if (blackPieces == 0) { // ends game if either player has no pieces
									// left
				checkersBoard.gameOver = true;
			} else if (redPieces == 0) {
				checkersBoard.gameOver = true;
			}

			if (checkersBoard.allowedMoves.size() == 0) { 
				try {
					clock.changeclock();
				} catch (NullPointerException e) {

				}
				if (checkersBoard.playerTurn == 'B') {

					checkersBoard.playerTurn = 'R';
				} else {
					checkersBoard.playerTurn = 'B';
				}
			}

		} while (!checkersBoard.gameOver);
		try {
			clock.dispose();
		} catch (NullPointerException e) {
			
		}
	}

	void gameChess(boolean gameLoaded) { // contains some game functionality of
											// chess
		int repeatedBoard;
		int startPieces;
		int endPieces;
		clock clock = null;

		drawBoard();
		chessBoard.gameType = "Chess";
		chessBoard.playerTurn = 'W';
		

		if (gameLoaded) { // if a game save is loaded, runs 'readBoard' method
							// to set piecePosition and playerTurn to the
							// correct values
			chessBoard.readBoard(gameID);
			if (chessBoard.p1Time != 0) {
					(new Thread(clock = new clock(chessBoard.p1Time, chessBoard.p2Time))).start();
			}
		} else if (time != 0) {
			(new Thread(clock = new clock(time, time))).start();
		}
		
		do {
			String currentBoard = chessBoard
					.writeBoard(chessBoard.piecePosition);
			chessBoard.previousBoards.add(currentBoard);
			chessBoard.moveMade = false;

			startPieces = 0;

			for (int x = 0; x < 8; x++) { // counts pieces before move is made,
											// used in comparison to check if
											// pieces are taken
				for (int y = 0; y < 8; y++) {
					if (chessBoard.piecePosition[x][y] != "  ") {
						startPieces++;
					}
				}
			}

			// have a rest, have a mars bar.
			try {
				Thread.sleep(12);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			chessBoard.gameOver = chessBoard.checkCheckMate(
					chessBoard.startRow, chessBoard.endRow,
					chessBoard.startCol, chessBoard.endCol,
					chessBoard.piecePosition, chessBoard.playerTurn);

			while (!chessBoard.moveMade && !chessBoard.gameOver) { 
				try {
					if (clock.playerLost() != 0) {
						chessBoard.gameOver = true;
					}
					chessBoard.p1Time = clock.getWhiteTime();
					chessBoard.p2Time = clock.getBlackTime();
				} catch (NullPointerException e) {

				}
				try {
					Thread.sleep(12);
				} catch (InterruptedException e) {
					
				}
			}
			chessBoard.repaint();

			endPieces = 0;

			for (int x = 0; x < 8; x++) { // counts pieces after move is made,
											// used in comparison to check if
											// pieces are taken
				for (int y = 0; y < 8; y++) {
					if (chessBoard.piecePosition[x][y] != "  ") {
						endPieces++;
					}
				}
			}

			if (chessBoard.piecePosition[chessBoard.endRow][chessBoard.endCol]
					.charAt(1) == 'P' || startPieces > endPieces) { 
				chessBoard.previousBoards.clear();
			}

			repeatedBoard = 0;

			for (int x = 0; x < chessBoard.previousBoards.size(); x++) { 
				if (chessBoard.writeBoard(chessBoard.piecePosition).equals(
						chessBoard.previousBoards.get(x))) {
					repeatedBoard++;
				}
			}

			if (repeatedBoard == 2) { // if the board position has been repeated
										// 3 times the game is ended in a draw
				chessBoard.gameOver = true;
			}
			try {
				clock.changeclock();
			} catch (NullPointerException e) {

			}
				chessBoard.playerTurn = (chessBoard.playerTurn == 'W') ? 'B':'W'; 
		} while (!chessBoard.gameOver);
		
		JOptionPane.showMessageDialog(null, chessBoard.playerTurn + " won the game");
		try {
			clock.dispose();
		} catch (NullPointerException e) {

		}
	}

	String loadGameMenu() { // displays previous game saves, which can be
							// selected and loaded from where they were left
		String gameID;
		if (getSaveNames() == null) {
			JOptionPane.showMessageDialog(null,
					"There are no existing save files");
			gameID = null;
		} else {
			gameID = (String) JOptionPane.showInputDialog(null,
					"Select the game save you would like to load", "Load Game",
					JOptionPane.PLAIN_MESSAGE, null, getSaveNames(),
					getSaveNames()[0]);
		}
		return gameID;
	}

	public Object[] getSaveNames() {
		char[] nameArray;
		String tmpName;
		File folder = new File("GameSave/");

		File[] listOfFiles = folder.listFiles();
		if (listOfFiles.length != 0) { // validation that files do exist
			Object[] sIDArray = new String[listOfFiles.length];
			for (int i = 0; i < listOfFiles.length; i++) {
				tmpName = "";
				nameArray = listOfFiles[i].getName().toCharArray();
				for (int j = 0; j < (nameArray.length); j++) {
					tmpName = tmpName + nameArray[j];
				}
				sIDArray[i] = tmpName;
			}
			return sIDArray;
		} else
			return null;
	}

	void menuExit() { // displays message before closing game
		int exit;
		exit = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to exit?");
		if (exit == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
		menuValue = 0;
	}

	int settingsMenu() {
		String time;
		String[] timeOptions = { "None", "1:00", "3:00", "5:00", "10:00",
				"30:00" };
		time = (String) JOptionPane.showInputDialog(null,
				"Select how much time you would like to put on the timer",
				"Timer Settings", JOptionPane.PLAIN_MESSAGE, null, timeOptions,
				timeOptions[0]);
		if (time.equals(null))
			return 0;
		else if (time.equals("None"))
			return 0;
		else if (time.equals("1:00"))
			return 60;
		else if (time.equals("3:00"))
			return 180;
		else if (time.equals("5:00"))
			return 300;
		else if (time.equals("10:00"))
			return 600;
		else
			return 1800;
	}

	public static void main(String args[]) {
		new Main();
	}

}
