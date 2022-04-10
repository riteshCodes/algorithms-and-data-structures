package lab;


import frame.TreeNode;
import frame.Process;

public class CompletelyFairScheduler {

    private RedBlackTree tree;
    private int windowMaxLength;

    /**
     * Creating a new CompletelyFairScheduler
     *
     * @param windowMaxLength is the maximal length of one execution window
     */
    public CompletelyFairScheduler(int windowMaxLength) {
        tree = new RedBlackTree();
        this.windowMaxLength = windowMaxLength;
    }

    /**
     * Distribute execution windows to the processes.
     *
     * @param windows The number of execution windows to distribute
     */
    public void run(int windows) {
        for (int i = 0; i < windows; i++) {
            if (tree.root() != tree.nil()) {
                TreeNode minimum = tree.minimum(); // search minimum node
                if (minimum == tree.nil()) // check if node is nil
                    break;
                minimum.value.run(windowMaxLength); // execute process

                tree.delete(minimum); // delete process and add with new time, if process is not finished
                if (!minimum.value.finished()) {
                    minimum.key = minimum.value.executionTime();
                    tree.insert(minimum);
                }
            }
        }
    }

    /**
     * Add a process to the Scheduler.
     */
    public void add(Process process) {
        int executionTime = process.executionTime();

        while (tree.search(executionTime) != tree.nil())
            executionTime += 1;

        TreeNode n = new TreeNode();
        n.key = executionTime;
        n.value = process;
        tree.insert(n);
    }

    // DO NOT MODIFY
    // used for the tests
    public RedBlackTree getTree() {
        return tree;
    }

}
