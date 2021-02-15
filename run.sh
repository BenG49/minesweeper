#!/usr/bin/env bash

javac minesweeper/*.java
javac minesweeper/game/*.java
javac minesweeper/solver/*.java
javac minesweeper/display/*.java
javac minesweeper/display/shapes/*.java

java minesweeper.Main
