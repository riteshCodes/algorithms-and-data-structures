package lab;


public class Card {

    // DO NOT MODIFY
    public enum Suit {
        Hearts, Diamonds, Clubs, Spades
    }

    // DO NOT MODIFY
    public int value;
    public Suit suit;

    // DO NOT MODIFY
    public Card() {
    }

    // DO NOT MODIFY

    /**
     * Initialize a Card with a suit and a value
     */
    public Card(int value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    // DO NOT MODIFY

    /**
     * Create a new card from an existing card
     */
    public Card(Card other) {
        this.value = other.value;
        this.suit = other.suit;
    }

    /**
     * Return a printable representation of the card
     */
    public String toString() {
        return value + ";" + suit;
    }


    /**
     * Compare two card objects. Return -1 if this is deemed smaller than the object other, 0 if they are
     * deemed of identical value, and 1 if this is deemed greater than the object other.
     *
     * @param other The object we compare this to.
     * @return -1, 0 or 1
     */
    public int compareTo(Card other) {
        if (this.value < other.value) {
            return -1;
        } else if (this.value > other.value) {
            return 1;
        } else {
            if (compareSuits(this) < compareSuits(other)) return -1;
            else if (compareSuits(this) > compareSuits(other)) return 1;
            else return 0;
        }
    }

    /**
     * Return specific unique value for the different cases of Suit(Diamonds, Hearts, Spades and Clubs),
     * where these values represent the sequence of the cards in ascending order.
     * The sequence of order: Diamonds < Hearts < Spades < Clubs
     *
     * @param other
     * @return 0, 1, 2, 3 or throw an RuntimeException
     */
    private int compareSuits(Card card) {
        switch (card.suit) {
            case Diamonds:
                return 0;
            case Hearts:
                return 1;
            case Spades:
                return 2;
            case Clubs:
                return 3;
            // Default: Exception thrown, when the suit from the given Card doesn't exist.
            default:
                throw new RuntimeException();
        }
    }

}
