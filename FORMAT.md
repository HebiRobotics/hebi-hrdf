# Overview

The HEBI Robot Configuration format has a root "robot" XML element, which contains a number of "robot model element" XML elements.  The structure of these robot model elements describe how they are connected.

Examples of this format in use can be seen in the kits in this directory.

## Version and versioning

The following specification is version 1.0.0 of this format.

The version numbering follows semantic versioning practices. Major version changes (e.g., 1.x.x->2.x.x) imply non-backwards compatible changes, whereas minor version changes (e.g., 1.3.0->1.4.0) imply backwards compatibility: existing robot configuration files will work with the updated specification, although files specifically using the newer specification may not be supported by tools using an older version of the standard.  Revision changes (1.4.2 -> 1.4.3) imply only clarification of the documentation, and should be treated as compatible.  Each new version change of the specification will be associated with a tag/release in this repository.

## Robot Elements

Each "robot element" has an assumed input interface, and zero or one output interfaces. It may or may not have mass and inertia, depending on its type.  Note that the rotation matrix are given as a row-major, space delineated lists of numbers, and the vectors are also space delineated.

### `<actuator>`

The actuator element represents actuators such as the X5-4.  It is assumed to have a mass and inertia, as well as an output interface.  All actuators have one output interface.  It is assumed that there is an associated degree of freedom in the robot model with this element.

**Required attributes:**
- `type` (string/enum) Currently supported values:
  - X5-1
  - X5-4
  - X5-9
  - X8-3
  - X8-9
  - X8-16

**Example:**

`<actuator type="X5-9"/>`

### `<link>`

The link element refers to a parameterized rigid body with two parameters (extension and twist).  All links have one output interface.

**Required attributes:**
- `type` (string/enum) the only currently supported value is X5
- `extension` (floating point value, meters)
- `twist` (floating point value, radians)

**Example:**

`<link type="X5" extension="0.25" twist="1.57"/>`

### `<bracket>`

The bracket element refers to a rigid body that connects modules, such as a light wrist bracket.  All brackets have one output interface.

**Required attributes:**
- `type` (string/enum) supported values are:
  - X5LightLeft
  - X5LightRight
  - X5HeavyLeftInside
  - X5HeavyLeftOutside
  - X5HeavyRightInside
  - X5HeavyRightOutside

### `<rigid-body>`

The rigid body refers to a solid body with mass and one or more outputs.

**Required attributes:**
`mass` (floating point)

**Optional attributes:**
- `com_rot` (3x3 rotation matrix, row-major) the orientation of the center of mass (used for simplifying the inertia tensor description if desired).  Defaults to identity.
- `com_trans` (3x1 vector) The position of the center of mass.  Defaults to (0,0,0).
- `output_rot` (3x3 rotation matrix, row-major): the orientation of the output frame.  Defaults to identity.
- `output_trans` (3x1 vector): The position the output frame.  Defaults to (0,0,0).

### `<joint>`

The joint refers to a massless degree of freedom.

**Required attributes:**
- `axis` (string/enum) This can be a rotational or translational degree of freedom about a principal coordinate axis; supported values are:
  - rx
  - ry
  - rz
  - tx
  - ty
  - tz

## `<robot>` 

The robot element is the root element of a robot model.

**Optional Attributes**
`rot` (3x3 floating point rotation matrix) specify the rotation of the base frame of the model; defaults to identity matrix.
`trans` (3 element floating point vector) specify the translation to the base frame of the model; defaults to (0,0,0)

**Example**
`<robot rot="1 0 0 0 1 0 0 0 1" trans="0 0 0"/>`

### Connecting Robot Model Elements

For the descriptions below, assume `<elem[0-9]*/>` is any of actuator, link, bracket, rigid-body, and joint, with all parameters defined as necessary.

The `robot` element can only contain `<elem>` subelements.  It contains an implicitly ordered list of them, with no minimum count:

```
<robot>
  <elem>
</robot>
```

When there is a list of `<elem>` elements, they are assumed to following each other in a kinematic chain - here, elem2 is more distal than elem1:

```
<robot>
  <elem1>
  <elem2>
</robot>
```
