import java.util.*;

public class EmergencyAmbulanceOptimization {

    // ============================================
    // Insertion Sort
    // ============================================
    public static String insertionSort(int[] arr) {
        if (arr == null) {
            return "Error: array is null";
        }
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
        return Arrays.toString(arr);
    }

    // ============================================
    // Binary Search
    // ============================================
    public static String binarySearch(int[] arr, int target) {
        if (arr == null || arr.length == 0) {
            return "Error: array is null or empty";
        }
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) {
                return "Error: array must be sorted";
            }
        }
        int low = 0, high = arr.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] == target) {
                return String.valueOf(mid);
            } else if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return "Not found";
    }
}
