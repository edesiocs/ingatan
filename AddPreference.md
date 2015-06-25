# Introduction #

The preferences window is available from the main menu. At time of writing this, there are currently two preferences:
  1. Which characters to use for each key for the [symbol menu](SymbolMenu.md).
  1. Whether or not to indicate question selection in the library manager question list using a little icon of Michael (Ingatan's mascot) or a blue dot.

All preferences are accessible at run time from the `IOManager` class, as with most things in Ingatan. Preferences are serialised to the prefs file, which is written and parsed by `ParserWriter` to the location specified by the `IOManager` at run time.

The following steps describe how to add a preference to Ingatan.

# Steps #
  1. Create the variable in `IOManager`. Usually this means adding a static boolean or String variable to the `IOManager` class, with corresponding getters and setters.
  1. If required, create the GUI components in the `PreferencesDialog` class. Use the `IOManager` get and set methods you have just created to show the user what the current setting is. You may wish to include a default setting to revert to when the user presses the `Defaults` button.
  1. Make changes to the `ParserWriter` class' methods: `writePreferencesFile()` and `parsePreferencesFile()` to include your new variable in the XML file.
  1. You must write a new file version number to the `writePreferencesFile()` method, as well as structure code in the `parsePreferencesFile()` method to handle older versions of the file structure that do not contain your newly added preference. As Ingatan is developed, many preferences should be added all at once to reduce the number of required file type versions.

The `ParserWriter`'s `parsePreferencesFile()` method is called as part of the `IOManager`'s initiation method, so all you need to do is ensure that the appropriate setter for your preference variable is used in the `parsePreferencesFile()` method.

Use the preference variable using the getter you created for the `IOManager`.