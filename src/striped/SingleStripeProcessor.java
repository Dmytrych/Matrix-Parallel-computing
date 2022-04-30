package striped;

public class SingleStripeProcessor {
    public void ProcessStripeAndWriteResult(int[] row, int[][] secondMatrix, int[][] resultingMatrix, int resultRow){
        if(row.length != secondMatrix[0].length){
            throw new ArrayIndexOutOfBoundsException("The row was not the same size as the matrix. Please, check the input arrays");
        }

        for(int i = 0; i < secondMatrix[0].length; i++){
            resultingMatrix[resultRow][i] = Multiply(row, extractColumn(secondMatrix, i));
        }
    }

    private void ProcessStripeAndWriteResults(int[] row, int[] column, int[][] resultingMatrix, int resultRow, int resultColumn){
        if(row.length != column.length){
            throw new ArrayIndexOutOfBoundsException("The row was not the same size as the matrix. Please, check the input arrays");
        }

        resultingMatrix[resultRow][resultColumn] = Multiply(row, column);
    }

    private int[] extractColumn(int[][] matrix, int colIndex){
        var result = new int[matrix.length];

        for(int i = 0; i < result.length; i++){
            result[i] = matrix[i][colIndex];
        }

        return result;
    }

    private int Multiply(int[] row, int[] column) {
        int result = 0;

        for(int i = 0; i < row.length; i++){
            result += row[i] * column[i];
        }

        return result;
    }
}
