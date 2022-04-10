package lab;

import java.util.Comparator;


import frame.SSSPNode;

public class SSSPNodeComparator implements Comparator<SSSPNode> {

    public int compare(SSSPNode nodeA, SSSPNode nodeB) {
        if (nodeA.distance == null && nodeB.distance != null)
            return 1;
        if (nodeA.distance != null && nodeB.distance == null)
            return -1;
        if (nodeA.distance == null && nodeB.distance == null)
            return 0;

        return nodeA.distance.compareTo(nodeB.distance);
    }

}