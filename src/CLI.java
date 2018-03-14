import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Kelsey McKenna
 */
public class CLI {

    /**
     * Loads words (lines) from the given file and inserts them into
     * a dictionary.
     *
     * @param f the file from which the words will be loaded
     * @return the dictionary with the words loaded from the given file
     * @throws IOException if there was a problem opening/reading from the file
     */
    static DictionaryTree loadWords(File f) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String word;
            DictionaryTree d = new DictionaryTree();
            int wordLine = 1;
            while ((word = reader.readLine()) != null) {
                d.insert(word, wordLine);
                wordLine++;
            }

            return d;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.print("Loading dictionary ... ");
        DictionaryTree d = loadWords(new File(args[0]));
        System.out.println("done");
        System.out.println(d.size());

//        System.out.println("Enter prefixes for prediction below.");

//        try (BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in))) {
//            while (true) {
//                System.out.println("---> " + d.predict(fromUser.readLine()), 3);
//            }
//        }
//        
//        DictionaryTree newTree = new DictionaryTree();
//        newTree.insert("abecawdlo");
//        newTree.insert("bdeadad");
        
//        System.out.println(d.size());
        //223875
        //223878
//        DictionaryTree unit = new DictionaryTree();
//        unit.insert("info");
//        unit.insert("information");
//        unit.remove("information");
//        System.out.println(unit.contains("info"));
//        System.out.println(d.allWords().size());
        
//        DictionaryTree unit = new DictionaryTree();
//        unit.insert("abcg", 9);
//        unit.insert("abca", 1);
//        unit.insert("abcc", 3);
//        unit.insert("abcd", 3);
//        unit.insert("abcf", 8);
//        unit.insert("abch", 0);
//        unit.insert("abcb", 2);
//        unit.insert("abce", 5);
//        
//        List<String> returned = d.predict("ph", 5);
//        
//        for (String s : returned) {
//          System.out.println(s);
//        }
        
        DictionaryTree unit = new DictionaryTree();
        unit.insert("information");
        System.out.println(unit.predict("info"));
    }

}
