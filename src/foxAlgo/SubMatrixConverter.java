package foxAlgo;

public class SubMatrixConverter {
    public int[][] Merge(SubMatrix[][] subMatrices){
        var chunkSize = subMatrices[0][0].subMatrix.length;
        var result = new int[subMatrices.length * chunkSize][subMatrices[0].length * chunkSize];

        for(int i = 0; i < subMatrices.length; i++){
            for(int j = 0; j < subMatrices[0].length; j++){
                WriteMatrix(i * chunkSize, j * chunkSize, result, subMatrices[i][j].subMatrix);
            }
        }

        return result;
    }

    public SubMatrix[][] Split(int[][] matrix, int size){
        if(matrix.length % size != 0 || matrix[0].length % size != 0){
            throw new ArrayIndexOutOfBoundsException("The matrix size does not fit the chunk size");
        }
        var rowChunks = matrix.length / size;
        var columnChunks = matrix[0].length / size;
        var result = new SubMatrix[rowChunks][columnChunks];

        for(int i = 0; i < rowChunks; i++){
            for(int j = 0; j < columnChunks; j++){
                result[i][j] = GetChunk(matrix, i * size, j * size, size);
            }
        }

        return result;
    }

    private void WriteMatrix(int row, int col, int[][] base, int[][] chunk){
        for(int i = 0; i < chunk.length; i++){
            for(int j = 0; j < chunk[0].length; j++){
                base[row + i][col + j] = chunk[i][j];
            }
        }
    }

    private SubMatrix GetChunk(int[][] matrix, int row, int col, int size){
        var chunk = new int[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                chunk[i][j] = matrix[row + i][col + j];
            }
        }
        return new SubMatrix(chunk);
    }
}
