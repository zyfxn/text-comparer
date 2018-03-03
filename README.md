# TextComparer

TextComparer is a **simple comparison tool** to show line numbers of difference between two text files.
It uses a high performance graph algorithm to find the most of match lines in the line sequence from two files.
This project is a research for a **high speed** and
**low memory used** algorithm, working for files which have more than 50 thousand lines.

## Usage

* Command execute:
```
java -jar compare.jar \<file1\> \<file2\>
```
* Library reference example: `src/main/java/App.java`, `src/test/java/AppTest.java`