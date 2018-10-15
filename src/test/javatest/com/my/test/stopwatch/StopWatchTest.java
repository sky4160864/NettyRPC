package com.my.test.stopwatch;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Huang Jianhai on 2018/6/25.
 */
public class StopWatchTest {

    @Test
    public void tes() throws InterruptedException {
        //并行度10
        int parallel = 10;
        StopWatch sw = new StopWatch();
        CountDownLatch signal = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(parallel);
        //sw.start(); err
        for (int i = 0; i < parallel; i++) {
            new Thread(new Sub(i,signal,finish)).start();
        }
        TimeUnit.MILLISECONDS.sleep(30);
        signal.countDown();
        sw.start();
        finish.await();
        sw.stop();
        System.out.println("耗时"+sw.getTime());
    }

    class Sub implements Runnable {
        private CountDownLatch signal;
        private CountDownLatch finish;
        private int id ;
        public Sub(int id,CountDownLatch signal,CountDownLatch finish){
            this.id = id;
            this.signal = signal;
            this.finish = finish;
        }
        @Override
        public void run() {
            try {
                signal.await();
                int timeout = new Random().nextInt(2000);
                System.out.println(Thread.currentThread().getName() +"-"+id+"-sleep:"+timeout+"  :"+get(id));
                //TimeUnit.MILLISECONDS.sleep(timeout);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                finish.countDown();
            }

        }
    }

    public static int get(int id){
        if(id==4){
            try {
                TimeUnit.SECONDS.sleep(id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return id*10;
    }

}
