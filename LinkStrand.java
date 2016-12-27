import java.util.ArrayList;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class LinkStrand implements IDnaStrand {
	
	public class Node {
		String info;
		Node next;
		
		Node(String s) {
			info = s;
			next = null;
		}
	}
	
	private Node myFirst, myLast;  // first and last node in list
	private long mySize;  // # of nucleotides in DNA
	private long myAppends;  // # of appends

	/**
	 * Create a strand representing an empty DNA strand, length of zero.
	 */
	public LinkStrand() {
		// TODO: Implement this method
		this("");
	}

	/**
	 * Create a strand representing s. No error checking is done to see if s
	 * represents valid genomic/DNA data.
	 * 
	 * @param s
	 *            is the source of cgat data for this strand
	 */
	public LinkStrand(String s) {
		// TODO: Implement this method
		Node n = new Node(s);
		myFirst = n;
		myLast = n;
		mySize = s.length();
		myAppends = 0;
	}

	/**
	 * Cut this strand at every occurrence of enzyme, essentially replacing
	 * every occurrence of enzyme with splicee.
	 * 
	 * @param enzyme
	 *            is the pattern/strand searched for and replaced
	 * @param splicee
	 *            is the pattern/strand replacing each occurrence of enzyme
	 * @return the new strand leaving the original strand unchanged.
	 */
	@Override
	public IDnaStrand cutAndSplice(String enzyme, String splicee) {
		// TODO: Implement this method
		int pos = 0;
		int start = 0;
		String search = myFirst.info;
		boolean first = true;
		LinkStrand ret = null;
		
		if (myFirst != myLast) {
			throw new RuntimeException("There are multiple nodes.");
		}
		else {
			while ((pos = search.indexOf(enzyme, pos)) >= 0) {
				if (first) {
					ret = new LinkStrand(search.substring(start, pos));
					first = false;
				} 
				else {
					ret.append(search.substring(start, pos));
				}
				start = pos + enzyme.length();
				ret.append(splicee);
				pos++;
			}

			if (start < search.length()) {
				if (ret == null) {
					ret = new LinkStrand("");
				} 
				else {
					ret.append(search.substring(start));
				}
			}
		}	
		
		return ret;
	}
	

	/**
	 * Initialize this strand so that it represents the value of source. No
	 * error checking is performed.
	 * 
	 * @param source
	 *            is the source of this enzyme
	 */
	@Override
	public void initializeFrom(String source) {
		// TODO: Implement this method
		Node n = new Node(source);
		myFirst = n;
		myLast = n;
		mySize = source.length();
		myAppends = 0;	
	}

	/**
	 * Returns the number of nucleotides/base-pairs in this strand.
	 */
	@Override
	public long size() {
		// TODO: Implement this method
		return mySize;
	}

	/**
	 * Returns the sequence of DNA this object represents as a String
	 * 
	 * @return the sequence of DNA this represents
	 */
	@Override
	public String toString() {
		// TODO: Implement this method
		StringBuilder strand = new StringBuilder();
		Node current = myFirst;
		
		while (current != null)
		{
			strand.append(current.info);
			current = current.next;
		}
		
		return strand.toString();
	}

	/**
	 * Return some string identifying this class.
	 * 
	 * @return a string representing this strand and its characteristics
	 */
	@Override
	public String strandInfo() {
		// TODO: Implement this method
		return this.getClass().toString();
	}

	/**
	 * Append a strand of DNA to this strand. If the strand being appended is
	 * represented by a LinkStrand object then an efficient append is performed.
	 * 
	 * @param dna
	 *            is the strand being appended
	 */
	@Override
	public IDnaStrand append(IDnaStrand dna) {
		// TODO: Implement this method
		if (dna instanceof LinkStrand)
		{
			LinkStrand a = (LinkStrand) dna;
			myLast.next = a.myFirst;
			myLast = a.myLast;
			mySize = dna.size();
			myAppends++;
			return this;
		}
		else
		{
			String temp = dna.toString();
			return append(temp);
		}
	}

	/**
	 * Simply append a strand of dna data to this strand.
	 * 
	 * @param dna
	 *            is the String appended to this strand
	 */
	@Override
	public IDnaStrand append(String dna) {
		// TODO: Implement this method
		Node a = new Node(dna);
		myLast.next = a;
		myLast = a;
		mySize += dna.length();
		myAppends += 1;
		
		return this;
	}

	/**
	 * Returns an IDnaStrand that is the reverse of this strand, e.g., for
	 * "CGAT" returns "TAGC"
	 * 
	 * @return reverse strand
	 */
	@Override
	public IDnaStrand reverse() 
	{
		// TODO: Implement this method
		Stack<String> nodes = new Stack<String>();
		Map<String, String> strings = new HashMap<String, String>();
		Node current = myFirst;
			
		while (current != null) {
			nodes.push(current.info);
			current = current.next;
		}
		
		LinkStrand reverse = new LinkStrand();
		
		while (!nodes.empty()) {
			String info = nodes.pop();
			Node curr;
			
			if (strings.containsKey(info)) {
				String temp = strings.get(info);
				curr = new Node(temp);
				reverse.append(temp);
			}
			else {
				String strReversed = new StringBuilder(info).reverse().toString();
				strings.put(info, strReversed);
				curr = new Node(strReversed);
				reverse.append(strReversed);
			}
			curr = curr.next;
		}
		
		return reverse;
	}

	/**
	 * Returns a string that can be printed to reveal information about what
	 * this object has encountered as it is manipulated by append and
	 * cutAndSplice.
	 * 
	 * @return
	 */
	@Override
	public String getStats() {
		// TODO: Implement this method
		return String.format("# append calls = %d", myAppends);
	}

	/**
	 * Returns an ArrayList of Strings corresponding to the nodes in the linked
	 * list this class contains. That is, the first value of the list should be
	 * the info within the head of the linked list, the second value should be
	 * the info within the node the head points to, etc. The ArrayList returned
	 * should be generated at the time the method is called. That is, the
	 * ArrayList should have a scope of only this method (i.e. not global)
	 * 
	 * @return list of Strings corresponding to nodes
	 */
	public ArrayList<String> nodeList() {
		// TODO: Implement this method
		ArrayList<String> strands = new ArrayList<String>();
		Node current = myFirst;
		
		while (current != null) {
			strands.add(current.info);
			current = current.next;
		}
		
		return strands;
	}
}
