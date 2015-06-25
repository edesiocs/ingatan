Ingatan has been designed to be flexible and suitable for many different learning applications. Inserting special characters has been made very easy in Ingatan by the innovative symbol menu. When the user presses ctrl+space, followed by a letter or number, an inline menu of special characters is shown. The `SymbolMenu` class is simply an extended `JFrame` that encapsulates the String corresponding to each 'base character', as well as the current selection state. An external key listener added to the text field to which the symbol menu instance belongs calls the symbol menu's `moveSelectionLeft`, `moveSelectionRight`, `moveSelectionStart`, and `moveSelectionEnd` methods. The `getSelectedCharacter` method can then be called upon the user pressing enter, and handled appropriately.

![http://lh5.ggpht.com/_ciQpatlgzzs/TDkHoLM_8NI/AAAAAAAAADw/IfWAJmw4GHU/s800/symbolMenu.png](http://lh5.ggpht.com/_ciQpatlgzzs/TDkHoLM_8NI/AAAAAAAAADw/IfWAJmw4GHU/s800/symbolMenu.png)

## Default Values ##
The following are the default values for the symbols corresponding to each of the base keys. If the base key is capitalised, then the symbols are also capitalised.

| <h3> base key </h3>| <h3> symbols </h3> |
|:-------------------|:-------------------|
| a                  | äåàáãâæāăą |
| c                  | çćĉċč         |
| d                  | ďđ               |
| e                  | èéêëęēěĕɛ |
| f                  | ƒ                 |
| g                  | ĝğġģɠ         |
| h                  | ĥħɦɧ           |
| i                  | ìíîïĩīĭį   |
| j                  | ĵ                 |
| k                  | ĸķ               |
| l                  | ĺļľŀł         |
| m                  | ɯɰɱ             |
| n                  | ñńņňŉŋ       |
| o                  | öøœòóőôõōŏǿ |
| r                  | ŕŗř             |
| s                  | ʂșśŝşš       |
| t                  | ţťŧțʈʇ       |
| u                  | ùúűûüũūŭůų |
| w                  | ŵʍ               |
| y                  | ŷźżžʏ         |
| z                  | ʐʑʓʒ           |
| 0                  | asdsad             |
| 1                  | παλεθβγδ   |
| 2                  | κημιζνξο   |
| 3                  | ρςστυφχψω |
| 4                  |                    |
| 5                  |                    |
| 6                  |                    |
| 7                  |                    |
| 8                  |                    |
| 9                  |                    |