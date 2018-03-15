# Solution
##### GitLab repo: https://git.cs.bham.ac.uk/dxr714/SWW-assignment_2

## Comment on the advantages/disadvantages of using a tree for predicting multiple words with ranked popularities


## Description of methods

* `size` - simply count amount of nodes on each level and return that value. add value of children. Recursive to the top call will get the total of all calls.

* `height` - simply return the length of the longest word - this will be the height of the tree.

* `maximumBranching` - get the amount of nodes at a level and compare with the level under it. Return the highest. Highest call will get the maximum value.

* `longestWord` - create an `Arraylist` of all words at each level that are leaves and return the 

* `numLeaves` - check the amount of leaves at each level return. If a node is not a leaf, call the method on it. Highest call will get the total of all leaves. The return value is initially set to 1 and changed to 0 if there are children so that the root node counts as a leaf if no words have been inserted, as `.isEmpty()` can't be called on the `DictionaryTree` object.

* `contains` - checks if each level contains the next character of the input word. If the last node is reached, check if it's the end of the word - return the result.

* `allWords` - traverses down the tree and adds nodes that are the end of a word to an `ArrayList`that gets returned at the end of the method call. Stops traversing when reaches a leaf node.

* `insert` - if called without a popularity, calls the method that takes the popularity but with the popularity as 0. The word is then inserted and the popularity set at the final node. *NOTE* - if no popularity of popularity of 0 is passed, the word gets set a popularity of `Optional.empty()`. This makes sense as it's referred to have no popularity. The method doesn't check if the word to be inserted is already stored in the tree, `insert` should have the ability to override the popularity.

* `remove` - first the method checks if the word can be removed - i.e. if it's in the tree. If it can, `removeHelper` is called that checks if the word is part of another word, and if it is, simply sets the `endOfWord` boolean of the passed string to false. -1 is then returned. If the word to be removed is not part of any other word, instead the helper will return the index of the last endOfWord in the input word - past this node all nodes will have to be removed. If this helper returns an index, removeRemover is called that clears the children of the node that was the end of the word.

  * Alternate solution for remove - returns `true` if word can be removed, and returns `false` if the word can't be removed. I didn't see how returning what the assignment asked for initially would be beneficial at all. This shouldn't matter either way as Kelsey McKenna mentioned that this won't be checked.

* `predict` - if predict called without a specified amount of outputs, it is passed to the other `predict` method with n = 1. That method first checks if the prefix is a word within itself and if it is, returns its popularity. If it's not, returns `Optional` of -1. This is then passed to a helper. The helper traverses down the tree to get to a depth that words can be predicted from. Once there, if it reaches it, it calls the helper method `predictStringBuilder` which gets all the possible predicted words (words with the entered prefix) and returns them as a `HashMap` with which stored each predicted word with its popularity. The original helper method then adds the prefix if it's also a word into the returned `HashMap` and sorts it by word popularity. Then the specified amount of words is added to the `result` `ArrayList` by popularity - if needed, words without a popularity are assigned to the end of this `ArrayList`. The list is then returned.

