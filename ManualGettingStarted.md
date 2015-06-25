# Getting Started #
First you will need to download a copy of Ingatan from the [download](DownloadIngatan.md) page. You must also have a copy of the Java Runtime Environment installed on your system. You can get a copy of this [here](http://www.java.com/en/download/manual.jsp) or by installing [OpenJDK](http://openjdk.java.net/) from your operating system repository.

## Installing Ingatan ##
  * You do not need to install Ingatan; the one file you download is everything you need. Place it in any directory of your choice:
    * If you are using Windows you might like to place it in the Program Files folder and create a shortcut to it in your Start Menu, or on the desktop.
    * If you are running linux, you could put it in /usr/bin/, or your home folder, and then create a launcher for the panel (if you use gnome).

  * Ingatan will create its own directory in your user home folder. This is where Ingatan will save the data you create while using the program. You can change the directory in which Ingatan saves this data by using the command line parameter --homeDir="directory\_to\_use" when running Ingatan.

## Running Ingatan ##


---


### Linux ###
![http://lh3.ggpht.com/_ciQpatlgzzs/TDqld8VJRTI/AAAAAAAAAGA/778Km3LLGGo/s800/tux.png](http://lh3.ggpht.com/_ciQpatlgzzs/TDqld8VJRTI/AAAAAAAAAGA/778Km3LLGGo/s800/tux.png)
Use the following command in terminal:
```
    java -jar [path_to_Ingatan.jar]
```

or, if you are using openjdk and have noticed performance issues, use:
```
    java -Dsun.java2d.pmoffscreen=false -jar [path_to_ingatan.jar]
```

You might like to create a launcher for your gnome panel using this command.


---


### Windows ###
![http://lh4.ggpht.com/_ciQpatlgzzs/TDqleLf9_cI/AAAAAAAAAGE/AUIOlCjTp0o/s800/Windows_logo_35px.png](http://lh4.ggpht.com/_ciQpatlgzzs/TDqleLf9_cI/AAAAAAAAAGE/AUIOlCjTp0o/s800/Windows_logo_35px.png) You should be able to simply double click the Ingatan.jar file that you downloaded. If not, then you may not have the Java Runtime Environment installed. You can download a copy for free at: [java.com](http://www.java.com/en/download/manual.jsp).


---


### Mac OS ###
![http://lh6.ggpht.com/_ciQpatlgzzs/TDqleNezrAI/AAAAAAAAAGI/0_uzj7Bo2_Y/s800/macos.png](http://lh6.ggpht.com/_ciQpatlgzzs/TDqleNezrAI/AAAAAAAAAGI/0_uzj7Bo2_Y/s800/macos.png) You should be able to simply double click the Ingatan.jar file that you downloaded.
  * If you are asked to choose a program to open the file with, choose `Jar Launcher`.