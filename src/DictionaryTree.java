import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
  public void insert(String word) {
    if (!contains(word)) {
      DictionaryTree tempTree = new DictionaryTree();
      if (word.length() > 0) {

        if (children.containsKey(word.charAt(0))) {
          tempTree = children.get(word.charAt(0));
        }

        tempTree.insert(word.substring(1, word.length()));

        if (word.length() == 1) {
          tempTree.endOfWord = true;
        }

        children.put(word.charAt(0), tempTree);
      }
    }
  }

  /**
   * Inserts the given word into this dictionary with the given popularity. If the word already
   * exists, the popularity will be overriden by the given value.
   *
   * @param word the word to insert
   * @param popularity the popularity of the inserted word
   */
  public void insert(String word, int popularity) {
    if (!contains(word)) {
      DictionaryTree tempTree = new DictionaryTree();
      if (word.length() > 0) {

        if (children.containsKey(word.charAt(0))) {
          tempTree = children.get(word.charAt(0));
        }

        tempTree.insert(word.substring(1, word.length()), popularity);

        if (word.length() == 1) {
          tempTree.endOfWord = true;
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
  public boolean remove(String word) {
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

  private int removeHelper(String word, int indexOfLastEndOfWord, int currentIndex) {
    int result = -1;
    if (word.length() > 1) {
      for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
        Character key = entry.getKey();
        DictionaryTree value = entry.getValue();

        if (key.equals(word.charAt(0))) {
          if (value.endOfWord) {
            result = value.removeHelper(word.substring(1, word.length()), currentIndex,
                currentIndex + 1);
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
        this.endOfWord = false;
        result = -1;
      }
    }
    return result;
  }

  private void removeRemover(String word, int indexOfLastEndOfWord /* the index to remove after */,
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
  public boolean contains(String word) {
    boolean containsWord = false;
    if (word.length() > 0) {
      // Check if first letter is a child node
      if (children.containsKey(word.charAt(0))) {
        if (word.length() > 1) {
          DictionaryTree extractedDictionaryTree = children.get(word.charAt(0));
          containsWord = extractedDictionaryTree.contains(word.substring(1, word.length()));
        }
        // If only one character left, check if end of word
        else if (children.get(word.charAt(0)).endOfWord) {
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
  public Optional<String> predict(String prefix) {
    ArrayList<String> returnedList = (ArrayList<String>) predict(prefix, 1);
    if (returnedList.size() == 0) {
      return Optional.empty();
    } else {
      return Optional.of(returnedList.get(0));
    }
  }

  /**
   * Predicts the (at most) n most popular full English words based on the specified prefix. If no
   * word with the specified prefix is found, an empty Optional is returned.
   *
   * @param prefix the prefix of the words found
   * @return the (at most) n most popular words with the specified prefix
   */
  public List<String> predict(String prefix, int n) {
    return predictHelper(prefix, prefix, n);
  }

  private List<String> predictHelper(String inputString, String initialString, int n) {
    List<String> result = new ArrayList<String>();
    if (inputString.length() > 0) {
      for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
        Character key = entry.getKey();
        DictionaryTree value = entry.getValue();
        if (key.equals(inputString.charAt(0))) {
          result =
              value.predictHelper(inputString.substring(1, inputString.length()), initialString, n);
          break;
        }
      }
    }
    // adequate size to check any word
    else {
      // sort hashmap
      HashMap<String, Optional<Integer>> returnedHashMap = predictStringBuilder(initialString);

      List<String> mapKeys = new ArrayList<>(returnedHashMap.keySet());
      List<Optional<Integer>> mapValues = new ArrayList<>(returnedHashMap.values());

      List<Integer> mapValuesConverted = new ArrayList<>();
      for (Optional<Integer> value : mapValues) {
        if (value.isPresent()) {
          mapValuesConverted.add(value.get());
        } else {
          mapValuesConverted.add(0);
        }
      }

      Collections.sort(mapValuesConverted);
      Collections.sort(mapKeys);

      LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

      Iterator<Integer> valueIt = mapValuesConverted.iterator();
      while (valueIt.hasNext()) {
        Integer val = valueIt.next();
        Iterator<String> keyIt = mapKeys.iterator();

        while (keyIt.hasNext()) {
          String key = keyIt.next();
          Integer comp1 = null;
          if (returnedHashMap.get(key).isPresent()) {
            comp1 = returnedHashMap.get(key).get();
          } else {
            comp1 = 0;
          }

          Integer comp2 = val;

          if (comp1.equals(comp2)) {
            keyIt.remove();
            sortedMap.put(key, val);
            break;
          }
        }
      }

      // check amount of zeros
      int zeroPopularityCounter = 0;
      for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
        Integer value = entry.getValue();
        if (value == 0) {
          zeroPopularityCounter++;
        }
      }

      int limit = 0;
      if (n > sortedMap.size()) {
        limit = sortedMap.size();
      } else {
        limit = n;
      }

      ArrayList<String> strings = new ArrayList<String>();
      strings.addAll(sortedMap.keySet());

      ArrayList<Integer> popularities = new ArrayList<Integer>();
      popularities.addAll(sortedMap.values());
      result = new ArrayList<String>();
      while (result.size() < limit) {
        if (zeroPopularityCounter >= limit) {
          zeroPopularityCounter = 0;
        }
        result.add(strings.get(zeroPopularityCounter));
        zeroPopularityCounter++;
      }

    }

    return result;
  }

  private HashMap<String, Optional<Integer>> predictStringBuilder(String inputString) {
    HashMap<String, Optional<Integer>> result = new HashMap<String, Optional<Integer>>();

    for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
      Character key = entry.getKey();
      DictionaryTree value = entry.getValue();

      if (value.endOfWord) {
        result.put(inputString + key, value.popularity);
        result.putAll(value.predictStringBuilder(inputString + key));
      } else if (!value.isLeaf()) {
        result.putAll(value.predictStringBuilder(inputString + key));
      }
    }

    return result;
  }

  /**
   * @return the number of leaves in this tree, i.e. the number of words which are not prefixes of
   *         any other word.
   */
  public int numLeaves() {
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
  public int maximumBranching() {
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
  public int height() {
    return longestWord().length();
  }

  /**
   * @return the number of nodes in this tree
   */
  public int size() {
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
  public String longestWord() {
    return longestHelper("");
  }

  private String longestHelper(String inputString) {
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
  public List<String> allWords() {

    return allWordsHelper("");
  }

  private ArrayList<String> allWordsHelper(String inputString) {
    ArrayList<String> stringList = new ArrayList<String>();

    for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
      Character key = entry.getKey();
      DictionaryTree value = entry.getValue();

      if (value.isLeaf() == true) {
        stringList.add(inputString + key);
      } else if (value.endOfWord) {
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
  private boolean isLeaf() {
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
