package contextDependentSimilarity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class IsolationNode {
	
	private float borderValue;
	private boolean isLeaf;
	private int depth;
	private IsolationNode left;
	private IsolationNode right;
	private Map<Integer, Float> objects = new HashMap<>();
	private Map<Integer, Float> objectsLeft = new HashMap<>();
	private Map<Integer, Float> objectsRight = new HashMap<>();
	
	
	public IsolationNode(Map<Integer, Float> objects, int parentDepth) throws TreeException {
		if (objects == null || objects.size() == 0) {
			throw new TreeException("Error while buidling iTree - wrong input");
		}
		
		this.depth = parentDepth + 1;
		this.objects = objects;
		
		if (objects.size() == 1) {
			this.isLeaf = true;
			this.borderValue = -1; // no more division
		} else {
			this.isLeaf = false;
			Random random = new Random();
			this.borderValue = random.nextFloat();
			splitObjects(borderValue);
			left = new IsolationNode(objectsLeft, depth);
			right = new IsolationNode(objectsRight, depth);
		}
	}
	
	
	public void splitObjects(float borderValue) {
		for (Entry<Integer, Float> entry : this.objects.entrySet()) {
			if (entry.getValue() <= borderValue) {
				objectsLeft.entrySet().add(entry);
			} else {
				objectsRight.entrySet().add(entry);
			}
			
			if (objectsLeft.size() == 0 || objectsRight.size() == 0) {
				objectsLeft = null;
				objectsRight = null;
				Random random = new Random();
				this.borderValue = random.nextFloat();
				splitObjects(borderValue);
			} else {
				
			}
		}
	}
	
	public Map<Integer, Float> getObjects() {
		return Collections.unmodifiableMap(this.objects);
	}
	
	public boolean isLeaf() {
		return this.isLeaf;
	}
	
	public boolean hasChildren() {
		if (left == null & right == null) {
			return false;
		}
		return true;
	}
	
	public IsolationNode getLeft() {
		return this.left;
	}
	
	public IsolationNode getRight() {
		return this.right;
	}
	
	public int getDepth() {
		return this.depth;
	}

}
