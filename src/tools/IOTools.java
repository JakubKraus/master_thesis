package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import messif.objects.LocalAbstractObject;
import messif.objects.impl.ObjectFloatVector;
import messif.objects.impl.ObjectFloatVectorL2;
import messif.objects.util.StreamGenericAbstractObjectIterator;
import sf.objects.impl.Sketch;

/**
 * Class containing static methods for I/O
 * 
 * @author xkraus <xkraus@mail.muni.cz>
 */
public class IOTools {
	
	public static void parseInputForIForest() {
		
	}

    /**
     * Creates iterator of class from parameter c read from file
     *
     * @param c class of iterator type
     * @param filePath path to source file
     * @return Iterator containing Cosine objects
     * @throws IOException throws if there was error reading the file
     */
    public static StreamGenericAbstractObjectIterator iterator(Class c, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        return new StreamGenericAbstractObjectIterator(c, filePath);
    }

    /**
     *
     * @param className name of the class of iterator type to be created
     * @param filePath path to source file
     * @return Iterator containing Cosine objects
     * @throws IOException if there was error while reading the input file
     * @throws ClassNotFoundException if there is no such known class className
     */
    public static StreamGenericAbstractObjectIterator iterator(String className, String filePath) throws IOException, ClassNotFoundException {
        return IOTools.iterator(Class.forName(className), filePath);
    }


    /**
     * Writes vector to file in format: "#objectKey
     * messif.objects.keys.AbstractObjectKey [locatorURI]" "[vector]"
     *
     * @param vector vector to be written
     * @param outputFileName file to write output to
     * @param locatorURI identifier of object
     * @throws IOException throws if there was error while writing to file
     */
    public static void writeVectorToFile(float[] vector, String outputFileName, String locatorURI) throws IOException {
        try (final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFileName, true)))) {
            out.println("#objectKey messif.objects.keys.AbstractObjectKey " + locatorURI);
            for (int i = 0; i < vector.length; i++) {
                out.print(vector[i] + " ");
            }
            out.println();
        }
    }

    /**
     * Loads vectors from input file, normalizing them and writing to output
     * file
     *
     * @param inputFileName file of non-normalized vectors
     * @param outputFileName file to be written result to
     * @throws IOException if there was error reading or writing the file
     */
    public static void createNormalizedVectorFile(String inputFileName, String outputFileName) throws IOException {
        Iterator<ObjectFloatVectorL2> objectsEuclidean = IOTools.iterator(ObjectFloatVectorL2.class, inputFileName);
        while (objectsEuclidean.hasNext()) {
            ObjectFloatVector objVect = objectsEuclidean.next();
            float[] normalized = MathTools.normalizeVector(objVect.getVectorData());
            IOTools.writeVectorToFile(normalized, outputFileName, objVect.getLocatorURI());
        }
    }

    /**
     * Writes Gauss matrix into output file in format first line - "height
     * width" rest of the lines - .csv format of vector dimensions, each vector
     * on separate line
     *
     * @param vectors Gauss matrix to be saved
     * @param outputFileName name of output file to be written to
     * @throws IOException throws if there was error while writting to file
     */
    public static void writeFloatVectorsToCSVFile(float[][] vectors, String outputFileName) throws IOException {
        int numberOfVectors = vectors.length;
        try (final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFileName, false)))) {
            out.println(numberOfVectors + " " + vectors[0].length);
            for (int i = 0; i < numberOfVectors; i++) {
                int sizeOfVectors = vectors[0].length;
                for (int j = 0; j < sizeOfVectors; j++) {
                    if (j == sizeOfVectors - 1) {
                        out.print(vectors[i][j] + "\n");
                    } else {
                        out.print(vectors[i][j] + ", ");
                    }
                }
            }
        }
    }

    /**
     * Read each object from the iterator and compute its distance to the given
     * query object. Write out the distances.
     *
     * @param iterator the iterator over all objects
     * @param queryObj the object against which to get the distance
     */
    public static void countDistanceWriteOutput(Iterator<LocalAbstractObject> iterator, LocalAbstractObject queryObj) {
        while (iterator.hasNext()) {
            LocalAbstractObject obj = iterator.next();
            System.out.println("Distance of " + obj.getLocatorURI() + " to " + queryObj.getLocatorURI() + " = " + queryObj.getDistance(obj));
        }
    }
    
    /**
     * Deletes all files in directory
     * 
     * @param dirName directory to have all files deleted
     */
    public static void clearDirectory(String dirName) {
        File dir = new File(dirName);
        for (File file: dir.listFiles()) {
            file.delete();
        }
    }   
}
