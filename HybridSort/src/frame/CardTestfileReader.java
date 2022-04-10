package frame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import lab.Card;

/**
 * Do NOT change anything in this class!
 * This class contains a method for reading the input files into a Card class.
 */

public class CardTestfileReader {

    private String filename = null;

    /**
     * The file should be in the same directory as the java application. if not,
     * you have to provide the absolute or relative path information within the
     * filename string.
     *
     * @param filename the name of the file to read
     */
    public CardTestfileReader(String filename) {
        this.filename = filename;
    }

    /**
     * Reads a file, specified in the private field filename and returns the
     * information read. The file should have the same format as specified in
     * the first lab.
     *
     * @return Returns a Vector which holds the Card objects. In the case
     * an error occurred we throw a RuntimeException.
     */
    public ArrayList<Card> readFile() {
        try {
            ArrayList<Card> cards = new ArrayList<Card>();
            FileReader fr = new FileReader(filename);
            BufferedReader in = new BufferedReader(fr);

            String line;
            while ((line = in.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                String[] data = line.split(";");
                Card card = new Card();
                card.value = Integer.parseInt(data[0]);
                card.suit = parseSuit(data[1]);
                cards.add(card);
            }

            in.close();
            fr.close();

            return cards;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Testfile is broken!");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Testfile is broken!");
        }
    }

    private Card.Suit parseSuit(String suit) {
        suit = suit.trim();
        if (suit.contentEquals("Hearts")) {
            return Card.Suit.Hearts;
        }
        if (suit.contentEquals("Diamonds")) {
            return Card.Suit.Diamonds;
        }
        if (suit.contentEquals("Clubs")) {
            return Card.Suit.Clubs;
        }
        if (suit.contentEquals("Spades")) {
            return Card.Suit.Spades;
        }
        throw new RuntimeException("Testfile contains illegal suit " + suit + "!");
    }
}
