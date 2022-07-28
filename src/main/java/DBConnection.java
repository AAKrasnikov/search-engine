import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.print.Doc;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBConnection {
    private static String url = "jdbc:mysql://localhost:3306/search_engine";
    private static String dbUser = "root";
    private static String dbPass = "CgZAp1KD";
    private Connection connection;
    private Set<SitePage> sitePageSet;

    public DBConnection() {
        this.connection = getConnection();
    }

    public DBConnection(Set<SitePage> sitePageSet) throws SQLException {
        this.connection = getConnection();
        this.sitePageSet = sitePageSet;
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, dbUser, dbPass);
            connection.createStatement().execute("DROP TABLE IF EXISTS field");
            connection.createStatement().execute("CREATE TABLE field(" +
                    "id INT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "selector VARCHAR(255) NOT NULL, " +
                    "weight FLOAT NOT NULL" +
                    "PRIMARY KEY (id))");
            connection.createStatement().execute("INSERT INTO field(name, selector, weight) " +
                    "VALUES('title', 'title', 1.0), ('body', 'body', 0.8)");
            connection.createStatement().execute("DROP TABLE IF EXISTS page");
            connection.createStatement().execute("CREATE TABLE page(" +
                    "id INT NOT NULL AUTO_INCREMENT, " +
                    "path TEXT NOT NULL, " +
                    "code INT NOT NULL, " +
                    "content MEDIUMTEXT NOT NULL," +
                    "PRIMARY KEY (id))");
            connection.createStatement().execute("DROP TABLE IF EXISTS lemma");
            connection.createStatement().execute("CREATE TABLE lemma(" +
                    "id INT NOT NULL AUTO_INCREMENT, " +
                    "lemma VARCHAR(255) NOT NULL, " +
                    "frequency INT NOT NULL," +
                    "PRIMARY KEY (id)," +
                    "UNIQUE(lemma))");
            connection.createStatement().execute("DROP TABLE IF EXISTS indexi");
            connection.createStatement().execute("CREATE TABLE indexi(" +
                    "id INT NOT NULL AUTO_INCREMENT, " +
                    "page_id INT NOT NULL, " +
                    "lemma_id INT NOT NULL, " +
                    "rankr FLOAT NOT NULL," +
                    "PRIMARY KEY (id))");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void insert() {
        String sql = "INSERT INTO page(path, code, content) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (SitePage sp : sitePageSet) {
                preparedStatement.setString(1, sp.getPatch());
                preparedStatement.setInt(2, sp.getCode());
                preparedStatement.setString(3, sp.getContent());
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void parsePageFromBD() throws SQLException {
        String insertIntoLemm = "insert into lemma (lemma, frequency) " +
                "values(?, ?) " +
                "on DUPLICATE KEY UPDATE frequency = frequency + 1;";
        PreparedStatement preparedStatement = connection.prepareStatement(insertIntoLemm);

        ResultSet pages = connection.createStatement().executeQuery("SELECT content FROM page");
        while (pages.next()) {
            String content = pages.getString(1);
            Document document = Jsoup.parse(content);
            String contentTitle = document.select("title").text();
            String contentBody = document.select("body").text();

            try {
                Map<String, Integer> lemmTitle = Lemmatizator.getLemmm(contentTitle);
                Map<String, Integer> lemmBody = Lemmatizator.getLemmm(contentBody);

                //добавлять уникальные значения слов в табл lemma, если слово встречается в БД то увеличить значение frequency
                insertIntoLemma(lemmTitle);
                insertIntoLemma(lemmBody);

                //расчитать rank для каждого слова в Map
                //добавить связку в табл indexi между page и lemma + добавить значение rank

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //метод берет лемму и вставляет ее в lemm таблицу в бд
    //if selector = 1 => title, if selector = 0.8 => body
    public void insertIntoLemma(Map<String, Integer> map) throws SQLException {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            connection.createStatement().execute("insert into lemma (lemma, frequency) " +
                    "values(" + key + ", 1) " +
                    "on DUPLICATE KEY UPDATE frequency = frequency + 1;");
        }
    }

    //расчитывает rank
    public static double calculateRank(int countLemm, int selector) {
        return 0;
    }

    //вставляет в indexi связь между page и lemma
    public static void insertIntoIndei() {

    }


    //пример с вставкой в табл, с проверкой о наличии записи
    //insert into test (lemma, frequency) values('сестра', 1) on DUPLICATE KEY UPDATE frequency = frequency + 1;
}