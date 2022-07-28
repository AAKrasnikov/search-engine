import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lemmatizator {
    private static final Pattern patternCheckRus = Pattern.compile("^[А-Яа-яЁё]*");
    private static final Pattern patternCheckParts = Pattern.compile("СОЮЗ|ПРЕДЛ|МЕЖД|ЧАСТ");


    public static Map<String, Integer> getLemmm(String text) throws IOException {
        LuceneMorphology luceneMorph = new RussianLuceneMorphology();

        String[] separateWords = text.split(" ");  //разбиваем текст на слова
        List<String> pureWords = new ArrayList<>();

        //очищаем от знаков препинания и заглавных букв
        for (int i = 0; i < separateWords.length; i++) {
            String word = separateWords[i];
            Matcher matcher = patternCheckRus.matcher(word);
            if (matcher.matches()) {
                pureWords.add(clearWord(word));
            }
        }

        //очищаем от СОЮЗ ПРЕДЛ МЕЖД ЧАСТ
        Map<String, Integer> resultMap = new HashMap<>();
        for (String pw : pureWords) {
            List<String> wordBaseForms = luceneMorph.getMorphInfo(pw);
            String s = wordBaseForms.get(0);
            Matcher matcher1 = patternCheckParts.matcher(s);
            if (!matcher1.find()) {
                resultMap.put(pw, resultMap.containsKey(pw) ? resultMap.get(pw) + 1 : 1); //ПОЧЕМУ ТО ИНОГДА ПРОЛЕТАЮ ПРЕДЛОГИ
            }
        }
        return resultMap;
    }

    public static String clearWord(String word) {
        return word.toLowerCase().replaceAll("\\p{Punct}", "");
    }
}
