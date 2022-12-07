package it.unipi.dsmt.jakartaee.lab_09_ejb;

import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.interfaces.QuotesEJB;
import jakarta.ejb.Stateless;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

@Stateless
public class QuotesEJBImpl implements QuotesEJB {

    private List<String> quotes;
    private Random random = new Random();

    public QuotesEJBImpl(){
        init();
    }

    public void init() {
        try {
            String quotesTxtFile = "data/quotes.txt";
            ClassLoader classLoader = QuotesEJBImpl.class.getClassLoader();
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

    @Override
    public String pickOneQuote() {
        int randomIndex = random.nextInt(quotes.size());
        return quotes.get(randomIndex);
    }
}
