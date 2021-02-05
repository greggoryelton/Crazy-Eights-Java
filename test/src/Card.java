public class Card {
    public static char[] suits = {'C', 'D', 'H', 'S'};
    public static String[] values = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};

    public int suit, value;

    public Card(){
        this.suit = 0;
        this.value =0;
    }

    public Card(int s, int v){
        this.suit = s;
        this.value = v;

    }

    public String toString(){
        return suits[suit] + values[value];

    }

    public static void main(String[] args) {
        Card c = new Card();
        Card c1 = new Card(1,2);
        System.out.println(c.toString());
        System.out.println(c1.toString());
    }





}
