# Introduction
Writing code for parsing json is repetitive & tedious.
This program can be used to generate this boilerplate code.

# Usage
``` bash
sbt --error 'run-main Main' 
sbt --error 'run-main Main https://api.github.com/repos/raghavgautam/js2Code'
```
# Running Demo
``` bash
sbt --error 'run-main demo.Demo'
```
# TODO
- code cleanup
- web demo
- add commandline example 

# Creating a fat jar
``` bash
sbt assembly
```
