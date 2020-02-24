package contextDependentSimilarity;

import messif.objects.impl.ObjectFloatVectorCosine;
import sf.main.tools.TransformObjectsToSketches;
import sketching.MaxCutSketchCreator;
import tools.IOTools;
import tools.KNNGroundTruthGenerator;
import tools.RecallEvaluator;

/**
 * Demo class for presenting techniques on small sample of data
 *
 * @author xkraus <xkraus@mail.muni.cz>
 */
public class Main {
    
    // 1) set the parameters
    public static final int LENGTH_OF_SKETCHES = 128;
    public static final int K = 10;
    public static final String IN_DESCRIPTORS = "input\\input_descriptors.data";
    public static final String IN_QUERIES = "input\\10_queries.data";
    public static final int[] IN_CANDIDATE_SET_SIZES = new int[]{20, 50, 100};
    
    public static final String IN_PIVOTS = "input\\pivots-random-2560-normalized.data";
    public static final String IN_PIVOTS_SKETCHES = "input\\pivots_decaf_128.csv";
    public static final String IN_GAUSSEAN_VECTORS = "input\\gaussean_vectors_128.csv";
    
    public static final String OUT_NORMALIZED_DESCRIPTORS = "output\\normalized_descriptors.data";
    public static final String OUT_NORMALIZED_QUERIES = "output\\10_queries_normalized.data";
    public static final String OUT_SKETCHES_MAXCUT = "output\\sketches_500_MAX_CUT_128.data";
    public static final String OUT_SKETCHES_MAXCUT_QUERIES = "output\\sketches_10_queries_MAX_CUT_128.data";
    public static final String OUT_SKETCHES_GHP_50 = "output\\sketches_500_GHP_50_128.data";
    public static final String OUT_SKETCHES_GHP_50_QUERIES = "output\\sketches_10_queries_GHP_50_128.data";
    public static final String OUT_GROUND_TRUTH = "output\\groundtruth_q10.txt";
    public static final String OUT_RECALL_OUTPUT_PATH_MAXCUT = "output\\recall_demo_MAX_CUT_128.csv";
    public static final String OUT_RECALL_OUTPUT_PATH_GHP_50 = "output\\recall_demo_GHP_50_128.csv";   
    

    /**
     * @param args no arguments are expected
     * @throws java.io.IOException if there was error reading the input file
     */
    public static void main(String[] args) throws Exception {

        IOTools.clearDirectory("output");
        
        // 2) first we normalize the input data
        IOTools.createNormalizedVectorFile(IN_DESCRIPTORS, OUT_NORMALIZED_DESCRIPTORS);
        IOTools.createNormalizedVectorFile(IN_QUERIES, OUT_NORMALIZED_QUERIES);
        
        // 3) then we can create sketches of given length
        // the longer the sketches, the more precise approximation
        // max cut sketching technique
        MaxCutSketchCreator.createMaxCutFromFile(
                OUT_NORMALIZED_DESCRIPTORS,                         // input file name
                OUT_SKETCHES_MAXCUT,                                // output file name
                IN_GAUSSEAN_VECTORS,                               // gaussean random vectors file name
                LENGTH_OF_SKETCHES                              // desired length of sketches
        );
        
        // 4)
        // same for query objects
        MaxCutSketchCreator.createMaxCutFromFile(
                OUT_NORMALIZED_QUERIES,                             // input file name
                OUT_SKETCHES_MAXCUT_QUERIES,                        // output file name
                IN_GAUSSEAN_VECTORS,                               // gaussean random vectors file name
                LENGTH_OF_SKETCHES                              // desired length of sketches
        );

        // 5) ghp_50 sketching technique
        String[] argz = new String[6];
        argz[0] = OUT_NORMALIZED_DESCRIPTORS;                       // input file - descriptors
        argz[1] = IN_PIVOTS_SKETCHES;                              // sketches of pivots - created before
        argz[2] = OUT_SKETCHES_GHP_50;                              // output file name
        argz[3] = Integer.toString(LENGTH_OF_SKETCHES);         // desired sketch length (lambda)
        argz[4] = IN_PIVOTS;                                       // pivots      
        argz[5] = ObjectFloatVectorCosine.class.getName();      // class to read vectors from file as
        TransformObjectsToSketches.withAnalysedPivots(argz);
        
        // 6) and same for query objects
        String[] argzz = new String[6];
        argzz[0] = OUT_NORMALIZED_QUERIES;                          // input file - descriptors
        argzz[1] = IN_PIVOTS_SKETCHES;                             // sketches of pivots - created before
        argzz[2] = OUT_SKETCHES_GHP_50_QUERIES;                     // output file name
        argzz[3] = Integer.toString(LENGTH_OF_SKETCHES);        // desired sketch length (lambda)
        argzz[4] = IN_PIVOTS;                                      // pivots      
        argzz[5] = ObjectFloatVectorCosine.class.getName();     // class to read vectors from file as
        TransformObjectsToSketches.withAnalysedPivots(argzz);
        
        // 7) generate the ground truth - exactly counted distances on set of descriptors
        // needed for recall evaluation of sketches
        KNNGroundTruthGenerator generator = new KNNGroundTruthGenerator(K); // k is parameter of kNN search used in recall evaluation
        generator.generateGroundTruthFile(
                OUT_NORMALIZED_DESCRIPTORS,                         // input file name
                OUT_NORMALIZED_QUERIES,                             // queries input file
                OUT_GROUND_TRUTH                                   // output file name
        );
        
        // 8) evaluate recall of MAX_CUT sketches
        RecallEvaluator.evaluate(
                OUT_GROUND_TRUTH,                                   // ground truth file name
                OUT_SKETCHES_MAXCUT,                                // path to sketches to evaluate
                OUT_SKETCHES_MAXCUT_QUERIES,                        // path to query objects for evaluation
                OUT_RECALL_OUTPUT_PATH_MAXCUT,                      // recall output path
                IN_CANDIDATE_SET_SIZES,                            // sizes of candidate set to use for recall evaluation
                K                                               // k is parameter of kNN search used in recall evaluation
        );

        // 9) evaluate recall of GHP_50 sketches
        RecallEvaluator.evaluate(
                OUT_GROUND_TRUTH,                                   // ground truth file name
                OUT_SKETCHES_GHP_50,                                // path to sketches to evaluate
                OUT_SKETCHES_GHP_50_QUERIES,                        // path to query objects for evaluation
                OUT_RECALL_OUTPUT_PATH_GHP_50,                      // recall output path
                IN_CANDIDATE_SET_SIZES,                            // sizes of candidate set to use for recall evaluation
                K                                               // k is parameter of kNN search used in recall evaluation
        );  
    }
}
