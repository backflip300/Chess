package board;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public abstract class Board extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;
	boolean pieceHeld = false;
	public boolean moveMade = false;
	boolean stickyFingers = false;
	public boolean gameOver = false;

	public ArrayList<Integer> allowedMoves = new ArrayList<Integer>();
	public ArrayList<String> previousBoards = new ArrayList<String>();

	public int startRow = 0;
	public int startCol = 0;
	public int endRow = 0;

	public int endCol = 0;
	public char playerTurn;
	String heldPiece;
	public String gameType = "";
	public String[][] piecePosition = new String[8][8];
	
	public int p1Time = 0;
	public int p2Time = 0;

	JButton gameMenu = new JButton("Menu");
	JButton gameSave = new JButton("Save");
	JButton gameExit = new JButton("Exit");

	public Board() {
		addMouseListener(this);
		this.setLayout(new MigLayout());
		gameSave.setPreferredSize(gameMenu.getPreferredSize());
		gameExit.setPreferredSize(gameMenu.getPreferredSize());
		
		gameMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gameOver = true;
				System.out.println(gameOver);				
			}
		});

		gameSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					gameSave();
				} catch (NullPointerException | FileSystemException e) {

				}
			}
		});

		gameExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gameExit();
			}
		});

		this.add(gameMenu, "cell 0 0, gaptop 420, gapleft 41");
		this.add(gameSave, "cell 1 0, gaptop 420, gapleft 50");
		this.add(gameExit, "cell 2 0, gaptop 420, gapleft 50");

	}

	public void paintComponent(Graphics g) { // paints board

		super.paintComponent(g);
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				int x = col * 50;
				int y = row * 50;
				if ((row % 2) == (col % 2)) {
					g.setColor(Color.LIGHT_GRAY);
				} else {
					g.setColor(Color.GRAY);
				}
				g.fillRect(x, y, 50, 50);
			}
		}

	}

	public void gameExit() { // displays message before closing game
		int exit;
		exit = JOptionPane
				.showConfirmDialog(null,
						"Exiting will not save the current game. Do you still want to exit?");
		if (exit == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	void gameSave() throws NullPointerException, FileSystemException { 
		String gameID;
		String path = "GameSave/";
		int overWrite;
		do {
			gameID = JOptionPane
					.showInputDialog("What do you want to call this game?");
			path = path + gameID;
			if (gameID.equals("")) {
				JOptionPane
						.showMessageDialog(null, "Please enter a valid name");
			}
			if (new File(path).exists()) { // if file path already exists, asks
											// user if they would like to
											// overwrite the file
				overWrite = JOptionPane
						.showConfirmDialog(null,
								"This file already exists, would you like to overwrite?");
				if (overWrite == JOptionPane.YES_OPTION) {
					try {
						Files.delete(Paths.get((path)));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					gameID = "";
				}
			}
		} while (gameID.equals(""));

		if (gameID != null) {
			File f = new File("GameSave/" + gameID);
			try {
				f.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						"GameSave/" + gameID, true));
				writer.write(gameType + "\n" + playerTurn + "\n" + p1Time
						+ "\n" + p2Time + "\n" + writeBoard(piecePosition)
						+ "\n");
				for (int x = 0; x < previousBoards.size(); x++) {
					writer.write(previousBoards.get(x) + "\n");
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isMoveLegal(int startRow, int endRow, int startCol,
			int endCol, String[][] piecePosition, char playerTurn) {
		boolean legalMove = true;
		if ((startRow == endRow) && (startCol == endCol)) {
			legalMove = false;
		}
		if (piecePosition[startRow][startCol].charAt(0) != playerTurn) {
			legalMove = false;
		}
		if (piecePosition[endRow][endCol].charAt(0) == playerTurn) {
			legalMove = false;
		}
		return legalMove;
	}

	public void movePiece(int startRow, int endRow, int startCol, int endCol) { 
		this.startRow = startRow;
		this.endRow = endRow;
		this.startCol = startCol;
		this.endCol = endCol;
		piecePosition[endRow][endCol] = piecePosition[startRow][startCol];
		piecePosition[startRow][startCol] = "  ";
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	public void readBoard(String gameID) { // reads saved game file and
											// transfers onto board
		FileInputStream fStream = null;

		int x = 0;
		String board = null;
		String gameType = null;
		char playerTurn = 0;
		try {
			fStream = new FileInputStream("GameSave/" + gameID);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				fStream));

		String strLine;

		// Read File Line By Line
		try {
			while ((strLine = reader.readLine()) != null) {
				// Print the content on the console
				if (x == 0) {
					gameType = strLine;

				} else if (x == 1) {
					playerTurn = strLine.charAt(0);
				} else if (x == 2){
					p1Time = Integer.parseInt(strLine);
				} else if (x == 3) {
					p2Time = Integer.parseInt(strLine);
				} else if (x == 4) {
					board = strLine;
				} else {
					previousBoards.add(strLine);
				}
				x++;

			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int c = 0;
		for (int v = 0; v < 8; v++) {
			for (int h = 0; h < 8; h++) {
				String temp = (Character.toString(board.charAt(c)));
				if (temp.matches("[0-9]+")) {
					for (int i = 0; i < Integer.parseInt(temp); i++) {
						piecePosition[v][h] = "  ";
						h++;
					}
					h--;
					c++;
				} else if (Character.isUpperCase(board.charAt(c))) {
					piecePosition[v][h] = "B" + temp;
					c++;
				} else {
					if (gameType.equals("Chess")) {
						piecePosition[v][h] = "W" + temp.toUpperCase();
					} else {
						piecePosition[v][h] = "R" + temp.toUpperCase();
					}
					c++;
				}

			}

		}

		this.playerTurn = playerTurn;
	}

	public String writeBoard(String[][] board) { // turns board into string when
													// game is saved
		String boardState = "";
		int n = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (board[x][y] == "  ") {
					n++;
				} else {
					if (n != 0) {
						boardState = boardState + n;
						n = 0;
					}
					if (board[x][y].charAt(0) == 'B') {
						boardState = boardState + board[x][y].charAt(1);
					} else {
						boardState = boardState
								+ (char) ((int) board[x][y].charAt(1) + 32);
					}
				}
			}
			if (n > 0) {
				boardState = boardState + n;
				n = 0;
			}
		}

		return boardState;
	}

}
