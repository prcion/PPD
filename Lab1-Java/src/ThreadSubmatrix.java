public class ThreadSubmatrix extends Thread {
    private int[][] matrix, kernel, finalMatrix;
    private int lineStart, lineEnd, columnStart, columnEnd;

    public ThreadSubmatrix(int[][] matrix, int[][] kernel, int[][] finalMatrix, int lineStart, int lineEnd, int columnStart, int columnEnd) {
        this.matrix = matrix;
        this.kernel = kernel;
        this.finalMatrix = finalMatrix;
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.columnStart = columnStart;
        this.columnEnd = columnEnd;
    }

    @Override
    public void run() {
        for (int i = lineStart; i < lineEnd; ++i) {
            for (int j = columnStart; j < columnEnd; ++j) {
                finalMatrix[i][j] = Calculate.calculateFilterKernel(kernel, matrix, i, j);
            }
        }
    }
}
