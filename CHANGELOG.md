# Changelog

## v1.1.0

Changes:
- added requirement of HRDF version number in file
- added relaxed whitespace parsing for vectors and matrices (spaces, tabs, carriage returns, and newlines)
- defined that implementations should support case-insensitive parsing of enums, but add warnings when encountering non-matches cases
- defined format for basic formula interpretation for single-element floating point fields
- added additional formula support grammer for rotation matrices
- added inertias (defaults to point mass)
- add offset and override attributes for built-in robot model elements (links, brackets, and actuators)
