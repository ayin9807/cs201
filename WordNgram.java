import java.util.Arrays;

/*
 * This class encapsulates N words/strings so that the
 * group of N words can be treated as a key in a map or an
 * element in a set, or an item to be searched for in an array.
 * <P>
 * @author Annie Yin
 */

public class WordNgram implements Comparable<WordNgram> {

	private String[] myWords;

	/*
	 * Store the n words that begin at index start of array list as the N words
	 * of this N-gram.
	 * 
	 * @param list contains at least n words beginning at index start
	 * 
	 * @start is the first of the N words to be stored in this N-gram
	 * 
	 * @n is the number of words to be stored (the n in this N-gram)
	 */
	public WordNgram(String[] list, int start, int n) {
		myWords = new String[n];
		System.arraycopy(list, start, myWords, 0, n);
	}
	
	public WordNgram(String[] words)
	{
		myWords = words;
	}
	
	public String toString()
	{
		String value = "";
		for (int i = 0; i < myWords.length; i++)
		{
			value += myWords[i] + " ";
		}
		return value;
	}
	
	public String getLastWord(int k)
	{
		return myWords[k-1];
	}

	/**
	 * Return value that meets criteria of compareTo conventions.
	 * 
	 * @param wg
	 *            is the WordNgram to which this is compared
	 * @return appropriate value less than zero, zero, or greater than zero
	 */
	public int compareTo(WordNgram wg) 
	{
		// TODO implement compareTo
		return Arrays.toString(myWords).compareTo(Arrays.toString(wg.myWords));
	}

	/**
	 * Return true if this N-gram is the same as the parameter: all words the
	 * same.
	 * 
	 * @param o
	 *            is the WordNgram to which this one is compared
	 * @return true if o is equal to this N-gram
	 */
	public boolean equals(Object o) 
	{
		if (o == this)
		{
			return true;
		}
		
		if (o == null || o.getClass() != this.getClass())
		{
			return false;
		}
		
		WordNgram wg = (WordNgram) o;
		// TODO return correct value
		for (int i = 0; i < myWords.length; i++)
		{
			if (!myWords[i].equals(wg.myWords[i]))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns a good value for this N-gram to be used in hashing.
	 * 
	 * @return value constructed from all N words in this N-gram
	 */
	public int hashCode() 
	{
		// TODO return a better hash value
		int sum = 0;
		
		for (int i = 0; i < myWords.length; i++)
		{
			sum = 1001*sum + myWords[i].hashCode();
		}
		
		return sum;
		
	}
}
