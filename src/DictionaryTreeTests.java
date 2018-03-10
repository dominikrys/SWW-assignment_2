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

}
