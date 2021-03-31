package dstructure;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BPlusTree {
    private Node root;
    private final int maxKeys;

    public BPlusTree(int maxKeys) {
        this.maxKeys = maxKeys;
        this.root = new Node(maxKeys);
    }

    public Node searchNode(Integer key) {
        return searchNode(key, this.root);
    }

    public Node searchNode(Integer key, Node node) {
        if (node.isLeaf()) return node;

        ArrayList<Integer> keys = node.getKeys();
        ArrayList<Node> children = node.getChildren();

        if (key <= keys.get(0)) {
            return searchNode(key, children.get(0));
        }

        int size = keys.size();
        for (int i = 1; i < size; i++) {
            if (key >= keys.get(i) && key <= keys.get(i + 1)) {
                return searchNode(key, children.get(i));
            }
        }
        return searchNode(key, children.get(size - 1));
    }

    public boolean hasKey(Integer key) {
        return getAllKeys().stream().allMatch(it -> it.equals(key));
    }

    public Node findFirstLeaf() {
        return findFirstLeaf(root);
    }

    public Node findFirstLeaf(Node node) {
        if (node.isLeaf()) return node;
        return findFirstLeaf(node.getChildren().get(0));
    }

    public LinkedList<Node> getLeafs() {
        LinkedList<Node> leafs = new LinkedList<>();
        Node node = findFirstLeaf();
        leafs.add(node);
        while (node.hasNext()) {
            node = node.getNext();
            leafs.add(node);
        }
        return leafs;
    }

    public List<Integer> getAllKeys() {
        return getLeafs().stream()
                .flatMap(it -> it.getKeys().stream())
                .collect(Collectors.toList());
    }

    public String toLeafsString() {
        return toLeafsString(", ");
    }

    public String toLeafsString(String delimiter) {
        return getAllKeys().stream().map(Object::toString).collect(Collectors.joining(delimiter));
    }

    public void add(Integer value) {
        Node found = searchNode(value);
        if (found.addKey(value)) return;
        this.splitAndInsert(found, value);
    }

    private void createNewRoot(Node left, Node right) {
        Node newRoot = new Node(this.maxKeys);
        newRoot.addChildren(left);
        newRoot.addChildren(right);
        newRoot.addKey(right.getKeys().get(0));
        this.root = newRoot;
    }

    private void addChildrenToNode(Node target, Node nodeToAdd) {
        if (target.addChildren(nodeToAdd) &&
            target.addKey(nodeToAdd.getKeys().get(0))
        ) {
            return;
        }
        Node newNode = target.split();
        if (target.isRoot()) {
            this.createNewRoot(target, newNode);
            return;
        }
        this.addChildrenToNode(target.getParent(), newNode);
    }

    private void splitRoot() {

    }

    private void splitAndInsert(Node node, int value) {
        Node rightNode = node.split();
        rightNode.addKey(value);
        if (!node.isRoot()) {
            this.addChildrenToNode(node.getParent(), rightNode);
        } else {
            this.createNewRoot(node, rightNode);
        }
    }

}

class Node {
    private ArrayList<Integer> keys;
    private ArrayList<Node> children;
    private Node parent;
    private Node next;
    private final int maxKeys;

    public Node(int maxKeys) {
        this.maxKeys = maxKeys;
        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public boolean isLeaf() {
        return this.children.isEmpty();
    }

    public boolean isRoot() {
        return this.parent == null;
    }

    public boolean canAddChildren() {
        return this.children.size() < this.maxKeys + 1;
    }

    public boolean canAddKey() {
        return this.keys.size() < this.maxKeys;
    }

    public boolean addKey(Integer key) {
        if (!canAddKey()) return false;
        return this.keys.add(key);
    }

    public boolean addChildren(Node node) {
        if (!canAddChildren()) return false;
        node.setParent(this);
        if (children.size() > 0) {
            children.get(children.size() - 1).setNext(node);
        }
        return this.children.add(node);
    }



//    public Node addKey(Integer key) {
////        if (!canAddKey()) return this.parent;
////        return this.keys.add(key);
//
//        if (canAddKey()) {
//            this.keys.add(key);
//            return this;
//        }
//
//        Node newNode = new Node(this.maxKeys);
//        int size = this.keys.size();
//        int halfSize = (int) Math.ceil(size / 2D);
//        for (int i = 0; i < halfSize; i++) {
//            newNode.keys.add(this.keys.removeLast());
//        }
//        return this.parent.addKey(this.keys.getFirst());
//
//    }

    public void moveKeysToNode(Node node, int amount) {
        while (keys.size() > amount) {
            node.getKeys().add(keys.remove(keys.size() - 1));
        }
    }

    public void moveChildrenToNode(Node node, int amount) {
        while (children.size() > amount) {
            node.getChildren().add(children.remove(children.size() - 1));
        }
    }

    public Node split() {
        Node node = new Node(this.maxKeys);
        this.moveKeysToNode(node, Math.floorDiv(this.keys.size(), 2));
        this.moveChildrenToNode(node, Math.floorDiv(this.children.size(), 2));
        return node;
    }

    public ArrayList<Integer> getKeys() {
        return keys;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public Node getParent() {
        return parent;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getNext() {
        return next;
    }

    public boolean hasNext() {
        return next != null;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}