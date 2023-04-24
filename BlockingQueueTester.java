public class BlockingQueueTester {
  public static void main(String[] args) throws InterruptedException {

    final BlockingQueue lockQueue = new BlockingQueueUsingLockCondition(5);
    final BlockingQueue syncQueue = new BlockingQueueUsingSynchronized(5);

    System.out.println("Starting BlockingQueueUsingLockCondition test.");
    BlockingQueueTest(lockQueue);
    System.out.println("\nStarting BlockingQueueUsingSynchronized test.");
    BlockingQueueTest(syncQueue);
  }

  public static void BlockingQueueTest(BlockingQueue queue) throws InterruptedException {
    Producer producer = new Producer(queue);
    Consumer consumer = new Consumer(queue);

    Thread t1 = new Thread(producer);
    Thread t2 = new Thread(consumer);

    t1.start();
    t2.start();

    Thread.sleep(10000);

    t1.interrupt();
    t2.interrupt();

    t1.join();
    t2.join();
  }
}