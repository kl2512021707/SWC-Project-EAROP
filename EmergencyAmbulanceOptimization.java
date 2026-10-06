import java.util.*;

public class EmergencyAmbulanceOptimization {

    // ============================================
    // Min-Heap
    // ============================================
    static class MinHeap {
        private PriorityQueue<Integer> heap = new PriorityQueue<>();

        public void insert(int value) {
            if (value < 0) {
                throw new IllegalArgumentException("Priority value cannot be negative");
            }
            heap.add(value);
        }

        public int extractMin() {
            if (heap.isEmpty()) {
                throw new NoSuchElementException("Heap is empty");
            }
            return heap.poll();
        }

        public int peekMin() {
            if (heap.isEmpty()) {
                throw new NoSuchElementException("Heap is empty");
            }
            return heap.peek();
        }
    }

    // ============================================
    // Splay Tree
    // ============================================
    static class SplayTree {
        private class Node {
            int key;
            Node left, right;
            Node(int key) { this.key = key; }
        }

        private Node root;

        private Node rotateRight(Node x) {
            Node y = x.left;
            x.left = y.right;
            y.right = x;
            return y;
        }

        private Node rotateLeft(Node x) {
            Node y = x.right;
            x.right = y.left;
            y.left = x;
            return y;
        }

        private Node splay(Node node, int key) {
            if (node == null || node.key == key) return node;
            if (key < node.key) {
                if (node.left == null) return node;
                if (key < node.left.key) {
                    node.left.left = splay(node.left.left, key);
                    node = rotateRight(node);
                } else if (key > node.left.key) {
                    node.left.right = splay(node.left.right, key);
                    if (node.left.right != null) node.left = rotateLeft(node.left);
                }
                return (node.left == null) ? node : rotateRight(node);
            } else {
                if (node.right == null) return node;
                if (key > node.right.key) {
                    node.right.right = splay(node.right.right, key);
                    node = rotateLeft(node);
                } else if (key < node.right.key) {
                    node.right.left = splay(node.right.left, key);
                    if (node.right.left != null) node.right = rotateRight(node.right);
                }
                return (node.right == null) ? node : rotateLeft(node);
            }
        }

        public void insert(int value) {
            if (root == null) {
                root = new Node(value);
                return;
            }
            root = splay(root, value);
            if (root.key == value) return;
            Node n = new Node(value);
            if (value < root.key) {
                n.right = root;
                n.left = root.left;
                root.left = null;
            } else {
                n.left = root;
                n.right = root.right;
                root.right = null;
            }
            root = n;
        }

        public boolean search(int value) {
            if (root == null) return false;
            root = splay(root, value);
            return root.key == value;
        }

        public int getRootValue() {
            if (root == null) throw new NoSuchElementException("Tree is empty");
            return root.key;
        }
    }

    // ============================================
    // Driver Method (Min-Heap and Splay Tree only)
    // ============================================
    public static void main(String[] args) {
        // Min-Heap Test
        MinHeap heap = new MinHeap();
        heap.insert(10);
        heap.insert(3);
        heap.insert(15);
        System.out.println("Min-Heap Extract Minimum Priority Value: " + heap.extractMin());

        // Splay Tree Test
        SplayTree tree = new SplayTree();
        tree.insert(20);
        tree.insert(10);
        tree.insert(30);
        System.out.println("Splay Tree Search (Emergency Case 10 found): " + tree.search(10));

        // Extra tests for screenshots
        System.out.println("Min-Heap next minimum (peek): " + heap.peekMin());
        System.out.println("Root after search(10): " + tree.getRootValue());
        tree.search(30);
        System.out.println("Root after search(30): " + tree.getRootValue());
        System.out.println("Search 99: " + tree.search(99));
    }
}