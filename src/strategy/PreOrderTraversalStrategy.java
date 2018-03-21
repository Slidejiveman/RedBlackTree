package strategy;

import tree.RedBlackTree;
import tree.RedBlackTreeColorEnum;

/**
 * This class recursively performs a pre-order tree traversal.
 * It is a concrete strategy of tree traversal.
 */
public class PreOrderTraversalStrategy implements TraversalStrategy {
    public PreOrderTraversalStrategy() { }

    public void execute(RedBlackTree.RedBlackTreeNode n, RedBlackTree.RedBlackTreeNode nullNode) {
        if (n != nullNode) {
            System.out.print(((n.getColor() == RedBlackTreeColorEnum.RED) ? "Color: Red " : "Color: Black ") +
                    "Key: " + n.getElement() + " Parent: " + n.getParent().getElement() + "\n");
            execute(n.getLeft(), nullNode);
            execute(n.getRight(), nullNode);
        }
    }
}
