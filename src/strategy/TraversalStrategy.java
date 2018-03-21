package strategy;

import tree.RedBlackTree;

/**
 * The TraversalStrategy interface must be implemented by any
 * Strategy object that would implement a tree-traversal algorithm.
 * By doing this, the concrete strategies can be swapped and bound
 * at runtime because the concrete implementations all share the
 * same type.
 */
public interface TraversalStrategy {

    void execute(RedBlackTree.RedBlackTreeNode n, RedBlackTree.RedBlackTreeNode nullNode);
}
