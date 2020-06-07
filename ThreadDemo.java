public class ThreadDemo {
    // 多线程:
    // 创建线程需要使用 Thread 类, 来创建一个 Thread 的实例
    // 快捷方式: tt -> table
    // 方法一: 显式继承 Thread 类, 重写其中的 run 方法
    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("线程");
        }
    }

    public static void main1(String[] args) {
        // 当 Thread 对象被创建出来时, 内核中并没有随之产生一个线程(PCB);
        Thread t = new MyThread();
        // 执行 start 方法时, 才真正创建出一个线程.
        // 这个 PCB 会对应让 CPU 来执行该线程的代码(run 方法)
        t.start();
        // 运行程序, 输出: "线程"

        // 这个代码涉及两个线程:
        // 1. main 方法对应的主线程
        // 2. MyThread 创建出的线程
        // 要查看该进程中的线程情况, JDK 内置了 jdk1.8 -> bin -> jconsole 程序 (程序运行时才能差到线程)
    }
}

class ThreadDemo2 {
    private static long count = 10_0000_0000L;
    // 较大整数中插入下划线, 提高代码的可读性

    public static void main2(String[] args) {
        serial();
        // 串行: 针对两个整数累计相加 100亿次大概需要 5.5s.
        //concurrency();
        // 并发: 针对两个整数累计相加 100亿次大概需要 3s.

        // 线程数量多之后, 效率是会提高, 但不一定会成倍提高
        // 线程的创建和销毁需要时间, 线程的调度也需要时间
    }

    private static void serial() {
        long begin = System.currentTimeMillis();
        // System.currentTimeMillis(); 获取当前时间戳(毫秒级)

        int a = 0;
        for (long i = 0; i < count; i++) {
            a++;
        }
        int b = 0;
        for (long i = 0; i < count; i++) {
            b++;
        }

        long end = System.currentTimeMillis();
        System.out.println("time: " + (end - begin) + " ms");
    }

    private static void concurrency() {
        long begin = System.currentTimeMillis();

        Thread t1 = new Thread() {
            @Override
            public void run() {
                int a = 0;
                for (long i = 0; i < count; i++) {
                    a++;
                }
            }
        };

        // 方法二: 通过匿名内部类的方式继承 Thread
        Thread t2 = new Thread() {
            @Override
            public void run() {
                int b = 0;
                for (long i = 0; i < count; i++) {
                    b++;
                }
            }
        };
        t1.start();
        t2.start();

        try {
            // 线程等待, 让 主线程(main) 等待 t1 t2 执行结束, 再继续往下执行.
            t1.join();
            t2.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        // t1 t2 和 main 线程之间都是并发执行的
        // 需保证 t1 t2 都计算完毕, 再来计算 end 的时间戳
        long end = System.currentTimeMillis();
        System.out.println("time: " + (end - begin) + " ms");
    }

    // 方法三: 显式创建一个类, 实现 Runnable 接口, 然后把这个 Runnable 的实例关联到 Thread 实例上
    // Runnable 本质上就是描述了一段要执行的任务代码
    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("一个线程");
        }
    }

    public static void main3(String[] args) {
        Thread t = new Thread(new MyRunnable());
        t.start();
    }

    public static void main4(String[] args) {
        // 方法四: 通过匿名内部类实现 Runnable 接口
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("新线程");
            }
        };
        Thread t = new Thread(new MyRunnable());
        t.start();
    }

    public static void main(String[] args) {
        // 方法五: 使用 lamba 表达式来指定
        Thread t = new Thread(() -> {
            System.out.println("新线程");
        });
        t.start();
    }
}