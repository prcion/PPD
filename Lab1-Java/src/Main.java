import file.ReadFromFile;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static AtomicInteger at = new AtomicInteger(0);
    public static void main(String[] args) throws IOException {
        int p = 3;
        p = Integer.parseInt(args[0]);
        String fileName = "date.txt";
        String fileNameKernel = "date_kernel.txt";
//        WriteToFile writeToFile = new WriteToFile(fileName, 10000000, 10, 10000);
//        writeToFile.writeToFileRandomNumbers();

//        WriteToFile writeToFile = new WriteToFile(fileNameKernel, 100, 0, 10);
//        writeToFile.writeToFileRandomNumbers();

        int N = 10, M = 10000;
        ReadFromFile readFromFile = new ReadFromFile();
        int[][] matrix = readFromFile.readFromFile(fileName, N, M);

        int n = 5, m = 5;
        int[][] matrixKernel = readFromFile.readFromFile(fileNameKernel, n, m);

        long startTime = System.nanoTime();
//        secvential(N, M, matrix, matrixKernel);
        threadLineByLine(N, M, matrix, matrixKernel, p);
//        threadColumnByColumn(N, M, matrix, matrixKernel, p);
//        threadSubmatrix(N, M, matrix, matrixKernel, p);
//        threadDistributie(N, M, matrix, matrixKernel, p);
        long endTime = System.nanoTime();

        System.out.println((double)(endTime - startTime) / 1E6);
    }

    public static void secvential(int N, int M, int[][] matrix, int[][] matrixKernel) {
        int[][] finalMatrix = new int[N][M];
        Secvential secvential = new Secvential(matrix, matrixKernel, finalMatrix, N, M);
        secvential.filterMatrix();
//        printMatrix(finalMatrix, N, M);
    }

    public static void threadLineByLine(int lines, int M, int[][] matrix, int[][] matrixKernel, int nrThreads) {
        int[][] finalMatrix = new int[lines][M];

        int size = lines / nrThreads;
        int rest = lines % nrThreads;
        int start = 0, end;

        ThreadLineByLine[] threads = new ThreadLineByLine[nrThreads];

        for (int i = 0; i < nrThreads; ++i) {
            end = start + size;
            if (rest > 0) {
                end++;
                rest--;
            }
            ThreadLineByLine threadLineByLine = new ThreadLineByLine(matrix, matrixKernel, finalMatrix, start, end);
            threads[i] = threadLineByLine;
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

    public static void threadColumnByColumn(int N, int M, int[][] matrix, int[][] matrixKernel, int p) {
        int[][] finalMatrix = new int[N][M];
        int nrThreads = p;
        int size = M / nrThreads;
        int rest = M % nrThreads;
        int start = 0, end;

        ThreadColumnByColumn[] threads = new ThreadColumnByColumn[nrThreads];

        for (int i = 0; i < nrThreads; ++i) {
            end = start + size;
            if (rest > 0) {
                end++;
                rest--;
            }
            ThreadColumnByColumn threadColumnByColumn = new ThreadColumnByColumn(matrix, matrixKernel, finalMatrix, N, start, end);
            threads[i] = threadColumnByColumn;
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

    public static void threadSubmatrix(int lines, int columns, int[][] matrix, int[][] matrixKernel, int nrThreads) {
        int[][] finalMatrix = new int[lines][columns];

        int sizeLine = lines / nrThreads;
        int restLine = lines % nrThreads;
        int startLine = 0, endLine;

        int sizeColumn = columns / nrThreads;
        int restColumn = columns % nrThreads;
        int startColumn = 0, endColumn;


        ThreadSubmatrix[] threads = new ThreadSubmatrix[nrThreads];

        for (int i = 0; i < nrThreads; ++i) {
            endLine = startLine + sizeLine;
            if (restLine > 0) {
                endLine++;
                restLine--;
            }

            endColumn = startColumn + sizeColumn;
            if (restColumn > 0) {
                endColumn++;
                restColumn--;
            }

            ThreadSubmatrix threadColumnByColumn = new ThreadSubmatrix(matrix, matrixKernel, finalMatrix, startLine, endLine, startColumn, endColumn);
            threads[i] = threadColumnByColumn;
            threads[i].start();

            startLine = endLine;
            startColumn = endColumn;
        }

        for (int i = 0; i < nrThreads; ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        long endTime = System.currentTimeMillis();
//        double allTime = (double)(endTime - startTime);
//        System.out.println(allTime);
//        System.out.println(allTime / 1E6);
    }
    static class ThreadDistributie extends Thread {
        private int[][] matrix, kernel;
        private int start, end, columns;
        private int[] array;

        public ThreadDistributie(int[][] matrix, int[][] kernel, int start, int end, int columns) {
            this.matrix = matrix;
            this.kernel = kernel;
            this.start = start;
            this.end = end;
            this.columns = columns;
            this.array = new int[start + end];
        }

        @Override
        public void run() {
            int nr = 0;
            for (int k = start; k < end; ++k) {
                int i = k / columns;
                int j = k % columns;
                array[nr] = Calculate.calculateFilterKernel(kernel, matrix, i, j);
                nr ++;
            }
            at.getAndIncrement();
        }
        public void a() {
            int nr = 0;
            for (int k = start; k < end; ++k) {
                int i = k / columns;
                int j = k % columns;
                matrix[i][j] = array[nr];
                nr ++;
            }
        }
    }


    public static void threadDistributie(int lines, int columns, int[][] matrix, int[][] matrixKernel, int nrThreads) {
        int n = lines * columns;
        int size = n / nrThreads;
        int rest = n % nrThreads;
        int start = 0, end;

        ThreadDistributie[] threads = new ThreadDistributie[nrThreads];

        for (int i = 0; i < nrThreads; ++i) {
            end = start + size;
            if (rest > 0) {
                end++;
                rest--;
            }

            ThreadDistributie threadDistributie = new ThreadDistributie(matrix, matrixKernel, start, end, columns);
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

        if (at.get() == nrThreads) {
            for (int i = 0; i < nrThreads; ++i) {
                threads[i].a();
            }
        }
    }
}
