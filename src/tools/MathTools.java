package tools;

import java.io.IOException;
import java.util.Random;
import messif.objects.impl.ObjectFloatVector;
import messif.objects.util.StreamGenericAbstractObjectIterator;

/**
 * Class containing static methods for mathematical operations
 * 
 * @author xkraus <xkraus@mail.muni.cz>
 */
public class MathTools {

    /**
     * Calculates the norm of vector
     *
     * @param vector to count length of
     * @return norm of vector
     */
    public static float vectorNorm(float[] vector) {
        float sum = 0;
        for (int i = 0; i < vector.length; i++) {
            sum += (vector[i] * vector[i]);
        }
        return (float) Math.sqrt(sum);
    }

    /**
     * returns random Gaussean float vector
     *
     * @param dim desired dimensionality
     * @return random Gaussean vector with dimensionality dim
     */
    public static float[] gaussVector(int dim) {
        Random random = new Random();
        float[] ret = new float[dim];
        for (int i = 0; i < dim; i++) {
            ret[i] = (float) random.nextGaussian();
        }
        return ret;
    }

    /**
     * Generates Gauss matrix of random numbers of size numberOfVectors x
     * lengthOfVectors
     *
     * @param numberOfVectors future dimensionality of sketches, height of
     * matrix
     * @param lengthOfvectors dimensionality of vectors, width of matrix
     * @param outputFileName file to write result matrix to, if not null
     * @return Gauss matrix generated
     * @throws IOException if there was error while writting to file
     */
    public static float[][] createGaussMatrix(int numberOfVectors, int lengthOfvectors, String outputFileName) throws IOException {
        float[][] ret = new float[numberOfVectors][];
        Random r = new Random();
        for (int i = 0; i < numberOfVectors; i++) {
            ret[i] = new float[lengthOfvectors];
            for (int j = 0; j < lengthOfvectors; j++) {
                ret[i][j] = (float) r.nextGaussian();
            }
        }
        if (outputFileName != null) {
            IOTools.writeFloatVectorsToCSVFile(ret, outputFileName);
        }
        return ret;
    }

    /**
     * Calculates dot product of two float vectors
     *
     * @param first first vector
     * @param second second vector
     * @return dot product
     */
    public static float dotProduct(float[] first, float[] second) {
        float ret = 0;
        for (int i = 0; i < Math.min(first.length, second.length); i++) {
            ret += first[i] * second[i];
        }
        return ret;
    }

    /**
     * Checks float vector for existence of negative number in it
     *
     * @param filePath file to be checked
     * @return Are there negative values in given vectors of floats?
     * @throws java.io.IOException if there is error while reading the file
     */
    public static boolean checkForNegativeValues(String filePath) throws IllegalArgumentException, IOException {
        StreamGenericAbstractObjectIterator<ObjectFloatVector> iterator = new StreamGenericAbstractObjectIterator<>(ObjectFloatVector.class, filePath);
        while (iterator.hasNext()) {
            ObjectFloatVector obj = iterator.next();
            float[] data = obj.getVectorData();
            for (float n : data) {
                if (n < 0) {
                    System.out.println("There are negative values in vectors in file " + filePath);
                    return true;
                }
            }
        }
        System.out.println("There are no negative values in vectors in file " + filePath);
        return false;
    }

    /**
     * Generates normalized vector from input vector, such with its norm equal
     * to 1
     *
     * @param vector vctor to be normalized
     * @return normalized vector
     */
    public static float[] normalizeVector(float[] vector) {
        float[] result = new float[vector.length];
        float norm = MathTools.vectorNorm(vector);
        for (int i = 0; i < vector.length; i++) {
            result[i] = vector[i] / norm;
        }
        return result;
    }
}
