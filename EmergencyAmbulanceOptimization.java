import java.util.*;

public class EmergencyAmbulanceOptimization
{
    
    // Travel Cost Matrix
    
    static int[][] costMatrix = {
        {0, 15, 25, 35},
        {15, 0, 30, 28},
        {25, 30, 0, 20},
        {35, 28, 20, 0}
    };

    
    // Location Names
    
    static String[] locations = {
        "Hospital",
        "Emergency Location B",
        "Emergency Location C",
        "Emergency Location D"
    };


    
    // Cost Matrix Validation
    
    private static void validateCostMatrix(int[][] dist)
    {
        if (dist == null || dist.length == 0)
        {
            throw new IllegalArgumentException(
                "Invalid cost matrix."
            );
        }

        int n = dist.length;

        for (int i = 0; i < n; i++)
        {
            if (dist[i] == null || dist[i].length != n)
            {
                throw new IllegalArgumentException(
                    "Cost matrix must be square."
                );
            }
        }
    }


    
    // 1. Greedy Route Optimization
    
    public static String greedyEAROP(int[][] dist)
    {
        validateCostMatrix(dist);

        int n = dist.length;
        boolean[] visited = new boolean[n];
        StringBuilder path = new StringBuilder();

        int current = 0;
        int totalCost = 0;

        visited[current] = true;
        path.append(locations[current]);

        // Choose the cheapest unvisited location
        for (int count = 1; count < n; count++)
        {
            int next = -1;
            int minCost = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++)
            {
                if (!visited[i] &&
                    dist[current][i] < minCost)
                {
                    minCost = dist[current][i];
                    next = i;
                }
            }

            if (next == -1)
            {
                throw new IllegalStateException(
                    "No valid next location was found."
                );
            }

            visited[next] = true;
            totalCost += minCost;

            path.append(" -> ");
            path.append(locations[next]);

            current = next;
        }

        // Return to Hospital
        totalCost += dist[current][0];

        path.append(" -> ");
        path.append(locations[0]);

        return "Greedy Ambulance Route: "
            + path
            + " | Total Cost: "
            + totalCost;
    }


    
    // 2. Dynamic Programming Route Optimization
    
    public static String dynamicProgrammingEAROP(int[][] dist)
    {
        int n = dist.length;
        int VISITED_ALL = (1 << n) - 1;

        int[][] memo = new int[n][1 << n];
        String[][] paths = new String[n][1 << n];

        for (int i = 0; i < n; i++)
        {
            Arrays.fill(memo[i], -1);
        }

        int minCost =
            dynamicProgrammingEAROPHelper(
                0,
                1,
                dist,
                memo,
                VISITED_ALL,
                paths
            );

        String route = "Hospital";

        int pos = 0;
        int mask = 1;

        while (mask != VISITED_ALL)
        {
            int nextLocation = -1;
            int bestCost = Integer.MAX_VALUE;

            for (int next = 1; next < n; next++)
            {
                if ((mask & (1 << next)) == 0)
                {
                    int newCost =
                        dist[pos][next]
                        + dynamicProgrammingEAROPHelper(
                            next,
                            mask | (1 << next),
                            dist,
                            memo,
                            VISITED_ALL,
                            paths
                        );

                    if (newCost < bestCost)
                    {
                        bestCost = newCost;
                        nextLocation = next;
                    }
                }
            }

            route += " -> " + locations[nextLocation];
            pos = nextLocation;
            mask = mask | (1 << nextLocation);
        }

        route += " -> Hospital";

        return "Dynamic Programming Ambulance Route: "
            + route
            + " | Total Cost: "
            + minCost;
    }


    
    // Dynamic Programming Helper Method
    
    private static int dynamicProgrammingEAROPHelper(
        int pos,
        int mask,
        int[][] dist,
        int[][] memo,
        int VISITED_ALL,
        String[][] paths)
    {
        // All locations have been visited
        if (mask == VISITED_ALL)
        {
            return dist[pos][0];
        }

        // Return previously calculated result
        if (memo[pos][mask] != -1)
        {
            return memo[pos][mask];
        }

        int minimumCost = Integer.MAX_VALUE;

        for (int next = 1; next < dist.length; next++)
        {
            if ((mask & (1 << next)) == 0)
            {
                int newCost =
                    dist[pos][next]
                    + dynamicProgrammingEAROPHelper(
                        next,
                        mask | (1 << next),
                        dist,
                        memo,
                        VISITED_ALL,
                        paths
                    );

                minimumCost =
                    Math.min(minimumCost, newCost);
            }
        }

        memo[pos][mask] = minimumCost;

        return minimumCost;
    }


    
    // 3. Backtracking Route Optimization
    
    static int bestBacktrackingCost;
    static String bestBacktrackingPath;

    public static String backtrackingEAROP(int[][] dist)
    {
        int n = dist.length;
        boolean[] visited = new boolean[n];

        // Start from the Hospital
        visited[0] = true;

        bestBacktrackingCost = Integer.MAX_VALUE;
        bestBacktrackingPath = "";

        StringBuilder path =
            new StringBuilder("Hospital");

        earopBacktracking(
            0,
            dist,
            visited,
            n,
            1,
            0,
            path
        );

        return "Backtracking Ambulance Route: "
            + bestBacktrackingPath
            + " | Total Cost: "
            + bestBacktrackingCost;
    }


    
    // Backtracking Helper Method
    
    private static int earopBacktracking(
        int pos,
        int[][] dist,
        boolean[] visited,
        int n,
        int count,
        int cost,
        StringBuilder path)
    {
        // All locations been visited
        if (count == n)
        {
            int totalCost =
                cost + dist[pos][0];

            if (totalCost < bestBacktrackingCost)
            {
                bestBacktrackingCost = totalCost;

                bestBacktrackingPath =
                    path.toString()
                    + " -> Hospital";
            }

            return totalCost;
        }

        // Try unvisited location
        for (int next = 1; next < n; next++)
        {
            if (!visited[next])
            {
                visited[next] = true;

                int oldLength = path.length();

                path.append(" -> ")
                    .append(locations[next]);

                earopBacktracking(
                    next,
                    dist,
                    visited,
                    n,
                    count + 1,
                    cost + dist[pos][next],
                    path
                );

                // Backtrack
                path.setLength(oldLength);
                visited[next] = false;
            }
        }

        return bestBacktrackingCost;
    }


   
    // 4. Divide and Conquer Route Optimization
    
    static int bestDivideCost;
    static String bestDividePath;

    public static String divideAndConquerEAROP(
        int[][] dist)
    {
        // Check if the matrix exists
        if (dist == null || dist.length == 0)
        {
            return "Invalid cost matrix.";
        }

        int n = dist.length;

        // Check if the matrix is square
        for (int i = 0; i < n; i++)
        {
            if (dist[i] == null ||
                dist[i].length != n)
            {
                return "Invalid cost matrix.";
            }
        }

        bestDivideCost = Integer.MAX_VALUE;
        bestDividePath = "";

        boolean[] visited = new boolean[n];

        // Hospital is the starting location
        visited[0] = true;

        StringBuilder path =
            new StringBuilder();

        path.append(locations[0]);

        divideAndConquerHelper(
            0,
            visited,
            0,
            dist,
            n,
            path
        );

        return "Divide & Conquer Route: "
            + bestDividePath
            + " | Total Cost: "
            + bestDivideCost;
    }


    
    // Divide and Conquer Helper Method
    
    private static int divideAndConquerHelper(
        int pos,
        boolean[] visited,
        int currentCost,
        int[][] dist,
        int n,
        StringBuilder path)
    {
        if (allVisited(visited))
        {
            int totalCost =
                currentCost + dist[pos][0];

            String completePath =
                path.toString()
                + " -> "
                + locations[0];

            if (totalCost < bestDivideCost)
            {
                bestDivideCost = totalCost;
                bestDividePath = completePath;
            }

            return totalCost;
        }

        int minCost = Integer.MAX_VALUE;

        for (int next = 0; next < n; next++)
        {
            if (!visited[next])
            {
                visited[next] = true;

                int newCost =
                    currentCost
                    + dist[pos][next];

                int oldLength =
                    path.length();

                path.append(" -> ");
                path.append(locations[next]);

                int routeCost =
                    divideAndConquerHelper(
                        next,
                        visited,
                        newCost,
                        dist,
                        n,
                        path
                    );

                if (routeCost < minCost)
                {
                    minCost = routeCost;
                }

                path.setLength(oldLength);
                visited[next] = false;
            }
        }

        return minCost;
    }


    
    // Check All Locations Are Visited
    
    private static boolean allVisited(
        boolean[] visited)
    {
        for (boolean locationVisited : visited)
        {
            if (!locationVisited)
            {
                return false;
            }
        }

        return true;
    }


    
    // 5. Insertion Sort
    
    public static String insertionSort(int[] arr)
    {
        for (int i = 1; i < arr.length; i++)
        {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key)
            {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }

        return Arrays.toString(arr);
    }


    
    // 6. Binary Search
    
    public static String binarySearch(
        int[] arr,
        int target)
    {
        if (arr == null || arr.length == 0)
        {
            return "Error: array is null or empty";
        }

        // Binary Search requires a sorted array
        for (int i = 1; i < arr.length; i++)
        {
            if (arr[i - 1] > arr[i])
            {
                return "Error: array must be sorted";
            }
        }

        int low = 0;
        int high = arr.length - 1;

        while (low <= high)
        {
            int mid =
                low + (high - low) / 2;

            if (arr[mid] == target)
            {
                return String.valueOf(mid);
            }
            else if (arr[mid] < target)
            {
                low = mid + 1;
            }
            else
            {
                high = mid - 1;
            }
        }

        return "Not found";
    }


   
    // 7. Min-Heap
    
    static class MinHeap
    {
        private PriorityQueue<Integer> heap =
            new PriorityQueue<>();

        public void insert(int value)
        {
            if (value < 0)
            {
                throw new IllegalArgumentException(
                    "Priority value cannot be negative"
                );
            }

            heap.add(value);
        }

        public int extractMin()
        {
            if (heap.isEmpty())
            {
                throw new NoSuchElementException(
                    "Heap is empty"
                );
            }

            return heap.poll();
        }

        public int peekMin()
        {
            if (heap.isEmpty())
            {
                throw new NoSuchElementException(
                    "Heap is empty"
                );
            }

            return heap.peek();
        }
    }


    
    // 8. Splay Tree
    
    static class SplayTree
    {
        private class Node
        {
            int key;
            Node left;
            Node right;

            Node(int key)
            {
                this.key = key;
            }
        }

        private Node root;


        private Node rotateRight(Node x)
        {
            Node y = x.left;

            x.left = y.right;
            y.right = x;

            return y;
        }


        private Node rotateLeft(Node x)
        {
            Node y = x.right;

            x.right = y.left;
            y.left = x;

            return y;
        }


        private Node splay(Node node, int key)
        {
            if (node == null ||
                node.key == key)
            {
                return node;
            }

            if (key < node.key)
            {
                if (node.left == null)
                {
                    return node;
                }

                if (key < node.left.key)
                {
                    node.left.left =
                        splay(
                            node.left.left,
                            key
                        );

                    node =
                        rotateRight(node);
                }
                else if (key > node.left.key)
                {
                    node.left.right =
                        splay(
                            node.left.right,
                            key
                        );

                    if (node.left.right != null)
                    {
                        node.left =
                            rotateLeft(
                                node.left
                            );
                    }
                }

                return (node.left == null)
                    ? node
                    : rotateRight(node);
            }
            else
            {
                if (node.right == null)
                {
                    return node;
                }

                if (key > node.right.key)
                {
                    node.right.right =
                        splay(
                            node.right.right,
                            key
                        );

                    node =
                        rotateLeft(node);
                }
                else if (key < node.right.key)
                {
                    node.right.left =
                        splay(
                            node.right.left,
                            key
                        );

                    if (node.right.left != null)
                    {
                        node.right =
                            rotateRight(
                                node.right
                            );
                    }
                }

                return (node.right == null)
                    ? node
                    : rotateLeft(node);
            }
        }


        public void insert(int value)
        {
            if (root == null)
            {
                root = new Node(value);
                return;
            }

            root = splay(root, value);

            if (root.key == value)
            {
                return;
            }

            Node n = new Node(value);

            if (value < root.key)
            {
                n.right = root;
                n.left = root.left;

                root.left = null;
            }
            else
            {
                n.left = root;
                n.right = root.right;

                root.right = null;
            }

            root = n;
        }


        public boolean search(int value)
        {
            if (root == null)
            {
                return false;
            }

            root = splay(root, value);

            return root.key == value;
        }


        public int getRootValue()
        {
            if (root == null)
            {
                throw new NoSuchElementException(
                    "Tree is empty"
                );
            }

            return root.key;
        }
    }


   
    // 9. Main / Driver Method
    
    public static void main(String[] args)
    {
        
        // Route Optimization Tests
        
        System.out.println(
            greedyEAROP(costMatrix)
        );

        System.out.println(
            dynamicProgrammingEAROP(costMatrix)
        );

        System.out.println(
            backtrackingEAROP(costMatrix)
        );

        System.out.println(
            divideAndConquerEAROP(costMatrix)
        );


        
        // Sorting and Searching Tests
        
        int[] arr = {8, 3, 5, 1, 9, 2};

        insertionSort(arr);

        System.out.println(
            "Sorted Emergency Response Times: "
            + Arrays.toString(arr)
        );

        System.out.println(
            "Binary Search for 5: Index "
            + binarySearch(arr, 5)
        );


        // Min-Heap Test
        
        MinHeap heap = new MinHeap();

        heap.insert(10);
        heap.insert(3);
        heap.insert(15);

        System.out.println(
            "Min-Heap Extract Minimum Priority Value: "
            + heap.extractMin()
        );


        // Splay Tree Test
        
        SplayTree tree = new SplayTree();

        tree.insert(20);
        tree.insert(10);
        tree.insert(30);

        System.out.println(
            "Splay Tree Search "
            + "(Emergency Case 10 found): "
            + tree.search(10)
        );
    }
}