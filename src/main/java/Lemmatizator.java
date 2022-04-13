import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lemmatizator {


    public static Set<String> getLemms(List<String> list) throws IOException {
        Set<String> result = new HashSet<>();
        LuceneMorphology luceneMorphology = new RussianLuceneMorphology();
        for (String s : list) {
            List<String> wordBaseForms = luceneMorphology.getMorphInfo(s);
            String wordInfo = wordBaseForms.get(0);
            boolean b = wordInfo.contains("ЧАСТ") || wordInfo.contains("МЕЖД") || wordInfo.contains("СОЮЗ") || wordInfo.contains("ПРЕДЛ") || s.length() < 3;
            if (!b) {
                result.add(s);
            }
        }
        return result;
    }
}
