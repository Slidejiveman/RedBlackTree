package strategy;

import tree.RedBlackTree;

public interface TraversalStrategy {

    void execute(RedBlackTree.RedBlackTreeNode n, RedBlackTree.RedBlackTreeNode nullNode);
}
