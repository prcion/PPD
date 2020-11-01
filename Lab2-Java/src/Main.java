import file.ReadFromFile;

import java.io.FileNotFoundException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static AtomicInteger at = new AtomicInteger(0);

    public static void main(String[] args) throws FileNotFoundException {
        int p = 3;
        p = Integer.parseInt(args[0]);
        String fileName = "date.txt";
        String fileNameKernel = "date_kernel.txt";
//        WriteToFile writeToFile = new WriteToFile(fileName, 10000000, 10, 10000);
//        writeToFile.writeToFileRandomNumbers();

//        WriteToFile writeToFile = new WriteToFile(fileNameKernel, 100, 0, 10);
//        writeToFile.writeToFileRandomNumbers();

        int N = 10000, M = 10;
        ReadFromFile readFromFile = new ReadFromFile();
        int[][] matrix = readFromFile.readFromFile(fileName, N, M);

        int n = 5, m = 5;
        int[][] matrixKernel = readFromFile.readFromFile(fileNameKernel, n, m);

        long startTime = System.nanoTime();
        threadDistributie(N, M, matrix, matrixKernel, p);
        long endTime = System.nanoTime();

        System.out.println((double)(endTime - startTime) / 1E6);
    }

    static class ThreadDistributie extends Thread {
        private int[][] matrix, kernel;
        private int start, end, columns, nrThread;
        private int[] array;
        private CyclicBarrier barrier;

        public ThreadDistributie(int[][] matrix, int[][] kernel, int start, int end, int columns, int nrThread, CyclicBarrier barrier) {
            this.matrix = matrix;
            this.kernel = kernel;
            this.start = start;
            this.end = end;
            this.columns = columns;
            this.array = new int[start + end];
            this.nrThread = nrThread;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            int p = 0;
            for (int k = start; k < end; ++k) {
                int i = k / columns;
                int j = k % columns;
                this.array[p] = Calculate.calculateFilterKernel(kernel, matrix, i, j);
                p ++;
            }
//            at.incrementAndGet();

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
                return;
            }

            p = 0;
            for (int k = start; k < end; ++k) {
                int i = k / columns;
                int j = k % columns;
                this.matrix[i][j] = array[p];
                p ++;
            }

        }

    }


    public static void threadDistributie(int lines, int columns, int[][] matrix, int[][] matrixKernel, int nrThreads) {
        int n = lines * columns;
        int size = n / nrThreads;
        int rest = n % nrThreads;
        int start = 0, end;

        ThreadDistributie[] threads = new ThreadDistributie[nrThreads];
        CyclicBarrier barrier = new CyclicBarrier(nrThreads);
        for (int i = 0; i < nrThreads; ++i) {
            end = start + size;
            if (rest > 0) {
                end++;
                rest--;
            }

            ThreadDistributie threadDistributie = new ThreadDistributie(matrix, matrixKernel, start, end, columns, nrThreads, barrier);
            threads[i] = threadDistributie;
            threads[i].start();

            start = end;
        }
        for (int i = 0; i < nrThreads; ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
