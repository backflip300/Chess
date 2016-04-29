package board;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class checkersBoard extends Board {

	private static final long serialVersionUID = 1L;
	int rowVal;
	
	int colVal;
	private boolean aM;
	private BufferedImage Crown;
	
	{ // 2D array for referencing piece type and position on checkers board
		for (int rowNo = 0; rowNo < 8; rowNo++) {
			for (int colNo = 0; colNo < 8; colNo++) {
				if (rowNo < 3 && (rowNo % 2 != colNo % 2)) {
					piecePosition[rowNo][colNo] = "BM";
				} else if (rowNo > 4 && (rowNo % 2 != colNo % 2)) {
					piecePosition[rowNo][colNo] = "RM";
				} else {
					piecePosition[rowNo][colNo] = "  ";
				}
			}
		}
	}

	public checkersBoard() {
		super();
	}

	public void paintComponent(Graphics g) { // paints pieces onto board
		super.paintComponent(g);
		getImages();

		if (pieceHeld == true) {
			g.setColor(Color.YELLOW);
			g.fillRect(50 * startCol, 50 * startRow, 50, 50);
		}
		for (int rowNo = 0; rowNo < 8; rowNo++) {
			for (int colNo = 0; colNo < 8; colNo++) {
				if (piecePosition[rowNo][colNo].charAt(0) == 'B') {
					g.setColor(Color.BLACK);
					g.fillOval((50 * colNo) + 2, (50 * rowNo) + 2, 45, 45);
				} else if (piecePosition[rowNo][colNo].charAt(0) == 'R') {
					g.setColor(Color.RED);
					g.fillOval((50 * colNo) + 2, (50 * rowNo) + 2, 45, 45);
				}
				if (piecePosition[rowNo][colNo].charAt(1) == 'K') {
					g.drawImage(Crown, (50 * colNo) + 12, (50 * rowNo) + 17,
							null);
				}
			}
		}
	}

	public void findMoves() { // finds allowed moves after a piece is captured
		allowedMoves.clear();
		boolean[] toCheck = new boolean[4];
		int numchecking = 0;
		Arrays.fill(toCheck, true);
		for (int r = -2; r <= 2; r += 4) {
			for (int c = -2; c <= 2; c += 4) {
				try {
					if (piecePosition[endRow + r][endCol + c] != "  ") {
						toCheck[numchecking] = false;
					}
				} catch (Exception e) {
					toCheck[numchecking] = false;
				}
				try {
					if (piecePosition[endRow + (r / 2)][endCol + (c / 2)]
							.charAt(0) == piecePosition[endRow][endCol]
							.charAt(0)
							|| piecePosition[endRow + (r / 2)][endCol + (c / 2)] == "  ") {
						toCheck[numchecking] = false;
					}
				} catch (Exception e) {
					toCheck[numchecking] = false;
				}

				if (piecePosition[endRow][endCol] == "BM" && r < 0) {
					toCheck[numchecking] = false;
				} else if (piecePosition[endRow][endCol] == "RM" && r > 0) {
					toCheck[numchecking] = false;
				}

				numchecking++;
				if (toCheck[((r / 2) + 1) + ((c + 2) / 4)] == true) {
					allowedMoves.add(((endRow + r) * 10) + endCol + c);
				}
			}

		}
	}

	public void getImages() {
		try {
			Crown = ImageIO.read(new File("images/Crown.png"));
		} catch (IOException ex) {
			// handle exception...
		}
	}

	@Override
	public boolean isMoveLegal(int startRow, int endRow, int startCol, int endCol, String[][] piecePosition, char playerTurn) { // checks if moves comply with rules specific to checkers

		boolean legalMove = super.isMoveLegal(startRow, endRow, startCol,
				endCol, piecePosition, playerTurn);
		aM = false;
		if (allowedMoves.size() != 0) {
			for (int i = 0; i < allowedMoves.size(); i++) {
				if (allowedMoves.get(i) / 10 == endRow
						&& allowedMoves.get(i) % 10 == endCol) {
					aM = true;
				}
			}
			legalMove = aM;
		}
		if (legalMove == true) {
			if (Math.abs(startRow - endRow) != Math.abs(startCol - endCol)) {
				legalMove = false;
			}
			if (piecePosition[endRow][endCol] != "  ") {
				legalMove = false;
			}
			switch (piecePosition[startRow][startCol]) {
			case "BM":
				if (startRow > endRow) {
					legalMove = false;
				}
				break;

			case "RM":
				if (startRow < endRow) {
					legalMove = false;
				}
				break;
			default:

			}

			if (startRow == endRow || startCol == endCol) {
				legalMove = false;
			}
			if (Math.abs(startRow - endRow) != 1) {
				if (Math.abs(startRow - endRow) != 2) {
					legalMove = false;
				} else if (piecePosition[(startRow + endRow) / 2][(startCol + endCol) / 2]
						.charAt(0) == ' '
						|| piecePosition[(startRow + endRow) / 2][(startCol + endCol) / 2]
								.charAt(0) == playerTurn) {
					legalMove = false;
				}
			}
		}
		return legalMove;
	}

	public void checkIfPiecePromoted(int startRow, int endRow, int startCol, int endCol, String[][] piecePosition) { // checks if a 'Man' reaches the back rank
		if (piecePosition[endRow][endCol].equals("BM") && endRow == 7) {
			piecePosition[endRow][endCol] = "BK";
		} else if (piecePosition[endRow][endCol].equals("RM") && endRow == 0) {
			piecePosition[endRow][endCol] = "RK";
		}
	}

	public boolean pieceTaken(int startRow, int endRow, int startCol, int endCol, String[][] piecePosition) { // checks if a piece is captured
		boolean pieceTaken = false;
		if ((piecePosition[(startRow + endRow) / 2][(startCol + endCol) / 2]
				.charAt(0) != playerTurn)
				&& (piecePosition[(startRow + endRow) / 2][(startCol + endCol) / 2]
						.charAt(0) != ' ')) {
			pieceTaken = true;
		}
		return pieceTaken;
	}

	public void removePiece(int startRow, int endRow, int startCol, int endCol, String[][] piecePosition) { // removes piece if it is captured
		int rowDirection = (int) Math.signum(endRow - startRow);
		int colDirection = (int) Math.signum(endCol - startCol);
		piecePosition[startRow + rowDirection][startCol + colDirection] = "  ";
	}

	@Override
	public void mousePressed(MouseEvent e) { // handles mouse event
		repaint();
		boolean legalMove = false;
		boolean pieceTaken = false;
		// clicked off board
		if (stickyFingers == true) {
			for (int i = 0; i < allowedMoves.size(); i++) {
				if (e.getY() / 50 == allowedMoves.get(i) / 10
						&& e.getX() / 50 == allowedMoves.get(i) % 10) {
					stickyFingers = false;
				}
			}
		}
		if (stickyFingers == false) {
			if (e.getX() > 400 || e.getY() > 400) {
				/*
				 * Stops errors when you click off the board
				 */

			} else if (pieceHeld == false) {
				// no piece selected
				startRow = e.getY() / 50;
				startCol = e.getX() / 50;
				if (piecePosition[startRow][startCol].charAt(0) != playerTurn) {
					pieceHeld = false;
				} else {
					pieceHeld = true;
				}
			} else {
				// piece already selected
				legalMove = isMoveLegal(startRow, e.getY() / 50, startCol,
						e.getX() / 50, piecePosition, playerTurn);

				if (legalMove) {
					movePiece(startRow, e.getY() / 50, startCol, e.getX() / 50);
					checkIfPiecePromoted(startRow, e.getY() / 50, startCol,
							e.getX() / 50, piecePosition);
					pieceTaken = pieceTaken(startRow, e.getY() / 50, startCol,
							e.getX() / 50, piecePosition);
					if (pieceTaken) {
						removePiece(startRow, e.getY() / 50, startCol,
								e.getX() / 50, piecePosition);
						findMoves();
					}

					moveMade = true;
				}
				if (allowedMoves.size() == 0) {
					pieceHeld = false;

				} else {
					stickyFingers = true;
					startRow = endRow;
					startCol = endCol;
				}
			}
		}
		repaint();
	}
}
