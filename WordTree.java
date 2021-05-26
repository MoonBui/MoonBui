/**
 * @author Moon Bui
 * Class CSE 274
 * Class WordTree
 */
import java.util.*;

public class WordTree {
	Node root;
	
	// Constructor
	public WordTree() {
		root = new Node(' ');
	}
	
	/**
	 * This method adds a word to the tree
	 * @param word string value user wants to add to the tree
	 * @return true if the word is added successfully, false otherwise
	 * or if the word is empty
	 */
	public boolean add(String word) {
		// Check if the word is empty or it already exists in the tree
		// and return  false if it is the case
		if (word.isEmpty() || this.contains(word))
			return false;
		
		// Create a node that starts from the root
		Node curr = root;
		
		// Loop the tree
		for (int i = 0; i < word.length(); i++) {
			// Add the character from the word to the tree
			// Because the tree is set-based, duplicates won't
			// be added
			curr.addChild(word.charAt(i));
			curr = curr.getChild(word.charAt(i));
		}
		// Set the boolean for the final value of the word to true
		// and return true
		curr.end = true;
		return true;
	}
	
	/**
	 * This method removes the specified word from the tree
	 * @param word specified string value that needs to be removed
	 * @return true if the word has been removed, false otherwise
	 */
	public boolean remove(String word) {
		// If the word is empty, remove the word
		if (word.isEmpty())
			return false;
		
		// Create a stack (stores all character nodes of the word
		// in reverse order) and a node that starts from the root
		Stack<Node> reverse = new Stack<>();
		Node curr = root;
		
		// Loop through the tree to find the characters in the word
		for (int i = 0; i < word.length(); i++) {
			// Add the node to the stack and proceed to the 
			// next node in the set
			reverse.push(curr);
			curr = curr.getChild(word.charAt(i));
			// If the character is not present, return false
			if (curr == null)
				return false;
		}
		
		// Index variable
		int pin = word.length()-1;
		
		// Loop until the stack is empty
		while (!reverse.isEmpty()) {
			// Create a parent node that contains
			// the node for the final character in the word
			Node parent = reverse.pop();
			Node child = parent.getChild(word.charAt(pin));
			
			// If the child is empty and isn't the end of a word,
			// remove it
			if (!child.end && child.children.size()==0) 
				parent.children.remove(child);
			// If the child is the end of word
			if (child.end) {
				// remove the child node if it doesn't have children
				if (child.children.size()==0) {
					parent.children.remove(child);
					// if the parent node is the end of a word
					// break the loop
					if (parent.end)
						break;
				} // Otherwise change the boolean of the character to false
				else {
					child.end = false;
					break;
				}
			} 
			
			// Decrease the index value by one
			pin--;
		}
		// Word has been successfully removed from the tree
		return true;	
}

	
	/**
	 * This method checks to see if the wordTree 
	 * contains the specified word
	 * @param word string value that need to be checked
	 * @return true if the tree contains the specified word,
	 * false otherwise
	 */
	public boolean contains(String word) {
		// Create a node starting from the root
		Node curr = root;
		// Traverse the tree
		for (int i = 0; i < word.length(); i++) {
			// Find the character of the word in the tree
			Node temp = curr.getChild(word.charAt(i));
			// If the character doén't exist in the tree
			// return false
			if (temp == null)
				return false;
			curr = temp;
		}
		
		// Return the boolean value at the final character
		// of the specified word
		return curr.end; 
		
	}
	
	/*
	 * This method returns the number of nodes in the tree
	 */
	public int nodeCount() {
		return nodeCount(root);
	}
	
	// Private helper method for nodeCount() method
	private int nodeCount(Node top) {
		// If the node is null, return 0
		if (top == null)
			return 0;
		
		// Count variable to track the nodes
		int count = 0;
		
		// Loop through the nodes in the set
		for (Node n : top.children) {
			// Increment by one whenever the method is called
			count+= nodeCount(n)+1;
		}
		
		return count;
	}
	
	
	/**
	 * this method returns the number of words in the wordTree
	 * @return the number of words stored in the tree
	 */
	public int wordCount() {		
		return wordCount(root);
	}
	
	// Private helper method for wordCount()
	private int wordCount(Node top) {
		// If the node is null, return 0
		if (top == null)
			return 0;
		
		// Count variable to track the words
		int count = 0;
		
		// If the node contains a character at the end of a word
		// increment the count by 1
		if (top.end)
			count++;
		
		// Loop through the nodes in the set
		for (Node n : top.children) {
			// Add the count to the total count
			count+=wordCount(n);
		}
		return count;
	}
	
	 
	/**
	 * This method clears the entire set of words
	 */
	public void clear() {
		root.children = new HashSet<>();
	}
	
	

	
	/**
	 * This method returns how much space is saved
	 * using the wordTree
	 * @return the number of letters that are saved/not used
	 * in the wordTree
	 */
	public int lettersSaved() {
		// Create a set containing all words
		Set<String> all = this.allWords();
		int total = 0;
		
		// Count the total letters of all the words
		for (String word : all) {
			total+=word.length();
		}
		
		// Subtract the number of nodes used 
		// from the total letters of all words
		// and return the value
		return total - this.nodeCount();
	}
	
	
	/**
	 * This method traverses the wordTree and 
	 * returns a set of all the words stored
	 * @return a set of the words the tree contains
	 */
	public Set<String> allWords() {
		Set<String> result = new HashSet<>();
		String word = "";
		allWords(root, result, word);
		return result;
	}
	
	// Private helper method for allWords()
	private void allWords(Node top, Set<String> wordsSoFar, String word) {
		// If the node is null. don't return anything
		if (top == null)
			return;
		
		//Add the character stored in the node to the word
			word+=top.data;
			// If reaching end of a word
			if (top.end) {
				// Replace all whitespaces and add the word
				// to the set
				word = word.replaceAll("\\s", "");
				wordsSoFar.add(word);
			}
			
		// Loop the nodes in the set
		for (Node n : top.children) {
			allWords(n, wordsSoFar, word);		
		}
	}
	
	/**
	 * This method traverses through the tree to find
	 * all words containing the specified prefix
	 * @param prefix of words that need to be found
	 * @return a set containing all the words with the specified prefix
	 */
	// Can be solved by calling allWords() then looping 
	// but I like to write recursives okay :D
	public Set<String> allStartingWith(String prefix) {
		Set<String> result = new HashSet<>();
		String word = "";
		allStartingWith(result, root, word, prefix);
		return result;
	}
	
	// Helper method for allStartingWith() 
	private void allStartingWith(Set<String> wordsSoFar, Node top, String word, String prefix) {
		if (top == null)
			return;
		//Add the character stored in the node to the word
			word+=top.data;
			// if reaching end of a word
			if (top.end) {
				// Replace white spaces
				word = word.replaceAll("\\s", "");
				// Checks if the word starts with the specified prefix
				// Add the word to the set if it does
				if (word.startsWith(prefix))
					wordsSoFar.add(word);
			}
		// Loop the nodes in the set
		for (Node n : top.children) {
			allStartingWith(wordsSoFar, n, word, prefix);		
		}
	}
	
	/**
	 * This method returns a map whose key is the 
	 * first character of a word and values are all words
	 * starting with the character
	 * @return a map with the first-character key and words
	 * starting with that character as value
	 */
	public Map<Character, Set<String>> wordMap() {
		Map<Character, Set<String>> result = new HashMap<>();
		
		// Loops the tree
		for (Node n : root.children) {
			char first = n.data;
			// Calls allStartingWith() to find all the words in the
			// the tree that start with the specified character
			result.put(first, this.allStartingWith(first+""));
		}
		
		return result; 
	}
	


	
	
	
	// returns a String representation of the entire tree 
	public String toString() {
		return root + " ";
	}
	
	// Private Node class
	private class Node {
		// Fields
		private char data;
		private boolean end;
		private Set<Node> children;
		
		// Constructor
		private Node(char l) {
			this.data = l;
			this.end = false;
			this.children = new HashSet<>();
		}
	
	
	// This method adds a new Node containing a 
	// character to the Set. Returns the child Node
	// if added successfully, if the set already contains
	// a Node with that character, return that Node instead
	public Node addChild(char letter) {
		Node child = new Node(letter);
		if (this.getChild(letter) == null) {
			children.add(child);
			return child;
		}
		return (this.getChild(letter));
	}
	
	// This method returns a Node containing the specified
	// character from the set. Returns null if the character
	// is not in the set
	public Node getChild(char letter) {		
		for (Node curr : this.children) {
			if (curr.data == letter)
				return curr;
		}
		return null;
	}
	
	// toString() method for inner Node class
	public String toString() {
		return data + " " + end + " " + children;
	}
}
}
