public class ThreadLineByLine extends Thread {
    private int[][] matrix, kernel, finalMatrix;
    private int lineStart, lineEnd;

    public ThreadLineByLine(int[][] matrix, int[][] kernel, int[][] finalMatrix, int lineStart, int lineEnd) {
        this.matrix = matrix;
        this.kernel = kernel;
        this.finalMatrix = finalMatrix;
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
    }

    @Override
    public void run() {

        for (int i = lineStart; i < lineEnd; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                finalMatrix[i][j] = Calculate.calculateFilterKernel(kernel, matrix, i, j);
            }
        }
    }
}
