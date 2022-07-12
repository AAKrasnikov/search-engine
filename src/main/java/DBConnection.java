import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
//            connection.createStatement().execute("DROP TABLE IF EXISTS field");
//            connection.createStatement().execute("CREATE TABLE field(" +
//                    "id INT NOT NULL AUTO_INCREMENT primary key, " +
//                    "name VARCHAR(255) NOT NULL, " +
//                    "selector VARCHAR(255) NOT NULL, " +
//                    "weight FLOAT NOT NULL)");
//            connection.createStatement().execute("INSERT INTO field(name, selector, weight) " +
//                    "VALUES('title', 'title', 1.0), ('body', 'body', 0.8)");
//
//            connection.createStatement().execute("DROP TABLE IF EXISTS page");
//            connection.createStatement().execute("CREATE TABLE page(" +
//                    "id INT NOT NULL AUTO_INCREMENT primary key, " +
//                    "path TEXT NOT NULL, " +
//                    "code INT NOT NULL, " +
//                    "content MEDIUMTEXT NOT NULL)");
//
//            connection.createStatement().execute("DROP TABLE IF EXISTS lemma");
//            connection.createStatement().execute("CREATE TABLE lemma(" +
//                    "id INT NOT NULL AUTO_INCREMENT primary key, " +
//                    "lemma VARCHAR(255) NOT NULL, " +
//                    "frequency INT NOT NULL)");
//
//            connection.createStatement().execute("DROP TABLE IF EXISTS indexi");
//            connection.createStatement().execute("CREATE TABLE indexi(" +
//                    "id INT NOT NULL AUTO_INCREMENT primary key, " +
//                    "page_id INT NOT NULL, " +
//                    "lemma_id INT NOT NULL, " +
//                    "rankr FLOAT NOT NULL)");

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

//    public void createLemmBD() throws SQLException, IOException {
//        for (int id = 1; id == 0; id++) {
//            String contentSQL = "SELECT content FROM page WHERE id = " + id;
//            ResultSet rs = connection.createStatement().executeQuery(contentSQL);
//            String content = rs.getString(1);
//            Document document = Jsoup.parse(content);
//
//            String resText = document.select("title").text() + document.select("body").text();
//            List<String> listWords = new ArrayList<>();
//            Pattern pattern = Pattern.compile("[а-яА-ЯЁё]+");
//            Matcher matcher = pattern.matcher(resText);
//            while (matcher.find()) {
//                String word = resText.substring(matcher.start(), matcher.end());
//                listWords.add(word);
//            }
//
//            //listWords коллекция со словами которую очищает от лишнего с помощь getLemms
//            Set<String> setWords = Lemmatizator.getLemms(listWords);
//
//            int countLemms = setWords.size();
//
//
//            //Перебор setWords поиск вхождений в таблице
//            for(String s : setWords) {
//                String searchSQL = "SELECT * FROM lemm WHERE lemma = '" + s + "'";
//                ResultSet rsSearchSQL = connection.createStatement().executeQuery(searchSQL);
//                if(rsSearchSQL.next()) {
//                    //вставка в бд если список пустой то false если нашел значение в таблице то true значит нужно увеличить френси
//                } else {
//                    // если не нашел в таблице лемму то нужно ее вставить
//                }
//                // найти место для заполнения таблицы индекс и реализовать ее заполнение
//            }
 //      }
 //   }
}