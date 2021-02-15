#!/usr/bin/env bash

javac minesweeper/*.java
javac minesweeper/game/*.java
javac minesweeper/solver/*.java

java minesweeper.Main
