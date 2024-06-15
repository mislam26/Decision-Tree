package sol;

import src.ITreeNode;

/**
 * A class that represents the edge of an attribute node in the decision tree
 */
public class ValueEdge {

    private String value;
    private ITreeNode childNode;

    /**
     * constructor that creates a ValueEdge object
     * @param valueAtEdge - string that represents a value
     * @param nextNode - the node it points to
     */
    public ValueEdge(String valueAtEdge, ITreeNode nextNode) {
        this.value = valueAtEdge;
        this.childNode = nextNode;
    }

    /**
     * @return the value associated with the edge
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @return the childNode it points to
     */
    public ITreeNode getChildNode() {
        return this.childNode;
    }
}
