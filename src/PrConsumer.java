import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PrConsumer {


    public static void main(String[] args) throws InterruptedException {
        WaitAndNotify wn = new WaitAndNotify();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wn.produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wn.consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}

class WaitAndNotify{
    private Queue<Integer> queue = new LinkedList<>();
    private final int LIMIT = 10;
    private final Object lock = new Object();

    public void produce() throws InterruptedException {
        while(true){
            synchronized (lock){
                while (queue.size() == LIMIT){
                    lock.wait();
                }
                queue.add(new Random().nextInt(100));
                lock.notify();
            }
        }
    }
    public void consume() throws InterruptedException {

        while (true) {

            synchronized (lock){
                while (queue.size() == 0){
                    lock.wait();
                }
                System.out.println(queue.poll());
                System.out.println("Queue size is " + queue.size());
                lock.notify(); // notify должен вызываться в конце блока
            }
            Thread.sleep(1000);

        }
    }
}