package implementations;

import interfaces.AbstractBinaryTree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BinaryTree<E> implements AbstractBinaryTree<E> {
    private E key;
    private AbstractBinaryTree<E> left;
    private AbstractBinaryTree<E> right;

    public BinaryTree(E key, BinaryTree<E> left, BinaryTree<E> right) {
        this.key = key;
        this.left = left;
        this.right = right;
    }

    @Override
    public E getKey() {
        return this.key;
    }

    @Override
    public AbstractBinaryTree<E> getLeft() {
        return this.left;
    }

    @Override
    public AbstractBinaryTree<E> getRight() {
        return this.right;
    }

    @Override
    public void setKey(E key) {
        this.key = key;
    }

    @Override
    public String asIndentedPreOrder(int indent) {
        String padding = createPadding(indent) + this.getKey();
        if (this.getLeft() != null) {
            padding += "\r\n" + this.getLeft().asIndentedPreOrder(indent + 2);
        }
        if (this.getRight() != null) {
            padding += "\r\n" + this.getRight().asIndentedPreOrder(indent + 2);
        }
        return padding;
    }

    private String createPadding(int indent) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ".repeat(Math.max(0, indent)));

        return builder.toString();
    }

    @Override
    public List<AbstractBinaryTree<E>> preOrder() {
        List<AbstractBinaryTree<E>> trees = new ArrayList<>();
        trees.add(this);

        if (this.left != null) {
            trees.addAll(this.left.preOrder());
        }

        if (this.right != null) {
            trees.addAll(this.right.preOrder());
        }

        return trees;
    }

    @Override
    public List<AbstractBinaryTree<E>> inOrder() {
        List<AbstractBinaryTree<E>> trees = new ArrayList<>();

        if (this.left != null) {
            trees.addAll(this.left.inOrder());
        }

        trees.add(this);

        if (this.right != null) {
            trees.addAll(this.right.inOrder());
        }

        return trees;
    }

    @Override
    public List<AbstractBinaryTree<E>> postOrder() {
        List<AbstractBinaryTree<E>> trees = new ArrayList<>();

        if (this.left != null) {
            trees.addAll(this.left.postOrder());
        }

        if (this.right != null) {
            trees.addAll(this.right.postOrder());
        }

        trees.add(this);

        return trees;
    }

    @Override
    public void forEachInOrder(Consumer<E> consumer) {

        if (this.left != null) {
            this.getLeft().forEachInOrder(consumer);
        }

        consumer.accept(this.getKey());

        if (this.right != null) {
            this.getRight().forEachInOrder(consumer);
        }
    }
}
