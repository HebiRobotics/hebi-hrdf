# Overview

This directory provides tests that ensure the grammar accurately describes the
allowable input, as well as provides test cases that can be used by various
implementations of the format.

Note -- the instructions below and build scripts assume linux; for Windows -
you're on your own for now (please fix and add documentation here if you work
on this!)

# Setup

To install the ANTLR 4 dependency, run (from this directory)
`curl -O http://www.antlr.org/download/antlr-4.7.1-complete.jar`

# Build

Run `build.sh`

# Run

Run `run_tests.sh`

# Tests

Adding tests can be done by adding to one of the existing ".testcases" files, or
by creating a new one.  For a new file, be sure you add it to the "run_tests.sh"
script.

Tests in a ".testcases" file are indicated by either "# Good" (and then two
lines with the source expression and the expected result) or "# Bad" (and then
one line with the source expression which should not parse). Any other lines are
ignored (but lines cannot be added between the "Good" and "Bad" tags, and the
expression/result content lines immediately following them).
