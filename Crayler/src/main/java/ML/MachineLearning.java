package ML;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.BufferedReader;
import java.io.FileReader;

public class MachineLearning {

    void training(String path, String modelPath) throws Exception {
        BufferedReader datafile = new BufferedReader(new FileReader(path));

        Instances data = new Instances(datafile);
        data.setClassIndex(data.numAttributes() - 1);

        Instances[][] split = new Instances[2][6];

        for (int i = 0; i < 6; i++) {
            split[0][i] = data.trainCV(6, i);
            split[1][i] = data.testCV(6, i);
        }
        Instances[] trainingSplits = split[0];
        Instances[] testingSplits = split[1];
        StringToWordVector filter = new StringToWordVector();
        FilteredClassifier classifier = new FilteredClassifier();
        classifier.setFilter(filter);
        classifier.setClassifier(new NaiveBayes());
        for (int i = 0; i < trainingSplits.length; i++) {
            classifier.buildClassifier(trainingSplits[i]);
            Evaluation evaluation = new Evaluation(trainingSplits[i]);

            evaluation.evaluateModel(classifier, testingSplits[i]);
            System.out.println(evaluation.toSummaryString());

            weka.core.SerializationHelper.write(modelPath, classifier);
        }
    }

    void test_model(String model, String path) throws Exception {
        Classifier cls = (Classifier) weka.core.SerializationHelper.read(model);

        ConverterUtils.DataSource source2 = new ConverterUtils.DataSource(path);
        Instances test = source2.getDataSet();

        if (test.classIndex() == -1)
            test.setClassIndex(1);

        for (int j = 0; j < test.size(); j++) {
            double[] prediction = cls.distributionForInstance(test.get(j));

            System.out.println(test.get(j));
            for (int i = 0; i < prediction.length; i = i + 1) {
                System.out.println("Probability of class " +
                        test.classAttribute().value(i) +
                        " : " + Double.toString(prediction[i]));
            }
        }
    }
}
