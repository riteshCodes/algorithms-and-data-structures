package lab;


import frame.ListNode;
import frame.TableEntry;

public class LinkedList {

    private ListNode _head;
    private ListNode _nil;

    public LinkedList() {
        _nil = new ListNode(null, null, null);
        _nil.setNext(_nil);
        _nil.setPrev(_nil);
        _head = _nil;
    }

    public ListNode head() {
        return _head;
    }

    public ListNode nil() {
        return _nil;
    }

    /**
     * Return the number of elements in the linked list.
     */
    public int length() {
        int counter = 0;
        ListNode pointer = this._head;
        while (pointer != _nil) { // iterate until the pointer reaches the sentinel (nil)
            pointer = pointer.next();
            counter++;
        }
        return counter;
    }

    /**
     * Insert an entry into the linked list at the position before the given node.
     */
    public void insertBefore(TableEntry entry, ListNode node) {
        ListNode beforeNode = node.prev();
        ListNode afterNode = node;

        // newNode with _prev as _prev of the given node and _next as the given node.
        ListNode newNode = new ListNode(entry, beforeNode, afterNode);
        node.setPrev(newNode); // set the pointer of the given node to newNode

        if (newNode.prev() != _nil) { // case: newNode has a preceding node
            newNode.prev().setNext(newNode); // set the _next pointer of preceding node to the newNode
        } else { // case: newNode has no preceding node
            _head = newNode; // newNode must be head
        }
    }

    /**
     * Append an entry to the end of the list.
     */
    public void append(TableEntry entry) {
        ListNode pointer = this._head;

        if (pointer == _nil) { // case: no nodes are present, and head must be initialised
            _head = new ListNode(entry, _nil, _nil); // preceding and succeeding nodes are the sentinels
            return;
        }

        // case: the list has nodes
        while (pointer.next() != _nil) pointer = pointer.next(); // iterate until the pointer reaches the last node

        ListNode newNode = new ListNode(entry, pointer, _nil); // _prev of newNode : last node of the list
        pointer.setNext(newNode);

    }

    /**
     * Delete the given node from the linked list.
     */
    public void delete(ListNode node) {

        if (node.prev() == _nil) _head = node.next(); // case: node to be deleted is head
        else {
            // pointers changed
            node.prev().setNext(node.next());
            node.next().setPrev(node.prev());
        }

    }
}
