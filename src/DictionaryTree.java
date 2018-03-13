import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class DictionaryTree {

  private Map<Character, DictionaryTree> children = new LinkedHashMap<>();
  private Optional<Integer> popularity;
  private boolean endOfWord;

  /*
   * The constructor
   */
  DictionaryTree() {
    popularity = Optional.empty();
    this.endOfWord = false;
  }

  /**
   * Inserts the given word into this dictionary. If the word already exists, nothing will change.
   *
   * @param word the word to insert
   */
  void insert(String word) {
    if (!contains(word)) {
      DictionaryTree tempTree = new DictionaryTree();
      if (word.length() > 0) {

        if (children.containsKey(word.charAt(0))) {
          tempTree = children.get(word.charAt(0));
        }

        tempTree.insert(word.substring(1, word.length()));

        if (word.length() == 1) {
          tempTree.setEndOfWord(true);
        }

        children.put(word.charAt(0), tempTree);
      }
    }
  }

  void setEndOfWord(boolean input) {
    this.endOfWord = input;
  }

  boolean isEndOfWord() {
    return endOfWord;
  }

  /**
   * Inserts the given word into this dictionary with the given popularity. If the word already
   * exists, the popularity will be overriden by the given value.
   *
   * @param word the word to insert
   * @param popularity the popularity of the inserted word
   */
  void insert(String word, int popularity) {
    if (!contains(word)) {
      DictionaryTree tempTree = new DictionaryTree();
      if (word.length() > 0) {

        if (children.containsKey(word.charAt(0))) {
          tempTree = children.get(word.charAt(0));
        }

        tempTree.insert(word.substring(1, word.length()));

        if (word.length() == 1) {
          tempTree.setEndOfWord(true);
          tempTree.popularity = Optional.of(popularity);
        }

        children.put(word.charAt(0), tempTree);
      }
    }
  }

  /**
   * Removes the specified word from this dictionary. Returns true if the caller can delete this
   * node without losing part of the dictionary, i.e. if this node has no children after deleting
   * the specified word.
   *
   * @param word the word to delete from this dictionary
   * @return whether or not the parent can delete this node from its children
   */
  boolean remove(String word) {
    if (contains(word)) {
      int removeIndex = removeHelper(word, 0, 0);
      if (removeIndex != -1) {
        removeRemover(word, removeIndex, 0);
      }
      return true;
    } else {
      return false;
    }
  }

  int removeHelper(String word, int indexOfLastEndOfWord, int currentIndex) {
    int result = -1;
    if (word.length() > 1) {
      for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
        Character key = entry.getKey();
        DictionaryTree value = entry.getValue();

        if (key.equals(word.charAt(0))) {
          if (value.isEndOfWord()) {
            result = value.removeHelper(word.substring(1, word.length()), currentIndex, currentIndex + 1);
          } else {
            result = value.removeHelper(word.substring(1, word.length()), indexOfLastEndOfWord,
                currentIndex + 1);
          }
          break;
        }
      }
    } else {
      if (this.isLeaf() || this.endOfWord == false) {
        result = indexOfLastEndOfWord;
      } else {
        this.setEndOfWord(false);
        result = -1;
      }
    }
    return result;
  }

  void removeRemover(String word, int indexOfLastEndOfWord /* the index to remove after */,
      int currentIndex) {
    if (indexOfLastEndOfWord < currentIndex) {
      children.clear();
    } else {
      for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
        Character key = entry.getKey();
        DictionaryTree value = entry.getValue();
        if (key.equals(word.charAt(0))) {
          value.removeRemover(word.substring(1, word.length()), indexOfLastEndOfWord,
              currentIndex + 1);
        }
      }
    }
  }

  /**
   * Determines whether or not the specified word is in this dictionary.
   *
   * @param word the word whose presence will be checked
   * @return true if the specified word is stored in this tree; false otherwise
   */
  boolean contains(String word) {
    boolean containsWord = false;
    if (word.length() > 0) {
      // Check if first letter is a child node
      if (children.containsKey(word.charAt(0))) {
        if (word.length() > 1) {
          DictionaryTree extractedDictionaryTree = children.get(word.charAt(0));
          containsWord = extractedDictionaryTree.contains(word.substring(1, word.length()));
        }
        // If only one character left, check if end of word
        else if (children.get(word.charAt(0)).isEndOfWord() == true) {
          containsWord = true;
        } else {
          containsWord = false;
        }
      } else {
        containsWord = false;
      }
    }

    return containsWord;
  }

  /**
   * @param prefix the prefix of the word returned
   * @return a word that starts with the given prefix, or an empty optional if no such word is
   *         found.
   */
  Optional<String> predict(String prefix) {
    return predictHelper(prefix, prefix);
  }

  Optional<String> predictHelper(String inputString, String initialString) {
    Optional<String> result = Optional.empty();
    if (inputString.length() > 0) {
      for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
        Character key = entry.getKey();
        DictionaryTree value = entry.getValue();
        if (key.equals(inputString.charAt(0))) {
          result =
              value.predictHelper(inputString.substring(1, inputString.length()), initialString);
          break;
        }
      }
    }
    // adequate size to check any word
    else {
      result = predictStringBuilder(initialString);
    }

    return result;
  }

  Optional<String> predictStringBuilder(String inputString) {
    Optional<String> result = Optional.empty();

    for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
      Character key = entry.getKey();
      DictionaryTree value = entry.getValue();

      if (value.isEndOfWord()) {
        result = Optional.of(inputString + key);
        break;
      } else {
        result = value.predictStringBuilder(inputString + key);
        break;
      }
    }

    return result;
  }


  /**
   * Predicts the (at most) n most popular full English words based on the specified prefix. If no
   * word with the specified prefix is found, an empty Optional is returned.
   *
   * @param prefix the prefix of the words found
   * @return the (at most) n most popular words with the specified prefix
   */
  List<String> predict(String prefix, int n) {
    throw new RuntimeException("DictionaryTree.predict not implemented yet");
  }

  /**
   * @return the number of leaves in this tree, i.e. the number of words which are not prefixes of
   *         any other word.
   */
  int numLeaves() {
    int leavesNo = 0;

    if (!children.isEmpty()) {
      for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
        DictionaryTree value = entry.getValue();

        if (value.isLeaf()) {
          leavesNo += 1;
        } else {
          leavesNo += value.numLeaves();
        }
      }
    }

    return leavesNo;
  }

  /**
   * @return the maximum number of children held by any node in this tree
   */
  int maximumBranching() {
    int maxBranch = children.size();

    for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
      DictionaryTree value = entry.getValue();

      if (value.maximumBranching() > maxBranch) {
        maxBranch = value.maximumBranching();
      }
    }

    return maxBranch;
  }

  /**
   * @return the height of this tree, i.e. the length of the longest branch
   */
  int height() {
    return longestWord().length();
  }

  /**
   * @return the number of nodes in this tree
   */
  int size() {
    int size = 1;

    for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
      DictionaryTree value = entry.getValue();

      size += value.size();
    }

    return size;
  }

  /**
   * @return the longest word in this tree
   */
  String longestWord() {
    return longestHelper("");
  }

  String longestHelper(String inputString) {
    ArrayList<String> words = new ArrayList<String>();
    for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
      Character key = entry.getKey();
      DictionaryTree value = entry.getValue();

      if (value.isLeaf() == true) {
        words.add(inputString + key);
      } else {
        words.add(value.longestHelper(inputString + key));
      }
    }

    for (String s : words) {
      if (s.length() > inputString.length()) {
        inputString = s;
      }
    }

    return inputString;
  }

  /**
   * @return all words stored in this tree as a list
   */
  List<String> allWords() {

    return allWordsHelper("");
  }

  ArrayList<String> allWordsHelper(String inputString) {
    ArrayList<String> stringList = new ArrayList<String>();

    for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
      Character key = entry.getKey();
      DictionaryTree value = entry.getValue();

      if (value.isLeaf() == true) {
        stringList.add(inputString + key);
      } else if (value.isEndOfWord()) {
        stringList.add(inputString + key);
        stringList.addAll(value.allWordsHelper(inputString + key));
      } else {
        stringList.addAll(value.allWordsHelper(inputString + key));
      }
    }

    return stringList;
  }

  /**
   * Determines whether the input node is a leaf node
   * 
   * @param node which will be checked whether it's a lead node
   * @return true if the passed node is a leaf, false otherwise
   */
  boolean isLeaf() {
    if (children.isEmpty()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 
   * /** Folds the tree using the given function. Each of this node's children is folded with the
   * same function, and these results are stored in a collection, cResults, say, then the final
   * result is calculated using f.apply(this, cResults).
   *
   * @param f the summarising function, which is passed the result of invoking the given function
   * @param <A> the type of the folded value
   * @return the result of folding the tree using f
   */
  <A> A fold(BiFunction<DictionaryTree, Collection<A>, A> f) {
    throw new RuntimeException("DictionaryTree.fold not implemented yet");
  }


}
