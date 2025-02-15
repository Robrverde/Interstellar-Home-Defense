# Java For Quest (J4Q) Project
Digital Arts & Sciences Programs | Digital Worlds Institute | University of Florida

The J4Q project demonstrates how to create Virtual Reality apps for Meta Quest headsets as Native Android applications using Java in Android Studio. This project can be used as a setup for learning how to develop from scratch a basic game engine for Virtual Reality in Java. It can also be used along with a structured curriculum for teaching various topics related to game engine development such as: 3D Transformations using Matrices and Vectors, 3D geometry using triangular mesh, Animation loop, Shaders using GLSL, Controller input, Vibration feedback, and many other topics.   

## How to setup and run J4Q:

The following instructions will guide you step by step on how to setup and run J4Q in your system. The instructions include details for the following steps:

- Install all the required software. 
- Configure your headset.
- Open in Android studio the project file: `J4Q_46.0/JavaForQuest_Demo/Projects/Android/build.gradle`

### 1. Prerequisites

You need to install the following software to be able to compile Android software for Meta Quest headsets:

- [x] The latest version of Android Studio: https://developer.android.com/studio
- [x] Open the SDK Manager of Android Studio and install the SDK Platform 8.0 (Oreo) API Level 26
- [x] In the SDK Manager of Android Studio, go to the SDK Tools tab and check the box `Show Package Details` and install: 1) NDK (21.0.6113669), 2) CMake (3.22.1)

### 2. Configure your Meta Quest headset

- [x] Update the headset to the latest firmware and software by going to Quick Settings (where the time and battery status is shown) -> Settings -> Software Update
- [x] Enable Developer Mode by using the Meta Quest app

### 3. Run the Java For Quest (J4Q) Project

- [x] Open in Android studio the project file: `J4Q_46.0/JavaForQuest_Demo/Projects/Android/build.gradle`
- [x] If there errors during the build process, follow our troubleshooting guide at the end of this document.
- [x] Connect your Quest headset to your development computer using the charging cable.
- [x] In the headset accept the pop-up permission dialog.
- [x] When the name of your headset device appears in the device list in the toolbar of Android Studio, click on the Run button to compile the project and install it on your headset.
- [x] Test the J4Q Demo app in your headset.
- [x] In general, you can find and run the app in your Quest headset in your Library in the category 'Unknown Sources'. 

## Tested with the following system configuration:

- Android Studio Dolphin 2021.3.1 Patch 1
- Android SDK Platform 8.0 (Oreo) API Level 26
- Gradle 6.1.1
- NDK 21.0.6113669
- CMake 3.22.1
- Development Systems: MacOS 12.5 Monterey (M2 chip), MacOS 11.7.1 Big Sur (Intel chip), Windows 10, Windows 11
- Meta Quest 1 & 2 (Testing systems)

## Troubleshooting

### Gradle sync failed: NDK Not Configured

1. Go to File -> Project Structure -> SDK Location and download NDK or select one from the list (if any). 
2. After that, the file `JavaForQuest_Demo/Projects/Android/local.properties` will automatically be generated with the parameters `sdk.dir` and `ndk.dir`. 
3. If the file does not contain `ndk.dir` you can edit the text and append a line with the path to your NDK. For example: `ndk.dir=...some path.../Android/sdk/ndk/21.0.6113669`. 
4. Then, copy the `local.properties` file and paste it in the `J4Q_46.0` folder. 
5. Finally, try again by clicking on the "try again" button on top of the text editor, when viewing the local.properties file.

### Execution failed for task ':JavaForQuest_Demo:Projects:Android:genDebugKeystore'. Process 'command python3' finished with non-zero exit value 1

1. This error appears if the file `JavaForQuest_Demo/Projects/Android/android.debug.keystore` is missing, possibly because it was automatically removed when performing Build -> Clean Project or Build -> Rebuild Project. 
2. An easy way to fix this is to copy the file `JavaForQuest_Demo/Projects/Android/keystore/android.debug.keystore` into the folder `JavaForQuest_Demo\Projects\Android` and then re-run. 

Otherwise, a more lengthy solution is to open a command line terminal and go into the folder `JavaForQuest_Demo\Projects\Android` and run the command `python3 ../../../bin/scripts/build/ovrbuild_keystore.py`. This step requires prior installation of python3 as well as setting the system variables `ANDROID_NDK_HOME` and `ANDROID_HOME`.

### Setting Android Studio as a Developer Tool in Mac

In MacOS it is helpful if you set Android Studio as a Developer Tool. 

1. To do that, first enable Developer Mode by opening the Terminal and typing `spctl developer-mode enable-terminal`. 
2. Then in System Preferences go to Security & Privacy and in the Privacy Tab, go to the option Developer Tools and add Android Studio.
