package dstructure;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BPlusTree {
    private Node root;
    private final int maxKeys;
    private final boolean saveHistory;
    private List<String> history;

    public BPlusTree(int maxKeys) {
        this(maxKeys, false);
    }

    public BPlusTree(int maxKeys, boolean saveHistory) {
        this.maxKeys = maxKeys;
        this.root = new Node(maxKeys);
        this.saveHistory = saveHistory;
        if (saveHistory) {
            history = new LinkedList<>();
        }
    }

    public Node findNode(Integer key) {
        return findNode(key, this.root);
    }

    public Node findNode(Integer key, Node node) {
        if (node.isLeaf()) return node;

        ArrayList<Integer> keys = node.getKeys();
        ArrayList<Node> children = node.getChildren();

        int size = keys.size();
        for (int i = 0; i < size; i++) {
            if (key <= keys.get(i)) {
                return findNode(key, children.get(i));
            }
        }
        return findNode(key, children.get(size));
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
            stringBuilder.append(delimiter);
            String childrenString = node.getChildren().stream()
                    .map(it -> toNodeString(it, delimiter, prefix, postfix))
                    .collect(Collectors.joining(delimiter));
            stringBuilder.append(childrenString);
        }
        stringBuilder.append(postfix);
        return stringBuilder.toString();
    }

    public String toHistoryString() {
        if (!saveHistory) throw new IllegalStateException("History saving is disabled");
        if (history.size() == 0) return "";
        return String.join("\n", history);
    }

    public void add(Integer value) {
        Node found = findNode(value);
        if (found.addKey(value)) {
            this.saveHistoryPoint();
            return;
        }
        this.splitAndInsert(found, value);
        this.saveHistoryPoint();
    }

    private void createNewRoot(Node left, Node right) {
        Node newRoot = new Node(this.maxKeys);
        newRoot.addKey(right.getKeys().get(0));
        newRoot.addChildren(left);
        newRoot.addChildren(right);
        this.root = newRoot;
    }

    private void addChildrenToNode(Node target, Node nodeToAdd) {
        if (
                target.addKey(nodeToAdd.getKeys().get(0)) &&
                target.addChildren(nodeToAdd)
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

    private void saveHistoryPoint() {
        if (!saveHistory) return;
        history.add(toNodeString());
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
        for (int i = 0; i < keys.size(); i++) {
            if (key <= keys.get(i)) {
                keys.add(i, key);
                return true;
            }
        }
        return this.keys.add(key);
    }

    public boolean addChildren(Node node) {
        if (!canAddChildren()) return false;
        node.setParent(this);
        if (children.size() == 0) {
            return this.children.add(node);
        }

        int key = node.keys.get(0);
        if (key < keys.get(0)) {
            node.setNext(children.get(0));
            children.add(0, node);
            return true;
        }
        int i;
        for (i = 1; i < keys.size(); i++) {
            if (key <= keys.get(i)) {
                children.get(i - 1).setNext(node);
                node.setNext(children.get(i));
                keys.add(i, key);
                return true;
            }
        }
        children.get(i - 1).setNext(node);
        return this.children.add(node);
    }

    public void moveLastKeysToNode(Node node, int amount) {
        for (int i = 0; i < amount && keys.size() > 0; i++) {
            node.getKeys().add(keys.remove(keys.size() - 1));
        }
    }

    public void moveLastChildrenToNode(Node node, int amount) {
        for (int i = 0; i < amount && children.size() > 0; i++) {
            node.getChildren().add(children.remove(children.size() - 1));
        }
    }

    public Node split() {
        Node node = new Node(maxKeys);
        this.moveLastKeysToNode(node, Math.floorDiv(keys.size(), 2));
        this.moveLastChildrenToNode(node, Math.floorDiv(children.size(), 2));
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
