import java.util.Scanner;
import java.util.Map;
import java.util.List;
import java.util.*;

public class BSTwithLRU <E extends Comparable<?super E>> implements CountMapWithLRU<E> {
  
  
  private Node<E> recent = null;
  private Node<E> root;
  private Node<E> least_recent = root; //start with assumption that the least recent is the node I add first
  private int size;
  
  
  //INTERFACE METHODS:
  
  public boolean update(E key, int amount) {
    
    
    //Node<E> recent = null;
    
    // keep track of parent of current and last direction travelled
    //Node<E> start = null;
    //Node<E> end = null;
    Node<E> parCurr = null;
    //root.next = end;
    int dir = 0;
    int depth = 0;
    int count = 0;
    boolean modified = false;
    
    Node<E> curr = root;
    
    
    while (curr != null) {
      if ((dir = key.compareTo(curr.data)) != 0) {    //if it hasn't found the data to be added so far it'll move on to left or right
        parCurr = curr;
        if (dir < 0) curr = curr.left;
        else curr = curr.right;
        depth++;
      }
      
      else {   // if its found the data it changes links and adds to the freq of curr
         /*
          * If its found the data, it is either in the beginning of the LRU list, the middle of the LRU list or end of the LRU list
          * If at beginning, then just add to frequency. Make no changes
          * If in the middle, move it to the beginning. Change the links to recent. Change the links to the previous and next values
          * If in the end, move it to the beginning, change the link to the previous.
          * */
        
           //updates te
        // Node x = curr.previous;  
        
        //if it is at the beginning:
        if (curr.previous == null) {
          curr.freq = amount; 
        }
       
        //if it is at the end
        else if (curr.next == null ) {  // there has to be more than one element because otherwise curr.previous would have also been null and would have been caught
          curr.freq = amount;
          least_recent = curr.previous;  // because if it is at the end, then the new least recent will be the one before curr
          curr.previous.next = null;  //the previous gets linked to null
          curr.previous = null; //since it is moving to the top of the list
          curr.next = recent;  // the next value will be the previous recent value
          recent.previous = curr; //it was null, now it becomes curr
          recent = curr; //change the value of recent to curr
        }
        
        else { //it is in the middle
          curr.freq = amount;
          curr.previous.next = curr.next;  //the previous gets linked to next
          curr.next.previous = curr.previous;  // the next gets linked to previous
          curr.previous = null; //since it is moving to the top of the list
          curr.next = recent;  // the next value will be the previous recent value
          recent.previous = curr; //it was null, now it becomes curr
          recent = curr; //change the value of recent to curr
        }
       
        modified = true;
        break;
      } 
    }
    
    return modified;  // this will be true only if it has reached the else loop (i.e. the data was found)
  }
      
  
  
  //THE BELOW METHOD LOOKS FOR THE KEY (UPDATES IF NECESSARY) AND THEN ADDS. 
  
  public void add(E key, int amount){
    
    // start at root
    Node<E> curr = root;
    
    
    
    // keep track of parent of current and last direction travelled
   
    Node<E> parCurr = null;
    int dir = 0;
    int depth = 0;
    int count = 0; // number of elements in the 
    
    // traverse tree to insertion location or containing interval - still needs to do this because it has to figure out wehre the add must happen!
    while (curr != null) {
      if ((dir = key.compareTo(curr.data)) != 0) {    //if it hasn't found the data to be added so far it'll move on to left or right
        parCurr = curr;
        if (dir < 0) curr = curr.left;
        else curr = curr.right;
        depth++;
    }
    
    if (curr == null)  {   /// Adding new node if data we are trying to add wasn't found in BST
      
      Node<E> newNode = new Node<E>(key, parCurr, curr);   //instead of curr I can say null and it won't make a difference
      newNode.freq = amount;
      size++; 
      
      // link from parent according to last direction
      if (parCurr == null)  {
        root = newNode;    //to check if it is a root
        recent = root;
      }
    }
      
      else { //curr is the data we are looking for
        
        //curr = newNode;   
        recent.previous = curr; //currr now occurs prior to the old recent
        curr.next = recent;  // same logic. curr's next value is old recent
        recent = curr; // change recent to be curr.
        
        
        /*System.out.println(parCurr.data);
        if (dir < 0) parCurr.left = newNode;
        else parCurr.right = newNode;
        
        if (curr.next != null && curr.previous != null) 
        
        
        if (curr.next == null) curr.previous.next = null;  //to prevent null pointer exception.
        else curr.previous.next = curr.next;  //changes the "next" value of the node that was connected to curr to be the next value of the next node that curr was connected to
        
        start.next = curr;  // connects start to curr
        curr.previous = start;  // connects curr to start
        curr.next = recent; // connects curr to the previous recent value
        recent = curr; */
      }
      recomputeHeights(parCurr);   // recompute heights on path back to root
    }
  }
  
  
  
  
  
  
  public Map.Entry<E, Integer> remove(E key){
    AbstractMap.SimpleEntry<E, Integer> x = null; 
    
    // start at root
    Node<E> curr = root;
    
    // keep track of parent of current and last direction travelled
    Node<E> parCurr = null;
    int dir = 0;
    int depth = 0;
    
    // traverse tree to insertion location or value
    while (curr != null && (dir = key.compareTo(curr.data)) != 0) {
      parCurr = curr;
      if (dir < 0) curr = curr.left;
      else curr = curr.right;
      depth++;
    }
    
    if (curr != null)  {//its found the data
      /*When you are trying to remove exising entity, there are three possibilities: element is in the beginning, middle or end
       * In the beginning, just change the link to recent
       * In the middle change the previous and next to link to each other
       * In the end, change the link of previos to null
       * */
      
      //When it is a root
      
      if(curr.previous == null && curr.next == null) {
        root = null;
        
      }
      
      else if (curr.previous == null) {
        curr.next.previous = null;
      }
      
      else if (curr.next == null) {
        curr.previous.next = null;
        least_recent = curr.previous;
      }
      
      else {
        curr.previous.next = curr.next;
        curr.next.previous = curr.previous;
      }
      
      x = new AbstractMap.SimpleEntry<E, Integer>(key, curr.freq);
      delete(curr);
    }
    size--;
    return x;
  }
  
  
 
  public Map.Entry<E, Integer> removeLRU(){
    Map.Entry<E, Integer> deleted = remove(least_recent.data);
    return deleted; 
   }
  
  public int get(E key){ 
    
    Node<E> curr = root;
    Node<E> start = null;
    Node<E> parCurr = null;
    int dir = 0;
    int depth = 0;
    int count = 0; // number of elements in the 
    
    while (curr != null) {
      if ((dir = key.compareTo(curr.data)) != 0) {    //if it hasn't found the data to be added so far it'll move on to left or right
        parCurr = curr;
        if (dir < 0) curr = curr.left;
        else curr = curr.right;
        depth++;
      }
      
      else return curr.freq;   // returns frequency if curr is the 
    }
      
      /*else {   // if its found the data it changes links and adds to the freq of curr
        int freq = curr.freq;
        if (curr == least_recent) least_recent = curr.previous;  // changes the value of least recent incase curr was the least recent
        curr.previous.next = curr.next;  //so the previous gets linked to the next (since we want to move this to the beginning)
        curr.next.previous = curr.previous; //next gets linked to previous
        start.next = curr;  // connects start to curr
        curr.previous = start;  // connects curr to start
        curr.next = recent; // connects curr to the previous recent value
        recent.previous = curr; //so it changes from being start to curr
        recent = curr; // updates recent to curr          
        return freq;
      } 
    }*/
    return 0;
  }
  
  
  public int size(){
  return size;
  }
  
  
  public void addToList(List<Map.Entry<E, Integer>> l){
    Node<E> curr = recent;
    while(curr != null) {
      AbstractMap.SimpleEntry<E, Integer> x = new AbstractMap.SimpleEntry<E, Integer>(curr.data, curr.freq);
      l.add(x);
      curr = curr.next;
    }
  }
    
      
  
  
  
  
  
  

  
  /// THE NODE CLASS
  
  public static class Node<E>
  {
    /**
     * References to the parents, children, previous and next (in order of when they were added/accessed) of the node
     */
    private Node<E> parent;
    private Node<E> left;
    private Node<E> right;
    private Node<E> previous;
    private Node<E> next;
    
    private E data;
    private int height;
    private int freq;
    
    /**
     * Creates a node holding the given data with the given parent
     * parent reference.  The parent's child references are not updated
     * to refer to the new node.
     *
     * @param d the data to store in the new node
     * @param p the parent of the new node
     * @param prev the node that was last accessed before this node
     */
    private Node(E d, Node<E> p, Node<E> prev) {
      data = d;
      parent = p;
      height = 1;
      previous = prev;
      //freq = d.frequency;   //so data must always have a field called freq!
    }
    
    /**
     * Determines if this node is a left child.
     *
     * @return true if and only if this node is its parent's left child
     */
    private boolean isLeftChild() {
      return (parent != null && parent.left == this);
    }
    
    /**
     * Determines if this node is a right child.
     *
     * @return true if and only if this node is its parent's right child
     */
    private boolean isRightChild()
    {
      return (parent != null && parent.right == this);
    }
    
    /**
     * Returns the height of the left subtree of this node.  The height
     * of an empty subtree is defined to be 0.
     *
     * @return the height of the left subtree of this node
     */
    private int leftHeight()
    {
      return (left != null ? left.height : 0);
    }
    
    /**
     * Returns the height of the right subtree of this node.  The height
     * of an empty subtree is defined to be 0.
     *
     * @return the height of the right subtree of this node
     */
    private int rightHeight()
    {
      return (right != null ? right.height : 0);
    }
    
    /**
     * Recomputes this node's height.  Intended to be used when the
     * heights of the children have possibly changed.
     */
    private void recomputeHeight()
    {
      height = 1 + Math.max(leftHeight(), rightHeight());
    }
  }
  
  
  
  // DELETE NODES
  
  private void delete(Node<E> curr)
  {
    size--; 
    
    if (curr.left == null && curr.right == null) {   // if it is the bottom most root
      Node<E> parent = curr.parent;
      if (curr.isLeftChild())
      {
        parent.left = null;
        curr.previous.next = curr.next;
        recomputeHeights(parent);
      }
      else if (curr.isRightChild())
      {
        parent.right = null;
        recomputeHeights(parent);
        curr.previous.next = curr.next;
      }
      else
      {
        root = null;// deleting the root
      }
    }
    
    else if (curr.left == null) { // node to delete has only right child
      Node<E> parent = curr.parent;
      
      if (curr.isLeftChild())
      {
        parent.left = curr.right;
        curr.right.parent = parent;
        curr.previous.next = curr.next;
        recomputeHeights(parent);
      }
      else if (curr.isRightChild())
      {
        parent.right = curr.right;
        curr.right.parent = parent;
        curr.previous.next = curr.next;
        recomputeHeights(parent);
      }
      else
      {
        root = curr.right;
        root.parent = null;
      }
    }
    else if (curr.right == null)
    {
      // node to delete only has left child
      Node<E> parent = curr.parent;
      
      if (curr.isLeftChild())
      {
        parent.left = curr.left;
        curr.left.parent = parent;
        curr.previous.next = curr.next;
        recomputeHeights(parent);
      }
      else if (curr.isRightChild())
      {
        parent.right = curr.left;
        curr.left.parent = parent;
        curr.previous.next = curr.next;
        recomputeHeights(parent);
      }
      else
      {
        root = curr.left;
        root.parent = null;
      }
    }
    else
    {
      // node to delete has two children
      
      // find inorder successor (leftmost in right subtree)
      Node<E> replacement = curr.right;
      while (replacement.left != null)
      {
        replacement = replacement.left;
      }
      
      Node<E> replacementChild = replacement.right;
      Node<E> replacementParent = replacement.parent;
      
      // stitch up
      if (curr.isLeftChild())
      {
        curr.parent.left = replacement;
        curr.previous.next = curr.next;
        
      }
      else if (curr.isRightChild())
      {
        curr.parent.right = replacement;
        curr.previous.next = curr.next;
      }
      else
      {
        root = replacement;
        curr.previous.next = curr.next;
      }
      
      if (replacement.parent != curr)
      {
        replacement.parent.left = replacementChild;
        if (replacementChild != null)
        {
          replacementChild.parent = replacement.parent;
        }
        
        replacement.right = curr.right;
        curr.right.parent = replacement;
        curr.previous.next = curr.next;
        curr.previous.next = curr.next;
      }
      
      replacement.left = curr.left;
      curr.left.parent = replacement;
      curr.previous.next = curr.next;
      curr.previous.next = curr.next;
      
      replacement.parent = curr.parent;
      
      // recompute heights (node we deleted is on the path
      // of nodes whose heights is recomputes because
      // replacementParent is a descendant of curr)
      if (replacementParent != curr)
      {
        recomputeHeights(replacementParent);
      }
      else
      {
        recomputeHeights(replacement);
      }
    }
  }
  
  // RECOMPUTE HEIGHTS 
  private void recomputeHeights(Node<E> n)
  {
    while (n != null)
    {
      n.recomputeHeight();
      n = n.parent;
    }
  }  
  
}
