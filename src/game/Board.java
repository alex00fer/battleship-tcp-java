package game;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import game.Tile.TileState;

public class Board {

	final int size, numberOfShips;
	Tile[][] myBoard, enemyBoard;

	public Board(int size, int numberOfShips) {

		if (size <= 0)
			throw new InvalidParameterException("Invalid board size! Cannot be <= 0");
		if (numberOfShips <= 0)
			throw new InvalidParameterException("Invalid number of ships! Cannot be <= 0");

		this.size = size;
		this.numberOfShips = numberOfShips;

		generateMyBoard();
		generateEnemyBoard();
	}

	public void generateMyBoard() {
		myBoard = new Tile[size][size];
		List<Tile> tempTiles = new ArrayList<Tile>(size * size);

		// Generate board tiles
		for (int row = 0; row < myBoard.length; row++) {
			for (int col = 0; col < myBoard[row].length; col++) {
				Tile newTile = new Tile();
				newTile.setState(TileState.CLEAR);
				myBoard[row][col] = newTile;
				tempTiles.add(newTile);
			}
		}

		// Randomize ships locations
		Collections.shuffle(tempTiles);
		int nShips = Math.min(tempTiles.size(), numberOfShips);
		for (int i = 0; i < nShips; i++) {
			tempTiles.get(i).setState(TileState.SHIP);
		}
	}

	public void generateEnemyBoard() {
		enemyBoard = new Tile[size][size];

		// Generate board tiles
		for (int row = 0; row < enemyBoard.length; row++) {
			for (int col = 0; col < enemyBoard[row].length; col++) {
				Tile newTile = new Tile();
				newTile.setState(TileState.UNKNOWN);
				enemyBoard[row][col] = newTile;
			}
		}
	}

	/**
	 * Update my board after an enemy shoot
	 * 
	 * @param x row
	 * @param y column
	 * @return The new tile State (SHOOTED_CLEAR or SHOOTED_SHIP)
	 */
	public boolean shooted(int x, int y) {
		Tile shootedTile = myBoard[y][x];
		switch (shootedTile.getState()) {
		case CLEAR:
		case SHOOTED_CLEAR:
			shootedTile.setState(TileState.SHOOTED_CLEAR);
			return false;
		case SHIP:
		case SHOOTED_SHIP:
			shootedTile.setState(TileState.SHOOTED_SHIP);
			return true;
		default:
			throw new InvalidParameterException("Unknown shooted tile state!");
		}
	}

	/**
	 * Update enemy tile state
	 * 
	 * @param x row
	 * @param y culumn
	 */
	public void updateEnemyTile(int x, int y, boolean state) {
		enemyBoard[y][x].setState(state ? TileState.SHOOTED_SHIP : TileState.SHOOTED_CLEAR);
	}

	
	/**
	 * Checks if the game has ended
	 */
	public boolean haveILost() {
		if (!checkIfBoardHasShips(myBoard)) 
			return true;
		else
			return false;
	}
	
	private boolean checkIfBoardHasShips(Tile[][] board) {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col].getState() == TileState.SHIP)
					return true;
			}
		}
		return false; // no ships
	}
	
	/**
	 * Prints the board to the console
	 */
	public void print() {
		System.out.println();
		System.out.println("          OPPONENT'S BOARD     ");
		printBoard(enemyBoard);
		System.out.println("             YOUR BOARD        ");
		printBoard(myBoard);
	}
	
	private void printBoard(Tile[][] board) {
		
		System.out.print("  X");
		for (int i = 0; i < enemyBoard.length; i++) {
			System.out.print("  " + i);
		}
		System.out.println();
		System.out.print("Y  ");
		for (int i = 0; i < enemyBoard.length; i++) {
			System.out.print("---");
		}
		System.out.println();
		
		for (int row = 0; row < enemyBoard.length; row++) {
			for (int col = 0; col < enemyBoard[row].length; col++) {
				if (col == 0) System.out.print(row + " |");
				Tile tile = board[row][col];
				System.out.print("  " + tile.getState().getRepresentation());
			}
			System.out.println();
		}
		System.out.println();
	}
}
