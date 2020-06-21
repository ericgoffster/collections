package collections.twothree.list;

final class NodeState<E> {
    final NodeState<E> parent;
    final Node23<E> n;
    final int which;
    public NodeState(Node23<E> n) {
        this(null, n, 0);
    }
    private NodeState(NodeState<E> parent, int which) {
        super();
        this.parent = parent;
        this.n = parent.n.getBranch(which);
        this.which = which;
    }
    private NodeState(NodeState<E> parent, Node23<E> n, int which) {
        super();
        this.parent = parent;
        this.n = n;
        this.which = which;
    }
    NodeState<E> next() {
        if (parent == null) {
            return null;
        }
        if (which == parent.n.numBranches() - 1) {
            return parent.next();
        }
        return new NodeState<>(parent, which + 1).leftMost();
    }
    NodeState<E> prev() {
        if (parent == null) {
            return null;
        }
        if (which == 0) {
            return parent.prev();
        }
        return new NodeState<>(parent, which - 1).rightMost();
    }
    
    NodeState<E> leftMost() {
        if (n.isLeaf()) {
            return this;
        }
        return new NodeState<E>(this, 0).leftMost();
    }
    NodeState<E> rightMost() {
        if (n.isLeaf()) {
            return this;
        }
        return new NodeState<E>(this, n.numBranches() - 1).rightMost();
    }
}