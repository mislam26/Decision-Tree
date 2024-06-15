package sol;

import java.util.*;

import src.AttributeSelection;
import src.IDataset;
import src.Row;

/**
 * A class representing a training dataset for the decision tree
 */
public class Dataset implements IDataset  {
    private List<String> attributeList;
    private List<Row> dataObjects;
    private AttributeSelection selectionType;
    private int size;
    /**
     * Constructor for a Dataset object
     * @param attributeList - a list of attributes
     * @param dataObjects -  a list of rows
     * @param attributeSelection - an enum for which way to select attributes
     */
    public Dataset(List<String> attributeList, List<Row> dataObjects, AttributeSelection attributeSelection) {
        this.selectionType = attributeSelection;
        this.attributeList = attributeList;
        this.dataObjects = dataObjects;
        this.size = dataObjects.size();
    }
    /**
     * Gets list of attributes in the dataset
     *
     * @return a list of strings
     */
    public List<String> getAttributeList(){
        return this.attributeList;
    };

    /**
     * Gets list of data objects (row) in the dataset
     *
     * @return a list of Rows
     */
    public List<Row> getDataObjects(){
        return this.dataObjects;
    };

    /**
     * Returns the attribute selection type (alphabetical, reverse alphabetical, random) for this Dataset
     *
     * @return the attribute selection type
     */
    public AttributeSelection getSelectionType(){
        return this.selectionType;
    };

    /**
     * finds the size of the dataset (number of rows)
     *
     * @return the number of rows in the dataset
     */
    public int size(){
        return this.dataObjects.size();
    };

    /**
     * @return a string that represents the attribute we have selected to split on
     */
    public String getAttributeToSplitOn() {
        switch (this.selectionType) {
            case ASCENDING_ALPHABETICAL -> {
                return this.attributeList.stream().sorted().toList().get(0);
            }
            case DESCENDING_ALPHABETICAL -> {
                return this.attributeList.stream().sorted().toList().get(this.attributeList.size() - 1);
            }
            case RANDOM -> {
                Random rand = new Random();
                return this.attributeList.get(rand.nextInt(0, this.attributeList.size()));
            }
        }
        throw new RuntimeException("Non-Exhaustive Switch Case");
    }

    /**
     *
     * @param remove - name of attribute you are taking out from attributeList
     * @return new attribute list without the old attribute
     */
    public void reduceAttributeList(String remove){
        this.attributeList.remove(remove);
    }

    /**
     * Method that finds the default attribute value for a specific attribute
     *
     * @param attribute - attribute we are looking at to find the default for
     * @return a string the represents the default atrribute value
     */
    public String findDefaultDecision(String attribute) {
        // lists to track attributes and their counts
        List<String> attributes = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        // iterate through each row in the dataset
        for (Row r : this.dataObjects) {
            String str = r.getAttributeValue(attribute);
            int index = attributes.indexOf(str);

            // if the attribute is already tracked, increment its count
            if (index != -1) {
                counts.set(index, counts.get(index) + 1);
            } else {
                // if it's a new attribute, add it to the list and initialize its count to 1
                attributes.add(str);
                counts.add(1);
            }
        }

        int max = 0;
        String maxKey = "";
        // iterate through the counts to find the maximum
        for (int i = 0; i < counts.size(); i++) {
            if (counts.get(i) > max) {
                max = counts.get(i);
                maxKey = attributes.get(i);
            }
        }

        return maxKey; //return the attribute with the highest count
    }

    /**
     * Method that splits the dataset to smaller datasets based on a certain attribute
     *
     * @param attribute - attribute we are looking at tto split data on based on differing values
     * @return - a lit of subset datasets split based on attribute
     */
    public List<Dataset> splitData(String attribute){
         List<String> attributeValues = new ArrayList<String>();
         List<Dataset> splitData = new ArrayList<Dataset>();

         List<String> updatedAttributes = new ArrayList<String>(this.attributeList); //reducing attributeList
         updatedAttributes.remove(attribute);

        for(Row r : this.dataObjects){
            String value = r.getAttributeValue(attribute);
            if(!attributeValues.contains(value)){
                //add to list of attribute values for specific attribute
                attributeValues.add(value);

                //creating new subset of original dataset and add it to list of datasets
                List<Row> rows = new ArrayList<Row>();
                rows.add(r);
                Dataset subset = new Dataset(updatedAttributes, rows, this.selectionType);
                splitData.add(subset);
            } else {
                //update the subset of dataset corresponding to value of attribute
                int index = attributeValues.indexOf(value);
                splitData.get(index).getDataObjects().add(r);
            }
        }
        return splitData;
    }


    /**
     * Method returning  String with null or a decision, that represents the same decision of
     * all the branches and nodes
     *
     * @param targetAttribute - input String representing the attribute we are looking for
     * @return - String representing the end decision or null
     */
    public String sameDecision(String targetAttribute) {
        String firstDecision = this.dataObjects.get(0).getAttributeValue(targetAttribute);
        for (Row currentRowData : this.dataObjects) {
            if (!firstDecision.equals(currentRowData.getAttributeValue(targetAttribute))) {
                return "";
            }
        }
        return firstDecision;
    }

    /**
     * Method that returns a list of all the values for a specific attribute
     *
     * @param attribute - attribute we are looking at in particular to find the distinct values of
     * @return list of strings that represent the distinct values of an attribute
     */

    public List<String> returnAttributeValues(String attribute) {
        List<String> attributeValues = new ArrayList<String>();

        for (Row r : this.dataObjects) {
            String value = r.getAttributeValue(attribute);
            if (!attributeValues.contains(value)) {
                //add to list of attribute values for specific attribute
                attributeValues.add(value);
            }
        }
        return attributeValues;
    }

}
