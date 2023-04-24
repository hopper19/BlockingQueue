/*
 * The blocking queue must be thread-safe and must include at least three methods: enqueue, dequeue and size.
 * The enqueue method appends an item to the queue;
 * the dequeue removes and returns the first item;
 * the size method returns the number of items in the queue.
 * It is called blocking because enqueue and dequeue methods block the calling thread from execution when the queue is full and empty, respectively.
 */

interface BlockingQueue {
    void enqueue(Runnable item);
    Runnable dequeue();
    int size();
}