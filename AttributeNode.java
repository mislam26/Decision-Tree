package sol;

import java.util.List;

import com.sun.jdi.Value;
import src.ITreeNode;
import src.Row;

/**
 * A class representing an inner node in the decision tree.
 */
public class AttributeNode  implements ITreeNode {
    private List<ValueEdge> outgoingEdges;
    private String defaultDecision;
    private String attributeName;

    /**
     * Constructor that creates an AttributeNode Object
     *
     * @param name - string value of attribute
     * @param def - default value of attribute
     * @param outgoing - list of edges that point out of the node
     */
    public AttributeNode(String name, String def, List<ValueEdge> outgoing){
        this.attributeName = name;
        this.defaultDecision = def;
        this.outgoingEdges = outgoing;
    }


    @Override
    /**
     * Method that recurs through tree to find a decision
     *
     * @param forDatum - takes in row object
     * @return the string representation of the decision associated with the row
     */
    public String getDecision(Row forDatum) {
        // for each of the value edge in the value edge list, checks if
        // forDatum contains the attribute specified by this.attributename, if not return default
        for (ValueEdge currEdge : this.outgoingEdges) {

            //if attribute is present, compares value of attribute in forDatum
            //recursively calls getDecision on the child node of current edge
            if (currEdge.getValue().equals(forDatum.getAttributeValue(this.attributeName))){
                return currEdge.getChildNode().getDecision(forDatum);
            }
        }
        // when no match is found
        return this.defaultDecision;
    }
}
