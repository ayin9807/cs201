import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
/**
 * General trie/priority queue algorithm for implementing Autocompletor
 * 
 * @author Austin Lu
 *
 */
public class TrieAutocomplete implements Autocompletor {

	/**
	 * Root of entire trie
	 */
	protected Node myRoot;

	/**
	 * Constructor method for TrieAutocomplete. Should initialize the trie
	 * rooted at myRoot, as well as add all nodes necessary to represent the
	 * words in terms.
	 * 
	 * @param terms
	 *            - The words we will autocomplete from
	 * @param weights
	 *            - Their weights, such that terms[i] has weight weights[i].
	 * @throws a
	 *             NullPointerException if either argument is null
	 */
	public TrieAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null)
			throw new NullPointerException("One or more arguments null");
		// Represent the root as a dummy/placeholder node
		myRoot = new Node('-', null, 0);

		for (int i = 0; i < terms.length; i++) {
			add(terms[i], weights[i]);
		}
	}

	/**
	 * Add the word with given weight to the trie. If word already exists in the
	 * trie, no new nodes should be created, but the weight of word should be
	 * updated.
	 * 
	 * In adding a word, this method should do the following: Create any
	 * necessary intermediate nodes if they do not exist. Update the
	 * subtreeMaxWeight of all nodes in the path from root to the node
	 * representing word. Set the value of myWord, myWeight, isWord, and
	 * mySubtreeMaxWeight of the node corresponding to the added word to the
	 * correct values
	 * 
	 * @throws a
	 *             NullPointerException if word is null
	 * @throws an
	 *             IllegalArgumentException if weight is negative.
	 * 
	 */
	private void add(String word, double weight) {
		// TODO: Implement add
		if (word == null)
			throw new NullPointerException("word is null.");
		if (weight < 0)
			throw new IllegalArgumentException("weight is negative");
		
		Node current = myRoot;

		// add new node
		for (int i = 0; i < word.length(); i++) {
			if (current.mySubtreeMaxWeight < weight) 
				current.mySubtreeMaxWeight = weight;
			if (!current.children.containsKey(word.charAt(i)))
				current.children.put(word.charAt(i), new Node(word.charAt(i), current, weight));
			current = current.children.get(word.charAt(i));
		}

		// set the weight, word, isWord
		current.myWeight = weight;
		current.isWord = true;
		current.myWord = word;
		if (current.mySubtreeMaxWeight < weight) 
			current.mySubtreeMaxWeight = weight;
		// implement duplicate add
		else if (current.mySubtreeMaxWeight > weight) {
			while (current != null) {
				current.mySubtreeMaxWeight = current.myWeight;
				for (char c: current.children.keySet()) {
					current.mySubtreeMaxWeight = Math.max(current.mySubtreeMaxWeight, current.getChild(c).mySubtreeMaxWeight);
				}
				current = current.parent;
			}
		}
		
	}

	@Override
	/**
	 * Required by the Autocompletor interface. Returns an array containing the
	 * k words in the trie with the largest weight which match the given prefix,
	 * in descending weight order. If less than k words exist matching the given
	 * prefix (including if no words exist), then the array instead contains all
	 * those words. e.g. If terms is {air:3, bat:2, bell:4, boy:1}, then
	 * topKMatches("b", 2) should return {"bell", "bat"}, but topKMatches("a",
	 * 2) should return {"air"}
	 * 
	 * @param prefix
	 *            - A prefix which all returned words must start with
	 * @param k
	 *            - The (maximum) number of words to be returned
	 * @return An array of the k words with the largest weights among all words
	 *         starting with prefix, in descending weight order. If less than k
	 *         such words exist, return an array containing all those words If
	 *         no such words exist, return an empty array
	 * @throws a
	 *             NullPointerException if prefix is null
	 */
	public String[] topKMatches(String prefix, int k) {
		// TODO: Implement topKMatches
		if (prefix == null)
			throw new NullPointerException("Prefix is null.");

		ArrayList<Node> list = new ArrayList<Node>();
		PriorityQueue<Node> pq = new PriorityQueue<Node>(new Node.ReverseSubtreeMaxWeightComparator());

		Node current = myRoot;
		String[] empty = new String[0];

		// finding the prefix
		for (int i = 0; i < prefix.length(); i++) {
			boolean childFound = false;
			if (prefix.equals(""))
				break;
			for (char c: current.children.keySet()) {
				if (prefix.charAt(i) == c) {
					current = current.getChild(c);
					childFound = true;
					break;
				}
			}
			if (childFound == false && !prefix.equals(""))
				return empty;
		}

		// add node that contains prefix
		pq.add(current);

		// find top k matches
		// list.get(k).getWeight() > current.mySubtreeMaxWeight)

		while (!pq.isEmpty()) {
			// check if list has k elements
			if (list.size() == k) {
				Collections.sort(list);
				if (list.get(0).myWeight > pq.peek().mySubtreeMaxWeight)
					break;
				else
					list.remove(0);
			}
			current = pq.poll();
			if (current.isWord){
				list.add(current);
			}
			for (char c: current.children.keySet()) {
				pq.add(current.getChild(c));
			}
		}

		// sort list in reverse order
		Collections.sort(list, Collections.reverseOrder());

		// the smaller of k and list.size() is the size of array returned
		int c = Math.min(k, list.size());
		String[] result = new String[c];
		for (int i = 0; i < c; i++) {
			result[i] = list.get(i).myWord;
		}
		return result;

	}

	@Override
	/**
	 * Given a prefix, returns the largest-weight word in the trie starting with
	 * that prefix.
	 * 
	 * @param prefix
	 *            - the prefix the returned word should start with
	 * @return The word from _terms with the largest weight starting with
	 *         prefix, or an empty string if none exists
	 * @throws a
	 *             NullPointerException if the prefix is null
	 * 
	 */
	public String topMatch(String prefix) {
		// TODO: Implement topMatch
		if (prefix == null)
			throw new NullPointerException("Prefix is null.");

		Node current = myRoot;
		boolean childFound = false;

		// find prefix
		for (int i = 0; i < prefix.length(); i++) {
			if (prefix.equals(""))
				break;
			for (char c: current.children.keySet()) {
				if (prefix.charAt(i) == c) {
					current = current.getChild(c);
					childFound = true;
					break;
				}			
			}
		}

		// if child not found and prefix isn't an empty string
		if (childFound == false && !prefix.equals("")) 
			return "";

		// iterate down the tree until find max weight
		while (current.mySubtreeMaxWeight != current.myWeight) {
			for (char c: current.children.keySet()) {
				if (current.getChild(c).mySubtreeMaxWeight == current.mySubtreeMaxWeight) {
					current = current.getChild(c);
					break;
				}
			}
		}

		return current.myWord;
	}

}
