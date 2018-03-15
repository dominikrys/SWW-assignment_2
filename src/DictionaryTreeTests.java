import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class DictionaryTreeTests {

  @Test
  public void treeContainsWordAferInsertion() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("example");
    Assertions.assertTrue(unit.contains("example"));
  }

  @Test
  public void numLeavesReturnsOneIfNoWordsInserted() {
    DictionaryTree unit = new DictionaryTree();
    assertEquals(1, unit.numLeaves());
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
  public void sizeReturnsRootNodeWhenNoWordsInserted() {
    DictionaryTree unit = new DictionaryTree();
    Assertions.assertEquals(1, unit.size());
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
  public void predictCanReturnItself() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("information");
    assertEquals(Optional.of("information"), unit.predict("information"));
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
  public void removeRemovesWordThatsPartOfAnotherWordWithoutRemovingTheOtherWord() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("info");
    unit.insert("information");
    unit.remove("info");
    Assertions.assertTrue(unit.contains("information"));
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

  @Test
  public void sizeWordsAsIntendedAfterWordRemoved() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("information");
    unit.remove("information");
    assertEquals(1, unit.size());
  }

  @Test
  public void predictWithPopularityReturnsObjectsAccordingToPopularity() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("phone", 484);
    unit.insert("photo", 247);
    unit.insert("pile", 37);
    unit.insert("test");
    unit.insert("phones", 8);
    unit.insert("physical", 4);
    unit.insert("photos", 90);

    ArrayList<String> predictedWords = (ArrayList<String>) unit.predict("ph", 5);
    ArrayList<String> correctList = new ArrayList<String>();
    correctList.add("phone");
    correctList.add("photo");
    correctList.add("photos");
    correctList.add("phones");
    correctList.add("physical");

    assertEquals(correctList, predictedWords);
  }

  @Test
  public void predictReturnsEmptyListIfNoWordFound() {
    DictionaryTree unit = new DictionaryTree();
    ArrayList<String> emptyArrayList = new ArrayList<String>();
    assertEquals(emptyArrayList, unit.predict("word", 4));
  }

  @Test
  public void insertOverridesPopularity() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("word", 10);
    unit.insert("wor", 5);
    unit.insert("word", 1);

    ArrayList<String> correctList = new ArrayList<String>();
    correctList.add("wor");
    correctList.add("word");

    assertEquals(correctList, unit.predict("wo", 2));
  }

  @Test
  public void removeCorrectlyRemovesOneLetterWords() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("m");
    unit.insert("mat");
    unit.insert("mad");
    unit.remove("m");
    Assertions.assertEquals(false, unit.contains("m"));
    Assertions.assertEquals(true, unit.contains("mat"));
    Assertions.assertEquals(true, unit.contains("mad"));
  }


  @Test
  public void removeCorrectlyRemovesLastLetterOfAWord() {
    DictionaryTree unit = new DictionaryTree();
    unit.insert("word");
    unit.insert("word1");
    int beforeSize = unit.size();
    unit.remove("word1");
    int afterSize = unit.size();
    Assertions.assertEquals(false, unit.contains("word1"));
    Assertions.assertEquals(1, beforeSize - afterSize);
  }
}
