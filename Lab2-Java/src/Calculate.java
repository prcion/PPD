public class Calculate {

    public static int calculateFilterKernel(int[][] kernel, int[][] matrix, int n, int m) {
        int result = 0;

        for (int k = 0; k < kernel.length; ++k) {
            for (int l = 0; l < kernel.length; ++l) {
                if (n - k < 0) continue;
                if (m - l < 0) continue;
                result += matrix[n - k][m - l] * kernel[k][l];
            }
        }
        return result;
    }
}
