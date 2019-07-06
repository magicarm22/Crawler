package ML;

import Crawler.Elastic;

public class Main {
    public static void main(String[] args) throws Exception {
        Elastic elastic = new Elastic();
        CSV_creator csv = new CSV_creator();
        MachineLearning ml = new MachineLearning();

        csv.create_CSV(elastic, "./classification.csv", 1);
        csv.convert_csv_to_arff("./classification.csv", "./ready_data.arff");
        ml.training("./ready_data.arff", "./NaiveBayes.model");
        csv.create_CSV(elastic, "./test.csv", 0);
        csv.convert_csv_to_arff("./test.csv", "./test_data.arff");
        ml.test_model("./NaiveBayes.model", "./test_data.arff");
    }
}
