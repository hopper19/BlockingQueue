import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueueUsingSynchronized implements BlockingQueue {
  private Queue<Runnable> queue;
  private int capacity;

  /*
   * The constructor of the class accepts a parameter to specify the capacity of
   * the queue.
   */
  public BlockingQueueUsingSynchronized(int capacity) {
    this.capacity = capacity;
    queue = new LinkedList<Runnable>();
  }

  @Override
  public synchronized void enqueue(Runnable item) {
    while (queue.size() == capacity) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    queue.add(item);
    notifyAll();
  }

  @Override
  public synchronized Runnable dequeue() {
    while (queue.isEmpty()) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    Runnable item = queue.remove();
    notifyAll();
    return item;
  }

  @Override
  public int size() {
    return queue.size();
  }
}
