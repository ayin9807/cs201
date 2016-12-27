import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import princeton.*;

/**
 * Print statistics on Percolation: prompts the user for N and T, performs T
 * independent experiments on an N-by-N grid, prints out the 95% confidence
 * interval for the percolation threshold, and prints mean and std. deviation
 * of timings
 * 
 * @author Kevin Wayne
 * @author Jeff Forbes
 */

public class PercolationStats {
	public static int RANDOM_SEED = 1234;
	public static Random ourRandom = new Random(RANDOM_SEED);
	
	public static int performExperiment(int x) {
		IPercolate perc = new PercolationDFS(x);
		//IPercolate perc = new PercolationUF(x, new QuickFind());
		//IPercolate perc = new PercolationUF(x, new QuickUWPC());
		
		ArrayList<ShuffleCell> list = new ArrayList<ShuffleCell>();
		
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < x; j++) {
				ShuffleCell temp = new ShuffleCell(i, j);
				list.add(temp);
			}
		}
		
		Collections.shuffle(list, ourRandom);
		
		int count = 0;
		
		for (ShuffleCell cell: list) {
			int row = cell.row;
			int col = cell.col;
			perc.open(row, col);
			count++;
			if (perc.percolates()) break;
		}
		// System.out.println(count);
		return count;
	}
	
	public static double calcStdDev(double mean, double[] data) {
		double variance = 0;
		
		for (int i = 0; i < data.length; i++) {
			variance += Math.pow(data[i]-mean, 2);
		}
		
		double result = Math.sqrt(variance/(data.length-1));
		
		return result;
	}
	
	public static double calcInterval(double stddev, int num) {
		double x = Math.sqrt(num);
		return (1.96*stddev)/x;
	}

	public static void main(String[] args) {
		int N, T;
		if (args.length == 2) { // use command-line arguments for
								// testing/grading
			N = Integer.parseInt(args[0]);
			T = Integer.parseInt(args[1]);
		} else {
			String input = JOptionPane.showInputDialog("Enter N and T", "20 100");
			// TODO: parse N and T from input
			String[] numbers = input.split(" ");
			N = Integer.parseInt(numbers[0]);
			T = Integer.parseInt(numbers[1]);
		}

		// TODO: Perform T experiments for N-by-N grid
		double[] percents = new double[T];
		double[] times = new double[T];
		double totalPercent = 0;
		double totalTime = 0;
		
		for (int i = 0; i < T; i++) {
			double start = System.currentTimeMillis();
			int cellsOpened = performExperiment(N);
			double end = System.currentTimeMillis();
			double time = (end - start) / 1000;
			
			percents[i] = ((double)cellsOpened)/(N*N);
			times[i] = time;
			totalPercent += ((double)cellsOpened)/(N*N);
			totalTime += time;
		}
		
		// TODO: print statistics and confidence interval
		double percentMean = (double)(totalPercent/T);
		double percentStdDev = calcStdDev(percentMean, percents);
		
		double meanTime = (double)(totalTime/T);
		double timeStdDev = calcStdDev(meanTime, times);
		
		double interval = calcInterval(percentStdDev, T);
		
		System.out.println("mean percolation threshold = " + percentMean);
		System.out.println("stddev = " + percentStdDev);
		System.out.println("95% confidence interval = [" + (percentMean-interval) 
				+ ", " + (percentMean + interval) + "]");
		System.out.println("total time = " + totalTime + "s");
		System.out.println("mean time per experiment = " + meanTime);
		System.out.println("stddev = " + timeStdDev);
		
	}
}
