public class ThreadColumnByColumn extends Thread {
    private int[][] matrix, kernel, finalMatrix;
    private int columnStart, columnEnd, lines;

    public ThreadColumnByColumn(int[][] matrix, int[][] kernel, int[][] finalMatrix, int lines, int columnStart, int columnEnd) {
        this.matrix = matrix;
        this.kernel = kernel;
        this.finalMatrix = finalMatrix;
        this.lines = lines;
        this.columnStart = columnStart;
        this.columnEnd = columnEnd;
    }

    @Override
    public void run() {
        for (int j = columnStart; j < columnEnd; ++j) {
            for (int i = 0; i < lines; ++i) {
                finalMatrix[i][j] = Calculate.calculateFilterKernel(kernel, matrix, i, j);
            }
        }
    }
}
