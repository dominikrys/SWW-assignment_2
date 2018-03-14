import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
  public void sizeShouldBeAmountOfCharactersAndRootAfterOneInsertion() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("word");
    Assertions.assertEquals(5, unit.size());
  }
  
  @Test
  public void sizeReturnsCorrectAmountOfNodesAfterMultipleInsertions() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("word");
    unit.insert("wors");
    unit.insert("wtty");
    unit.insert("r");
    unit.insert("test");
    Assertions.assertEquals(14, unit.size());
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

  @Test
  public void predictReturnsEmptyIfPrefixNotPartOfAnyWord() {
    DictionaryTree unit = new DictionaryTree();
    assertEquals(Optional.empty(), unit.predict("word"));
  }

  @Test
  public void predictReturnsAPredictedWord() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("information");
    assertEquals(Optional.of("information"), unit.predict("info"));
  }

  @Test
  public void removeRemovesSingleWord() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("word");
    unit.remove("word");
    Assertions.assertFalse(unit.contains("word"));
  }

  @Test
  public void removeReturnsTrueIfWordCanBeRemoved() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("word");
    Assertions.assertTrue(unit.remove("word"));
  }

  @Test
  public void removeReturnsFalseIfWordCantBeRemoved() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("word");
    Assertions.assertFalse(unit.remove("worddwa"));
  }

  @Test
  public void removeRemovesWordThatsPartOfAnotherWordWithoutRemoovingTheOtherWord() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("info");
    unit.insert("information");
    unit.remove("info");
    Assertions.assertFalse(unit.contains("info"));
  }

  @Test
  public void removeRemovesEndOfWordLeavingAnyPreviousWords() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("info");
    unit.insert("information");
    unit.remove("information");
    Assertions.assertTrue(unit.contains("info"));
  }
  
  // include size test
}
