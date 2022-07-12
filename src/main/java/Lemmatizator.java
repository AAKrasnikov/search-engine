import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lemmatizator {
    private String text;

    public Lemmatizator(String text) {
        this.text = text;
    }

    public Map<String, Integer> getLemmm() throws IOException {
        LuceneMorphology luceneMorph = new RussianLuceneMorphology();
        //разбиваем текст на слова
        String[] arrWords = text.split(" ");
        String[] arrWordsNew = new String[arrWords.length];

        //очищаем от знаков препинания и заглавных букв
        for (int i = 0; i < arrWords.length; i++) {
            arrWordsNew[i] = clearWord(arrWords[i]);
        }

        //очищаем от СОЮЗ ПРЕДЛ МЕЖД ЧАСТ
        Map<String, Integer> mWords = new HashMap<>();
        Pattern pattern = Pattern.compile("^[А-Яа-яЁё]*");
        Pattern pattern1 = Pattern.compile("СОЮЗ|ПРЕДЛ|МЕЖД|ЧАСТ");

        for (String w : arrWordsNew) {
            List<String> wordBaseForms = luceneMorph.getMorphInfo(w);
            String s = wordBaseForms.get(0);
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                String key = s.substring(matcher.start(), matcher.end());

                //проверить есть ли лемма в карте, если да то прибавить кол-во
                Matcher matcher1 = pattern1.matcher(s);
                if (!matcher1.find()) {
                    mWords.put(key, mWords.containsKey(key) ? mWords.get(key) + 1 : 1);
                }
            }
        }
        return mWords;
    }

    public static String clearWord(String word) {
        return word.toLowerCase().replaceAll("\\p{Punct}", "");
    }
}
