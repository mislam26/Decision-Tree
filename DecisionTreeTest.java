package sol;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import src.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * A class containing the tests for methods in the TreeGenerator and Dataset classes
 */
public class DecisionTreeTest {
    //TODO: Write more unit and system tests! Some basic guidelines that we will be looking for:
    // 1. Small unit tests on the Dataset class testing the IDataset methods
    // 2. Small unit tests on the TreeGenerator class that test the ITreeGenerator methods
    // 3. Tests on your own small dataset (expect 70% accuracy on testing data, 95% on training data)
    // 4. Test on the villains dataset (expect 70% accuracy on testing data, 95% on training data)
    // 5. Tests on the mushrooms dataset (expect 70% accuracy on testing data, 95% on training data)
    // Feel free to write more unit tests for your own helper methods -- more details can be found in the handout!

    String trainingPath = "data/seasons.csv";
    String targetAttribute = "season";
    TreeGenerator testGenerator;
    Dataset training;

    @Before
    public void setup() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        List<String> attributes = new ArrayList<>(dataObjects.get(0).getAttributes());
        this.training = new Dataset(attributes, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);

    }

    /**
     * #1 Testing IDataset methods
     */
    @Test
    public void testIDatasetMethods() {

        //testing basic IDataset methods
        List<String> attributeList = new ArrayList<String>();
        attributeList.add("temperature");
        attributeList.add("season");
        attributeList.add("leaves");
        attributeList.add("wind");
        Assert.assertEquals(attributeList, this.training.getAttributeList()); //before removing target
        Assert.assertEquals(AttributeSelection.ASCENDING_ALPHABETICAL, this.training.getSelectionType());
        Assert.assertEquals(7, this.training.size());

        //testing returnAttributeValues
        List<String> attributeValuesList = new ArrayList<String>();
        attributeValuesList.add("green");
        attributeValuesList.add("brown");
        attributeValuesList.add("yellow");
        Assert.assertEquals(attributeValuesList, this.training.returnAttributeValues("leaves"));
    }

    /**
     * #2 testing ITreeGenerator methods - getDecision
     */
    @Test
    public void testITreeGenerator(){
        this.testGenerator = new TreeGenerator();
        this.testGenerator.generateTree(this.training, this.targetAttribute);

        Row season8 = new Row("season8");
        season8.setAttributeValue("temperature", "cold");
        season8.setAttributeValue("leaves", "yellow");
        season8.setAttributeValue("wind", "no");
        Assert.assertEquals("winter", this.testGenerator.getDecision(season8));

        //when the attribute value is not present, and it goes to default
        Row season9 = new Row("season9");
        season9.setAttributeValue("temperature", "hot");
        season9.setAttributeValue("leaves", "green");
        season9.setAttributeValue("wind", "little");
        Assert.assertEquals("summer", this.testGenerator.getDecision(season9));
    }

    /**
     * throws an error to test on empty data set
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void testEmpty(){
        TreeGenerator emptyTree = new TreeGenerator();
        List<Row> empty = DecisionTreeCSVParser.parse("data/empty.csv");
        List<String> emptyAttributeList = new ArrayList<>(empty.get(0).getAttributes());
        Dataset emptyData = new Dataset(emptyAttributeList, empty, AttributeSelection.ASCENDING_ALPHABETICAL);

        emptyTree.generateTree(emptyData, "season");
        String emptyDecision = emptyTree.getDecision(empty.get(0));
        Assert.assertEquals("empty", emptyDecision);
    }

    // #3 Tests on your own small dataset (expect 70% accuracy on testing data, 95% on training data)
    @Test
    public void miniDataTest() {
        //set up training data
        String basketballTraining = "data/basketball-training.csv";
        List<Row> trainBasketball = DecisionTreeCSVParser.parse(basketballTraining);
        List<String> trainBasketballAttributes = new ArrayList<>(trainBasketball.get(0).getAttributes());
        Dataset trainingB = new Dataset(trainBasketballAttributes, trainBasketball,
                AttributeSelection.ASCENDING_ALPHABETICAL);

        //set up testing data
        String basketballTesting = "data/basketball-testing.csv";
        List<Row> testVillains = DecisionTreeCSVParser.parse(basketballTesting);
        List<String> testBasketballAttributes = new ArrayList<>(testVillains.get(0).getAttributes());
        Dataset testingB = new Dataset(testBasketballAttributes, testVillains,
                AttributeSelection.ASCENDING_ALPHABETICAL);

        DecisionTreeTester<TreeGenerator, Dataset> tester;
        try {
            tester = new DecisionTreeTester<>(TreeGenerator.class, Dataset.class);
            double accuracy =
                    tester.getDecisionTreeAccuracy(trainingB, trainingB, "basketball");
            System.out.println("Accuracy on training data: " + accuracy);
            int numIters = 100;
            accuracy = tester.getAverageDecisionTreeAccuracy(trainingB, testingB, "basketball", numIters);
            System.out.println("Accuracy on testing data: " + accuracy);
        }
        catch (InstantiationException | InvocationTargetException
               | NoSuchMethodException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // #4 testing villainData accuracy - NEED TO MODIFY
        @Test
        public void testVillainData(){
            //set up training data
            String villainTraining = "data/villains/training.csv";
            List<Row> trainVillains = DecisionTreeCSVParser.parse(villainTraining);
            List<String> trainVillainAttributes = new ArrayList<>(trainVillains.get(0).getAttributes());
            Dataset trainingVillain = new Dataset(trainVillainAttributes, trainVillains,
                    AttributeSelection.ASCENDING_ALPHABETICAL);

            //set up testing data
            String villainTesting = "data/villains/testing.csv";
            List<Row> testVillains = DecisionTreeCSVParser.parse(villainTesting);
            List<String> testVillainAttributes = new ArrayList<>(testVillains.get(0).getAttributes());
            Dataset testingVillain = new Dataset(testVillainAttributes, testVillains,
                    AttributeSelection.ASCENDING_ALPHABETICAL);


            DecisionTreeTester<TreeGenerator, Dataset> tester;
            try {
                tester = new DecisionTreeTester<>(TreeGenerator.class, Dataset.class);
                double accuracy =
                        tester.getDecisionTreeAccuracy(trainingVillain, trainingVillain, "isVillain");
                System.out.println("Accuracy on training data: " + accuracy);
                int numIters = 100;
                accuracy = tester.getAverageDecisionTreeAccuracy(trainingVillain, testingVillain, "isVillain", numIters);
                System.out.println("Accuracy on testing data: " + accuracy);
            }
            catch (InstantiationException | InvocationTargetException
                   | NoSuchMethodException | IllegalAccessException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

        /**
         * #5 - testing the accuracy of the provided Mushrooms dataset
         */
        @Test
        public void testMushroomData(){
            //set up training data
            String mushroomTraining = "data/mushrooms/training.csv";
            List<Row> trainMushrooms = DecisionTreeCSVParser.parse(mushroomTraining);
            List<String> trainMushroomAttributes = new ArrayList<>(trainMushrooms.get(0).getAttributes());
            Dataset trainingMushroom = new Dataset(trainMushroomAttributes, trainMushrooms,
                    AttributeSelection.DESCENDING_ALPHABETICAL);

            //set up testing data
            String mushroomTesting = "data/mushrooms/testing.csv";
            List<Row> testMushrooms = DecisionTreeCSVParser.parse(mushroomTesting);
            List<String> testMushroomAttributes = new ArrayList<>(testMushrooms.get(0).getAttributes());
            Dataset testingMushroom = new Dataset(testMushroomAttributes, testMushrooms,
                    AttributeSelection.DESCENDING_ALPHABETICAL);

            DecisionTreeTester<TreeGenerator, Dataset> tester;
            try {
                tester = new DecisionTreeTester<>(TreeGenerator.class, Dataset.class);
                double accuracy =
                        tester.getDecisionTreeAccuracy(trainingMushroom, trainingMushroom, "isPoisonous");
                System.out.println("Accuracy on training data: " + accuracy);
                int numIters = 100;
                accuracy = tester.getAverageDecisionTreeAccuracy(trainingMushroom, testingMushroom, "isPoisonous", numIters);
                System.out.println("Accuracy on testing data: " + accuracy);
            }
            catch (InstantiationException | InvocationTargetException
                   | NoSuchMethodException | IllegalAccessException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

