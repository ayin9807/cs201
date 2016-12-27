import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

//1. count characters in file
//2. create Huffman tree
//3. traverse tree and extract code
//4. write the header
//5. compress/write the body
//6. write the pseudo-EOF

public class HuffProcessor implements Processor {
	
	public static final int BITS_PER_WORD = 8;
	public static final int BITS_PER_INT = 32;
	public static final int ALPH_SIZE = (1 << BITS_PER_WORD); // or 256
	public static final int PSEUDO_EOF = ALPH_SIZE;
	public static final int HUFF_NUMBER = 0xface8200;
	
	private int[] frequency;
	private PriorityQueue<HuffNode> pq;
	private Map<Integer, String> myMap;

	// count characters in a file
	public void countBits (BitInputStream in) {
		int current = in.readBits(BITS_PER_WORD);
		
		while (current != -1) {
			frequency[current]++;
			current = in.readBits(BITS_PER_WORD);
		}
		
		in.reset();
	}
	
	// create huffman tree
	public void createTree() {
		for (int i = 0; i < ALPH_SIZE; i++) {
			if (frequency[i] != 0) {
				pq.add(new HuffNode(i, frequency[i], null, null));
			} 
		}
		
		// add pseudo-EOF node
		pq.add(new HuffNode(PSEUDO_EOF, 0, null, null));
		
		// combine 2 smallest nodes
		while (pq.size() > 1) {
			HuffNode left = pq.poll();
			HuffNode right = pq.poll();
			pq.add(new HuffNode(-1, left.weight()+right.weight(), left, right));	
		}
	}
	
	// get codes
	public void extractCodes(HuffNode current, String path) {
		if (current.value() != -1) {
			myMap.put(current.value(), path);
			return;
		}
		
		extractCodes(current.left(), path + 0);
		extractCodes(current.right(), path + 1);
	}
	
	// write header
	public void writeHeader(HuffNode current, BitOutputStream out) {
		if (current.value() != -1) {
			out.writeBits(1, 1);
			out.writeBits(9, current.value());
			return;
		}
		
		out.writeBits(1, 0);
		writeHeader(current.left(), out);
		writeHeader(current.right(), out);
	}
	
	@Override
	public void compress(BitInputStream in, BitOutputStream out) {
		// count characters
		frequency = new int[ALPH_SIZE];
		countBits(in);
		
		// create tree
		pq = new PriorityQueue<HuffNode>();
		createTree();
		
		// get codes
		myMap = new HashMap<Integer, String>();
		HuffNode root = pq.poll();
		extractCodes(root, "");
		
		// System.out.println(myMap.size()-1);
		
		// write header
		out.writeBits(BITS_PER_INT, HUFF_NUMBER);
		writeHeader(root, out);
		
		// compress
		int current = in.readBits(BITS_PER_WORD);
		while (current != -1) {
			String code = myMap.get(current);
			out.writeBits(code.length(), Integer.parseInt(code, 2));
			current = in.readBits(BITS_PER_WORD);
		}
		
		// write pseudo-EOF
		out.writeBits(myMap.get(PSEUDO_EOF).length(), Integer.parseInt(myMap.get(PSEUDO_EOF), 2));
	}
	
	
	// decompress
	// read header
	public HuffNode readHeader(BitInputStream in) {
		if (in.readBits(1) == 0) {
			HuffNode left = readHeader(in);
			HuffNode right = readHeader(in);
			return new HuffNode(-1, 0, left, right);
		}
		else {
			return new HuffNode(in.readBits(9), 0, null, null);
		}
	} 
	
	@Override
	public void decompress(BitInputStream in, BitOutputStream out) {
		// check for HUFF_NUMBER
		if (in.readBits(BITS_PER_INT) != HUFF_NUMBER) {
			throw new HuffException("HuffNumber not there.");
		}
		
		// recreate tree from header
		HuffNode root = readHeader(in);
		
		// decode body
		HuffNode current = root;
		int bit = in.readBits(1);
		while (bit != -1) {
			if (bit == 1) 
				current = current.right();
			else
				current = current.left();
			
			if (current.value() != -1) {
				if (current.value() == PSEUDO_EOF)
					return;
				else
					out.writeBits(8, current.value());
					current = root;
			}
			
			bit = in.readBits(1);
		}
		
		throw new HuffException("Problem with Pseudo-EOF.");
	}

}
