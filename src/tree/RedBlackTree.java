package tree;

import strategy.InOrderTraversalStrategy;
import strategy.PostOrderTraversalStrategy;
import strategy.PreOrderTraversalStrategy;
import strategy.TraversalStrategy;

import java.util.Scanner;

public class RedBlackTree {

    private RedBlackTreeNode root;
    private final RedBlackTreeNode nullNode = new RedBlackTreeNode(-1);
    TraversalStrategy traversalStrategy;

    public RedBlackTree() {
        root = nullNode;
        root.setLeft(nullNode);
        root.setRight(nullNode);
    }

    public void insert (RedBlackTreeNode n) {
        RedBlackTreeNode temp = root;
        if (root == nullNode) {
            root = n;
            n.setColor(RedBlackTreeColorEnum.BLACK);
            n.setParent(nullNode);
        } else {
            n.setColor(RedBlackTreeColorEnum.RED);
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
            rebalance(n);
        }
    }

    private void rebalance(RedBlackTreeNode n) {
        while (n.getParent().getColor() == RedBlackTreeColorEnum.RED) {
            RedBlackTreeNode uncle = new RedBlackTreeNode(nullNode);
            if (n.getParent() == n.getParent().getParent().getLeft()) {
                uncle = n.getParent().getParent().getRight();

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

    private void deleteTree() {
        root = nullNode;
    }

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

    private boolean delete(RedBlackTreeNode target) {
        if ((target = find(target, root)) == nullNode) {
            return false;
        }
        RedBlackTreeNode x = new RedBlackTreeNode(nullNode);
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

    private RedBlackTreeNode treeMinimum(RedBlackTreeNode subTreeRoot) {
        while (subTreeRoot.getLeft() != nullNode) {
            subTreeRoot = subTreeRoot.getLeft();
        }
        return subTreeRoot;
    }

    public TraversalStrategy getTraversalStrategy() {
        return traversalStrategy;
    }

    public void setTraversalStrategy(TraversalStrategy traversalStrategy) {
        this.traversalStrategy = traversalStrategy;
    }

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
                    traversalStrategy.execute(root, nullNode);
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
                    traversalStrategy.execute(root, nullNode);
                    break;
                case 5:
                    deleteTree();
                    System.out.println("Tree deleted!");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        RedBlackTree rbt = new RedBlackTree();
        rbt.setTraversalStrategy(new InOrderTraversalStrategy());
        rbt.consoleUI();
    }

    public class RedBlackTreeNode {

        private int element;
        private RedBlackTreeNode left;
        private RedBlackTreeNode right;
        private RedBlackTreeNode parent;
        private int color;

        RedBlackTreeNode() {
            this(-1);
        }

        RedBlackTreeNode(int key) {
            this(key, nullNode, nullNode, nullNode);
        }

        RedBlackTreeNode(RedBlackTreeNode n) {
            this(n.getElement(), n.getLeft(), n.getRight(), n.getParent());
        }

        private RedBlackTreeNode(int key, RedBlackTreeNode leftChild,
                                 RedBlackTreeNode rightChild, RedBlackTreeNode p) {
            setElement(key);
            setLeft(leftChild);
            setRight(rightChild);
            setParent(p);
            setColor(RedBlackTreeColorEnum.BLACK);
        }

        public RedBlackTreeNode getParent() {
            return parent;
        }

        public void setParent(RedBlackTreeNode parent) {
            this.parent = parent;
        }

        public int getElement() {
            return element;
        }

        public void setElement(int element) {
            this.element = element;
        }

        public RedBlackTreeNode getLeft() {
            return left;
        }

        public void setLeft(RedBlackTreeNode left) {
            this.left = left;
        }

        public RedBlackTreeNode getRight() {
            return right;
        }

        public void setRight(RedBlackTreeNode right) {
            this.right = right;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

}
