import implementations.Tree;

import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        Tree<Integer> tree = new Tree<>(7,
                new Tree<>(19,
                        new Tree<>(1),
                        new Tree<>(12),
                        new Tree<>(31)),
                new Tree<>(21),
                new Tree<>(14,
                        new Tree<>(23),
                        new Tree<>(6))
        );

        tree.removeNode(19);
        Integer[] expected = {7, 21, 14, 23, 6};

        List<Integer> integers = tree.orderBfs();

        int index = 0;
        for (Integer num : integers) {
            System.out.println(Objects.equals(num, expected[index++]));
        }
    }
}
