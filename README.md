# Android Remote Control

A set of UI toolkits to remote control your Android device.

## Setup

Enter the `project` directory and run `./gradlew createRuntime` to set the
project up for running it.

## Tools

### Viewer

The Viewer is an application that mirrors your Android's screen in a window on
your development machine. It lets you emulate touch on the phone using your
mouse pointer. Run this to start the viewer:

    ./scripts/viewer

![Viewer](screenshots/viewer.png)

## Toolkit

The toolkit is an application that allows you to manage your Android device. You
can install APK files by dragging and dropping them onto a special area of the
toolkit's window, take screenshots and store them on your development machine,
uninstall applications easily and also clear data of any application to reset it
to its initial state. Run this to start the toolkit:

    ./scripts/toolkit

![Toolkit overview](screenshots/adb-toolkit.png)

Clicking the 'List packages' button opens a dialog that let's you perform
actions on installed applications on the device in batches. Currently you
can uninstall them or clear data to reset them to their initial state.

![Toolkit package list](screenshots/package-list.png)
