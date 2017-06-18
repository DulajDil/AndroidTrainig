package ru.stqa.threads;


public class ThreadsAndroid {

    public ThreadsAndroid() {
    }

    int sharedResource;

    public void startTwothreads() {
        Thread t1 = new Thread(() -> {
            synchronized (this) {
                sharedResource++;
                System.out.println("Первый поток значение " + sharedResource);
            }

        });
        t1.start();
        Thread t2 = new Thread(() -> {
            synchronized (this) {
                sharedResource--;
                System.out.println("Второй поток значение " + sharedResource);

            }
        });
        t2.start();
    }

    public int getSharedResource() {
        return sharedResource;
    }
}
