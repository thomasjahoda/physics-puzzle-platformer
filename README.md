

# Contributing

TODO - the documentation is a work-in-progress

TODO local.properties?

## Developing Maps

Define location of .exe in local.properties

Use the 'Tiled' map editor to create and edit maps within `core/src/main/tiledmaps`.

Choose `objecttypes.xml` as object types file using the 'View' > 'Object Types Editor' > 'File' > 'Choose Object Types File'.

Open 'View' > 'Views and Toolbars' > 'Templates'.
In the 'Templates' view, click the 'Choose Templates Directory' button.
Choose the `objecttemplates` directory.

Use object templates for common objects.
You can create custom objects and detach objects from templates if they should be unique or limited to this map.


## Android
The LibGDX project generation seems to introduce some problems that the IDEs do not correctly initialize some Android configuration.
If running the project via IntelliJ and the default android run-config does not work, follow these steps:

* Download and install Android Studio
** Use the same SDK as for your other IDE
* Create a new project via Android Studio based on some simple template
* Run the project
** This creates the ~/.android/debug.keystore and might also create some additional files that are necessary for developing on Android.
** Go back to the original IDE and run the project there using the default android run-config

The gradle task `android:run` only runs the application but does not build it.
You can build the application first with `android:build`, but this takes very long.
Using the IDE (either IntelliJ with Android plugin or Android Studio) is recommended.


## Executing tests
Either execute the tests via gradle (e.g. core:test) 
or use IntelliJ but change the test runner to "Gradle Test Runner" in the settings.
