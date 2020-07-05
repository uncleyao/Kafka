package testthread;

import com.sun.tools.jdi.InternalEventHandler;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class TestThread {
    static int sum = 0;

    //多线程例子
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1;i<10;i++) {
                    sum+=i;
                }
                System.out.println("10 sum =  "+ sum);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1;i<100;i++) {
                    sum+=i;
                }
                System.out.println("100 sum =  "+ sum);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1;i<1000;i++) {
                    sum+=i;
                }
                System.out.println("1000 sum =  "+ sum);
            }
        }).start();
        System.out.println("main sum: " + sum);*/


    FutureTask<Integer> ft1 = new FutureTask<Integer>(new MyCallable10());
    FutureTask<Integer> ft2 = new FutureTask<Integer>(new MyCallable100());
    FutureTask<Integer> ft3 = new FutureTask<Integer>(new MyCallable1000());

    Thread t1 = new Thread(ft1);
    Thread t2 = new Thread(ft2);
    Thread t3 = new Thread(ft3);

    t1.start();
    t2.start();
    t3.start();

    //只有调用get，就能阻塞线程【future方法】
    System.out.println("main sum: " + (ft1.get() + ft2.get() + ft3.get()));
    }
}

class MyCallable10 implements Callable<Integer>{
    // call可以返回结果，run不返回
    public Integer call() throws Exception {
        int sum = 0;
        for (int i=1;i<=10;i++){
            sum+=i;
        }
        return sum;
    }
}

class MyCallable100 implements Callable<Integer>{
    // call可以返回结果，run不返回
    public Integer call() throws Exception {
        int sum = 0;
        for (int i=1;i<=100;i++){
            sum+=i;
        }
        return sum;
    }
}

class MyCallable1000 implements Callable<Integer>{
    // call可以返回结果，run不返回
    public Integer call() throws Exception {
        int sum = 0;
        for (int i=1;i<=1000;i++){
            sum+=i;
        }
        return sum;
    }
}