package strategy;

import tree.RedBlackTree;
import tree.RedBlackTreeColorEnum;

/**
 * This class recursively performs a in-order tree traversal.
 * It is a concrete strategy of tree traversal.
 */
public class InOrderTraversalStrategy implements TraversalStrategy {

    public InOrderTraversalStrategy() { }

    public void execute(RedBlackTree.RedBlackTreeNode n, RedBlackTree.RedBlackTreeNode nullNode) {
        if (n != nullNode ) {
            execute(n.getLeft(), nullNode);
            System.out.print(((n.getColor() == RedBlackTreeColorEnum.RED) ? "Color: Red " : "Color: Black ") +
                    "Key: " + n.getElement() + " Parent: " + n.getParent().getElement() + "\n");
            execute(n.getRight(), nullNode);
        }
    }
}
