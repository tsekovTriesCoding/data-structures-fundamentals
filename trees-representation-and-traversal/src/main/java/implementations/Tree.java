package implementations;

import interfaces.AbstractTree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Tree<E> implements AbstractTree<E> {
    private E key;
    private Tree<E> parent;
    private List<Tree<E>> children;

    public Tree(E key, Tree<E>... children) {
        this.key = key;
        this.children = new ArrayList<>();
        for (Tree<E> child : children) {
            this.children.add(child);
            child.parent = this;
        }
    }

    @Override
    public List<E> orderBfs() {
        List<E> result = new ArrayList<>();
        Deque<Tree<E>> queue = new ArrayDeque<>();
        queue.offer(this);

        while (!queue.isEmpty()) {
            Tree<E> currentTree = queue.poll();
            result.add(currentTree.key);
            for (Tree<E> child : currentTree.children) {
                queue.offer(child);
            }
        }

        return result;
    }

    @Override
    public List<E> orderDfs() {
        List<E> result = new ArrayList<>();
        this.dfs(this, result);

        return result;
    }

    private void dfs(Tree<E> tree, List<E> result) {
        for (Tree<E> child : tree.children) {
            this.dfs(child, result);
        }

        result.add(tree.key);
    }

    @Override
    public void addChild(E parentKey, Tree<E> child) {
        Deque<Tree<E>> queue = new ArrayDeque<>();
        queue.offer(this);
        while (!queue.isEmpty()) {
            Tree<E> current = queue.poll();
            for (Tree<E> c : current.children) {
                if (c.key.equals(parentKey)) {
                    c.children.add(child);
                    return;
                }
                queue.offer(c);
            }
        }
    }

    @Override
    public void removeNode(E nodeKey) {
        Deque<Tree<E>> queue = new ArrayDeque<>();

        queue.offer(this);

        while (!queue.isEmpty()) {
            Tree<E> currentTree = queue.poll();

            if (currentTree.key.equals(nodeKey)) {
                if (!currentTree.children.isEmpty()) {
                    currentTree.children = null;
                }

                if (currentTree.parent != null) {
                    currentTree.parent.children.remove(currentTree);
                    currentTree.parent = null;
                }

                return;
            }

            for (Tree<E> child : currentTree.children) {
                queue.offer(child);
            }
        }

    }

    @Override
    public void swap(E firstKey, E secondKey) {

    }
}



