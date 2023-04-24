import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Producer implements Runnable {
  private final BlockingQueue queue;
  private final Queue<Integer> tasksProduced;
  private final Random random = new Random();
  private int numItemsProduced;

  public Producer(BlockingQueue queue) {
    this.queue = queue;
    this.tasksProduced = new LinkedList<Integer>();
    this.numItemsProduced = 0;
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        int num = random.nextInt(100);
        queue.enqueue(() -> System.out.println("Produced item: " + num));
        tasksProduced.add(num);
        // System.out.println("Enqueued task: " + num);
        numItemsProduced++;
        Thread.sleep(random.nextInt(1000));
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
    System.out.println("Number of Tasks Produced: " + numItemsProduced);
    System.out.println("Number of Tasks in Queue: " + queue.size());
    System.out.println("Tasks produced in order: " + tasksProduced.toString());    
  }
}
