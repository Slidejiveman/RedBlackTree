package strategy;

import tree.RedBlackTree;
import tree.RedBlackTreeColorEnum;

/**
 * This class recursively performs a post-order tree traversal.
 * It is a concrete strategy of tree traversal.
 */
public class PostOrderTraversalStrategy implements TraversalStrategy {
    public PostOrderTraversalStrategy() { }

    public void execute(RedBlackTree.RedBlackTreeNode n, RedBlackTree.RedBlackTreeNode nullNode) {
        if (n != nullNode) {
            execute(n.getLeft(), nullNode);
            execute(n.getRight(), nullNode);
            System.out.print(((n.getColor() == RedBlackTreeColorEnum.RED) ? "Color: Red " : "Color: Black ") +
                "Key: " + n.getElement() + " Parent: " + n.getParent().getElement() + "\n");
        }
    }
}
