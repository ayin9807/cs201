import java.util.Arrays;

import princeton.*;

/**
 * Simulate percolation thresholds for a grid-base system using depth-first-search,
 * aka 'flood-fill' techniques for determining if the top of a grid is connected
 * to the bottom of a grid.
 * <P>
 * Modified from the COS 226 Princeton code for use at Duke. The modifications
 * consist of supporting the <code>IPercolate</code> interface, renaming methods
 * and fields to be more consistent with Java/Duke standards and rewriting code
 * to reflect the DFS/flood-fill techniques used in discussion at Duke.
 * <P>
 * @author Kevin Wayne, wayne@cs.princeton.edu
 * @author Owen Astrachan, ola@cs.duke.edu
 * @author Jeff Forbes, forbes@cs.duke.edu
 */


public class PercolationDFS implements IPercolate {
	// possible instance variable for storing grid state
	public int[][] myGrid;
	public static final int BLOCKED = 0;
	public static final int OPEN = 1;
	public static final int FULL = 2;

	/**
	 * Initialize a grid so that all cells are blocked.
	 * 
	 * @param n
	 *            is the size of the simulated (square) grid
	 */
	public PercolationDFS(int n) {
		// TODO complete constructor and add necessary instance variables
		myGrid = new int[n][n];
		for (int[] row: myGrid) {
			Arrays.fill(row, BLOCKED);
		}
	}

	public void open(int i, int j) {
		// TODO complete open
		for (int c = 0; c < myGrid.length; c++) {
			for (int k = 0; k < myGrid.length; k++) {
				if (isFull(c, k)) {
					myGrid[c][k] = OPEN;
				}
			}
		}
		
		myGrid[i][j] = OPEN;
		
		for (int col = 0; col < myGrid.length; col++) {
			dfs(0, col);
		}
		
	}

	public boolean isOpen(int i, int j) {
		// TODO complete isOpen
		return (myGrid[i][j] == OPEN);
	}

	public boolean isFull(int i, int j) {
		// TODO complete isFull
		return (myGrid[i][j] == FULL);
	}

	public boolean percolates() {
		// TODO: run DFS to find all full sites
		for (int j = 0; j < myGrid.length; j++)	{
			if (isFull(myGrid.length-1, j)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Private helper method to mark all cells that are open and reachable from
	 * (row,col).
	 * 
	 * @param row
	 *            is the row coordinate of the cell being checked/marked
	 * @param col
	 *            is the col coordinate of the cell being checked/marked
	 */
	private void dfs(int row, int col) {
		// TODO: complete dfs
		if (row < 0 || row >= myGrid.length) return;
		if (col < 0 || col >= myGrid.length) return;
		if (!isOpen(row, col)) return;
		if (isFull(row, col)) return;
		
		myGrid[row][col] = FULL;
		
		dfs(row+1, col);
		dfs(row-1, col);
		dfs(row, col+1);
		dfs(row, col-1);	
	}

}
