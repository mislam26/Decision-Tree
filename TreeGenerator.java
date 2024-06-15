package sol;

import com.sun.jdi.Value;
import src.ITreeGenerator;
import src.ITreeNode;
import src.Row;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that implements the ITreeGenerator interface used to generate a decision tree
 */
public class TreeGenerator implements ITreeGenerator<Dataset> {
    private ITreeNode root;

    /**
     * constructor that makes the tree generator object
     */
    public TreeGenerator() {
        this.root = null;
    }

    /**
     * Method that generates a tree for a dataset
     *
     * @param data - dataset we are training the tree on
     * @param targetAttribute - the attribute we are trying to find at the end
     */
    @Override
    public void generateTree(Dataset data, String targetAttribute) {
        //creating list of attributes without target
        List<String> attributeList = new ArrayList<String>(data.getAttributeList());
        List<Row> rows = new ArrayList<Row>(data.getDataObjects());

        Dataset trainingData = new Dataset(attributeList, rows, data.getSelectionType());
        trainingData.reduceAttributeList(targetAttribute);

        //building tree
        if (trainingData.size() != 0) {
            this.root = this.generateNode(trainingData, targetAttribute);
        } else {
            throw new RuntimeException("Given Empty Dataset"); //error if empty dataset
        }
    }

    /**
     * helper to generateNode of decision tree recursively
     *
     * @param subsetData - part of the dataset we are generating a node on
     * //@param restAttributes - list of rest of attributes we still have to consider when building tree
     * @param targetAttribute - attribute that we try to create a decision around
     * @return the root node with the tree built around it
     */
    private ITreeNode generateNode(Dataset subsetData, String targetAttribute) {

        //checks if there are remaining attributes to consider or if instances in subset have same for target
        if ((subsetData.sameDecision(targetAttribute) != "") || subsetData.getAttributeList().isEmpty()) {
            //return decisionLeaf node with default decision
            return new DecisionLeaf(subsetData.findDefaultDecision(targetAttribute));
        } else {
            //find attribute to split on
            String attributeToSplit = subsetData.getAttributeToSplitOn();

            // partitioning through subset data -- splitting into multi subsets of data
            List<Dataset> partitionedDatasets = new ArrayList<Dataset>(subsetData.splitData(attributeToSplit));
            String defaultDecision = subsetData.findDefaultDecision(targetAttribute);
            List<ValueEdge> valueEdgesList = new ArrayList<ValueEdge>();

            //for each partition dataset create value edge -- linking attribute value to a recursive generateNode
            //by calling generateNode
            for (Dataset eachDataset : partitionedDatasets) {
                List<String> list = eachDataset.returnAttributeValues(attributeToSplit);
                valueEdgesList.add(new ValueEdge(list.get(0),
                        this.generateNode(eachDataset, targetAttribute)));
            }

            return new AttributeNode(attributeToSplit, defaultDecision, valueEdgesList);
        }
    }

    /**
     * @param forDatum the datum to look up a decision for
     * @return - string the represents the decision for a given row
     */
    public String getDecision(Row forDatum){
        return this.root.getDecision(forDatum);
    }
}

