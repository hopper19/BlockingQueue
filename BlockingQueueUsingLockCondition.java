import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.*;

public class BlockingQueueUsingLockCondition implements BlockingQueue {
  private Queue<Runnable> queue;
  private int capacity;
  private Lock lock = new ReentrantLock();
  private Condition queueFull = lock.newCondition();
  private Condition queueEmpty = lock.newCondition();
  
  /*
    * The constructor of the class accepts a parameter to specify the capacity of
    * the queue.
    */
  public BlockingQueueUsingLockCondition(int capacity) {
    this.capacity = capacity;
    queue = new LinkedList<Runnable>();
  }

  @Override
  public void enqueue(Runnable item) {
    lock.lock();
    try {
      while (queue.size() == capacity) {
        try {
          queueFull.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      queue.add(item);
      queueEmpty.signal();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public Runnable dequeue() {
    lock.lock();
    try {
      while (queue.isEmpty()) {
        try {
          queueEmpty.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      Runnable item = queue.remove();
      queueFull.signal();
      return item;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int size() {
    return queue.size();
  }
}
