# Changelog

## v1.6.0

Changes:
- Added support for T25 actuators and hardware

## v1.5.0

Changes:
- Added optional `gear_ratio` attribute for `joint` elements

## v1.4.0

Changes:
- Added support for T-series actuators
- Added support for tag attributes on robot model elements.
- Added support for web URLs for rigid-body meshes

## v1.3.0

Changes:
- Added tree support (including `output` element definition)
- Added support for include elements
- Added support for mesh attributes

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
