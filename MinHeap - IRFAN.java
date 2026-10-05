import java.util.PriorityQueue;

public class MinHeap
{
    private PriorityQueue<Integer> heap =
        new PriorityQueue<>();

    public void insert(int value)
    {
        heap.add(value);
    }

    public int extractMin()
    {
        return heap.poll();
    }
    
    public static void main(String[] args)
    {
        MinHeap heap = new MinHeap();

        heap.insert(10);
        heap.insert(3);
        heap.insert(15);

        System.out.println(
        "Min-Heap Extract Minimum Priority Value: "
        + heap.extractMin()
        );
    }
}