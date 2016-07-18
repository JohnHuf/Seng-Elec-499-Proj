/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.RandomForest;
  
import weka.core.Instances;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Attribute;
 
/**
 *
 * @author samy
 */
public class learning {

	Instances trainData;
        Evaluation evaluation;
	RandomForest rf;

	public learning() throws Exception {
		BufferedReader br = null;
		int numFolds = 10;
		br = new BufferedReader(new FileReader("punch_data.arff"));
	 
		// Create Instances object to store data instances
		trainData = new Instances(br);
		trainData.setClassIndex(trainData.numAttributes() - 1);
		br.close();

		// Instantiate, train, and validate classifier
		rf = new RandomForest();
		rf.setBreakTiesRandomly(true);
		System.out.println("Running");
		rf.buildClassifier(trainData);
		evaluation = new Evaluation(trainData);
		evaluation.crossValidateModel(rf, trainData, numFolds, new Random(1));

	}
	

	
 
    public static void main(String[] args) throws Exception {
	
	double[] data = new double[37];
	data[0] = 90.0;
	data[1] = 1;
	data[2] = 40.0;
	data[3] = 1;
	data[4] = 40.0;
	data[5] = 0;
	data[6] = 123.0;
	data[7] = 5;
	data[8] = 127.0;
	data[9] = 2;
	data[10] = 23.0;
	data[11] = 32;
	data[12] = 127.0;
	data[13] = 14;
	data[14] = 127.0;
	data[15] = 1;
	data[16] = 127.0;
	data[17] = 14;
	data[18] = 1;
	data[19] = 40.0;
	data[20] = 1;
	data[21] = 40.0;
	data[22] = 0;
	data[23] = 123.0;
	data[24] = 5;
	data[25] = 127.0;
	data[26] = 2;
	data[27] = 23.0;
	data[28] = 32;
	data[29] = 127.0;
	data[30] = 14;
	data[31] = 127.0;
	data[32] = 1;
	data[33] = 127.0;
	data[34] = 14;
	data[35] = 1;
	data[36] = 69; //Placeholder to be set as missing later


	learning model = new learning();
	//model.buildARFF();
	String sim = model.getFit(data);

	// Test classifying a reading
	System.out.println("TEST");

	//Load data for instance to be classified
	System.out.println("The test instance was classified as a: " + sim);







    }

    // Run the python script to build the .arff file
    public static void buildARFF() throws Exception {
        Process pyscript = Runtime.getRuntime().exec("python learning_Sophisticated.py");
	pyscript.waitFor();
    }

    public String getFit(double[] data) throws Exception {

        // Create new instance to be classified
	Instance inst = new DenseInstance(37);

	// Set instance's dataset to be the dataset "trainData" (the punches one) 
	inst.setDataset(trainData);

	// Fill the instance with data (metrics from incoming punch)
	inst.replaceMissingValues(data);

	// Set the class (punch type) to missing
	inst.setMissing(36);

	// CLASSIFY!
	double classVal = rf.classifyInstance(inst);

	// Set class value
	inst.setClassValue(classVal);

	return inst.stringValue(36);
    }
}
