
public class QuickUWPC implements IUnionFind {
	private int[] myID;   // myID[i] = parent of i
    private int[] size;   // size[i] = number of sites in subtree rooted at i
    private int myComponents;      // number of components
    
    public QuickUWPC() {
		myID = null;
		size = null;
		myComponents = 0;
	}
    
    public QuickUWPC(int n) {
    	initialize(n);
    }
    
    public void initialize(int n) {
        myComponents = n;
        size = new int[n];
        myID = new int[n];
        for (int i = 0; i < n; i++) {
            size[i] = 1;
            myID[i] = i;
        }
    }
    
    public int components() {
		return myComponents;
	}
    
    public int find(int x) {
    	int root = x;
    	while (root != myID[root]) {
    		root = myID[root];
    	}
    	
    	while (x != root) {
    		int temp = myID[x];
    		myID[x] = root;
    		x = temp;
    	}
		return root;
	}
    
    public boolean connected(int p, int q) {
		return find(p) == find(q);
	}
    
    public void union(int p, int q) {
    	int i = find(p);
    	int j = find(q);
    	
    	if (i == j) return;
    	
    	if (size[i] < size[j]) {
    		myID[i] = j;
    		size[j] += size[i];
    	}
    	else {
    		myID[j] = i;
    		size[i] += size[j];
    	}
    }

}
