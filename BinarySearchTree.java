

import java.util.Scanner;


/**
 * A binary search tree.
 *
 * @author Jim Glenn
 * @version 0.1 2014-10-08
 */

public class BinarySearchTree<E extends Comparable<? super E>>
{
    private Node<E> root;
    private int size;

    /**
     * Adds the given value to this tree if it is not already present.
     *
     * @param r a value
     */
    public boolean add(E r)
    {
 // start at root
 Node<E> curr = root;

 // keep track of parent of current and last direction travelled
 Node<E> parCurr = null;
 int dir = 0;
 int depth = 0;

 // traverse tree to insertion location or containing interval
 while (curr != null && (dir = r.compareTo(curr.data)) != 0)
     {
  parCurr = curr;
  if (dir < 0)
      {
   curr = curr.left;
      }
  else
      {
   curr = curr.right;
      }
  depth++;
     }

 if (curr == null)
     {
  // didn't find value; create new node
  Node<E> newNode = new Node<E>(r, parCurr);
  size++;

  // link from parent according to last direction
  if (parCurr == null)        //to check if it is a root
      {
   root = newNode;
      }
  else
      {
   if (dir < 0)
       {
    parCurr.left = newNode;
       }
   else
       {
    parCurr.right = newNode;
       }
      }

  // recompute heights on path back to root
  recomputeHeights(parCurr);
  
  // yes, we added
  return true;
     }
 else
     {
  // no, we didn't add
  return false;
     }
    }

    
   // REMOVE METHOD
    
    
    public void remove(E r)
    {
 // start at root
 Node<E> curr = root;

 // keep track of parent of current and last direction travelled
 Node<E> parCurr = null;
 int dir = 0;
 int depth = 0;

 // traverse tree to insertion location or value
 while (curr != null && (dir = r.compareTo(curr.data)) != 0)
     {
  parCurr = curr;
  if (dir < 0)
      {
   curr = curr.left;
      }
  else
      {
   curr = curr.right;
      }
  depth++;
     }
 
 if (curr == null)
     {
     }
 else
     {
  delete(curr);
     }
    }

    /**
     * Deletes the given node from this tree.
     *
     * @param curr a node in this tree
     */
    private void delete(Node<E> curr)
    {
 size--; 

 if (curr.left == null && curr.right == null)
     {
  Node<E> parent = curr.parent;
  if (curr.isLeftChild())
      {
   parent.left = null;
   recomputeHeights(parent);
      }
  else if (curr.isRightChild())
      {
   parent.right = null;
   recomputeHeights(parent);
      }
  else
      {
   // deleting the root
   root = null;
      }
     }
 else if (curr.left == null)
     {
  // node to delete has only right child
  Node<E> parent = curr.parent;

  if (curr.isLeftChild())
      {
   parent.left = curr.right;
   curr.right.parent = parent;
   recomputeHeights(parent);
      }
  else if (curr.isRightChild())
      {
   parent.right = curr.right;
   curr.right.parent = parent;
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
   recomputeHeights(parent);
      }
  else if (curr.isRightChild())
      {
   parent.right = curr.left;
   curr.left.parent = parent;
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
        }
    else if (curr.isRightChild())
        {
     curr.parent.right = replacement;
        }
    else
        {
     root = replacement;
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
        }

    replacement.left = curr.left;
    curr.left.parent = replacement;

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

    /**
     * Recomputes the heights for nodes on the path from the given node
     * back to the root.
     *
     * @param n a node in this tree
     */
    private void recomputeHeights(Node<E> n)
    {
 while (n != null)
     {
  n.recomputeHeight();
  n = n.parent;
     }
    }

    /**
     * Returns a printable representation of this tree.
     *
     * @return a printable representation of this tree
     */
    public String toString()
    {
 StringBuilder s = new StringBuilder();
 buildOutput(root, s, 0);
 return s.toString();
    }

    /**
     * Appends a printable representation of the subtree rooted at the
     * given node to the given string builder.
     *
     * @param curr a node in this tree
     * @param s a string builder
     * @param depth the depth of curr
     */
    private void buildOutput(Node<E> curr, StringBuilder s, int depth)
    {
 if (curr != null)
     {
  buildOutput(curr.left, s, depth + 1);
  s.append(depth + "/" + curr.height + " " + curr.data + "\n");
  buildOutput(curr.right, s, depth + 1);
     }
    }

    public void validate()
    {
 validate(root, null, null, 0);
    }

    private void validate(Node<E> curr, E lowerBound, E upperBound, int dir)
    {
 if (curr != null)
     {
  assert (dir == 0 && curr.parent == null) || (dir == -1 && curr.parent.left == curr) || (dir == 1 && curr.parent.right == curr) : "Parent is incorrect at " + curr.data;

  assert (lowerBound == null || lowerBound.compareTo(curr.data) < 0) : "value violates lower bound: " + curr.data + "<=" + lowerBound;

  assert (upperBound == null || upperBound.compareTo(curr.data) > 0) : "value violates upper bound: " + curr.data + ">=" + upperBound;

  assert curr.height == 1 + Math.max(curr.leftHeight(), curr.rightHeight()) :  "height is incorrect at " + curr.data + " : " + curr.height;

  validate(curr.left, lowerBound, curr.data, -1);
  validate(curr.right, curr.data, upperBound, 1);
     }
    }

    public static class Node<E>
    {
 /**
  * References to the parents and children of this node.
  */
 private Node<E> parent;
 private Node<E> left;
 private Node<E> right;

 /**
  * The data stored in this node.
  */
 private E data;

 /**
  * The height of the subtree rooted at this node.
  */
 private int height;

 /**
  * Creates a node holding the given data with the given parent
  * parent reference.  The parent's child references are not updated
  * to refer to the new node.
  *
  * @param d the data to store in the new node
  * @param p the parent of the new node
  */
 private Node(E d, Node<E> p)
     {
  data = d;
  parent = p;
  height = 1;
     }

 /**
  * Determines if this node is a left child.
  *
  * @return true if and only if this node is its parent's left child
  */
 private boolean isLeftChild()
 {
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

    /**
     * Command-driven test driver for binary search trees.  Commands
     * are 'a' for add and 'r' for remove; both take an integer to add
     * or remove from the tree.  The final tree is written to standard output
     * in sorted order with the depths of the corresponding nodes.
     * The commands can be specified as a single command-line argument
     * (quoted in most shells to avoid being broken into several strings), or,
     * if there are no command-line arguments, from standard input.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
 BinarySearchTree<Integer> t = new BinarySearchTree<Integer>();

 Scanner in;
 if (args.length != 0)
     {
  in = new Scanner(args[0]);
     }
 else
     {
  in = new Scanner(System.in);
     }
 while (in.hasNext())
     {
  String command = in.next();
  if (command.toLowerCase().startsWith("a"))
      {
   int i = in.nextInt();
   t.add(i);
      }
  else if (command.toLowerCase().startsWith("r"))
      {
   int i = in.nextInt();
   t.remove(i);
      }
  else
      {
   in.nextLine();
      }
  t.validate();
     }
 System.out.println(t);
    }
}