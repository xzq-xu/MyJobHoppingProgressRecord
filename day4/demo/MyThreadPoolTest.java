package day4.demo;

public class MyThreadPoolTest {
    public static void main(String[] args) {
        MyThreadPool0 myThreadPool  = new MyThreadPool0();

        myThreadPool.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println("main thread end");

    }
}

