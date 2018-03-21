package tree;

import strategy.InOrderTraversalStrategy;
import strategy.PostOrderTraversalStrategy;
import strategy.PreOrderTraversalStrategy;
import strategy.TraversalStrategy;

import java.util.Scanner;

/**
 * Implementation of a Red-Black Tree that also includes a
 * main method as a test driver. A Red-Black Tree is a self-balancing
 * binary search tree. The self-balancing of this kind of tree is not
 * perfect, but it guarantees that insertion, deletion, rearrangement,
 * and recoloring are all performed in O(log n) time, which is very good.
 *
 * Properties:
 * 1. All the constraints placed on a Binary Search Tree
 * 2. Each node is red or black
 * 3. The root is always black (not always enforced)
 * 4. All leaves are black
 * 5. If a node is red, then both of its children are black
 * 6. Every path from a given node to any of its descendant
 *    null nodes contains the same number of black nodes.
 * In general, the path from the root to the farthest leaf is no more
 * than twice as long as the path from the root to the nearest leaf.
 *
 * @version 1.0
 * @author Slidejiveman
 * Please see References.txt for additional credits.
 */
public class RedBlackTree {

    /**
     * The root is always black, and it can be changed during a rotation.
     */
    private RedBlackTreeNode root;

    /**
     * The nullNode is used to protect from null pointer exceptions.
     * It is an implementation of the Null Object design pattern.
     * This node implements "do nothing" behavior, meaning it serves
     * wherever a null would be located.
     */
    private final RedBlackTreeNode nullNode = new RedBlackTreeNode(-1);

    /**
     * The traversalStrategy is used to implement the strategy design
     * pattern in this RBT. Any of the traversals that implement this
     * interface can be used here, which demonstrates polymorphism.
     */
    private TraversalStrategy traversalStrategy;

    /**
     * The default constructor ensures that the root and its children
     * point to the nullNode. Already, the benefits of having a Null
     * Object can be seen here. It is like a fence, protecting the
     * code from NPEs.
     */
    public RedBlackTree() {
        root = nullNode;
        root.setLeft(nullNode);
        root.setRight(nullNode);
    }

    /**
     * The insertion method for the RBT accepts a RedBlackTreeNode.
     * It handles the case when the tree is empty as well as
     * insertion based on the BST principles that smaller key
     * values are inserted on the left side of the tree and
     * higher values on the right.
     * @param n - the node to insert into the RBT.
     */
    public void insert (RedBlackTreeNode n) {
        RedBlackTreeNode temp = root;
        // in this case, the tree is empty, n is the root.
        if (root == nullNode) {
            root = n;
            n.setColor(RedBlackTreeColorEnum.BLACK);
            n.setParent(nullNode);
        } else { // in this case, n is added on the left or right
                 // based on key value
            n.setColor(RedBlackTreeColorEnum.RED); // not root => RED
            while (true) {
                if (n.getElement() < temp.getElement()) {
                    if (temp.getLeft() == nullNode) {
                        temp.setLeft(n);
                        n.setParent(temp);
                        break;
                    } else {
                        temp = temp.getLeft();
                    }
                } else if (n.getElement() >= temp.getElement()) {
                    if (temp.getRight() == nullNode) {
                        temp.setRight(n);
                        n.setParent(temp);
                        break;
                    } else {
                        temp = temp.getRight();
                    }
                }
            }
            // after an insertion, a rebalancing might be necessary
            rebalance(n);
        }
    }

    /**
     * Called after an insertion occurs. A rebalancing ensures that all
     * of the properties listed above are still true after inserting.
     * If there is a point at which the properties are not held, then
     * the tree isn't an RBT. This method, then, amounts to ensuring
     * that the nodes are the correct colors and that the lower values
     * are on the left side of the tree as opposed to the right.
     * In the cases where values have to be moved, there are rotations
     * and double rotations that may occur.
     * @param n - the inserted node that caused the rebalance to occur
     */
    private void rebalance(RedBlackTreeNode n) {
        // for each red, ensure that the children are both black
        while (n.getParent().getColor() == RedBlackTreeColorEnum.RED) {
            RedBlackTreeNode uncle;
            // the passed in node is the child of a left child.
            if (n.getParent() == n.getParent().getParent().getLeft()) {
                uncle = n.getParent().getParent().getRight(); // its uncle is a right child

                if (uncle != nullNode && uncle.getColor() == RedBlackTreeColorEnum.RED) {
                    n.getParent().setColor(RedBlackTreeColorEnum.BLACK);
                    uncle.setColor(RedBlackTreeColorEnum.BLACK);
                    n.getParent().getParent().setColor(RedBlackTreeColorEnum.RED);
                    n = n.getParent().getParent();
                    continue;
                }
                if (n == n.getParent().getRight()) {
                    n = n.getParent();
                    rotateLeft(n); // double rotation required to rebalance
                }
                n.getParent().setColor(RedBlackTreeColorEnum.BLACK);
                n.getParent().getParent().setColor(RedBlackTreeColorEnum.RED);

                // case where only single rotation is needed
                rotateRight(n.getParent().getParent());
            } else {
                uncle = n.getParent().getParent().getLeft();
                if (uncle != nullNode && uncle.getColor() == RedBlackTreeColorEnum.RED) {
                    n.getParent().setColor(RedBlackTreeColorEnum.BLACK);
                    uncle.setColor(RedBlackTreeColorEnum.BLACK);
                    n.getParent().getParent().getParent().setColor(RedBlackTreeColorEnum.RED);
                    n = n.getParent().getParent();
                    continue;
                }
                if (n == n.getParent().getLeft()) {
                    n = n.getParent();
                    rotateRight(n); // double rotation required to rebalance
                }
                n.getParent().setColor(RedBlackTreeColorEnum.BLACK);
                n.getParent().getParent().setColor(RedBlackTreeColorEnum.RED);

                // case where only single rotation is needed
                rotateLeft(n.getParent().getParent());
            }
        }
        root.setColor(RedBlackTreeColorEnum.BLACK);
    }

    /**
     * Rotating to the left twists the right child
     * into the parent position.
     * @param n - the node to rotate about
     */
    private void rotateLeft(RedBlackTreeNode n) {
        if (n.getParent() != nullNode) {
            if (n == n.getParent().getLeft()) {
                n.getParent().setLeft(n.getRight());
            } else {
                n.getParent().setRight(n.getRight());
            }
            n.getRight().setParent(n.getParent());
            n.setParent(n.getRight());
            if (n.getRight().getLeft() != nullNode) {
                n.getRight().getLeft().setParent(n);
            }
            n.setRight(n.getRight().getLeft());
            n.getParent().setLeft(n);
        } else { // rotate the root
            RedBlackTreeNode right = root.getRight();
            root.setRight(right.getLeft());
            right.getLeft().setParent(root);
            root.setParent(right);

            right.setLeft(root);
            right.setParent(nullNode);
            root = right;
        }
    }

    /**
     * Rotating to the right twists the left child
     * into the parent position
     * @param n - the node to rotate about
     */
    private void rotateRight(RedBlackTreeNode n) {
        if (n.getParent() != nullNode) {
            if (n == n.getParent().getLeft()) {
                n.getParent().setLeft(n.getLeft());
            } else {
                n.getParent().setRight(n.getLeft());
            }
            n.getLeft().setParent(n.getParent());
            n.setParent(n.getLeft());
            if (n.getLeft().getRight() != nullNode) {
                n.getLeft().getRight().setParent(n);
            }
            n.setLeft(n.getLeft().getRight());
            n.getParent().setRight(n);
        } else {
            RedBlackTreeNode left = root.getLeft();
            root.setLeft(root.getLeft().getRight());
            left.getRight().setParent(root);
            root.setParent(left);
            left.setRight(root);
            left.setParent(nullNode);
            root = left;
        }
    }

    /**
     * Searches for a particular node in the RBT.
     * This method is also recursive. It terminates when
     * the candidate node becomes the nullNode.
     *
     * @param target - the Node to find
     * @param searchable - the candidate node that might match
     * @return either the nullNode or the found node
     */
    private RedBlackTreeNode find(RedBlackTreeNode target, RedBlackTreeNode searchable) {
        if (root != nullNode) {
            if (target.getElement() < searchable.getElement()) {
                if (searchable.getLeft() != nullNode) {
                    return find(target, searchable.getLeft());
                }
            } else if (target.getElement() > searchable.getElement()) {
                if (searchable.getRight() != nullNode) {
                    return find(target, searchable.getRight());
                }
            } else if (target.getElement() == searchable.getElement()) {
                return searchable;
            }
        }
        return nullNode;
    }

    /**
     * Sets the root to the nullNode. This invalidates
     * the rest of the references and marks the memory
     * for garbage collection.
     */
    private void deleteTree() {
        root = nullNode;
    }

    /**
     * Used during the deletion routine
     *
     * @param target - the node to replace
     * @param with - the node that replaces the target
     */
    private void transplant (RedBlackTreeNode target, RedBlackTreeNode with) {
        if (target.getParent() == nullNode) {
            root = with;
        } else if (target == target.getParent().getLeft()) {
            target.getParent().setLeft(with);
        } else {
            target.getParent().setRight(with);
        }
        with.setParent(target.getParent());
    }

    /**
     * Removes the specified node from the tree.
     * @param target - the node to remove
     * @return True if the node is found. False otherwise
     */
    private boolean delete(RedBlackTreeNode target) {
        if ((target = find(target, root)) == nullNode) {
            return false;
        }
        RedBlackTreeNode x;
        RedBlackTreeNode y = target;
        int original = y.getColor(); // enums represent ints

        if (target.getLeft() == nullNode) {
            x = target.getRight();
            transplant(target, target.getRight());
        } else if (target.getRight() == nullNode) {
            x = target.getLeft();
            transplant(target, target.getLeft());
        } else {
            y = treeMinimum(target.getRight());
            original = y.getColor();
            x = y.getRight();
            if (y.getParent() == target) {
                x.setParent(y);
            } else {
                transplant(y, y.getRight());
                y.setRight(target.getRight());
                y.getRight().setParent(y);
            }
            transplant(target, y);
            y.setLeft(target.getLeft());
            y.getLeft().setParent(y);
            y.setColor(target.getColor());
            if(original == RedBlackTreeColorEnum.BLACK) {
                deleteFixup(x);
            }
        }
        return true;
    }

    /**
     * This method ensures that a tree is balanced and all
     * of the properties are upheld after a deletion/transplant.
     *
     * @param n - the node reference for the fixup.
     */
    private void deleteFixup(RedBlackTreeNode n) {
        while (n != root && n.getColor() != RedBlackTreeColorEnum.BLACK) {
            if (n == n.getParent().getLeft()) {
                RedBlackTreeNode temp = n.getParent().getRight();
                if (temp.getColor() == RedBlackTreeColorEnum.RED) {
                    temp.setColor(RedBlackTreeColorEnum.BLACK);
                    n.getParent().setColor(RedBlackTreeColorEnum.RED);
                    rotateLeft(n.getParent());
                    temp = n.getParent().getRight();
                }
                if (temp.getLeft().getColor() == RedBlackTreeColorEnum.BLACK &&
                        temp.getRight().getColor() == RedBlackTreeColorEnum.BLACK) {
                    temp.setColor(RedBlackTreeColorEnum.RED);
                    n = n.getParent();
                    continue;
                } else if (temp.getRight().getColor() == RedBlackTreeColorEnum.BLACK){
                    temp.getLeft().setColor(RedBlackTreeColorEnum.BLACK);
                    temp.setColor(RedBlackTreeColorEnum.RED);
                    rotateRight(temp);
                    temp = n.getParent().getRight();
                }
                if (temp.getRight().getColor() == RedBlackTreeColorEnum.RED) {
                    temp.setColor(n.getParent().getColor());
                    n.getParent().setColor(RedBlackTreeColorEnum.BLACK);
                    temp.getRight().setColor(RedBlackTreeColorEnum.BLACK);
                    rotateLeft(n.getParent());
                    n = root;
                }
            } else {
                RedBlackTreeNode temp = n.getParent().getLeft();
                if (temp.getColor() == RedBlackTreeColorEnum.RED) {
                    temp.setColor(RedBlackTreeColorEnum.BLACK);
                    n.getParent().setColor(RedBlackTreeColorEnum.RED);
                    rotateRight(n.getParent());
                    temp = n.getParent().getLeft();
                }
                if (temp.getRight().getColor() == RedBlackTreeColorEnum.BLACK &&
                        temp.getLeft().getColor() == RedBlackTreeColorEnum.BLACK) {
                    temp.setColor(RedBlackTreeColorEnum.RED);
                    n = n.getParent();
                    continue;
                } else if (temp.getLeft().getColor() == RedBlackTreeColorEnum.BLACK) {
                    temp.getRight().setColor(RedBlackTreeColorEnum.BLACK);
                    temp.setColor(RedBlackTreeColorEnum.RED);
                    rotateLeft(temp);
                    temp = n.getParent().getLeft();
                }
                if (temp.getLeft().getColor() == RedBlackTreeColorEnum.RED) {
                    temp.setColor(n.getParent().getColor());
                    n.getParent().setColor(RedBlackTreeColorEnum.BLACK);
                    temp.getLeft().setColor(RedBlackTreeColorEnum.BLACK);
                    rotateRight(n.getParent());
                    n = root;
                }
            }
        }
        n.setColor(RedBlackTreeColorEnum.BLACK);
    }

    /**
     * Returns the minimum value in the tree, which is the left-most node.
     *
     * @param subTreeRoot - a node at which to start the search for the min
     * @return the minimum value in the subtree.
     */
    private RedBlackTreeNode treeMinimum(RedBlackTreeNode subTreeRoot) {
        while (subTreeRoot.getLeft() != nullNode) {
            subTreeRoot = subTreeRoot.getLeft();
        }
        return subTreeRoot;
    }

    /**
     * @return the TraversalStrategy currently assigned to this RBT.
     */
    public TraversalStrategy getTraversalStrategy() {
        return traversalStrategy;
    }

    /**
     * @param traversalStrategy - the TraversalStrategy to assign to this RBT.
     */
    public void setTraversalStrategy(TraversalStrategy traversalStrategy) {
        this.traversalStrategy = traversalStrategy;
    }

    /**
     * Simple CLI that allows testing of the RBT.
     * -999 is the command to stop entry.
     * After a selection (1-5) is made, then enter the values
     * separated by a space. Press enter again for the processing to
     * occur. Borrowed from Source 2 in references.txt
     */
    private void consoleUI() {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("\n1.- Add items\n"
                    + "2.- Delete items\n"
                    + "3.- Check items\n"
                    + "4.- Print tree\n"
                    + "5.- Delete tree\n");
            int choice = scan.nextInt();

            int item;
            RedBlackTreeNode node;
            switch (choice) {
                case 1:
                    item = scan.nextInt();
                    while (item != -999) {
                        node = new RedBlackTreeNode(item);
                        insert(node);
                        item = scan.nextInt();
                    }
                    // start with in order, then print all three strategies.
                    // return to in order so it is ready for the next run
                    System.out.println("In Order Strategy.");
                    traversalStrategy.execute(root, nullNode);
                    System.out.println("\nPre Order Strategy.");
                    setTraversalStrategy(new PreOrderTraversalStrategy());
                    traversalStrategy.execute(root, nullNode);
                    System.out.println("\nPost Order Strategy.");
                    setTraversalStrategy(new PostOrderTraversalStrategy());
                    traversalStrategy.execute(root, nullNode);
                    setTraversalStrategy(new InOrderTraversalStrategy());
                    break;
                case 2:
                    item = scan.nextInt();
                    while (item != -999) {
                        node = new RedBlackTreeNode(item);
                        System.out.print("\nDeleting item " + item);
                        if (delete(node)) {
                            System.out.print(": deleted!");
                        } else {
                            System.out.print(": does not exist!");
                        }
                        item = scan.nextInt();
                    }
                    System.out.println();
                    System.out.println("In Order Strategy.");
                    traversalStrategy.execute(root, nullNode);
                    System.out.println("\nPre Order Strategy.");
                    setTraversalStrategy(new PreOrderTraversalStrategy());
                    traversalStrategy.execute(root, nullNode);
                    System.out.println("\nPost Order Strategy.");
                    setTraversalStrategy(new PostOrderTraversalStrategy());
                    traversalStrategy.execute(root, nullNode);
                    setTraversalStrategy(new InOrderTraversalStrategy());
                    break;
                case 3:
                    item = scan.nextInt();
                    while (item != -999) {
                        node = new RedBlackTreeNode(item);
                        System.out.println((find(node, root) != nullNode) ? "found" : "not found");
                        item = scan.nextInt();
                    }
                    break;
                case 4:
                    System.out.println("In Order Strategy.");
                    traversalStrategy.execute(root, nullNode);
                    System.out.println("\nPre Order Strategy.");
                    setTraversalStrategy(new PreOrderTraversalStrategy());
                    traversalStrategy.execute(root, nullNode);
                    System.out.println("\nPost Order Strategy.");
                    setTraversalStrategy(new PostOrderTraversalStrategy());
                    traversalStrategy.execute(root, nullNode);
                    setTraversalStrategy(new InOrderTraversalStrategy());
                    break;
                case 5:
                    deleteTree();
                    System.out.println("Tree deleted!");
                    break;
            }
        }
    }

    /**
     * The entry point of the program creates the RBT,
     * sets the initial traversal strategy, and executes
     * the "game loop" of the UI.
     * @param args - ignored in version 1.0
     */
    public static void main(String[] args) {
        RedBlackTree rbt = new RedBlackTree();
        rbt.setTraversalStrategy(new InOrderTraversalStrategy());
        rbt.consoleUI();
    }

    /**
     * This inner class must be in this file so that it can
     * use the nullNode in its constructors.
     */
    public class RedBlackTreeNode {

        /**
         * The key value of the node, which is used to
         * determine the node's value. It is used for all
         * comparisons.
         */
        private int element;

        /**
         * The left child of the node
         */
        private RedBlackTreeNode left;

        /**
         * The right child of the node
         */
        private RedBlackTreeNode right;

        /**
         * The parent of the node
         */
        private RedBlackTreeNode parent;

        /**
         * The Red or Black state of the Node
         * This could be represented with 1 bit....
         */
        private int color;

        /**
         * The default constructor makes a nullNode
         */
        RedBlackTreeNode() {
            this(-1);
        }

        /**
         * The constructor with one integer argument
         * could also be used to make a nullNode if -1
         * is passed in.
         * @param key - the value of the node
         */
        RedBlackTreeNode(int key) {
            this(key, nullNode, nullNode, nullNode);
        }

        /**
         * This constructor can be used to copy a node.
         * @param n
         */
        RedBlackTreeNode(RedBlackTreeNode n) {
            this(n.getElement(), n.getLeft(), n.getRight(), n.getParent());
        }

        /**
         * This private constructor does all of the work. The public
         * constructor are a facade for this one.
         *
         * @param key - the value of the node
         * @param leftChild - the child of lesser value
         * @param rightChild - the child of greater value
         * @param p - the node that will be the parent of this one
         */
        private RedBlackTreeNode(int key, RedBlackTreeNode leftChild,
                                 RedBlackTreeNode rightChild, RedBlackTreeNode p) {
            setElement(key);
            setLeft(leftChild);
            setRight(rightChild);
            setParent(p);
            setColor(RedBlackTreeColorEnum.BLACK);
        }

        /**
         * @return the parent
         */
        public RedBlackTreeNode getParent() {
            return parent;
        }

        /**
         * @param parent - to set on this node
         */
        public void setParent(RedBlackTreeNode parent) {
            this.parent = parent;
        }

        /**
         * @return the value of the node
         */
        public int getElement() {
            return element;
        }

        /**
         * @param element - the value to set on this node
         */
        public void setElement(int element) {
            this.element = element;
        }

        /**
         * @return the left child (lower element value)
         */
        public RedBlackTreeNode getLeft() {
            return left;
        }

        /**
         * @param left - the node to set as the left child.
         */
        public void setLeft(RedBlackTreeNode left) {
            this.left = left;
        }

        /**
         * @return the right child (higher element value)
         */
        public RedBlackTreeNode getRight() {
            return right;
        }

        /**
         * @param right - the node to set as the right child
         */
        public void setRight(RedBlackTreeNode right) {
            this.right = right;
        }

        /**
         * @return the integer representation of the colors RED or BLACK
         */
        public int getColor() {
            return color;
        }

        /**
         * Only the Enumeration should be used here as a best practice.
         * @param color - the integer value associated with RED or BLACK
         */
        public void setColor(int color) {
            this.color = color;
        }
    }

}
