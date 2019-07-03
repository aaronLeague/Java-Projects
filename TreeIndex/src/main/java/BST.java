import java.util.HashSet;
import java.util.NoSuchElementException;

//Modified from CSC241 class code, SUNY Oswego, fall 2018
class BST<K extends Comparable<K>, V>{
 
    @SuppressWarnings("ClassCanBeStatic")
    private class BSTNode<K1 extends Comparable<K1>, V1>{
        private BSTNode<K1, V1> left, right;
        private K1 key;
	private HashSet<V1> values = new HashSet<>();
 
        public BSTNode(K1 key, V1 value){
	    this.key = key;
	    values.add(value);
        }
 
        // Adapted from Todd Davies answer to printing a BST on Stack Overflow.
        // https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
        private StringBuilder toString(StringBuilder prefix, boolean isTail, StringBuilder sb) {
            if(right!=null) {
                right.toString(new StringBuilder().append(prefix).append(isTail ? "│   " : "    "), false, sb);
            }
            sb.append(prefix).append(isTail ? "└── " : "┌── ").append(key).append("\n");
            if(left!=null) {
                left.toString(new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), true, sb);
            }
            return sb;
        }
 
        @Override
        public String toString() {
            return this.toString(new StringBuilder(), true, new StringBuilder()).toString();
        }
    }
 
    private BSTNode<K,V> root;
 
    private BSTNode<K,V> search(BSTNode<K,V> curr, K key){
        if (curr == null || curr.key.equals(key)) return curr;
 
        if (key.compareTo(curr.key) > 0)
            return search(curr.right, key);
 
        return search(curr.left, key);
    }
 
    public boolean contains(K key){
        return search(root, key) != null;
    }

    public HashSet<V> get(K key){
	BSTNode<K,V> temp = search(root, key);
	if (temp == null) throw new NoSuchElementException();
	return temp.values;
    }

    private HashSet<V> getInRange(BSTNode<K,V> curr, HashSet<V> data, K lowKey, K highKey){
	if (curr != null){
	    if (curr.key.compareTo(lowKey) > 0)
		getInRange(curr.left, data, lowKey, highKey);
	    if (curr.key.compareTo(lowKey) >= 0
		&& curr.key.compareTo(highKey) <= 0)
		data.addAll(curr.values);
	    if (curr.key.compareTo(highKey) < 0)
		getInRange(curr.right, data, lowKey, highKey);
	}
	return data;
    }

    public HashSet<V> getInRange(K lowKey, K highKey){
	return getInRange(root, new HashSet<>(), lowKey, highKey);
    }
     
    private BSTNode<K,V> insert(BSTNode<K,V> curr, K key, V value){
        if (curr == null)
            return new BSTNode<K,V>(key, value);
 
        if (key.compareTo(curr.key) > 0)
            curr.right = insert(curr.right, key, value);
	else if (key.compareTo(curr.key) == 0)
	    curr.values.add(value);
        else if (key.compareTo(curr.key) < 0)
            curr.left = insert(curr.left, key, value);
 
        return curr;
    }
 
    public void insert(K key, V value){
        root = insert(root, key, value);
    }

    public boolean isEmpty(){return root == null;}

    private void printInOrder(BSTNode<K,V> curr){
        if (curr != null){
            // Print left subtree
            printInOrder(curr.left);
            // Print curr
            System.out.print(curr.key + " ");
            // Print right subtree
            printInOrder(curr.right);
        }
    }
    
    public void printInOrder(){
        printInOrder(root);
        System.out.println();
    }
 
    @Override
    public String toString(){
	if (root != null)
	    return root.toString();
	return "\n THE TREE IS EMPTY\n";
    }
}
