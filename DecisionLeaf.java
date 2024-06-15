package sol;

import src.ITreeNode;
import src.Row;

/**
 * A class representing a leaf in the decision tree.
 */
public class DecisionLeaf implements ITreeNode {
    public String decision;

    /**
     * constructor that creates a decisionLeaf Object
     * @param d - string that represents the value of the decision
     */
    public DecisionLeaf(String d){
        this.decision = d;
    }

    /**
     *
     * @param forDatum the datum to look up a decision for
     * @return - decision associated with the row we are looking for
     */
    public String getDecision(Row forDatum){
        return this.decision;
    };
}
