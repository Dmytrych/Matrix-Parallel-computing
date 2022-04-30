package foxAlgo;

import parallelMatrixGeneral.Result;

public class OrdinaryMatrixMultiplier implements MatrixSumator {
    public Result Multiply(int[][] firstMatrix, int[][] secondMatrix) {
        var newMatrix = new int[firstMatrix.length][secondMatrix[0].length];

        for(int i = 0; i < newMatrix.length; i++){
            for(int j = 0; j < newMatrix[0].length; j++){
                newMatrix[i][j] += GetElement(firstMatrix, secondMatrix, i, j);
            }
        }

        return new Result(newMatrix);
    }

    @Override
    public Result Sum(int[][] firstMatrix, int[][] secondMatrix) {
        var newMatrix = new int[firstMatrix.length][secondMatrix[0].length];

        for(int i = 0; i < newMatrix.length; i++){
            for(int j = 0; j < newMatrix[0].length; j++){
                newMatrix[i][j] = firstMatrix[i][j] + secondMatrix[i][j];
            }
        }

        return new Result(newMatrix);
    }

    private int GetElement(int[][] firstMatrix, int[][] secondMatrix, int row, int column){
        int cell = 0;
        for (int i = 0; i < secondMatrix.length; i++) {
            cell += firstMatrix[row][i] * secondMatrix[i][column];
        }
        return cell;
    }
}
