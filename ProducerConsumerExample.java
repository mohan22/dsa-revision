import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProducerConsumerExample {

    private static final int PRODUCER_COUNT = 3;
    private static final int JOBS_PER_PRODUCER = 4;

    static class Job {
        private final int producerId;
        private final int jobId;
        private final String payload;
        private final CompletableFuture<String> responseFuture;

        Job(int producerId, int jobId, String payload, CompletableFuture<String> responseFuture) {
            this.producerId = producerId;
            this.jobId = jobId;
            this.payload = payload;
            this.responseFuture = responseFuture;
        }
    }

    static class Producer implements Runnable {
        private final int producerId;
        private final int jobsToCreate;
        private final Queue<Job> queue;
        private final CountDownLatch producersDoneSignal;

        Producer(int producerId, int jobsToCreate, Queue<Job> queue, CountDownLatch producersDoneSignal) {
            this.producerId = producerId;
            this.jobsToCreate = jobsToCreate;
            this.queue = queue;
            this.producersDoneSignal = producersDoneSignal;
        }

        @Override
        public void run() {
            try {
                for (int jobNumber = 1; jobNumber <= jobsToCreate; jobNumber++) {
                    String payload = "payload-from-producer-" + producerId + "-job-" + jobNumber;
                    CompletableFuture<String> responseFuture = new CompletableFuture<>();
                    Job job = new Job(producerId, jobNumber, payload, responseFuture);

                    queue.offer(job);
                    System.out.println("Producer " + producerId + " submitted job " + jobNumber
                            + " with payload: " + payload);

                    String ack = responseFuture.join();
                    System.out.println("Producer " + producerId + " received response: " + ack);
                }
            } finally {
                producersDoneSignal.countDown();
            }
        }
    }

    static class Consumer implements Runnable {
        private final Queue<Job> queue;
        private final List<String> datastore;
        private final CountDownLatch producersDoneSignal;
        private final AtomicBoolean running;

        Consumer(Queue<Job> queue, List<String> datastore, CountDownLatch producersDoneSignal,
                 AtomicBoolean running) {
            this.queue = queue;
            this.datastore = datastore;
            this.producersDoneSignal = producersDoneSignal;
            this.running = running;
        }

        @Override
        public void run() {
            try {
                while (running.get()) {
                    Job job = queue.poll();

                    if (job != null) {
                        datastore.add(job.payload);
                        String ack = "Stored job " + job.jobId + " from producer " + job.producerId
                                + ". Datastore size=" + datastore.size();
                        job.responseFuture.complete(ack);
                        continue;
                    }

                    if (producersDoneSignal.getCount() == 0 && queue.isEmpty()) {
                        running.set(false);
                        break;
                    }

                    Thread.sleep(25);
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Queue<Job> jobQueue = new ConcurrentLinkedQueue<>();
        List<String> datastore = new CopyOnWriteArrayList<>();
        CountDownLatch producersDoneSignal = new CountDownLatch(PRODUCER_COUNT);
        AtomicBoolean consumerRunning = new AtomicBoolean(true);

        Thread consumerThread = new Thread(
                new Consumer(jobQueue, datastore, producersDoneSignal, consumerRunning),
                "single-consumer"
        );

        consumerThread.start();

        ExecutorService producerPool = Executors.newFixedThreadPool(PRODUCER_COUNT);
        for (int producerId = 1; producerId <= PRODUCER_COUNT; producerId++) {
            producerPool.submit(new Producer(producerId, JOBS_PER_PRODUCER, jobQueue, producersDoneSignal));
        }

        producerPool.shutdown();
        if (!producerPool.awaitTermination(10, TimeUnit.SECONDS)) {
            producerPool.shutdownNow();
        }

        consumerThread.join();

        int expectedJobs = PRODUCER_COUNT * JOBS_PER_PRODUCER;
        System.out.println();
        System.out.println("All work completed.");
        System.out.println("Expected jobs: " + expectedJobs);
        System.out.println("Actual datastore size: " + datastore.size());
        System.out.println("Datastore contents:");
        for (String entry : datastore) {
            System.out.println(entry);
        }
    }
}
