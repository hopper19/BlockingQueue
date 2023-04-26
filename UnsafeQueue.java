import java.util.LinkedList;
import java.util.Queue;


/* This queue is not threadsafe  */
public class UnsafeQueue implements BlockingQueue{

  private Queue<Runnable> queue;

  UnsafeQueue() {
    queue = new LinkedList<Runnable>();
  }

  @Override
  public void enqueue(Runnable item) {
    queue.add(item);
  }

  @Override
  public Runnable dequeue() {
    return queue.remove();
  }

  @Override
  public int size() {
    return queue.size();
  }
  
}
