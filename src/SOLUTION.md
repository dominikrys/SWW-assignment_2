# Solution
##### GitLab repo: https://git.cs.bham.ac.uk/dxr714/SWW-assignment_2

## Comment on the advantages/disadvantages of using a tree for predicting multiple words with ranked popularities



## General notes

* For insert(), a popularity of 0 means that the word hasn't been specified a popularity and therefore will be assigned a popularity of `Optional.empty()`.

* Alternate solution for remove - returns `true` if word can be removed, and returns `false` if the word can't be removed. I didn't see how returning what the assignment asked for initially would be beneficial at all. This shouldn't matter either way as Kelsey McKenna mentioned that this won't be checked.