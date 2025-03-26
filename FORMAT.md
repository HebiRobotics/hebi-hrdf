# Overview

The HEBI Robot Configuration format has a root "robot" XML element, which contains a number of "robot model element" XML elements.  The structure of these robot model elements describe how they are connected.

Examples of this format in use can be seen in the kits in this directory.

## Version and versioning

The following specification is version 1.6.0 of this format.

The version numbering follows semantic versioning practices. Major version changes (e.g., 1.x.x->2.x.x) imply non-backwards compatible changes, whereas minor version changes (e.g., 1.3.0->1.4.0) imply backwards compatibility: existing robot configuration files will work with the updated specification, although files specifically using the newer specification may not be supported by tools using an older version of the standard.  Revision changes (1.4.2 -> 1.4.3) imply only clarification of the documentation, and should be treated as compatible.  Each new version change of the specification will be associated with a tag/release in this repository.

## Terms

When the term _whitespace_ is used in this document, it will refer to the ASCII space, tab, carriage return, and new line characters (values in the string " \t\r\n").

The attributes supported by the elements are of several basic types, described in more detail at the bottom of this document.

* [enums](#enums) (possible string values for an attribute)
* Basic [floating point](#floating-point) numbers
* [floating point formulas](#floating-point-formula) combining numbers, ()+-/* operators, and the constant `pi`
* [rotation matrix](#rotation-matrix) values, either in `Rx/Ry/Rz` shorthand or all entries of the matrix
* [translation vector](#translation-vector) values, containing x/y/z position

## Root element: `<robot>` 

The robot element is the root element of a robot model.

**Required Attributes for v > 1.0.0:**
- `version` (enum) Specifies the version number of the HRDF format used to define this file.  To allow parsing of v1.0.0 files, this defaults to `1.0.0` if not given.  The rules used to parse the file are defined by the given version.  For example, a `mass_offset` attribute on an actuator may cause a parsing error if this attribute is not given, or is set to `1.0.0`. Supported values are:
  - 1.0.0
  - 1.1.0
  - 1.2.0
  - 1.3.0
  - 1.4.0
  - 1.5.0
  - 1.6.0

**Optional Attributes:**
- `rot` (rotation matrix) specify the rotation of the base frame of the model; defaults to identity matrix.
- `trans` (translation vector) specify the translation to the base frame of the model; defaults to (0,0,0)
- `description` (string) human readable description of this robot

**Content:**
Zero or more of the following:
- `<actuator>`
- `<bracket>`
- `<link>`
- `<rigid-body>`
- `<joint>`
- `<end-effector>`
- `<include>`

**Example:**
```xml
<robot rot="1 0 0 0 1 0 0 0 1" trans="0 0 0"/>
```

The `robot` element can only contain [`robot element`](robot-elements) subelements, which are described below.

## Robot Elements

Each "robot element" element has an assumed input interface, and zero or more output interfaces. It may or may not have mass and inertia, depending on its type.

Note that the parsing of the element name (e.g., `actuator`) is case sensitive, and so `Actuator` will generate and error.

### `<actuator>`

The actuator element represents actuators such as the T5-4.  It is assumed to have a mass and inertia, as well as a single output interface.  It is assumed that there is a single associated degree of freedom in the robot model with this element.  The `actuator` element can have no child elements.

**Required attributes:**
- `type` (string/enum) Currently supported values:
  - X5-1
  - X5-4
  - X5-9
  - X8-3
  - X8-9
  - X8-16
  - R8-3
  - R8-9
  - R8-16
  - T5-1
  - T5-4
  - T5-9
  - T8-3
  - T8-9
  - T8-16
  - T25-8
  - T25-20
  - T25-40

**Content:**
None

**Example:**

```xml
<actuator type="T5-9"/>
```

### `<link>`

The link element refers to a parameterized rigid body.  All links have one output interface.  The `link` element can have no child elements.

Note that the "extension" and "twist" values correspond to those shown on http://docs.hebi.us/hardware.html.

**Required attributes:**
- `type` (string/enum) The style of link.  Currently supported values::
  - X5
  - R8
  - RT25
  - RT25-R8 (adaptor link for RT25 to R8 series hardware)
- `extension` (floating point formula, meters)
- `twist` (floating point formula, radians)

**Optional attributes:**  
- `input` (string/enum) The type of the input interface.  Defaults to `RightAngle`. Currently supported values:
  - RightAngle (supported for X5, R8, RT25, and RT25-R8 link types)
  - Inline (supported for both X5 and R8 link types)
- `output` (string/enum) The type of the output interface.  Defaults to `RightAngle`. Currently supported values:
  - RightAngle (supported for both X5, R8, RT25, and RT25-R8 link types)
  - Inline (supported for both X5 and R8 link types)

**Content:**
None

**Example:**

```xml
<link type="R8" extension="0.325" twist="pi/2"/>
```

### `<bracket>`

The bracket element refers to a rigid body that connects modules, such as a light wrist bracket.  Brackets can have one or more output interfaces.  The bracket element can contain 0 to _n_ `output` child elements, where _n_ is the number of output interfaces defined by this bracket, but no other child elements.

**Required attributes:**
- `type` (string/enum) supported values are:
  - X5LightLeft
  - X5LightRight
  - X5HeavyLeftInside
  - X5HeavyLeftOutside
  - X5HeavyRightInside
  - X5HeavyRightOutside
  - R8LightLeft
  - R8LightRight
  - R8HeavyLeftInside
  - R8HeavyLeftOutside
  - R8HeavyRightInside
  - R8HeavyRightOutside  
  - RT25HeavyLeftInside
  - RT25HeavyLeftOutside
  - RT25HeavyRightInside
  - RT25HeavyRightOutside  

**Content:**
Zero or more of the following:
- `<output>`: Zero to _n_; used to describe tree-like kinematic structures. The value of _n_ depends on the bracket type.  See the `<output>` element section below.

Currently, all of the given bracket elements have a single output interface.

### `<rigid-body>`

The rigid body refers to a solid body with mass and one or more outputs. Defaults to a single output interface. The rigid-body element can contain any number of `output` child elements, but no other child elements.

**Required attributes:**
`mass` (floating point formula, kg)

**Optional attributes:**
- `com_rot` (rotation matrix): the orientation of the center of mass (used for simplifying the inertia tensor description if desired). Defaults to identity.
- `com_trans` (translation vector, m): The position of the center of mass. Defaults to (0,0,0).
- `output_rot` (rotation matrix): The default orientation of the output frames. Defaults to an identity matrix.
- `output_trans` (translation vector, m): The default position the output frames. Defaults to (0,0,0).
- `ixx`, `iyy`, `izz`, `ixy`, `ixz`, `iyz` (floating point formulae, kg m^2): The 6 elements of the inertia tensor, relative to the COM frame as given above.  Each defaults to 0 (note, this means overall default is a point mass).
- `mesh_path` (string): Relative file path or web URL to a file used to store 3D mesh information for visualization purposes. A forward slash should be used as a file separation character. File paths are relative to the current HRDF file being parsed; absolute paths are not allowed.  The double dot ".." pattern moves up a directory.  Web URLs must start with `http://` or `https://`. Supported file types, extensions, and sources (e.g., local vs web) depend on the application consuming the HRDF file for visualization.
- `mesh_rot` (rotation matrix): specify the rotation of the base frame of the mesh; defaults to identity matrix. Considered an error if present without a `mesh_path` attribute.
- `mesh_trans` (translation vector): specify the translation to the base frame of the mesh; defaults to (0,0,0). Considered an error if present without a `mesh_path` attribute.

**Content:**
Zero or more of the following:
- `<output>` Used to describe tree-like kinematic structures. See the `<output>` element section below.

**Examples:**

Single-output rigid body:

```xml
<rigid-body mass="0.5" com_trans="0.25 0 0" output_rot="Rx(pi/4)" output_trans="0.5 0 0"/>
```

Multi-output rigid body (see below for "output" element details):

```xml
<rigid-body mass="0.5" com_trans="0.25 0 0" output_rot="Rx(pi)">
  <output rot="Rx(pi/4)" trans="0.5 0 0"/>
  <output trans="1.5 0 0"/> <!-- note that this defaults to the Rx(pi) rotation -->
  <output/> <!-- note that this defaults to the Rx(pi) rotation and (0,0,0) translation -->
</rigid-body>
```

### `<joint>`

The joint refers to a massless degree of freedom; it always has a single output interface. The `joint` element can have no child elements.

**Required attributes:**
- `axis` (string/enum) This can be a rotational or translational degree of freedom about a principal coordinate axis; supported values are:
  - rx
  - ry
  - rz
  - tx
  - ty
  - tz

**Optional attributes:**
- `gear_ratio` (floating point formula). Defaults to 1.0. Represents the ratio of a gear on the output of the joint, and effectively scales the effect of the joint angle on the robot kinematics.  For example, a gear ratio of "10" would have the rotation/translation of the output frame move at 1/10 the speed of the joint angle.  Can be used to represent a linear stage driven by an actuator; in this case, the units are effectively `1/m`.

**Content:**
None

**Example:**

```xml
<joint axis="rx"/>
```

### `<end-effector>`

An end effector refers to a component at the end of a kinematic chain (e.g., it has no children/output interfaces). The key aspect of the end effector is that it identifies the location of an "end effector frame" at a specified relative position. The `end-effector` element can have no child elements.

(Note that HRDFs of version <= 1.1.0 do not explicitly have the notion of an end effector frame, so when being loaded into a compliant parser, the API adds an implicit "end effector frame" to the end of the chain of elements).

**Optional attributes:**
- `type` (string/enum) The style of end effector.  Defaults to `Custom`. Currently supported values:
  - Custom (fully specifiable by the user)
  - X5Parallel (matches the parallel jaw gripper attachment to a HEBI gripper)
  - R8Parallel (matches the parallel jaw gripper attachment to a HEBI gripper)
- `output_rot` (rotation matrix): the orientation of the output of the end effector.  Defaults to identity.
- `output_trans` (translation vector, m): The position the output of the end effector.  Defaults to (0,0,0).  

**Content:**
None

**Example:**

```xml
<end-effector type="Custom" mass="0.1" com_trans="0 0 0.5" output_trans="0 0 0.1"/>
```

**Implementation notes:**

As with other built-in element types, the `end-effector` has mass, center of mass, interia, and output frame information.  The default values for the optional attributes depend on the type of the end effector.  For the `Custom` type, these values match a `rigid-body` of mass 0.

### Identifying elements by name

The built in and custom robot model elements (`actuator`, `link`, `bracket`, `end-effector`, `rigid-body`, and `joint`) all have an attribute that allows retrieval through the APIs by name instead of by index. The values of `tag` attributes must be globally unique within all descendants of a `<robot>` object.

**Tag Attribute**
- `tag` (string) A human-readable name identifying this element; this can be used to retrieve robot model elements by name instead of by index. 

**Example:**

```xml
<actuator type="X5-9" mass_offset="0.2" tag="wrist"/>
```

Then from APIs, one can call functions such as `getFrameByName("wrist")`.


### Offsetting and overwriting dynamic properties

The built in robot model elements (`actuator`, `link`, `bracket`, and `end-effector`) all have attributes that allow modification or overwriting of certain properties.

**Offset**
The optional offset attributes set a constant offset from the library values (e.g., to account for an additional weight on a module).

- `mass_offset` (floating point formula, kg) offset to be added (or subtracted) to the mass
- `com_trans_offset` (translation vector) offset to be added to the center of mass position

**Override**
The optional override attributes completely override the library values.  These have the same meaning/types as for the `rigid_body` element.
- `mass`
- `com_rot`
- `com_trans`
- `ixx`
- `iyy`
- `izz`
- `ixy`
- `ixz`
- `iyz`

Note: the HRDF file is ill-formed and should generate a parsing error if both `mass` and `mass_offset` attributes are defined, and similarly should fail if both `com_trans` and `com_trans_offset` elements are defined.

**Example:**

```xml
<actuator type="T5-9" mass_offset="0.2"/>
```

### `<include>`

The `include` element is used to allow commonly used snippets of HRDF to be reused in a single file or in multiple files.  The `include` element is used in place of a normal robot model element, and the filename referenced must be a complete and valid .HRDF file.  The contents of the "robot" element of this .HRDF file replace the "include" element in the final HRDF.

Note that any attributes on the "robot" element in the included are ignored.  A compliant parser should generate and error if the file cannot be found.

**Required Attributes:**
- `path` (string) Relative path to the HRDF file to be included. A forward slash should be used as a file separation character. Paths are relative to the current HRDF file being parsed; absolute paths are not allowed.  The double dot ".." pattern moves up a directory.

**Content:**
None

**Notes/Constraints:**

All files that are included must match the same HRDF file version as the parent file.

Note: The relative path within an hierarchical include chain is dependent on the file with that particular include tag, not the root file in the chain.

**Examples:**

```xml
<include path="my_robot/left_arm.hrdf"/>
```

```xml
<include path="./my_robot/left_arm.hrdf"/>
```

```xml
<include path="left_arm.hrdf"/>
```

```xml
<include path="../robot_parts/left_arm.hrdf"/>
```

## Output element

The `output` element is a special child element used to define the connection points for brackets and rigid bodies with multiple output interfaces.  It is unnecessary for pure kinematic chains. It may only be present as the child of a `bracket` with multiple output interfaces or a `rigid-body` element.  For `bracket` and `rigid-body` elements with a single output interface, omitting the `output` element and using a chain of robot model elements is preferred (see below, "Connecting Robot Model Elements").

The `output` element can only contain [`robot element`](robot-elements) subelements.

**Optional attributes:**
- `rot` (rotation matrix): the orientation of the output interface frame relative to the input interface. Defaults to identity or the value defined by the parent `bracket` or `rigid-body` element. This attribute will cause a parsing error if parent element of the `output` element is not a `rigid-body`.
- `trans` (translation vector, m): The position the output interface frame.  Defaults to (0,0,0). Defaults to identity or the value defined by the parent `bracket` or `rigid-body` element. This attribute will cause a parsing error if parent element of the `output` element is not a `rigid-body`.

**Content:**
Zero or more of the following:
- `<actuator>`
- `<bracket>`
- `<link>`
- `<rigid-body>`
- `<joint>`
- `<end-effector>`
- `<include>`

**Notes/Constraints:**

In a bracket, the output interfaces are inherently defined, and so the rotations and translations are not permitted to be changed.

For built-in bracket types, you may not have more than the number of outputs that the object defines.  You may omit unused outputs after the last used output.

**Example:**

```xml
<output rot="Rx(pi/4)" trans="0.5 0 0"/>
```

## Connecting Robot Model Elements

For the descriptions below, assume `<elem[0-9]*/>` is any of actuator, link, bracket, end-effector, rigid-body, and joint, with all parameters defined as necessary.

**Kinematic Chains**

The `robot` element can only contain `<elem>` subelements.  It contains an implicitly ordered list of them, with no minimum count:

```xml
<robot>
  <elem>
</robot>
```

When there is a list of `<elem>` elements, they are assumed to following each other in a kinematic chain - here, elem2 is more distal than elem1:

```xml
<robot>
  <elem1>
  <elem2>
</robot>
```
**Trees**

To form tree structures, or explicitly attach an `<elem>` to a specific output of a multi-output `bracket`/`rigid-body` (e.g., leg "2" on a hexapod), you must use the `<output>` element. The code below demonstrates attaching `<elem2>` to the first output of `<elem1>`:

```
<robot>
  <elem1>
    <output>
      <elem2/>
    </output>
  </elem1>
</robot>
```

Below demonstrates attaching `<elem2>` to the _second_ output of `<elem1>`.  Note the use of an empty `<output>` element:

```
<robot>
  <elem1>
    <output/>
    <output>
      <elem2/>
    </output>
  </elem1>
</robot>
```

Note that if `elem1` was a bracket with a single output interface, this would cause a parsing error. A `rigid-body` element supports a dynamic number of output interfaces, based on the number of `output` elements given.

You can use multiple "output" elements to explicitly attach multiple children; see this fictitious "hexapod body" bracket
example, with only four legs attached

```
...
<bracket type="XHexapodBody">
  <output>
    <elem1/>
  </output>
  <output/>
  <output>
    <elem2/>
  </output>
  <output>
    <elem3/>
  </output>
  <output/>
  <output>
    <elem4/>
  </output>
</bracket>
...
```

The contents of an `output` element follow the same rules as for the `robot` root element, and therefore can  contain a chain of robot model elements; see the hexapod leg attached to the second output of this ficticious hexapod body bracket.

```
...
<bracket type="XHexapodBody">
  <output/>  
  <output>
    <elem1/>
    <elem2/>
    <elem3/>
  </output>
</bracket>
...
```

For a bracket or rigid body with only the first output used, the kinematic chain format should be used, although both will be accepted by compatible parsers. Specifically, prefer:

```
...
<bracket>
<elem1/>
<elem2/>
<elem3/>
...
```

to


```
...
<bracket>
  <output>
    <elem1/>
    <elem2/>
    <elem3/>
  </output>
</bracket>
...
```

These both define the same structure, but the former has less nesting and is more readable.

**Interface types:**

Each robot model element has an input interface and zero or more output interface, each of a specific type and polarity. Following is a list of interface types; each listed type has two polarities, `A` and `B`.  

- `X-AH` X Actuator Housing Interface
- `R-AH` R Actuator Housing Interface
- `RT25-AH` R/T-25 Actuator Housing Interface
- `X-AO` X Actuator Output Interface
- `R-AO` R Actuator Output Interface
- `RT25-AO` R/T-25 Actuator Output Interface

Compatible interfaces are defined as having the same type and different polarity.  Adjacent elements must have compatible interfaces for the HRDF file to be valid.  In other words, in the following file, the output interface of `elem1` must be the same type but different polarity as that of the input interface of `elem2`.

```xml
<robot>
  <elem1>
  <elem2>
</robot>
```

As a special case, `rigid-body` and `joint` elements are assumed to have interface types that are compatible with anything.

A full list of element interface types is given below. An asterisk (`*`) in the type is used as a wildcard to indicate any matching element types:

| Element | Type | Input Interface | Output Interface |
| ------- | ---- | --------------- | ---------------- |
| `actuator` | `X*` | `X-AH-A` | `X-AO-A` |
| `actuator` | `R8*`, `T5*`, `T8*` | `R-AH-A` | `R-AO-A` |
| `actuator` | `T25*` | `RT25-AH-A` | `RT25-AO-A` |
| `bracket` | `X*` | `X-AO-B` | `X-AH-B` |
| `bracket` | `R8*` | `R-AO-B` | `R-AH-B` |
| `bracket` | `RT25*` | `RT25-AO-B` | `RT25-AH-B` |
| `link` | `X*` | `X-AO-B` | `X-AH-B` |
| `link` | `R8` | `R-AO-B` | `R-AH-B` |
| `link` | `RT25` | `RT25-AO-B` | `RT25-AH-B` |
| `link` | `RT25-R8` | `RT25-AO-B` | `R-AH-B` |
| `end-effector` | `Custom` | any | none |
| `end-effector` | `X5Parallel` | `X-AO-B` | none |
| `end-effector` | `R8Parallel` | `R-AO-B` | none |
| `rigid-body` | n/a | any | any |
| `joint` | n/a | any | any |

Note that the T-series actuators share the R-series bolt patterns and therefore have the same
interface types.

## Types

### enums

Enum types are lists of possible values that a certain attribute can have.

Implementation note: When parsing strings that are "enum" values (e.g., the "type" attribute of an actuator or link element), implementations should support reading these values in without regard to case (e.g., a value of "X5-1" or "x5-1" should both be interpreted correctly).  In the case of non-matching case, a warning should be provided by the API when importing the file.  If implementations support writing HRDF files, the case shown in this document should be written.

### floating point

A _floating point_ value is written as basic scientific notation.  Valid _floating point_ values include:

- `3.24`
- `0.324`
- `.324`
- `324`
- `3.24e2`
- `-3.24E-2`
- `-3.24e+2`
- `-32E4`
- `1.`

Invalid _floating point_ values include:

- `2.4.3`
- `2,000`
- `32,45`
- `3e2.4`
- `.`
- `- 1.3`
- `1. 02`

(A full railroad syntax diagram is available in the [Grammar](GRAMMAR.md) page)

### floating point formula

The _floating point formula_ type supports numeric values and simple formula elements. Basically, these support expressions using the _floating point_ type above, the constant pi (case sensitive), and allowing parenthesis (`()`), plus (`+`), minus (`-`), multiply (`*`), and divide (`/`) operators.  Whitespace is ignored (outside of the _floating point_ values)

Valid _floating point formula_ values include:

- `pi / 4`
- `1 + 4`
- `32*45`
- `1 + 2 / 3 - 4 * 5`
- `1 + 2 / (3 - 4) * 5`
- `(100 + 45) / (3*pi)`
- `32e-2*pi`

Invalid values include:

- `2 pi`
- `PI`

(A full formal grammar is available in the [Grammar](GRAMMAR.md) page)

### rotation matrix

Attributes which define a rotation matrix support either a row-major, whitespace delineated list of the 9 elements in a 3x3 rotation matrix, or a combination of axis-aligned rotations.

For the 9 element list, each element supports the basic _floating point_ attribute parsing described above.  For example,


```
1 0 0
0 1 0
0 0 1
```

or

```
1 0 0 0 1 0 0 0 1
```

would both be valid representations of an identity matrix.

For the combinations of axis-aligned rotations, the functions `Rx`, `Ry` and `Rz` are used to perform axis about rotations; their arguments can be any valid _floating point expression_ value:

```
Rz(pi/2)
```

or 

```
Rz(3 * pi/4 + 0.1)
```

These rotations can also be compounded by multiplying terms.

```
Rx(pi/2)*Rz(-pi/4)*Ry(pi/2)
```

(A full formal grammar is available in the [Grammar](GRAMMAR.md) page)

### translation vector

Attributes which define a translation vector support a whitespace delineated list of the 3 elements of a cartesian (x,y,z) vector.
