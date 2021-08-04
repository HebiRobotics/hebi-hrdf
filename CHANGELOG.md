# Changelog

## v1.3.0
Changes:
- added tree support (including `output` element definition)

## v1.2.0
Changes:
- Added description attribute to robot element
- Added R-series actuator, link, and bracket types
- Added inline link types
- Added end-effector robot model element type
- Explicitly defined strongly-typed interfaces

## v1.1.0

Changes:
- added requirement of HRDF version number in file
- added relaxed whitespace parsing for vectors and matrices (spaces, tabs, carriage returns, and newlines)
- defined that implementations should support case-insensitive parsing of enums, but add warnings when encountering non-matches cases
- defined format for basic formula interpretation for single-element floating point fields
- added additional formula support grammer for rotation matrices
- added inertias (defaults to point mass)
- add offset and override attributes for built-in robot model elements (links, brackets, and actuators)
