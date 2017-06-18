package ru.stqa.threads;


public class RunThreads {

    public static void main(String[] args) {
        ThreadsAndroid threadsAndroid = new ThreadsAndroid();
        threadsAndroid.startTwothreads();
        System.out.println(threadsAndroid.getSharedResource());
    }
}
