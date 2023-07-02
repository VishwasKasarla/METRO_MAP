import java.util.ArrayList;
import java.util.HashMap;

public class Heap<T extends Comparable<T>>
{
    ArrayList<T> data = new ArrayList<>();
    HashMap<T, Integer> map = new HashMap<>();


    // this function helps to add an item to our heap and calls to update(upheapify) the heap.
    public void add(T item)
    {
        data.add(item);
        map.put(item, this.data.size() - 1);
        up_heapify(data.size() - 1);
    }

    // this fuction helps to update(upheapify) the heap
    private void up_heapify(int ci)
    {
        int pi = (ci - 1) / 2;
        if (isLarger(data.get(ci), data.get(pi)) > 0)
        {
            swap(pi, ci);
            up_heapify(pi);
        }
    }

    // swaps two nodes.
    private void swap(int i, int j)
    {
        T ith = data.get(i);
        T jth = data.get(j);

        data.set(i, jth);
        data.set(j, ith);
        map.put(ith, j);
        map.put(jth, i);
    }

    // displays all the nodes(stations)
    public void display()
    {
        System.out.println(data);
    }

    // return size of the heap.
    public int size()
    {
        return this.data.size();
    }

    // checks if the heap is empty or not.
    public boolean isEmpty()
    {
        return this.size() == 0;
    }

    // removes and return the top node by swaping it by last node and then calls to update the heap(downheapify).
    public T remove()
    {
        swap(0, this.data.size() - 1);
        T rv = this.data.remove(this.data.size() - 1);
        downheapify(0);

        map.remove(rv);
        return rv;
    }

    // this fuction helps to update(downheapify) the heap.
    private void downheapify(int pi)
    {
        int lci = 2 * pi + 1;
        int rci = 2 * pi + 2;
        int mini = pi;

        if (lci < this.data.size() && isLarger(data.get(lci), data.get(mini)) > 0)
        {
            mini = lci;
        }

        if (rci < this.data.size() && isLarger(data.get(rci), data.get(mini)) > 0)
        {
            mini = rci;
        }

        if (mini != pi)
        {
            swap(mini, pi);
            downheapify(mini);
        }
    }

    // returns the top node.
    public T get()
    {
        return this.data.get(0);
    }

    // helps to compare two values.
    public int isLarger(T t, T o)
    {
        return t.compareTo(o);
    }

    // helps to reconstruct/update the heap.
    public void updatePriorityQueue(T pair)
    {
        int index = map.get(pair);
        up_heapify(index);
    }

}