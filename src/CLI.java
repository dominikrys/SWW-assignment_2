import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Kelsey McKenna
 */
public class CLI {

  /**
   * Loads words (lines) from the given file and inserts them into a dictionary.
   *
   * @param f the file from which the words will be loaded
   * @return the dictionary with the words loaded from the given file
   * @throws IOException if there was a problem opening/reading from the file
   */
  static DictionaryTree loadWords(File f) throws IOException {
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
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

    System.out.println("Enter prefixes for prediction below.");

    try (BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in))) {
      while (true) {
        System.out.println("---> " + d.predict(fromUser.readLine()));
      }
    }

    // DictionaryTree unit = new DictionaryTree();
    // System.out.println(unit.maximumBranching());
  }

}
