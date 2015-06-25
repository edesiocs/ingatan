# Introduction #

The bundled SketchEl templates are stored within the Ingatan jar file. There are two options for adding a new template:
  * Hard code a reference to the template in the net.sf.sketchel.Template.java and package the .el file into the Ingatan jar.
  * Save the .el file to Ingatan's custom SketchEl template directory (at `.ingatan/chem_templates`).


## Save as a Custom Template ##
  * Load Ingatan and go to the library manager.
  * Get into the image acquisition menu by creating a flexi question, or going into a pre-existing flexi question and pressing the 'add image' button on one of the text area toolbars.
  * Go into SketchEl, and draw your chemical structure.
  * Press the menu button at the top left hand corner and choose 'Save as Template'.
  * Save the file in the location that appears in the save dialog.
  * Reload Ingatan and your custom template should now be usable.

## Hard Code a Bundled Template ##
  * Open the `net.sf.sketchel.Templates` class source file.
  * You should see a big list of template names at the top, like this:
```
...
names.add("arrow.el");
names.add("half_arrow.el");
names.add("half_arrow2.el");
names.add("equilibrium.el");
names.add("carbonyl.el");
...
```
  * Add an entry for your own template, using only the filename of the template and its .el extension, like this: `names.add("yourTemplate.el");`.
  * Finally, add the template file to `/src/net/sf/sketchel/templ/` folder in your source directory.
  * Compile.
  * The template you added should now be usable in SketchEl.

# File Format #
The SketchEl file format is described in part here. Here is a sample (this is the benzene template):
```
SketchEl!(6,6)
C=6.1,0.25;0,0
C=4.800961894323342,-0.4999999999999999;0,0
C=7.399038105676658,-0.4999999999999999;0,0
C=4.800961894323342,-2.0;0,0
C=6.1,-2.75;0,0
C=7.399038105676658,-2.0;0,0
1-2=1,0
1-3=2,0
2-4=2,0
4-5=1,0
5-6=2,0
6-3=1,0
!End
```

  * `SketchEl!` is the file start signature.
  * The following `(6,6)` indicates that there are 6 atoms and 6 bonds in the form `(atom_count,bond_count)`.
  * The next 6 lines describe 1 atom each. 'C' is the atom label, the equals sign indicates that the properties of this atom follow. The two comma separated numbers that follow are the x y coordinates of the atom.
  * Unsure about what the `;0,0` on each atom line indicates.
  * After each atom has been specified, the bonds are specified. `1-2` indicates a bond _from_ atom 1 _to_ atom 2. `=1,0` indicates that it is a single bond. Not sure what the `,0` specifies.
  * For another example of how bonds are specified, `1-3=2,0` specifies a bond from atom 1 to atom 3 of bond order 2 (i.e. a double bond). Again, I am not sure about the `,0`.
  * The final line of the SketchEl file is the `!End` EOF signature. Redundant since bond count and atom count are specified. Heh.