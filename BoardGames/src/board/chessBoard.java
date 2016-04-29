package board;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class chessBoard extends Board {

	private static final long serialVersionUID = 1L;

	private BufferedImage WPawn;
	private BufferedImage WRook;
	private BufferedImage WKnight;
	private BufferedImage WBishop;
	private BufferedImage WQueen;
	private BufferedImage WKing;

	private BufferedImage BPawn;
	private BufferedImage BRook;
	private BufferedImage BKnight;
	private BufferedImage BBishop;
	private BufferedImage BQueen;
	private BufferedImage BKing;
	
	int rowChange;
	int colChange;
	int enPassant = 10;
	int piecesBefore;
	int piecesAfter;
	boolean piecePromoted = false;
	boolean pieceHeld = false;
	boolean whiteKingMoved = false;
	boolean whiteQueenMoved = false;
	boolean blackKingMoved = false;
	boolean blackQueenMoved = false;
	boolean kingCastle = false;
	boolean queenCastle = false;
	boolean inPassing = false;
	public boolean moveMade = false;
	
	char whoseTurn;

	{
		for (int rowNo = 0; rowNo < 8; rowNo++) {
			for (int colNo = 0; colNo < 8; colNo++) {
				if (rowNo == 1) {
					piecePosition[rowNo][colNo] = "BP";
				} else {
					if (rowNo == 6) {
						piecePosition[rowNo][colNo] = "WP";
					} else {
						if ((rowNo == 0) || (rowNo == 7)) {
							if (rowNo == 0) {
								piecePosition[rowNo][colNo] = "B";
							}
							if (rowNo == 7) {
								piecePosition[rowNo][colNo] = "W";
							}
							switch (colNo) {
							case 0:
							case 7:
								piecePosition[rowNo][colNo] = piecePosition[rowNo][colNo]
										+ "R";
								break;
							case 1:
							case 6:
								piecePosition[rowNo][colNo] = piecePosition[rowNo][colNo]
										+ "N";
								break;
							case 2:
							case 5:
								piecePosition[rowNo][colNo] = piecePosition[rowNo][colNo]
										+ "B";
								break;
							case 3:
								piecePosition[rowNo][colNo] = piecePosition[rowNo][colNo]
										+ "Q";
								break;
							case 4:
								piecePosition[rowNo][colNo] = piecePosition[rowNo][colNo]
										+ "K";
								break;
							}
						} else {
							piecePosition[rowNo][colNo] = "  ";
						}
					}
				}
			}
		}
	}

	public chessBoard() {
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
				switch (piecePosition[rowNo][colNo]) {
				case "WP":
					g.drawImage(WPawn, (colNo * 50) + 3, (rowNo * 50), null);
					break;

				case "WR":
					g.drawImage(WRook, (colNo * 50) + 3, (rowNo * 50) + 1, null);
					break;

				case "WN":
					g.drawImage(WKnight, (colNo * 50) + 3, (rowNo * 50) + 1,
							null);
					break;

				case "WB":
					g.drawImage(WBishop, (colNo * 50) + 3, (rowNo * 50) + 1,
							null);
					break;

				case "WQ":
					g.drawImage(WQueen, (colNo * 50) + 3, (rowNo * 50) + 1,
							null);
					break;

				case "WK":
					g.drawImage(WKing, (colNo * 50) + 3, (rowNo * 50) + 1, null);
					break;

				case "BP":
					g.drawImage(BPawn, (colNo * 50) + 3, (rowNo * 50), null);
					break;

				case "BR":
					g.drawImage(BRook, (colNo * 50) + 3, (rowNo * 50) + 1, null);
					break;

				case "BN":
					g.drawImage(BKnight, (colNo * 50) + 3, (rowNo * 50) + 1,
							null);
					break;

				case "BB":
					g.drawImage(BBishop, (colNo * 50) + 3, (rowNo * 50) + 1,
							null);
					break;

				case "BQ":
					g.drawImage(BQueen, (colNo * 50) + 3, (rowNo * 50) + 1,
							null);
					break;

				case "BK":
					g.drawImage(BKing, (colNo * 50) + 3, (rowNo * 50) + 1, null);
					break;
				}
			}
		}

		if (piecePromoted) {
			g.setColor(Color.WHITE);
			if (playerTurn == 'W') {
				g.fillRect(endCol * 50, 200, 50, 200);
				g.drawImage(BQueen, (endCol * 50) + 3, (endRow * 50) + 1, null);
				g.drawImage(BRook, (endCol * 50) + 3, ((endRow - 1) * 50) + 1,
						null);
				g.drawImage(BBishop, (endCol * 50) + 3,
						((endRow - 2) * 50) + 1, null);
				g.drawImage(BKnight, (endCol * 50) + 3,
						((endRow - 3) * 50) + 1, null);
			} else {
				g.fillRect(endCol * 50, 0, 50, 200);
				g.drawImage(WQueen, (endCol * 50) + 3, (endRow * 50) + 1, null);
				g.drawImage(WRook, (endCol * 50) + 3, ((endRow + 1) * 50) + 1,
						null);
				g.drawImage(WBishop, (endCol * 50) + 3,
						((endRow + 2) * 50) + 1, null);
				g.drawImage(WKnight, (endCol * 50) + 3,
						((endRow + 3) * 50) + 1, null);
			}
		}
	}

	public boolean checkCheck(int startRow, int endRow, int startCol, int endCol, String[][] piecePosition, char playerTurn) { // checks if a piece can make a legal move onto the King

		boolean inCheck = false;
		int kingRow = 0;
		int kingCol = 0;
		String tempEnd = piecePosition[endRow][endCol];
		String tempStart = piecePosition[startRow][startCol];

		if (startRow != endRow || startCol != endCol) {
			piecePosition[endRow][endCol] = piecePosition[startRow][startCol];
			piecePosition[startRow][startCol] = "  ";
		}
		
		for (int rowNo = 0; rowNo < 8; rowNo++) {
			for (int colNo = 0; colNo < 8; colNo++) {
				if (piecePosition[rowNo][colNo].equals(playerTurn + "K")) {
					kingRow = rowNo;
					kingCol = colNo;
				}
			}
		}

		for (int rowNo = 0; rowNo < 8; rowNo++) {
			for (int colNo = 0; colNo < 8; colNo++) {
				if (piecePosition[rowNo][colNo].charAt(0) != playerTurn
						&& piecePosition[rowNo][colNo].charAt(0) != ' ') {

					switch (piecePosition[rowNo][colNo].charAt(1)) {

					case 'P':
						
							if (isPawnMoveLegal(rowNo, kingRow, colNo, kingCol,
									piecePosition, playerTurn)) {
								inCheck = true;
							}
						break;

					case 'R':

						if (isRookMoveLegal(rowNo, kingRow, colNo, kingCol,
								piecePosition, playerTurn)) {
							inCheck = true;
						}
						break;

					case 'N':

						if (isKnightMoveLegal(rowNo, kingRow, colNo, kingCol,
								piecePosition, playerTurn)) {
							inCheck = true;
						}
						break;

					case 'B':

						if (isBishopMoveLegal(rowNo, kingRow, colNo, kingCol,
								piecePosition, playerTurn)) {
							inCheck = true;
						}
						break;

					case 'Q':

						if (isQueenMoveLegal(rowNo, kingRow, colNo, kingCol,
								piecePosition, playerTurn)) {
							inCheck = true;
						}
						break;

					case 'K':

						if (isKingMoveLegal(rowNo, kingRow, colNo, kingCol,
								piecePosition, playerTurn)) {
							inCheck = true;
						}
						break;
					}
				}
			}
		}
		piecePosition[startRow][startCol] = tempStart;
		piecePosition[endRow][endCol] = tempEnd;

		return inCheck;
	}
	
	public boolean checkStaleMate (int startRow, int endRow, int startCol, int endCol, String[][] piecePosition, char playerTurn) {
		boolean staleMate = true;
		int z = 0;
		
		if (checkCheck(startRow, endRow, startCol, endCol, piecePosition, playerTurn)) {
			staleMate = false;
		} else {
			while (staleMate) {
				int rowNo = z / 8;
				int colNo = z % 8;
				if (piecePosition[rowNo][colNo].charAt(0) == playerTurn) {
					for (int y = 0; y < 8; y++) {
						for (int x = 0; x < 8; x++) {
							if (isMoveLegal(rowNo, y, colNo, x, piecePosition, playerTurn)) {
								staleMate = false;
							}
						}
					}
				}
				z++;
			}
		}
		return staleMate;
	}

	public boolean checkCheckMate (int startRow, int endRow, int startCol, int endCol, String[][] piecePosition, char playerTurn) {

		boolean checkMate = true;
		boolean knightCheck = false;
		int kingRow = 0;
		int kingCol = 0;
		int checkingRow = 0;
		int checkingCol = 0;
		int checkingPieces = 0;
		
		if (checkCheck(startRow, startRow, startCol, startCol, piecePosition,
				playerTurn)) {
			for (int rowNo = 0; rowNo < 8; rowNo++) {
				for (int colNo = 0; colNo < 8; colNo++) {
					if (piecePosition[rowNo][colNo].equals(playerTurn + "K")) {
						kingRow = rowNo;
						kingCol = colNo;
					}
				}
			}
			
			for (int rowNo = 0; rowNo < 8; rowNo++) {
				for (int colNo = 0; colNo < 8; colNo++) {
					
					if (piecePosition[rowNo][colNo].charAt(0) != playerTurn
							&& piecePosition[rowNo][colNo].charAt(0) != ' ') {

						switch (piecePosition[rowNo][colNo].charAt(1)) {

						case 'P':

							if (isPawnMoveLegal(rowNo, kingRow, colNo, kingCol,
									piecePosition, playerTurn)) {
								checkingPieces++;
								checkingRow = rowNo;
								checkingCol = colNo;
								System.out.println(rowNo + "\t" + colNo);
							}
							break;

						case 'R':

							if (isRookMoveLegal(rowNo, kingRow, colNo, kingCol,
									piecePosition, playerTurn)) {
								checkingPieces++;
								checkingRow = rowNo;
								checkingCol = colNo;
								System.out.println(rowNo + "\t" + colNo);
							}
							break;

						case 'N':

							if (isKnightMoveLegal(rowNo, kingRow, colNo,
									kingCol, piecePosition, playerTurn)) {
								checkingPieces++;
								checkingRow = rowNo;
								checkingCol = colNo;
								knightCheck = true;
								System.out.println(rowNo + "\t" + colNo);
							}
							break;

						case 'B':

							if (isBishopMoveLegal(rowNo, kingRow, colNo,
									kingCol, piecePosition, playerTurn)) {
								checkingPieces++;
								checkingRow = rowNo;
								checkingCol = colNo;
								System.out.println(rowNo + "\t" + colNo);
							}
							break;

						case 'Q':

							if (isQueenMoveLegal(rowNo, kingRow, colNo,
									kingCol, piecePosition, playerTurn)) {
								checkingPieces++;
								checkingRow = rowNo;
								checkingCol = colNo;
								System.out.println(rowNo + "\t" + colNo);
							}
							break;

						case 'K':

							if (isKingMoveLegal(rowNo, kingRow, colNo, kingCol,
									piecePosition, playerTurn)) {
								checkingPieces++;
								checkingRow = rowNo;
								checkingCol = colNo;
								System.out.println(rowNo + "\t" + colNo);
							}
							break;
						}
					}
				}
			}
			System.out.println(checkingPieces);
			if (checkingPieces == 1) {
				System.out.println(piecePosition[checkingRow][checkingCol]);
					for (int rowNo = 0; rowNo < 8; rowNo++) {
						for (int colNo = 0; colNo < 8; colNo++) {
							if (piecePosition[rowNo][colNo].charAt(0) == playerTurn) {
								if (isMoveLegal(rowNo, checkingRow, colNo, checkingCol, piecePosition, playerTurn)) {
									checkMate = false;
								} else if (knightCheck) {
									
								} else {
									int rowDiff = checkingRow - kingRow;
									int colDiff = checkingCol - kingCol;
									int divider = 1;
									if (Math.abs(rowDiff) == Math.abs(colDiff)) {
										divider = 2;
									}
									if (Math.abs(rowDiff) == 1 || Math.abs(colDiff) == 1) {
										
									} else {
										for (int x = 0; x < ((Math.abs(rowDiff) + Math.abs(colDiff)) / divider); x++) {
											if (isMoveLegal(rowNo, kingRow + (int) (x * Math.signum(rowDiff)),
												colNo, kingCol + (int) (x * Math.signum(colDiff)), piecePosition, playerTurn)) {
												checkMate = false;
											}
										}
									}
								}
							}
						}
					}
				}
			for (int rowNo = kingRow - 1; rowNo < kingRow + 2; rowNo++) {
				for (int colNo = kingCol - 1; colNo < kingCol + 2; colNo++) {
					try {
						if (isMoveLegal(kingRow, rowNo, kingCol, colNo,
								piecePosition, playerTurn)) {
							checkMate = false;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			checkMate = false;
		}
		System.out.println(checkMate);
		return checkMate;
	}
	
	public boolean checkPromotion(int endRow, int endCol, String[][] piecePosition, char playerTurn) { // checks if a Pawn reaches the back rank

		boolean piecePromoted = false;

		if (piecePosition[endRow][endCol].charAt(1) == 'P') {
			if (playerTurn == 'W' && endRow == 0) {
				piecePromoted = true;
			} else if (playerTurn == 'B' && endRow == 7) {
				piecePromoted = true;
			}
		}
		return piecePromoted;
	}

	@Override
	public boolean isMoveLegal(int startRow, int endRow, int startCol, int endCol, String[][] piecePosition, char playerTurn) { // checks if moves comply with rules specific to chess

		boolean legalMove = super.isMoveLegal(startRow, endRow, startCol,
				endCol, piecePosition, playerTurn);

		if (legalMove) {
			if (checkCheck(startRow, endRow, startCol, endCol, piecePosition,
					playerTurn)) {
				legalMove = false;
			}
		}

		if (legalMove) {
			switch (piecePosition[startRow][startCol].charAt(1)) {

			case 'P':

				legalMove = isPawnMoveLegal(startRow, endRow, startCol, endCol,
						piecePosition, playerTurn);
				break;

			case 'R':

				legalMove = isRookMoveLegal(startRow, endRow, startCol, endCol,
						piecePosition, playerTurn);
				break;

			case 'N':

				legalMove = isKnightMoveLegal(startRow, endRow, startCol,
						endCol, piecePosition, playerTurn);
				break;

			case 'B':

				legalMove = isBishopMoveLegal(startRow, endRow, startCol,
						endCol, piecePosition, playerTurn);
				break;

			case 'Q':

				legalMove = isQueenMoveLegal(startRow, endRow, startCol,
						endCol, piecePosition, playerTurn);
				break;

			case 'K':

				legalMove = isKingMoveLegal(startRow, endRow, startCol, endCol,
						piecePosition, playerTurn);
				break;
			}

		}
		return legalMove;
	}

	public boolean isPawnMoveLegal(int startRow, int endRow, int startCol, int endCol, String[][] piecePosition, char playerTurn) { // checks if Pawn moves comply with rules specific to Pawn

		boolean legalMove = true;
		rowChange = endRow - startRow;
		colChange = endCol - startCol;

		if (piecePosition[startRow][startCol].charAt(0) == 'W' && rowChange > 0) {
			legalMove = false;
		} else if (piecePosition[startRow][startCol].charAt(0) == 'B'
				&& rowChange < 0) {
			legalMove = false;
		}
		if (Math.abs(rowChange) == 1 && colChange == 0) {
			if (!(piecePosition[endRow][endCol].equals("  "))) {
				legalMove = false;
			}
		}
		if (Math.abs(rowChange) != 1) {
			if (Math.abs(rowChange) == 2) {
				if (piecePosition[startRow][startCol].charAt(0) == 'W'
						&& startRow == 6) {
					if (piecePosition[startRow - 1][startCol] != "  ") {
						legalMove = false;
					}
				} else if (piecePosition[startRow][startCol].charAt(0) == 'B'
						&& startRow == 1) {
					if (piecePosition[startRow + 1][startCol] != "  ") {
						legalMove = false;
					}
				} else {
					legalMove = false;
				}
			} else {
				legalMove = false;
			}
			if (colChange != 0) {
				legalMove = false;
			}
			if (!(piecePosition[endRow][endCol].equals("  "))) {
				legalMove = false;
			}
		}
		if (Math.abs(colChange) > 1) {
			legalMove = false;
		}
		if (Math.abs(rowChange) == Math.abs(colChange)) {
			if (piecePosition[endRow][endCol].charAt(0) != piecePosition[startRow][startCol]
					.charAt(0)
					&& piecePosition[endRow][endCol].charAt(0) != ' ') {

			} else if (piecePosition[startRow][startCol].charAt(0) == 'W') {
				if (!(piecePosition[endRow][endCol].charAt(0) == ' '
						&& enPassant == endCol && startRow == 3)) {
					legalMove = false;
				} else {
					inPassing = true;
				}
			} else if (piecePosition[startRow][startCol].charAt(0) == 'B') {
				if (!(piecePosition[endRow][endCol].charAt(0) == ' '
						&& enPassant == endCol && startRow == 4)) {
					legalMove = false;
				} else {
					inPassing = true;
				}
			}
		}
		return legalMove;
	}

	public boolean isRookMoveLegal(int startRow, int endRow, int startCol, int endCol, String[][] piecePosition, char playerTurn) { // checks if Rook moves comply with rules specific to Rook

		boolean legalMove = true;

		rowChange = endRow - startRow;
		colChange = endCol - startCol;

		if (colChange == 0 || rowChange == 0) {
			for (int i = 1; i < Math.abs(rowChange) + Math.abs(colChange); i++) {
				if (piecePosition[(int) (startRow + (i * Math.signum(rowChange)))][(int) (startCol + (i * Math
						.signum(colChange)))] != "  ") {
					legalMove = false;
				}
			}
		} else {
			legalMove = false;
		}
		return legalMove;

	}

	public boolean isKnightMoveLegal(int startRow, int endRow, int startCol, int endCol, String[][] piecePosition, char playerTurn) { // checks if Knight moves comply with rules specific to Knight

		boolean legalMove = true;
		rowChange = Math.abs(startRow - endRow);
		colChange = Math.abs(startCol - endCol);
		if (!(rowChange + colChange == 3 && Math.abs(rowChange - colChange) == 1)) {
			legalMove = false;
		}
		return legalMove;

	}

	public boolean isBishopMoveLegal(int startRow, int endRow, int startCol, int endCol, String[][] piecePosition, char playerTurn) { // checks if Bishop moves comply with rules specific to Bishop

		boolean legalMove = true;

		rowChange = endRow - startRow;
		colChange = endCol - startCol;

		if (Math.abs(rowChange) == Math.abs(colChange)) {
			for (int i = 1; i < Math.abs(rowChange); i++) {
				if (piecePosition[(int) (startRow + (i * Math.signum(rowChange)))][(int) (startCol + (i * Math
						.signum(colChange)))] != "  ") {
					legalMove = false;
				}
			}
		} else {
			legalMove = false;
		}
		return legalMove;

	}

	public boolean isQueenMoveLegal(int startRow, int endRow, int startCol, int endCol, String[][] piecePosition, char playerTurn) { // checks if Queen moves comply with rules specific to Queen

		boolean legalMove = true;

		rowChange = endRow - startRow;
		colChange = endCol - startCol;

		if (colChange == 0 || rowChange == 0) {
			for (int i = 1; i < Math.abs(rowChange) + Math.abs(colChange); i++) {
				if (piecePosition[(int) (startRow + (i * Math.signum(rowChange)))][(int) (startCol + (i * Math
						.signum(colChange)))] != "  ") {
					legalMove = false;
				}
			}
		} else if (Math.abs(rowChange) == Math.abs(colChange)) {
			for (int i = 1; i < Math.abs(rowChange); i++) {
				if (piecePosition[(int) (startRow + (i * Math.signum(rowChange)))][(int) (startCol + (i * Math
						.signum(colChange)))] != "  ") {
					legalMove = false;
				}
			}
		} else {
			legalMove = false;
		}
		return legalMove;

	}

	public boolean isKingMoveLegal(int startRow, int endRow, int startCol, int endCol, String[][] piecePosition, char playerTurn) { // checks if King moves comply with rules specific to King

		boolean legalMove = true;

		rowChange = endRow - startRow;
		colChange = endCol - startCol;

		if (colChange == 2 && rowChange == 0) {
			legalMove = false;
			if (playerTurn == 'W' && !whiteKingMoved) {
				if (piecePosition[endRow][endCol - 1].equals("  ")
						&& piecePosition[endRow][endCol].equals("  ")) {
					if (!checkCheck(startRow, endRow, startCol, endCol - 1,
							piecePosition, playerTurn)
							&& !checkCheck(startRow, startRow, startCol,
									startCol, piecePosition, playerTurn)) {
						kingCastle = true;
						legalMove = true;
					}
				}

			}
			if (playerTurn == 'B' && !blackKingMoved) {
				if (piecePosition[endRow][endCol - 1].equals("  ")
						&& piecePosition[endRow][endCol].equals("  ")) {
					if (!checkCheck(startRow, endRow, startCol, endCol - 1,
							piecePosition, playerTurn)
							&& !checkCheck(startRow, startRow, startCol,
									startCol, piecePosition, playerTurn)) {
						kingCastle = true;
						legalMove = true;
					}
				}
			}
		} else if (colChange == -2 && rowChange == 0) {
			legalMove = false;
			if (playerTurn == 'W' && !whiteQueenMoved) {
				if (piecePosition[endRow][endCol + 1].equals("  ")
						&& piecePosition[endRow][endCol].equals("  ")) {
					if (!checkCheck(startRow, endRow, startCol, endCol + 1,
							piecePosition, playerTurn)
							&& !checkCheck(startRow, startRow, startCol,
									startCol, piecePosition, playerTurn)) {
						queenCastle = true;
						legalMove = true;
					}
				}

			}
			if (playerTurn == 'B' && !blackQueenMoved) {
				if (piecePosition[endRow][endCol + 1].equals("  ")
						&& piecePosition[endRow][endCol].equals("  ")) {
					if (!checkCheck(startRow, endRow, startCol, endCol + 1,
							piecePosition, playerTurn)
							&& !checkCheck(startRow, startRow, startCol,
									startCol, piecePosition, playerTurn)) {
						queenCastle = true;
						legalMove = true;
					}
				}
			}

		} else if (!(Math.abs(rowChange) <= 1 && Math.abs(colChange) <= 1)) {
			legalMove = false;
		}

		return legalMove;

	}

	public void getImages() {
		try {
			WPawn = ImageIO.read(new File("images/pawn_white.png"));
			WRook = ImageIO.read(new File("images/rook_white.png"));
			WKnight = ImageIO.read(new File("images/knight_white.png"));
			WBishop = ImageIO.read(new File("images/bishop_white.png"));
			WQueen = ImageIO.read(new File("images/queen_white.png"));
			WKing = ImageIO.read(new File("images/king_white.png"));

			BPawn = ImageIO.read(new File("images/pawn_black.png"));
			BRook = ImageIO.read(new File("images/rook_black.png"));
			BKnight = ImageIO.read(new File("images/knight_black.png"));
			BBishop = ImageIO.read(new File("images/bishop_black.png"));
			BQueen = ImageIO.read(new File("images/queen_black.png"));
			BKing = ImageIO.read(new File("images/king_black.png"));
		} catch (IOException ex) {
			// handle exception...
		}
	}

	@Override
	public void mousePressed(MouseEvent e) { // handles mouse event
		repaint();
		boolean legalMove;
		if (e.getX() > 400 || e.getY() > 400) {
			/*
			 * Stops errors when you click off the board
			 */

		} else if (piecePromoted) {
			if (e.getX() / 50 == endCol) {
				if (playerTurn == 'W') {
					switch (e.getY() / 50) {
					case 7:
						piecePosition[endRow][endCol] = "BQ";
						piecePromoted = false;
						break;

					case 6:
						piecePosition[endRow][endCol] = "BR";
						piecePromoted = false;
						break;

					case 5:
						piecePosition[endRow][endCol] = "BB";
						piecePromoted = false;
						break;

					case 4:
						piecePosition[endRow][endCol] = "BN";
						piecePromoted = false;
						break;

					default:
						break;
					}
				} else {
					switch (e.getY() / 50) {
					case 0:
						piecePosition[endRow][endCol] = "WQ";
						piecePromoted = false;
						break;

					case 1:
						piecePosition[endRow][endCol] = "WR";
						piecePromoted = false;
						break;

					case 2:
						piecePosition[endRow][endCol] = "WB";
						piecePromoted = false;
						break;

					case 3:
						piecePosition[endRow][endCol] = "WN";
						piecePromoted = false;
						break;

					default:
						break;
					}

				}
			}
		} else if (pieceHeld == false) {
			startRow = e.getY() / 50;
			startCol = e.getX() / 50;
			for (int x = 0; x > 8; x++) {
				for (int y = 0; y > 8; y++) {
					if (!piecePosition[x][y].equals("  ")) {
						piecesBefore++;
					}
				}
			}
			if (piecePosition[startRow][startCol].charAt(0) != playerTurn) {
				pieceHeld = false;
			} else {
				pieceHeld = true;
			}
		} else {
			legalMove = isMoveLegal(startRow, e.getY() / 50, startCol,
					e.getX() / 50, piecePosition, playerTurn);
			if (legalMove) {
				movePiece(startRow, e.getY() / 50, startCol, e.getX() / 50);
				if (piecePosition[endRow][endCol].charAt(1) == 'P'
						&& Math.abs(endRow - startRow) == 2) {
					enPassant = endCol;
				} else {
					enPassant = 10;
				}
				if (kingCastle) {
					piecePosition[endRow][endCol - 1] = piecePosition[endRow][endCol + 1];
					piecePosition[endRow][endCol + 1] = "  ";
					kingCastle = false;
				} else if (queenCastle) {
					piecePosition[endRow][endCol + 1] = piecePosition[endRow][endCol - 2];
					piecePosition[endRow][endCol - 2] = "  ";
					queenCastle = false;
				}
				if (inPassing) {
					if (playerTurn == 'W') {
						piecePosition[endRow + 1][endCol] = "  ";
					} else {
						piecePosition[endRow - 1][endCol] = "  ";
					}
					inPassing = false;
				}
				if (startRow == 0) {
					if (startCol == 0 || startCol == 4) {
						blackQueenMoved = true;
					}
					if (startCol == 7 || startCol == 4) {
						blackKingMoved = true;
					}
				} else if (startRow == 7) {
					if (startCol == 0 || startCol == 4) {
						whiteQueenMoved = true;
					}
					if (startCol == 7 || startCol == 4) {
						whiteKingMoved = true;
					}
				}
				if (endRow == 0 || endRow == 7) {
					if (checkPromotion(endRow, endCol, piecePosition,
							playerTurn)) {
						piecePromoted = true;
						repaint();
					}
				}
				
				for (int x = 0; x > 8; x++) {
					for (int y = 0; y > 8; y++) {
						if (!piecePosition[x][y].equals("  ")) {
							piecesAfter++;
						}
					}
				}
				
				if (piecesAfter != piecesBefore) {
					
				}
				moveMade = true;
			}
			pieceHeld = false;
		}
		repaint();
	}

}
