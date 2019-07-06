package ML;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import weka.core.Stopwords;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class PrepareData {
    public List<String> prepareData(String article){
        article = article.toLowerCase();
        article = article.replaceAll("[^a-zA-Z ]", "");

        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        List<String> lemmas = new LinkedList<>();
        Annotation annot = new Annotation(article);
        pipeline.annotate(annot);
        List<CoreMap> sentences = annot.get(CoreAnnotations.SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                lemmas.add(token.get(CoreAnnotations.LemmaAnnotation.class));
            }
        }

        Stopwords checker = new Stopwords();
        try(BufferedReader br = new BufferedReader(new FileReader("./src/main/resources/stopwords.txt"))) {
            String s;
            while ((s = br.readLine()) != null) {
                checker.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        for(int i = 0; i < lemmas.size(); i++){
            String c = lemmas.get(i);
            if(checker.is(c)){
                lemmas.remove(i);
                i--;
            }
            if(c.contains("http"))
                lemmas.remove(i--);
        }
        System.out.println(lemmas);
        return lemmas;
    }
}
