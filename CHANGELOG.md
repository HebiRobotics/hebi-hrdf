# Changelog

## v1.3.0

Changes:
- added tree support (including `output` element definition)
- added support for include elements
- added support for mesh elements

## v1.2.0

Changes:
- Added support for R-series actuators, links, and brackets
- Added support for input and output link types (allowing for inline or right angle styles)
- Added explicit end-effector types to correspond to API end-effector frames
- Added explicit notion of "interface types" that enforce addition of only mechnically compatible elements
- Added optional "description" parameter for <robot>
- Updated example HRDF files, and added ones for standard R-series arms and arms with grippers

## v1.1.0

Changes:
- added requirement of HRDF version number in file
- added relaxed whitespace parsing for vectors and matrices (spaces, tabs, carriage returns, and newlines)
- defined that implementations should support case-insensitive parsing of enums, but add warnings when encountering non-matches cases
- defined format for basic formula interpretation for single-element floating point fields
- added additional formula support grammer for rotation matrices
- added inertias (defaults to point mass)
- add offset and override attributes for built-in robot model elements (links, brackets, and actuators)
