# DAA Assignment 4: Dynamic Programming and String Algorithms

A Java project implementing edit distance and string search algorithms, with JUnit 5 tests and a reproducible experiment.

## Algorithms

Edit Distance: direct recursion, bottom-up table, and two-row dynamic programming.

String Search: Naive search and Knuth-Morris-Pratt (KMP), including the LPS array.

Counters record recursive calls, DP cell updates, and text-pattern character comparisons.

## Requirements

Java 17 or newer, Maven, Python 3, and Matplotlib.

Install Matplotlib if needed: python3 -m pip install matplotlib

## Build and test

Run the JUnit 5 tests from the project root with: mvn test

The tests cover reference values, empty strings, overlapping matches, edge cases, and randomized cross-checks.

## Run the experiment

Run Experiment.main() from IntelliJ IDEA, or from the project root run: mvn package

Then run: java -cp target/classes edu.daa.assignment4.Experiment

The experiment uses the fixed random seed 20261108 and writes measurements to results/results.csv. Each row records the algorithm, input type, input size, run number, runtime in milliseconds, operation count, and number of matches.

## Generate plots

From the project root, run: python3 scripts/plot_results.py

The script reads results/results.csv and saves four PNG charts in results/plots/.

## Project structure

src/main/java/edu/daa/assignment4/ contains the Java implementations and experiment runner.

src/test/java/edu/daa/assignment4/ contains the JUnit 5 tests.

results/results.csv contains the experiment measurements.

results/plots/ contains the generated charts.

scripts/plot_results.py generates the charts.

REPORT.md contains the experiment design and discussion.

## GitHub

Repository: https://github.com/adayaida7-spec/assignment4_daa

Branch: main

Final tag: v1.0