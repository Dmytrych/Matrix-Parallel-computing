package com.company;

import foxAlgo.FoxAlgoMatrixMultiplier;
import striped.StripedMatrixMultiplier;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class Main {
    private static final int[] DefaultDims = new int[] { 100, 1000, 1500, 2000 };
    private static final int[] ThreadCounts = new int[] { 6, 12, 24, 36, 72 };

    public static void main(String[] args) {
        for (int dim: DefaultDims) {
            for (int threadCount: ThreadCounts) {
                var multiplier = new FoxAlgoMatrixMultiplier();
                var firstMatrix = GetRandomMatrix(dim);
                var secondMatrix = GetRandomMatrix(dim);

                Instant starts = Instant.now();
                var result = multiplier.Multiply(firstMatrix, secondMatrix, threadCount);
                Instant ends = Instant.now();

                WriteOutput(dim, threadCount, starts, ends);
            }
        }
    }

    private static void WriteOutput(int dim, int threads,Instant starts, Instant ends){
        System.out.println("Matrix side: " + dim + " Threads: " + threads + " Time: " + Duration.between(starts, ends) + "\n");
    }

    private static int[][] GetRandomMatrixOne(int dimensionSize){
        var resultMatrix = new int[dimensionSize][dimensionSize];

        var random = new Random();

        for (int i = 0; i < dimensionSize; i++) {
            for (int j = 0; j < dimensionSize; j++) {
                resultMatrix[i][j] = i;
            }
        }

        return resultMatrix;
    }

    private static int[][] GetRandomMatrix(int dimensionSize){
        var resultMatrix = new int[dimensionSize][dimensionSize];

        var random = new Random();

        for (int i = 0; i < dimensionSize; i++) {
            for (int j = 0; j < dimensionSize; j++) {
                resultMatrix[i][j] = random.nextInt();
            }
        }

        return resultMatrix;
    }
}
