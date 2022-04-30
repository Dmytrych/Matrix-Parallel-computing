package foxAlgo;

import parallelMatrixGeneral.MatrixMultiplier;
import parallelMatrixGeneral.Result;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class FoxAlgoMatrixMultiplier implements MatrixMultiplier {
    private static final int chunkSize = 50;

    @Override
    public Result Multiply(int[][] firstMatrix, int[][] secondMatrix, int threadLimit) {
        var converter = new SubMatrixConverter();
        var splittedFirst = converter.Split(firstMatrix, chunkSize);
        var splittedSecond = converter.Split(secondMatrix, chunkSize);

        var result = RunMultiplication(splittedFirst, splittedSecond, threadLimit);

        return new Result(converter.Merge(result));
    }

    private SubMatrix[][] RunMultiplication(SubMatrix[][] firstMatrix, SubMatrix[][] secondMatrix, int threadLimit){
        var executorService = Executors.newFixedThreadPool(threadLimit);
        var tasks = new ArrayList<SubMatrixMultiplierRunnable>();
        var collectors = new SubMatrixMultiplicationResultsCollector[firstMatrix.length][secondMatrix[0].length];
        var finalResults = new SubMatrix[firstMatrix.length][secondMatrix[0].length];

        for(int i = 0; i < firstMatrix.length; i++){
            for(int j = 0; j < secondMatrix[0].length; j++){
                collectors[i][j] = RunSingleSubMatrixMultiplication(firstMatrix, secondMatrix, i, j, tasks);
            }
        }

        try{
            var resultsFuture = executorService.invokeAll(tasks);

            resultsFuture.forEach(task -> {
                try{
                    task.get();
                }
                catch (Exception e){
                }
            });

            executorService.shutdown();

            for(int i = 0; i < finalResults.length; i++){
                for (int j = 0; j < finalResults[0].length; j++){
                    finalResults[i][j] = SumAll(collectors[i][j].multipliedMatrices);
                }
            }
        }
        catch (Exception ex){
        }

        return finalResults;
    }

    private SubMatrix SumAll(ArrayList<SubMatrix> subMatrices){
        var sumator = new OrdinaryMatrixMultiplier();
        int[][] result = null;
        for (var matrix: subMatrices) {
            if(result == null){
                result = matrix.subMatrix;
                continue;
            }
            result = sumator.Sum(result, matrix.subMatrix).Result;
        }
        return new SubMatrix(result);
    }

    private SubMatrixMultiplicationResultsCollector RunSingleSubMatrixMultiplication(
            SubMatrix[][] firstMatrix,
            SubMatrix[][] secondMatrix,
            int row,
            int col,
            ArrayList<SubMatrixMultiplierRunnable> tasks){
        var result = new SubMatrixMultiplicationResultsCollector(row, col);

        for (int i = 0; i < secondMatrix.length; i++) {
            tasks.add(new SubMatrixMultiplierRunnable(firstMatrix[row][i], secondMatrix[i][col], result.multipliedMatrices));
        }
        
        return result;
    }

    private class SubMatrixMultiplierRunnable implements Callable<SubMatrix> {
        private SubMatrix firstSubMatrix;
        private SubMatrix secondSubMatrix;
        private ArrayList<SubMatrix> results;

        public SubMatrixMultiplierRunnable(SubMatrix firstSubMatrix, SubMatrix secondSubMatrix, ArrayList<SubMatrix> results){
            this.firstSubMatrix = firstSubMatrix;
            this.secondSubMatrix = secondSubMatrix;
            this.results = results;
        }

        @Override
        public SubMatrix call() {
            var matrixMultiplier = new OrdinaryMatrixMultiplier();

            var result = new SubMatrix(matrixMultiplier.Multiply(firstSubMatrix.subMatrix, secondSubMatrix.subMatrix).Result);

            results.add(result);
            return result;
        }
    }

    private class SubMatrixMultiplicationResultsCollector {
        public ArrayList<SubMatrix> multipliedMatrices;
        public int resultRow;
        public int resultCol;

        public SubMatrixMultiplicationResultsCollector(int resultRow, int resultCol){
            multipliedMatrices = new ArrayList<>();
            this.resultRow = resultRow;
            this.resultCol = resultCol;
        }
    }
}
