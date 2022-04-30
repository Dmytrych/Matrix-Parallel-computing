package parallelMatrixGeneral;

public interface MatrixMultiplier {
    Result Multiply(int[][] firstMatrix, int[][] secondMatrix, int threadLimit);
}
