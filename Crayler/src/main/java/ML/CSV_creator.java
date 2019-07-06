package ML;

import Crawler.Elastic;
import com.opencsv.CSVWriter;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CSV_creator {
    public void create_CSV(Elastic elastic, String path, int isCategory) throws Exception {

        File file = new File(path);
        FileWriter outputfile = new FileWriter(file);

        CSVWriter writer = new CSVWriter(outputfile);

        String[] header = {"article", "category"};
        writer.writeNext(header);
        Map<String, Object> document = elastic.download_next_doc_from_elastic();
        while (document != null) {
            if ((document.get("category").equals("other") && isCategory == 1) ||
                    !document.get("category").equals("") && isCategory == 0) {
                document = elastic.download_next_doc_from_elastic();
                continue;
            }
            // Prepare data

            String article = document.get("article").toString();

            PrepareData data = new PrepareData();
            List<String> prepared_data = data.prepareData(article);

            List<String> row = new ArrayList<>();
            row.add(String.join(" ", prepared_data));
            if (isCategory == 1)
                row.add((String) document.get("category"));
            else if (isCategory == 0)
                row.add("?");
            writer.writeNext(row.toArray(new String[0]));

            document = elastic.download_next_doc_from_elastic();
        }
        writer.close();
    }

    void convert_csv_to_arff(String path, String filename) throws IOException {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(path));
        Instances data = loader.getDataSet();

        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(filename));
        saver.writeBatch();
    }

}
