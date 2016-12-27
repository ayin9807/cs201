import java.util.Arrays;

/**
 * Simulate a system to see its Percolation Threshold, but use a UnionFind
 * implementation to determine whether simulation occurs. The main idea is that
 * initially all cells of a simulated grid are each part of their own set so
 * that there will be n^2 sets in an nxn simulated grid. Finding an open cell
 * will connect the cell being marked to its neighbors --- this means that the
 * set in which the open cell is 'found' will be unioned with the sets of each
 * neighboring cell. The union/find implementation supports the 'find' and
 * 'union' typical of UF algorithms.
 * <P>
 * 
 * @author Owen Astrachan
 * @author Jeff Forbes
 *
 */

public class PercolationUF implements IPercolate {
	private final int OUT_BOUNDS = -1;
	private int[][] myGrid;
	private IUnionFind myUniter;
	private int top;
	private int bottom;

	/**
	 * Constructs a Percolation object for a nxn grid that uses unionThing to
	 * store sets representing the cells and the top/source and bottom/sink
	 * virtual cells
	 */
	public PercolationUF(int n, IUnionFind unionThing) {
		// TODO complete PercolationUF constructor
		myGrid = new int[n][n];
		for (int[] row: myGrid) {
			Arrays.fill(row, BLOCKED);
		}
		myUniter = unionThing;
		myUniter.initialize(n*n+2);
		top = n*n;
		bottom = n*n+1;
		
		for (int i = 0; i < n; i++) {
			myUniter.union(top, getIndex(0, i));
			myUniter.union(bottom, getIndex(n-1, i));
		}
	}

	/**
	 * Return an index that uniquely identifies (row,col), typically an index
	 * based on row-major ordering of cells in a two-dimensional grid. However,
	 * if (row,col) is out-of-bounds, return OUT_BOUNDS.
	 */
	public int getIndex(int row, int col) {
		// TODO complete getIndex
		if (row < 0 || row >= myGrid.length) return OUT_BOUNDS;
		if (col < 0 || col >= myGrid.length) return OUT_BOUNDS;
		
		return row * myGrid.length + col;
	}

	public void open(int i, int j) {
		// TODO complete open
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid.length) 
			throw new ArrayIndexOutOfBoundsException("Cell out of bounds");
		
		myGrid[i][j] = OPEN;
		connect(i, j);
	}

	public boolean isOpen(int i, int j) {
		// TODO complete isOpen
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid.length)
			throw new ArrayIndexOutOfBoundsException("Cell out of bounds");
		
		return (myGrid[i][j] == OPEN);
			
	}

	public boolean isFull(int i, int j) {
		// TODO complete isFull
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid.length)
			throw new ArrayIndexOutOfBoundsException("Cell out of bounds");
		
		int index = getIndex(i, j);
		if (myGrid[i][j] == BLOCKED)
			return false;
		
		return (myUniter.connected(index, top) && isOpen(i, j));
	}

	public boolean percolates() {
		// TODO complete percolates
		return myUniter.connected(top, bottom);
	}

	/**
	 * Connect new site (row, col) to all adjacent open sites
	 */
	private void connect(int row, int col) {
		// TODO complete connect
		int index = getIndex(row, col);
		
		if (row+1 >= 0 && row+1 < myGrid.length && !(myGrid[row+1][col] == BLOCKED))
			myUniter.union(index, getIndex(row+1, col));
		if (row-1 >= 0 && row-1 < myGrid.length && !(myGrid[row-1][col] == BLOCKED))
			myUniter.union(index, getIndex(row-1, col));
		if (col+1 >= 0 && col+1 < myGrid.length && !(myGrid[row][col+1] == BLOCKED))
			myUniter.union(index, getIndex(row, col+1));
		if (col-1 >= 0 && col-1 < myGrid.length && !(myGrid[row][col-1] == BLOCKED))
			myUniter.union(index, getIndex(row, col-1));
	}

}
