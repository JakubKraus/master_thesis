package contextDependentSimilarity;

import java.util.Set;
import tools.IOTools;

// to build isolation tree we first need to parse data
// input is .data file with sequentially written vectors of floats with identifiers
// we need to "transpose" these vectors to get vectors of values in given dimension for each vector
public class IsolationForest {

	private Set<IsolationTree> iTrees;
	private String sourceFilePath;
	
	
	public IsolationForest(Set<IsolationTree> iTrees) {
		this.iTrees = iTrees;
	}
	
	public IsolationForest(String filePath) {
		this.sourceFilePath = filePath;
		IOTools.parseInputForIForest();
	}
	
	
	public int getAverageTreeDepth() {
		int depth = 0;
		
		for (IsolationTree t : iTrees) {
			depth += t.getTreeDepth();
		}
		
		return depth / iTrees.size();
	}
	
	public float getAverageDepthForObject(int objectId) {
		int depth = 0;
		
		try {
			for (IsolationTree t : iTrees) {
				depth += t.getDepthForObject(objectId);
			}
			
			return depth / iTrees.size();
		} catch (TreeException ex) {
			System.out.println(ex.getMessage());
			return 0;
		}
	}
}
