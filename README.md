# TextComparer

## A high performance text file compare tool

TextComparer is a **simple comparison tool** to show line numbers of difference between two text files.
It uses a high performance graph algorithm to find a longest match lines and collects the rest, the difference.
This project is a research for a **high speed** and
**low memory used** algorithm to use for files which have more than 50 thousand lines.

## Usage

* Executable jar: compare-x.x.jar <file1> <file2>
* Library reference example: src/main/java/App.java