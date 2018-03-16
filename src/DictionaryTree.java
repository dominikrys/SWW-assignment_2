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
    this.popularity = Optional.empty();
    this.endOfWord = false;
  }

  /**
   * Inserts the given word into this dictionary. If the word already exists, nothing will change.
   *
   * @param word the word to insert
   */
  public void insert(String word) {
    insert(word, 0);
  }

  /**
   * Inserts the given word into this dictionary with the given popularity. If the word already
   * exists, the popularity will be overriden by the given value.
   *
   * @param word the word to insert
   * @param popularity the popularity of the inserted word
   */
  public void insert(String word, int popularity) {
    // Create a temporary DictionaryTree that will have characters inserted into
    DictionaryTree tempTree = new DictionaryTree();
    if (word.length() > 0) {

      // Insert characters into children
      if (children.containsKey(word.charAt(0))) {
        tempTree = children.get(word.charAt(0));
      }
      tempTree.insert(word.substring(1, word.length()), popularity);

      // If on the last character, set endOfWord to true, and set the popularity - if popularity
      // already set it will be overridden
      if (word.length() == 1) {
        tempTree.endOfWord = true;
        if (popularity == 0) {
          tempTree.popularity = Optional.empty();
        } else {
          tempTree.popularity = Optional.of(popularity);
        }
      }

      children.put(word.charAt(0), tempTree);
    }
  }

  /**
   * Removes the specified word from this dictionary. Returns true if the word can be removed as
   * it's in the DictionaryTree, and false if the input word isn't in the tree. NOTE - this is not
   * what was initially asked for, explained further in SOLUTION.md
   *
   * @param word the word to delete from this dictionary
   * @return whether or not the parent can delete this node from its children
   */
  public boolean remove(String word) {
    // If the word is contained in the tree, call the remove helper method to remove it
    if (contains(word)) {

      // removeIndex == -1 when nodes don't have to be removed, otherwise it'sthe index of which
      // children have to be removed
      int removeIndex = removeHelper(word, 0, 0);

      if (removeIndex != -1) {
        removeRemover(word, removeIndex, 0);
      }
      return true;
    } else {
      return false;
    }
  }

  // remove helper method - checks if the word to be removed is part of another word and if it
  // is, removes it by setting a node to not be the end of word. If extra nodes would have to be
  // removed after, this method returns the index from which nodes have to be removed
  private int removeHelper(String word, int indexOfLastEndOfWord, int currentIndex) {
    int result = -1;
    if (word.length() > 1) {
      for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
        Character key = entry.getKey();
        DictionaryTree value = entry.getValue();

        // Keep traversing down the word, checking if any characters are the end of a word
        if (key.equals(word.charAt(0))) {
          if (value.endOfWord) {
            result = value.removeHelper(word.substring(1, word.length()), currentIndex + 1,
                currentIndex + 1);
          } else {
            result = value.removeHelper(word.substring(1, word.length()), indexOfLastEndOfWord,
                currentIndex + 1);
          }
          break;
        }
      }
    }
    // When on last character if more nodes have to be removed, return the index of the last node,
    // otherwise return -1 and set endOfWord to false.
    else {
      if (children.get(word.charAt(0)).isLeaf()
          || children.get(word.charAt(0)).endOfWord == false) {
        result = indexOfLastEndOfWord;
      } else {
        DictionaryTree extractedDictionary = children.get(word.charAt(0));
        extractedDictionary.endOfWord = false;
        children.put(word.charAt(0), extractedDictionary);
        result = -1;
      }
    }
    return result;
  }

  // This helper is called if nodes have to be removed from the tree - this method removes all
  // children of the last existing word
  private void removeRemover(String word, int indexOfLastEndOfWord /* the index to remove after */,
      int currentIndex) {
    if (indexOfLastEndOfWord <= currentIndex) {
      children.clear();
      children = new LinkedHashMap<>();
    } else {
      for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
        Character key = entry.getKey();
        DictionaryTree value = entry.getValue();

        if (key.equals(word.charAt(0))) {
          value.removeRemover(word.substring(1, word.length()), indexOfLastEndOfWord,
              currentIndex + 1);
          break;
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
   * Predicts a single word based on the input prefix
   * 
   * @param prefix the prefix of the word returned
   * @return a word that starts with the given prefix, or an empty optional if no such word is
   *         found.
   */
  public Optional<String> predict(String prefix) {
    // Call the other predict method with n as n = 1
    ArrayList<String> returnedList = (ArrayList<String>) predict(prefix, 1);

    // Convert output into required Optional
    if (returnedList.size() == 0) {
      return Optional.empty();
    } else {
      return Optional.of(returnedList.get(0));
    }
  }

  /**
   * Predicts the (at most) n most popular full English words based on the specified prefix. If no
   * word with the specified prefix is found, an empty list is returned.
   *
   * 
   * @param prefix the prefix of the words found
   * @return the (at most) n most popular words with the specified prefix
   */
  public List<String> predict(String prefix, int n) {
    // If prefix is a word, call predictHelper with its popularity. Otherwise with optional -1
    if (this.contains(prefix)) {
      return predictHelper(prefix, prefix, n, getPopularity(prefix));
    } else {
      return predictHelper(prefix, prefix, n, Optional.of(-1));
    }
  }

  // Helper for predict to get the popularity of the prefix - only called if the word exists
  private Optional<Integer> getPopularity(String inputString) {
    Optional<Integer> result = Optional.empty();
    if (inputString.length() > 0) {
      for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
        Character key = entry.getKey();
        DictionaryTree value = entry.getValue();

        if (inputString.length() == 1) {
          result = popularity;
          break;
        }
        if (key.equals(inputString.charAt(0))) {
          result = value.getPopularity(inputString.substring(1, inputString.length()));
          break;
        }
      }
    }
    return result;
  }

  // Helper method for predict which actually checks through the tree
  private List<String> predictHelper(String inputString, String originalString, int n,
      Optional<Integer> popularityOfPrefix) {
    List<String> result = new ArrayList<String>();

    // Traverse down the tree until at the depth for possible predicted words to be found - will not
    // finish if the word to be predicted is not in the arraylist
    if (inputString.length() > 0) {
      for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
        Character key = entry.getKey();
        DictionaryTree value = entry.getValue();
        if (key.equals(inputString.charAt(0))) {
          result = value.predictHelper(inputString.substring(1, inputString.length()),
              originalString, n, popularityOfPrefix);
          break;
        }
      }
    } else {
      // Get a hashmap of all words starting with the specified prefix and their popularities
      HashMap<String, Optional<Integer>> returnedHashMap = predictStringBuilder(originalString);

      // Insert prefix into returned hashmap if the prefix is contained in the tree
      if (!popularityOfPrefix.isPresent()) {
        returnedHashMap.put(originalString, popularityOfPrefix);
      } else if (popularityOfPrefix.get() != -1) {
        returnedHashMap.put(originalString, popularityOfPrefix);
      }

      // Extract values from hashmap
      List<String> mapKeys = new ArrayList<>(returnedHashMap.keySet());
      List<Optional<Integer>> mapValues = new ArrayList<>(returnedHashMap.values());

      // Convert values from optional to int for Collections comparisons
      List<Integer> mapValuesConverted = new ArrayList<>();
      for (Optional<Integer> value : mapValues) {
        if (value.isPresent()) {
          mapValuesConverted.add(value.get());
        } else {
          mapValuesConverted.add(0);
        }
      }

      // Sort the hashmap by values in descending order
      Collections.sort(mapValuesConverted, Collections.reverseOrder());
      Collections.sort(mapKeys, Collections.reverseOrder());

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

      // Maximum number of words to be output
      int limit = 0;
      if (n > sortedMap.size()) {
        limit = sortedMap.size();
      } else {
        limit = n;
      }

      // Extract words and popularities
      ArrayList<String> strings = new ArrayList<String>();
      strings.addAll(sortedMap.keySet());
      ArrayList<Integer> popularities = new ArrayList<Integer>();
      popularities.addAll(sortedMap.values());

      // Populate output arraylist with predicted words in order of popularity
      result = new ArrayList<String>();
      int currentIndex = 0;
      while (result.size() < limit) {
        result.add(strings.get(currentIndex));
        currentIndex++;
      }

    }

    return result;
  }

  // Helper method for returning a list of words with the input prefix
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
    // leavesNo initially set to 0 in case it's just the root with no words inserted
    int leavesNo = 1;

    if (!children.isEmpty()) {
      leavesNo = 0;

      for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
        DictionaryTree value = entry.getValue();

        if (value.isLeaf()) {
          leavesNo++;
        } else {
          leavesNo += value.numLeaves();
        }
      }
    }

    return leavesNo;
  }

  /**
   * Determines whether the input node is a leaf node
   * 
   * @param node which will be checked whether it's a lead node
   * @return true if the passed node is a leaf, false otherwise
   */
  private boolean isLeaf() {
    return (children.isEmpty());
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
    // Size starting from 1 like in the game tree example for the initial node
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

  // Helper for longest which can traverse down the input string
  private String longestHelper(String inputString) {
    // Make a list of all words that are leaves
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

    // Check every word and see which is the longest
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

  // Helper for allWords so it can take an argument
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
    // return fold((tree, cResults) -> { anonymous function here });
    throw new RuntimeException("DictionaryTree.fold not implemented yet");
  }


}
