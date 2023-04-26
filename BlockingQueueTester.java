public class BlockingQueueTester {
  static int NUM_THREADS = 10;
  static int NUM_RUNNABLES = 10;
  static int QUEUE_CAP = 5;

  public static void main(String[] args) throws InterruptedException {

    final BlockingQueue lockQueue = new BlockingQueueUsingLockCondition(QUEUE_CAP);
    final BlockingQueue syncQueue = new BlockingQueueUsingSynchronized(QUEUE_CAP);
    final UnsafeQueue unsafeQueue = new UnsafeQueue();

    System.out.println(NUM_THREADS + " threads will be created.");
    System.out.println("Each thread will enqueue and dequeue " + NUM_RUNNABLES + " Runnables.");
    System.out.println(
        "If each queue is thread-safe, then the number of Runnables in the queue should be 0 at the end of the test.");
    System.out.println("Test results for lockQueue:");
    ThreadSafeTest(lockQueue);
    System.out.println("Test results for syncQueue:");
    ThreadSafeTest(syncQueue);
    System.out.println("Test results for unsafeQueue:");
    ThreadSafeTest(unsafeQueue);

    System.out.println("\nBlocking when empty test for lockQueue: ");
    BlockingWhenEmptyTest(lockQueue);
    System.out.println("Blocking when empty test for syncQueue: ");
    BlockingWhenEmptyTest(syncQueue);

    System.out.println("\nBlocking when full test for lockQueue: ");
    BlockingWhenFullTest(lockQueue);
    System.out.println("Blocking when full test for syncQueue: ");
    BlockingWhenFullTest(syncQueue);
  }

  public static void ThreadSafeTest(BlockingQueue queue) throws InterruptedException {
    Thread[] threads = new Thread[NUM_THREADS];
    for (int i = 0; i < NUM_THREADS; i++) {
      threads[i] = new Thread(() -> {
        for (int j = 0; j < NUM_RUNNABLES; j++) {
          queue.enqueue(() -> System.out.println("Hello World"));
          queue.dequeue();
        }
      });
    }

    for (int i = 0; i < NUM_THREADS; i++) {
      threads[i].start();
    }

    for (int i = 0; i < NUM_THREADS; i++) {
      threads[i].join();
    }

    System.out.println("Thread-safe test: " + (queue.size() == 0) + " with size: " + queue.size());
  }

  public static void BlockingWhenFullTest(BlockingQueue queue) throws InterruptedException {
    for (int i = 0; i < QUEUE_CAP; i++) {
      queue.enqueue(() -> System.out.println("Runnable"));
    }
    System.out.println("Queue is FULL");

    Thread producer = new Thread(() -> {
      queue.enqueue(() -> System.out.println("Runnable 6"));
      System.out.println("Enqueued Runnable at " + System.currentTimeMillis() / 1000);
    });
    Thread consumer = new Thread(() -> {
      queue.dequeue();
    });

    producer.start();
    System.out.println("Started producer at " + System.currentTimeMillis() / 1000);
    System.out.println("Main sleeping for 1 seconds before starting consumer");
    Thread.sleep(1000);
    consumer.start();
    System.out.println("Started consumer at " + System.currentTimeMillis() / 1000);

    producer.join();
    consumer.join();

    System.out.println("Final queue size: " + queue.size());
    System.out.println(
        "Test is successful if producer also waits for 1 second before enqueuing another runnable, even though it is already running.");
  }

  public static void BlockingWhenEmptyTest(BlockingQueue queue) throws InterruptedException {
    Thread consumer = new Thread(() -> {
      queue.dequeue();
      System.out.println("Dequeued Runnable at " + System.currentTimeMillis() / 1000);
    });
    Thread producer = new Thread(() -> {
      queue.enqueue(() -> System.out.println("Runnable"));
    });

    consumer.start();
    System.out.println("Started consumer at " + System.currentTimeMillis() / 1000);
    System.out.println("Main sleeping for 1 seconds before starting producer");
    Thread.sleep(1000);
    producer.start();
    System.out.println("Started producer at " + System.currentTimeMillis() / 1000);

    producer.join();
    consumer.join();

    System.out.println("Final queue size: " + queue.size());
    System.out.println(
        "Test is successful if consumer also waits for 1 second before dequeuing another runnable, even though it is already running.");
  }
}
