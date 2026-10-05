import java.util.Arrays;

public class EmergencyAmbulanceOptimization
{
    // Travel Cost Matrix
    static int[][] costMatrix = {
    {0, 15, 25, 35},
    {15, 0, 30, 28},
    {25, 30, 0, 20},
    {35, 28, 20, 0}
    };

    // Location names
    static String[] locations = {
    "Hospital",
    "Emergency Location B",
    "Emergency Location C",
    "Emergency Location D"
    };
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

       // ============================================
      // Backtracking Route Optimization
      // ============================================

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

        StringBuilder path = new StringBuilder("Hospital");

        earopBacktracking(0, dist, visited, n, 1, 0, path);

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
        // All locations have been visited
        if (count == n)
        {
        int totalCost = cost + dist[pos][0];

        if (totalCost < bestBacktrackingCost)
        {
            bestBacktrackingCost = totalCost;
            bestBacktrackingPath = path.toString()
                    + " -> Hospital";
        }

        return totalCost;
        }

        // Try every unvisited location
        for (int next = 1; next < n; next++)
        {
            if (!visited[next])
            {
            visited[next] = true;

            int oldLength = path.length();

            path.append(" -> ").append(locations[next]);

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
    // ============================================
    // Dynamic Programming Route Optimization
    // ============================================

        public static String dynamicProgrammingEAROP(int[][] dist)
        {
            int n = dist.length;
            int VISITED_ALL = (1 << n) - 1;

            int[][] memo = new int[n][1 << n];
            String[][] paths = new String[n][1 << n];

            // Fill memo with -1 to show states not calculated yet
            for (int i = 0; i < n; i++)
            {
                Arrays.fill(memo[i], -1);
            }

            int minCost = dynamicProgrammingEAROPHelper(0,1,dist,memo,VISITED_ALL,paths);

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
                    int newCost = dist[pos][next]
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
            // Check if location has not been visited
            if ((mask & (1 << next)) == 0)
            {
                int newCost = dist[pos][next]
                + dynamicProgrammingEAROPHelper(
                    next,
                    mask | (1 << next),
                    dist,
                    memo,
                    VISITED_ALL,
                    paths
                    );

                minimumCost = Math.min(minimumCost, newCost);
            }
        }

        memo[pos][mask] = minimumCost;

        return minimumCost;
    }

    public static void main(String[] args)
    {
        int[] arr = {8, 3, 5, 1, 9, 2};

        insertionSort(arr);

        System.out.println(
            "Sorted Emergency Response Times: "
            + Arrays.toString(arr)
        );
        System.out.println(backtrackingEAROP(costMatrix));
        System.out.println(dynamicProgrammingEAROP(costMatrix));
    }
}