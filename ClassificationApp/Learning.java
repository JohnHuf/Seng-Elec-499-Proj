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
public class Learning {

	Instances trainData;
    Evaluation evaluation;
	RandomForest rf;

	public Learning() throws Exception {
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
