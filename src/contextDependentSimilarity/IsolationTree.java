package contextDependentSimilarity;

import java.util.Map;

// input should be vector of values of all objects in one given dimension
// working with normalized vector values
// one iTree = values of one dimension
public class IsolationTree {
	
	private Map<Integer, Float> objects;
	private IsolationNode root;
	private int depth;
	
	
	public IsolationTree(Map<Integer, Float> objects) { // int = ID, float = value in given dimension
		this.objects = objects;
		this.root = build(this.objects);
	}
	
	public int getTreeDepth() {
		return depth;
	}
	
	public int getDepthForObject(int objectId) throws TreeException {
		int depth = getDepthForObjectRecursively(objectId, root);
		if (depth == 0) {
			throw new TreeException("No object found in tree for given ID");
		} else {
			return depth;
		}
	}
	
	
	private IsolationNode build(Map<Integer, Float> objects) {
		if (objects == null || objects.size() == 0) {
			this.depth = 0;
			return null;
		} else {
			try {
				IsolationNode futureRoot = new IsolationNode(objects, 0);
				this.depth = computeTreeDepth(root, 0);
				return futureRoot;
			} catch (TreeException ex) {
				System.out.println(ex.getMessage());
				return null;
			}
		}
	}

	private int computeTreeDepth(IsolationNode startingNode, int previousDepth) {
		if (startingNode == null) {
			return 0;
		}
		if (startingNode.hasChildren()) {
			return Math.max(computeTreeDepth(startingNode.getLeft(), previousDepth),
							computeTreeDepth(startingNode.getRight(), previousDepth))
					+ previousDepth;
		} else {
			return previousDepth + 1;
		}
	}

	private int getDepthForObjectRecursively(int objectId, IsolationNode currentNode) {
		if (currentNode == null || currentNode.getObjects() == null) {
			return 0;
		}
		
		if (currentNode.getObjects().size() == 1) {
			if (currentNode.getObjects().get(objectId) != null) {
				return currentNode.getDepth();
			} else {
				return 0;
			}
		} else {
			int leftDepth = getDepthForObjectRecursively(objectId, currentNode.getLeft());
			int rightDepth = getDepthForObjectRecursively(objectId, currentNode.getLeft());
			return Math.max(leftDepth, rightDepth);
		}
	}
	

}
