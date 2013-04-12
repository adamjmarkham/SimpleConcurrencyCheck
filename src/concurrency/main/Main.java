package concurrency.main;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import concurrency.data.SharedObject;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		final SharedObject obj = new SharedObject(0);
		final Object synchObj = new Object();

		final int NUM_THREADS = 1000;
		final int POOL_SIZE = 5;

		ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
		final CountDownLatch cdl = new CountDownLatch(NUM_THREADS);

		for (int i = 0; i < NUM_THREADS; i++) {
			pool.submit(new Runnable() {
				@Override
				public void run() {
					cdl.countDown();
					try {
						cdl.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					synchronized (synchObj) {
						int val = obj.getValue();
						val++;
						obj.setValue(val);

						System.out.println(obj.getValue());
					}
				}
			});
		}
	}

}
