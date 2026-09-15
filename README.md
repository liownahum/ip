# Grower

Grower is a Java task management application. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/Grower.java` file, right-click it, and choose `Run Grower.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:


**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.


## Input errors and saved data

Enter one command per line. Task numbers start at 1; use `list` to see them.
The `list`, `sort`, and `bye` commands take no arguments. Task descriptions
cannot contain `|`, which is reserved for the saved file format.

Grower saves changes to `data/grower.txt`. If saving fails, the command is not
applied: check the folder, write permissions, and available disk space, then
retry. Viewing and searching tasks do not write to the file.

If saved data is unreadable or contains invalid records, Grower shows a startup
error and disables changes to protect the original file. Valid records remain
viewable. Back up the file before repairing it, then restart Grower. Each record
uses ` | ` between fields: task type (`T`, `D`, or `E`), completion (`0` or `1`),
and a nonempty description. Deadlines also require an ISO date and time (for
example, `2026-09-15T18:00:00`); events require start and end times, with the end
strictly after the start. Extra fields are rejected.

Saving requires a filesystem that supports atomic file replacement, so an
interrupted write cannot leave a partially overwritten task file.
