package it.unipi.dsmt.javaee.lab_06.dao;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

public class QuotesDAO {

    private static List<String> quotes;
    private static Random random = new Random();

    public static void init() {
        try {
            String quotesTxtFile = "data/quotes.txt";
            ClassLoader classLoader = QuotesDAO.class.getClassLoader();
            URL resource = classLoader.getResource(quotesTxtFile);
            File txtFile = new File(resource.toURI());
            quotes = Files.readAllLines(txtFile.toPath(), StandardCharsets.UTF_8);
            System.out.println("Quotes were loaded.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String pickOneQuote(){
        int randomIndex = random.nextInt(quotes.size());
        return quotes.get(randomIndex);
    }
}
