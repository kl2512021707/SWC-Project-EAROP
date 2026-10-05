public class SplayTree
{
    // Node of the Splay Tree
    private class Node
    {
        int value;
        Node left;
        Node right;

        Node(int value)
        {
            this.value = value;
        }
    }

    private Node root;
     // Right rotation
    private Node rotateRight(Node node)
        {
        Node newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;

        return newRoot;
    }

     // Left rotation
        private Node rotateLeft(Node node)
    {
    Node newRoot = node.right;
    node.right = newRoot.left;
    newRoot.left = node;

    return newRoot;
    }
        // Splay operation
    private Node splay(Node root, int value)
    {
        // Tree is empty or value is already at the root
        if (root == null || root.value == value)
        {
            return root;
        }

        // Value is on the left side
        if (value < root.value)
        {
        if (root.left == null)
        {
            return root;
        }

        // Zig-Zig
        if (value < root.left.value)
        {
            root.left.left = splay(root.left.left, value);
            root = rotateRight(root);
        }

        // Zig-Zag
        else if (value > root.left.value)
        {
            root.left.right = splay(root.left.right, value);

            if (root.left.right != null)
            {
                root.left = rotateLeft(root.left);
            }
        }

        if (root.left == null)
        {
            return root;
        }

        return rotateRight(root);
        }

        // Value is on the right side
        else
        {
        if (root.right == null)
        {
            return root;
        }

        // Zig-Zig
        if (value > root.right.value)
        {
            root.right.right = splay(root.right.right, value);
            root = rotateLeft(root);
        }

        // Zig-Zag
        else if (value < root.right.value)
        {
            root.right.left = splay(root.right.left, value);

            if (root.right.left != null)
            {
                root.right = rotateRight(root.right);
            }
        }

        if (root.right == null)
        {
            return root;
        }

        return rotateLeft(root);
        }
    }
         // Insert a value
    public void insert(int value)
    {
        if (root == null)
        {
            root = new Node(value);
            return;
        }

        root = splay(root, value);

        // Do not insert duplicate values
        if (root.value == value)
        {
            return;
        }

        Node newNode = new Node(value);

        if (value < root.value)
        {
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        }
        else
        {
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }

        root = newNode;
    }

    // Search for a value
    public boolean search(int value)
    {
        root = splay(root, value);

        return root != null && root.value == value;
    }
    
     public static void main(String[] args)
    {
        SplayTree tree = new SplayTree();

        tree.insert(20);
        tree.insert(10);
        tree.insert(30);

        System.out.println(
        "Splay Tree Search (Emergency Case 10 found): "
        + tree.search(10)
        );
    }    
}