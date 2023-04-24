import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Consumer implements Runnable {

  private final BlockingQueue queue;
  private final Queue<Runnable> tasksConsumed;
  private final Random random = new Random();
  private int numItemsConsumed;

  public Consumer(BlockingQueue queue) {
    this.queue = queue;
    this.tasksConsumed = new LinkedList<Runnable>();
    this.numItemsConsumed = 0;
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Runnable item = queue.dequeue();
        // System.out.println("Dequeued task.");
        // item.run();
        tasksConsumed.add(item);
        numItemsConsumed++;
        Thread.sleep(random.nextInt(1000));
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
    System.out.println("Number of Tasks Consumed: " + numItemsConsumed);
    System.out.println("Tasks consumed in order: ");
    for (Runnable item : tasksConsumed) {
      item.run();
    }
  }
}