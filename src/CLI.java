import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.junit.jupiter.api.Assertions;

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
            while ((word = reader.readLine()) != null) {
                d.insert(word);
            }

            return d;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.print("Loading dictionary ... ");
        DictionaryTree d = loadWords(new File(args[0]));
        System.out.println("done");

        System.out.println("Enter prefixes for prediction below.");

//        try (BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in))) {
//            while (true) {
//                System.out.println("---> " + d.predict(fromUser.readLine()));
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
        System.out.println(d.size());
//        
    }

}
