package lab;


import frame.TreeNode;
import frame.TreeNode.NodeColor;

/**
 * An implementation of a Black-Red-Tree
 */
public class RedBlackTree {

    private TreeNode _root;
    private TreeNode _nil;

    public RedBlackTree() {
        _nil = new TreeNode();
        _root = _nil;
    }

    public TreeNode root() {
        return this._root;
    }

    public TreeNode nil() {
        return this._nil;
    }

    /**
     * Return the node with the given key, or nil, if no such node exists.
     */
    public TreeNode search(int key) {
        TreeNode node = this._root;
        // iteration until the node is nil (until the sentinel is reached)
        while (node != nil()) {
            if (node.key == key)
                return node; // the node with the given key is found
            if (node.key < key)
                node = node.right;
            else
                node = node.left;
        }
        // case below: node is nil
        return node;
    }

    /**
     * Return the node with the smallest key
     */
    public TreeNode minimum() {
        return minimumSubtree(_root);
    }

    /**
     * Return the node with the smallest key in the subtree that has x as root node.
     */
    public TreeNode minimumSubtree(TreeNode x) {
        TreeNode node = x;
        while (node.left != nil()) // iteration until the left child of the leftmost node reaches the sentinel
            node = node.left;
        return node; // leftmost node of the tree
    }

    /**
     * Return the node with the greatest key.
     */
    public TreeNode maximum() {
        return maximumSubtree(_root);
    }

    /**
     * Return the node with the greatest key in the subtree that has x as root node.
     */
    public TreeNode maximumSubtree(TreeNode x) {
        TreeNode node = x;
        while (node.right != nil()) // iteration until the right child of the rightmost node reaches the sentinel
            node = node.right;
        return node; // rightmost node of the tree
    }

    /**
     * Insert newNode into the tree.
     */
    // Regel zum Einfuegen: Farbe des neuen Knoten auf rot setzen, dann Rot-Schwarz-Baum Bedingung wieder herstellen
    public void insert(TreeNode newNode) {

        // both children of the newNode have the sentinel (_nil)
        newNode.right = nil();
        newNode.left = nil();

        TreeNode x = this._root; // root set to x
        TreeNode px = this._nil; // px is set to the sentinel as nil

        while (x != nil()) { // iterate through the tree, until the correct position for the newNode is reached
            px = x; // set px as the parent node of iterator x
            if (x.key > newNode.key)
                x = x.left;
            else
                x = x.right;
        }
        newNode.p = px; // set parent of newNode to parent of iterator x (px)

        if (px == nil()) // if px is nil, the tree is empty.So the root of the new tree is newNode (given)
            this._root = newNode;
        else {
            if (px.key > newNode.key) // if key of px is greater than the key of newNode, make the newNode a left child
                px.left = newNode;
            else
                px.right = newNode; // else make the newNode a right child
        }

        newNode.color = NodeColor.RED; // newNode is always red!
        fixColorsAfterInsertion(newNode); // fix colors of the nodes of the modified tree (RedBlack-Tree-Rules to be checked)

    }

    // Regeln:
    // 1. Wurzel-Knoten ist immer schwarz.(sofern der Baum nicht leer ist)
    // 2. Wenn ein Knoten rot ist, sind seine Kinder schwarz.(Nicht-Rot-Rot-Regel)
    // 3. Knoten mit nur einem Kind ist immer schwarz.
    // 4. Rote Knoten haben genau 0 oder 2 Kinder.
    // Aus Regel 3 und 4 folgt, dass es fuer jeden Pfad im Teilbaum zu einem Blatt oder Halbblatt
    // die gleiche Anzahl von schwarzen Knoten hat.

    // Moegliche Verletzungen:
    // Parent Knoten ist rot
    // Nach jeder Rotation koennen die Rot-Schwarz Regeln verletzt werden

    // Idee:

    private void fixColorsAfterInsertion(TreeNode z) {
        // z is the newNode and is red
        while (z.p.color == NodeColor.RED) { // While parent of the given node(z) is red; redNode -> redNode (Verletzung der 2.Regel)
            if (z.p == z.p.p.left) { // Check, if the parent node of z is a left or a right child, for the specific rotation
                // (left-then-right or right-then-left)
                TreeNode y = z.p.p.right; // y is the sibling of the parent node of z (can be nil())
                if (y != nil() && y.color == NodeColor.RED) {
                    z.p.color = NodeColor.BLACK; // set parent of z black
                    y.color = NodeColor.BLACK; // set the sibling of parent of z black
                    z.p.p.color = NodeColor.RED; // set grand parent to red
                    z = z.p.p; // z is shifted upwards to it's own grand parent -> start new Iteration with new position of z
                } else { // case: if the sibling of the parent node of z is either black or nil
                    if (z == z.p.right) { // if z is the right child of it's parent -> rotate left
                        z = z.p; // shift z to the position of it's parent
                        rotateLeft(z); // and rotate left
                    }
                    z.p.color = NodeColor.BLACK; // set parent node of z to black
                    z.p.p.color = NodeColor.RED; // set grand parent of z to red
                    rotateRight(z.p.p); // then right rotation
                }
            } else {
                // Case: same algorithm as above but the right and left positions are swapped
                TreeNode y = z.p.p.left;
                if (y != nil() && y.color == NodeColor.RED) {
                    z.p.color = NodeColor.BLACK;
                    y.color = NodeColor.BLACK;
                    z.p.p.color = NodeColor.RED;
                    z = z.p.p;
                } else {
                    if (z == z.p.left) {
                        z = z.p;
                        rotateRight(z); // right rotation
                    }
                    z.p.color = NodeColor.BLACK;
                    z.p.p.color = NodeColor.RED;
                    rotateLeft(z.p.p); // then left rotation
                }
            }
        }

        this._root.color = NodeColor.BLACK; // Root of the tree set to black
    }

    private void rotateLeft(TreeNode x) {
        // Struktur vor der Rotation:
        //		x
        //	A		y
        //		B		C

        // Struktur nach der Rotation:
        //			y
        //		x		C
        //	A		B

        // Haenge linken Teilbaum(B) von y an, an das rechte Kind von x
        TreeNode y = x.right; // Definiere den rechten Knoten von x als y
        x.right = y.left; // Setze den linken Teilbaum von y auf den rechten Teilbaum von x

        if (y.left != nil()) // wenn der linke Teilbaum nicht leer ist, setze zudem den Parent-Knoten neu auf x
            y.left.p = x;

        // Haenge y an den Eltern-Knoten von x
        y.p = x.p; // set the parent of y to the parent of x
        if (x.p == this._nil) // Pruefe ob x die Wurzel von dem Baum war
            this._root = y; // wenn ja, dann ist y die neue Wurzel des Baumes
        else { // ansonsten ueberpruefe, ob x ein linkes oder rechtes Kind von dem anderen Knoten
            // war und haenge entsprechend y an den richtigen Zeiger (rechtes oder linkes Kind) von x.p (Eltern-Knoten von x)
            if (x == x.p.left)
                x.p.left = y;
            else
                x.p.right = y;
        }

        // Haenge x an linkes Kind von y
        y.left = x;
        x.p = y; // Eltern Knoten vom x ist jetzt y
    }

    private void rotateRight(TreeNode x) {
        // Struktur vor der Rotation:
        //			x
        //		y		C
        //	A		B

        // Struktur nach der Rotation:
        //		y
        //	A		x
        //		B		C

        // Case: same algorithm as rotateLeft but the right and left positions are swapped
        TreeNode y = x.left;
        x.left = y.right; // Setze den rechten Teilbaum von y auf den linken Teilbaum von x
        if (y.right != nil())
            y.right.p = x; //  Setze den parent vom rechten Teilbaum von y auf x

        y.p = x.p;
        if (x.p == this._nil)
            this._root = y;
        else {
            if (x == x.p.right)
                x.p.right = y;
            else
                x.p.left = y;
        }

        y.right = x;
        x.p = y;
    }

    /**
     * Delete node toDelete from the tree.
     */
    public void delete(TreeNode z) {
        if (this.search(z.key) == nil())
            return; // if the node to be deleted is not found in the tree, the process breaks here. (Do nothing and return)

        // if the node to be deleted is found:
        TreeNode x;
        TreeNode y = z;

        NodeColor deleteColor = y.color; // save the color of the node to be deleted
        // case: if the color of the node to be deleted is BLACK: fixUpDelete is needed

        if (z.left == nil()) { // wenn das linke Kind vom z null ist
            x = z.right; // the right child of z stored in x for the fixUpDelete operation
            transplant(z, z.right); // setze das rechte Kind von z an den Eltern-Knoten von z
        } else {
            if (z.right == nil()) { // wenn das rechte Kind vom z null ist
                x = z.left;  // the right child of z stored in x for the fixUpDelete operation
                transplant(z, z.left); // setze das linke Kind von z an den Eltern-Knoten von z
            } else {
                y = minimumSubtree(z.right); // store the smallest key to y from the right subtree of z (z is the root of the subtree)

                x = y.right; // setze das rechte Kind von y an den x
                deleteColor = y.color; // color of the node to be deleted

                if (y.p == z) // pruefe ob z der Eltern-Knoten von y ist
                    x.p = y; // wenn ja, definiere den Eltern-Knoten vom x als y fuer die fixUpDelete Operation
                else {
                    transplant(y, y.right); // Setze das rechte Kind von y an die Stelle von y selbst
                    y.right = z.right; // tausche den rechten Kind-Knoten von y und z miteinander
                    y.right.p = y; // aendere den Eltern vom rechten Kind zu y
                }
                transplant(z, y); // Haenge y an den Eltern Knoten von z
                y.left = z.left; // Haenge y an die Position von z
                y.left.p = y; // setze den Eltern vom linken Kind von y auf y
                y.color = z.color; // andere die Farben
            }
        }
        if (deleteColor == NodeColor.BLACK)
            fixUpDelete(x); // Do color FixUpDelete, if the deleted Node is black

    }

    // Haengt den Teilbaum von v an den Eltern-Knoten von u
    // u is the node to remove and v is the node to be transplanted at the place of u
    private void transplant(TreeNode u, TreeNode v) {

        if (u.p == nil()) // wenn u die Wurzel ist, setze die Wurzel des Baums auf v
            _root = v;
        else {
            if (u == u.p.left) // wenn u ein linkes Kind ist, setzte v als das neue linke Kind
                u.p.left = v;
            else // ansonsten soll v ein neue rechte Kind sein
                u.p.right = v;
        }

        v.p = u.p; // the parent of v should be now the parent of u
    }

    // Rule: When the node to be deleted is BLACK, colors of the nodes in the tree must be fixed up and then the node is deleted
    // In every node: difference between the number of black nodes in left subtree and in right subtree must be 0
    // When a black node is deleted, this requirement of Red-Black-Tree is not fulfilled
    private void fixUpDelete(TreeNode x) {

        while (x != _root && x.color == NodeColor.BLACK) {
            if (x == x.p.left) { // if x is a left child
                TreeNode w = x.p.right; // then store the sibling of x in w
                if (w.color == NodeColor.RED) { // the color of the sibling of x differs (black-red)
                    w.color = NodeColor.BLACK; // change the color of the sibling to black
                    x.p.color = NodeColor.RED; // the parent of two black nodes must be red
                    rotateLeft(x.p);
                    w = x.p.right; // after the left rotation, w is shifted upwards to new parent, which is
                    // the old sibling of x
                }
                // case: if the both children of the sibling of x are black
                if (w.left.color == NodeColor.BLACK && w.right.color == NodeColor.BLACK) {
                    w.color = NodeColor.RED; // if both child of w are black, w must be red (Nicht-Rot-Rot-Regel)
                    x = x.p; // x is set to the node of it's parent. x is deleted
                    continue; // start again from the while loop
                }
                // case: if the right child of the sibling of x is black
                if (w.right.color == NodeColor.BLACK) {
                    w.left.color = NodeColor.BLACK; // the left child must also be black
                    w.color = NodeColor.RED; // and w must be red
                    rotateRight(w);
                    w = x.p.right; // w is shifted to new parent, which is the old sibling of x
                }
                // case: if the right child of the sibling of x is red
                if (w.right.color == NodeColor.RED) {
                    w.color = x.p.color; // color of w is changed to the color of parent of x
                    x.p.color = NodeColor.BLACK; // parent color of x is set to black
                    w.right.color = NodeColor.BLACK; // and the right child of the sibling of x is black
                    rotateLeft(x.p);
                    x = _root; // after the left rotation x should be the root and is black
                }
            } else { // if x is a right child
                // same algorithm as above, but right and left are swapped
                TreeNode w = x.p.left;
                if (w.color == NodeColor.RED) {
                    w.color = NodeColor.BLACK;
                    x.p.color = NodeColor.RED;
                    rotateRight(x.p);
                    w = x.p.left;
                }
                if (w.right.color == NodeColor.BLACK && w.left.color == NodeColor.BLACK) {
                    w.color = NodeColor.RED;
                    x = x.p;
                    continue;
                } else if (w.left.color == NodeColor.BLACK) {
                    w.right.color = NodeColor.BLACK;
                    w.color = NodeColor.RED;
                    rotateLeft(w);
                    w = x.p.left;
                }
                if (w.left.color == NodeColor.RED) {
                    w.color = x.p.color;
                    x.p.color = NodeColor.BLACK;
                    w.left.color = NodeColor.BLACK;
                    rotateRight(x.p);
                    x = _root;
                }
            }
        }

        // case: when x is the root or the color of x is red
        x.color = NodeColor.BLACK; // change the color to black
    }

}