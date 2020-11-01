public class Secvential {
    private int[][] matrix, kernel, finalMatrix;
    private int N, M;

    public Secvential(int[][] matrix, int[][] kernel, int[][] finalMatrix, int N, int M) {
        this.matrix = matrix;
        this.kernel = kernel;
        this.finalMatrix = finalMatrix;
        this.N = N;
        this.M = M;
    }

    public void filterMatrix() {
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < M; ++j) {
                finalMatrix[i][j] = Calculate.calculateFilterKernel(kernel, matrix, i, j);
            }
        }
    }
}
