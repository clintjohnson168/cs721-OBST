import java.util.Random;

// class to implement Optimal Binary Search Tree 
public class OBST {
	
	public double p[];		//contains probabilities of elements in tree
	public double q[];		//contains probabilities of elements not in tree
	public double w[][];	//weight w[i,j] of a tree having r[i,j]
	public double e[][];	//cost e[i,j] of a tree having r[i,j]
	public int R[][];		//represents root
	public int n;			//n nodes
	public int keys[];		//contains all the keys
	private OptimalBST root;
	
	public OBST(int x){
		p = new double[x];
		q = new double[x];
		w = new double[x][x];
		e = new double[x][x];
		R = new int[x][x];	
		n = x;
		root = null;
		keys = new int[x];
	}
	
	private OptimalBST constructTree(int i, int j){
		OptimalBST node;
		if(i == j){
			node = null;
		}
		else{
			node = new OptimalBST();
			node.key = keys[R[i][j]];
			node.left = constructTree(i, R[i][j] - 1);
			node.right = constructTree(R[i][j], j);
		}
		return node;
	}
	
	// This function will calculate the w, r and e matrices 
	public void calcWRE(){
		calcW();
		calcER();
	}

	// This function will calculate the w matrix
	private void calcW(){
		int i, j;
		double sumI = 0;
		double sumJ = 0;
		for(i = 0; i < p.length; i ++){
			if(i < p.length-1){
				sumI = p[i+1];
			}
			else sumI = 0;
			for(j = 0; j < q.length; j++){
				if(j == i){
					w[i][j] = q[j];
				}
				else if(j > i){
					if(j == i + 1){
						sumJ = sumI + q[j-1];
						if(j < p.length){
							sumJ += q[j];
						}
						w[i][j] = sumJ;
					}
					else{
						w[i][j] = w[i][j-1] + p[j] + q[j];
					}
				}
			}
		}		
	}
	
	// This function will calculate the e and r matrices given that w has been calculate 
	private void calcER(){
		int i, j, r, h, m;
		double x, minVal;
		
		//set the diagonal of the matrix root and e
		for(i = 0; i < n; i ++){
			e[i][i] = q[i];
		}
		for(i = 0; i < n -1; i++){	
			j = i + 1;
			e[i][j] = e[i][i] + e[j][j] + w[i][j];
			R[i][j] = j;
		}
			
		for(h = 2; h <= n; h++){
			for(i = 0; i < n - h; i++){
				j = i + h;
				m = R[i][j-1];
				minVal = e[i][m-1] + e[m][j];
				for(r = m+1; r <= R[i+1][j]; r++){
					x = e[i][r-1] + e[r][j];
					if(x < minVal){
						m = r;
						minVal = x;
					}
				}
				e[i][j] = w[i][j] + minVal;
				R[i][j] = m;
			}				
		}			
	}
	
	// This function will print out the matrix w 
	public void printW(){
		int i;
		int j;
		for(i = 0; i < w.length; i++){
			for(j = 0; j < w.length; j++){
				double val = w[i][j];
				System.out.print("\t");
				System.out.printf("%.2f", val);
				
			}
			System.out.println("");
		}		
	}
	
	// This function will print out the matrix e
	public void printE(){
		int i;
		int j;
		for(i = 0; i < e.length; i++){
			for(j = 0; j < e.length; j++){
				double val = e[i][j];
				System.out.print("\t");
				System.out.printf("%.2f", val);
				
			}
			System.out.println("");
		}
	}
	
	// This function will print out the matrix R
	public void printR(){
		int i;
		int j;
		for(i = 0; i < R.length; i++){
			for(j = 0; j < R.length; j++){
				double val = R[i][j];
				System.out.print("\t");
				System.out.printf("%.2f", val);
				
			}
			System.out.println("");
		}
	}
	
	// this function will print out the given double array
	public void print(double[] a){
		for (int i = 0; i < a.length; i++){
			System.out.print(a[i] + " ");			
		}
		System.out.println("");
	}
	
	// this function will print out the given integer array
	public void print(int[] a){
		for(int i = 0; i < a.length; i++){
			System.out.print(a[i] + " ");
		}
		System.out.println("");
	}
	
	// will print out the tree based on the root node of the tree
	// level is used to print the key value at the correct height of the tree
	private void printTree(OptimalBST root, int level){
		int i, x;
		if(root != null){
			printTree(root.right, level + 1);
			for(i = 0; i <= level; i ++){
				if(level == 0){
					System.out.print("Root-> ");
				}
				else{
					System.out.print("       ");				
				}
			}
			x = root.key;
			System.out.printf("%d\n", x);
			printTree(root.left, level + 1);
		}
	}
	
	// Print out the tree from the root node
	public void printTree(){
		this.root = this.constructTree(0, this.n-1);
		this.printTree(this.root, 0);
	}
	
	// this function will take a given tree with a given number of and give values
	//  to p, q, and keys array and will print out the amount of time to 
	//  generate this OBST tree with the given number of nodes 
	public static void time(OBST tree, int nNodes){
		Random rand = new Random();
		long start, stop, duration;
		int keyVal, pVal, qVal, sum = 0;
		
		start = System.currentTimeMillis();	//start timer
		
		//compute the arrays p, q, and the key values for array p
		for(int i = 0; i < nNodes; i++){
			keyVal = rand.nextInt(1000);
			pVal = rand.nextInt(3);
			sum += pVal;	// add pVal to sum to calculate probability 
			qVal = rand.nextInt(3);
			sum += qVal;	// add qVal to sum to calculate probability 
			
			//add values to tree
			if(i != 0){
				tree.keys[i] = keyVal;
				tree.p[i] = pVal;
			}			
			tree.q[i] = qVal;
		}
		
		//set p and q to probabilities instead of occurrences 
		for(int i = 0; i < tree.n; i++){
			if(i != 0){
				tree.p[i] = tree.p[i] / sum;
			}
			tree.q[i] = tree.q[i] / sum;
		}
		
		//construct 3 matrices
		tree.calcWRE();
		
		stop = System.currentTimeMillis(); //stop timer
		duration = stop - start;
		System.out.println("Time to add " + nNodes + " nodes: " + duration + " miliseconds.");
		
	}
	
	public static void main(String []args){
		OBST myTree = new OBST(7);
		myTree.p[0] = 0;
		myTree.p[1] = 10;
		myTree.p[2] = 3;
		myTree.p[3] = 9;
		myTree.p[4] = 2;
		myTree.p[5] = 0;
		myTree.p[6] = 10;
		
		System.out.print("Probabilitys: ");
		myTree.print(myTree.p);		
		
		myTree.keys[1] = 4;
		myTree.keys[2] = 8;
		myTree.keys[3] = 2;
		myTree.keys[4] = 15;
		myTree.keys[5] = 9;
		myTree.keys[6] = 6;
		System.out.print("Key values: ");
		myTree.print(myTree.keys);
		
				
		myTree.q[0] = 5;
		myTree.q[1] = 6;
		myTree.q[2] = 4;
		myTree.q[3] = 4;
		myTree.q[4] = 3;
		myTree.q[5] = 8;
		myTree.q[6] = 0;
		System.out.print("Failure pribabilities :");
		myTree.print(myTree.q);
		
		myTree.calcWRE();
		System.out.println("W:");
		myTree.printW();
		System.out.println("Root:");
		myTree.printR();
		System.out.println("e:");
		myTree.printE();
		
		System.out.println("Displaying Tree: \n");
		myTree.printTree();		
		System.out.println("\n");
		
		// create and time OBST tree with 10 nodes
		myTree = new OBST(10);
		OBST.time(myTree, myTree.n);
		//System.out.println("E: ");
		//myTree.printE();
		//myTree.printTree();
		
		//create tree with 100 nodes
		myTree = new OBST(100);
		OBST.time(myTree, myTree.n);
		
		//create tree with 1000 nodes
		myTree = new OBST(1000);
		OBST.time(myTree, myTree.n);
		
		//create tree with 10000 nodes
		myTree = new OBST(10000);
		OBST.time(myTree, myTree.n);
		
		//create tree with 100000 nodes
		//myTree = new OBST(100000);		//ran out of memory space
		System.out.println("Not enough memory space for 100000 nodes.");
		//OBST.time(myTree, myTree.n);
	}
}
