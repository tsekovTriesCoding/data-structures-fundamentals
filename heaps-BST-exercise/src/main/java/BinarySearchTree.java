import solutions.BinaryTree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.function.Consumer;

import java.util.List;

public class BinarySearchTree<E extends Comparable<E>> {
    private Node<E> root;

    public BinarySearchTree(E element) {
        this.root = new Node<>(element);
    }

    public BinarySearchTree() {

    }

    public static class Node<E> {
        private E value;
        private Node<E> leftChild;
        private Node<E> rightChild;
        private int count;

        public Node(E value) {
            this.value = value;
            this.count = 1;
        }

        public Node(Node<E> other) {
            this.value = other.value;
            this.count = other.count;

            if (other.leftChild != null) {
                this.leftChild = new Node<>(other.leftChild);
            }

            if (other.rightChild != null) {
                this.rightChild = new Node<>(other.rightChild);
            }
        }

        public Node<E> getLeft() {
            return this.leftChild;
        }

        public Node<E> getRight() {
            return this.rightChild;
        }

        public E getValue() {
            return this.value;
        }

        public int getCount() {
            return this.count;
        }
    }

    public void eachInOrder(Consumer<E> consumer) {
        nodeInOrder(this.root, consumer);
    }

    private void nodeInOrder(Node<E> node, Consumer<E> consumer) {
        if (root != null) {
            return;
        }

        nodeInOrder(node.leftChild, consumer);
        consumer.accept(node.value);
        nodeInOrder(node.rightChild, consumer);
    }

    public Node<E> getRoot() {
        return this.root;
    }

    public void insert(E element) {
        if (this.root == null) {
            this.root = new Node<>(element);
        } else {
            insertInto(this.root, element);
        }
    }

    private void insertInto(Node<E> node, E element) {
        if (isGreater(element, node)) {
            if (node.rightChild == null) {
                node.rightChild = new Node<>(element);
            } else {
                insertInto(node.rightChild, element);
            }
        } else if (isLess(element, node)) {
            if (node.leftChild == null) {
                node.leftChild = new Node<>(element);
            } else {
                insertInto(node.leftChild, element);
            }
        }

        node.count++;
    }

    public boolean contains(E element) {
        Node<E> current = this.root;

        while (current != null) {
            if (isEqual(element, current)) {
                break;
            } else if (isGreater(element, current)) {
                current = current.rightChild;
            } else {
                current = current.leftChild;
            }

        }

        return current != null;
    }

    private Node<E> containsNode(Node<E> node, E element) {
        if (node == null) {
            return null;
        }

        if (isEqual(element, node)) {
            return node;
        } else if (isGreater(element, node)) {
            return containsNode(node.rightChild, element);
        }

        return containsNode(node.leftChild, element);
    }

    public BinarySearchTree<E> search(E element) {
        Node<E> found = this.containsNode(this.root, element);

        return found == null ? null : new BinarySearchTree<>(found.value);
    }

    public List<E> range(E lower, E upper) {
        List<E> result = new ArrayList<>();

        if (this.root == null) {
            return result;
        }

        Deque<Node<E>> queue = new ArrayDeque<>();
        queue.offer(this.root);

        while (!queue.isEmpty()) {
            Node<E> current = queue.poll();

            if (current.getLeft() != null) {
                queue.offer(current.getLeft());
            }

            if (current.getRight() != null) {
                queue.offer(current.getRight());
            }

            if (isLess(lower, current) && isGreater(upper, current)) {
                result.add(current.getValue());
            } else if (isEqual(lower, current) || isEqual(upper, current)) {
                result.add(current.getValue());
            }
        }

        return result;
    }

    public void deleteMin() {
        ensureNonempty();

        if (this.root.getLeft() == null) {
            this.root = this.root.getRight();
            return;
        }

        Node<E> current = this.root;

        while (current.getLeft().getLeft() != null) {
            current.count--;
            current = current.getLeft();

        }

        current.count--;
        current.leftChild = current.getLeft().getRight();
    }

    public void deleteMax() {
        ensureNonempty();

        if (this.root.getRight() == null) {
            this.root = this.root.getLeft();
            return;
        }

        Node<E> current = this.root;

        while (current.getRight().getRight() != null) {
            current.count--;
            current = current.getRight();
        }

        current.count--;
        current.rightChild = current.getRight().getLeft();
    }

    public int count() {
        return this.root == null ? 0 : this.root.count;
    }

    public int rank(E element) {
        return nodeRank(this.root, element);
    }

    private int nodeRank(Node<E> node, E element) {
        if (node == null) {
            return 0;
        }

        if (isLess(element, node)) {
            return nodeRank(node.leftChild, element);
        } else if (isEqual(element, node)) {
            return getNodeCount(node.getLeft());
        }

        return getNodeCount(node.getLeft()) + 1 + nodeRank(node.getRight(), element);
    }

    private int getNodeCount(Node<E> node) {
        return node == null ? 0 : node.getCount();
    }

    public E floor(E element) {
        if (this.root == null) {
            return null;
        }

        Node<E> current = this.root;
        Node<E> nearestSmaller = null;

        while (current != null) {
            if (isGreater(element, current)) {
                nearestSmaller = current;
                current = current.getRight();
            } else if (isLess(element, current)) {
                current = current.getLeft();
            } else {
                Node<E> left = current.getLeft();

                if (left != null && nearestSmaller != null) {
                    nearestSmaller = isGreater(left.getValue(), nearestSmaller) ? left : nearestSmaller;
                } else if (nearestSmaller == null) {
                    nearestSmaller = left;
                }

                break;
            }
        }

        return nearestSmaller == null ? null : nearestSmaller.getValue();
    }

    public E ceil(E element) {
        if (this.root == null) {
            return null;
        }
        Node<E> current = this.root;
        Node<E> nearestBigger = null;

        while (current != null) {
            if (isLess(element, current)) {
                nearestBigger = current;
                current = current.getLeft();
            } else if (isGreater(element, current)) {
                current = current.getRight();
            } else {
                Node<E> right = current.getRight();

                if (right != null && nearestBigger != null) {
                    nearestBigger = isLess(right.getValue(), nearestBigger) ? right : nearestBigger;
                } else if (nearestBigger == null) {
                    nearestBigger = right;
                }

                break;
            }
        }

        return nearestBigger == null ? null : nearestBigger.getValue();
    }

    private boolean isLess(E element, Node<E> node) {
        return element.compareTo(node.value) < 0;
    }

    private boolean isGreater(E element, Node<E> node) {
        return element.compareTo(node.value) > 0;
    }

    private boolean isEqual(E element, Node<E> node) {
        return element.compareTo(node.value) == 0;
    }

    private void ensureNonempty() {
        if (this.root == null) {
            throw new IllegalArgumentException("Tree is empty");
        }
    }
}
