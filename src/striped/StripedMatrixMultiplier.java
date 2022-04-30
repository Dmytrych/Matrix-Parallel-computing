package striped;

import parallelMatrixGeneral.MatrixMultiplier;
import parallelMatrixGeneral.Result;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StripedMatrixMultiplier implements MatrixMultiplier {
    @Override
    public Result Multiply(int[][] firstMatrix, int[][] secondMatrix, int threadsLimit) {
        if(firstMatrix[0].length != secondMatrix.length || firstMatrix.length != secondMatrix[0].length){
            throw new ArrayIndexOutOfBoundsException("The matrices cannot be multiplied. Please, check the input arrays");
        }

        var resultingMatrix = new int[firstMatrix.length][secondMatrix[0].length];
        var futures = new ArrayList<Future>();
        var executorService = Executors.newFixedThreadPool(threadsLimit);

        for(int i = 0; i < firstMatrix.length; i++){
            futures.add(executorService.submit(new MatrixMultiplicationRunnable(firstMatrix[i], secondMatrix, resultingMatrix, i)));
        }

        futures.forEach(future -> {
            try {
                future.get();
            }
            catch (Exception ex){

            }
        });
        executorService.shutdown();

        return new Result(resultingMatrix);
    }

    private class MatrixMultiplicationRunnable implements Runnable {
        int[] row;
        int[][] secondMatrix;
        int[][] resultingMatrix;
        int resultRow;

        public MatrixMultiplicationRunnable(int[] row, int[][] secondMatrix, int[][] resultingMatrix, int resultRow){
            this.row = row;
            this.secondMatrix = secondMatrix;
            this.resultingMatrix = resultingMatrix;
            this.resultRow = resultRow;
        }

        @Override
        public void run() {
            var singleStripeProcessor = new SingleStripeProcessor();
            singleStripeProcessor.ProcessStripeAndWriteResult(row, secondMatrix, resultingMatrix, resultRow);
        }
    }
}
