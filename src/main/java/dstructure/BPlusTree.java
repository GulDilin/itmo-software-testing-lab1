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

    public Node findNode(Integer key) {
        return findNode(key, this.root);
    }

    public Node findNode(Integer key, Node node) {
        if (node.isLeaf()) return node;

        ArrayList<Integer> keys = node.getKeys();
        ArrayList<Node> children = node.getChildren();

        if (key <= keys.get(0)) {
            return findNode(key, children.get(0));
        }

        int size = keys.size();
        for (int i = 1; i < size; i++) {
            if (key >= keys.get(i) && key <= keys.get(i + 1)) {
                return findNode(key, children.get(i));
            }
        }
        return findNode(key, children.get(size - 1));
    }

    public boolean hasKey(Integer key) {
        return getAllKeys().stream().anyMatch(it -> it.equals(key));
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

    public String toLeafString() {
        return toLeafString("; ");
    }

    public String toLeafString(String delimiter) {
        return getAllKeys().stream().map(Object::toString).collect(Collectors.joining(delimiter));
    }

    public String toNodeString() {
        return toNodeString(root, "; ", "[", "]");
    }

    public String toNodeString(String delimiter, String prefix, String postfix) {
        return toNodeString(root, delimiter, prefix, postfix);
    }

    public String toNodeString(Node node, String delimiter, String prefix, String postfix) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);

        String keysString = node.getKeys().stream()
                .map(Object::toString)
                .collect(Collectors.joining(delimiter));
        stringBuilder.append(keysString);

        if (!node.isLeaf()) {
            stringBuilder.append(";");
            String childrenString = node.getChildren().stream()
                    .map(it -> toNodeString(it, delimiter, prefix, postfix))
                    .collect(Collectors.joining(delimiter));
            stringBuilder.append(childrenString);
        }
        stringBuilder.append(postfix);
        return stringBuilder.toString();
    }

    public void add(Integer value) {
        Node found = findNode(value);
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
    private final ArrayList<Integer> keys;
    private final ArrayList<Node> children;
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
