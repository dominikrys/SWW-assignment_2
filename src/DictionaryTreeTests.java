import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * @author Kelsey McKenna
 */
public class DictionaryTreeTests {

  @Test
  public void treeContainsWordAferInsertion() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("test word");
    Assertions.assertTrue(unit.contains("test word"));
  }

  @Test
  public void heightOfRootShouldBeZero() {
    DictionaryTree unit = new DictionaryTree();
    Assertions.assertEquals(0, unit.height());
  }

  @Test
  public void heightOfWordShouldBeWordLength() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("word", 0);
    Assertions.assertEquals("word".length(), unit.height());
  }

  @Test
  public void noOfLeavesShouldBeOneAfterSingleWordInsertion() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("word");
    Assertions.assertEquals(1, unit.numLeaves());
  }

  @Test
  public void noOfLeavesShouldBeTwoAfterSecondWordInsertionWithCommonLetters() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("word");
    unit.insert("worg");
    Assertions.assertEquals(2, unit.numLeaves());
  }
  
  @Test
  public void maximumBranchingShouldBeOneAfterSingleWordInsertion() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("word");
    Assertions.assertEquals(1, unit.maximumBranching());
  }
  
  @Test
  public void maximumBranchingShouldBeTwoAfterSecondWordInsertionWithCommonLetters() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("word");
    unit.insert("wdrd");
    Assertions.assertEquals(2, unit.maximumBranching());
  }
  
  @Test
  public void longestWordReturnsTheLongestWord() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("a");
    unit.insert("dec");
    unit.insert("feaddad");
    unit.insert("afaccafa");
    unit.insert("kpnifanifwa");
    unit.insert("kpnfiwfndid");
    unit.insert("dawbdwab");
    unit.insert("dd");
    unit.insert("dwadda");
    unit.insert("dwaddsasawddawda");
    Assertions.assertEquals("dwaddsasawddawda", unit.longestWord());
  }
  
  @Test
  public void allWordsReturnsAllInsertedWords() {
    DictionaryTree unit = new DictionaryTree();
    List<String> insertedWords = new ArrayList<String>();
    insertedWords.add("a");
    insertedWords.add("aawdwda");
    insertedWords.add("aad");
    insertedWords.add("fawfnifane");
    insertedWords.add("mfiawnfuawb");
    insertedWords.add("awffwa");
    insertedWords.add("bd");
    insertedWords.add("ndiawnida");
    insertedWords.add("nfiawnfawin");
    insertedWords.add("dnwiadnwinwandiwdani");
    insertedWords.add("dnwiadnwi");
    
    // Insert words from arraylist
    for (String s : insertedWords) {
      unit.insert(s);
    }
    
    // Take all words from dictionary
    List<String> extractedWords = unit.allWords();
    
    // See how many of the words from the original arraylist are in the DictionaryTree
    int dictContainsWord = 0;
    for (String s : insertedWords) {
      if (extractedWords.contains(s)) {
        dictContainsWord++;
      }
    }
    
    Assertions.assertEquals(dictContainsWord, extractedWords.size());
  }
}
