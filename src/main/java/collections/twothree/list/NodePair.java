package collections.twothree.list;

// Represents a pair of branches (perhaps degenerate with only 1 branch)
final class NodePair<E> {
    final Node23<E> b1;
    final Node23<E> b2;
    public NodePair(Node23<E> b1, Node23<E> b2) {
        super();
        this.b1 = b1;
        this.b2 = b2;
    }
    public NodePair(Node23<E> b1) {
        super();
        this.b1 = b1;
        this.b2 = null;
    }
    public Node23<E> b1() {
        return b1;
    }
    public Node23<E> b2() {
        return b2;
    }
    public NodePair<E> reverse() {
        if (b2 == null) {
            return new NodePair<E>(b1.reverse());
        }
        return new NodePair<E>(b2.reverse(), b1.reverse());
    }
}