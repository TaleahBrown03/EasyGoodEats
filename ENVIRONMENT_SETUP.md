# Wiki: Development Environment & VM Setup Guide

Setting up a robust development environment is the first step toward building successful Android applications. This guide details the process of configuring Android Studio and an Android Virtual Device (AVD).

## 1. Prerequisites
Before beginning, ensure your machine meets the following requirements:
* **Operating System:** Windows 10/11, macOS, or Linux.
* **RAM:** Minimum 8GB (16GB highly recommended for VM performance).
* **Disk Space:** At least 20GB of free space.

## 2. Installing the IDE
1. Download the latest version of **Android Studio (Hedgehog or later)** from the official developer website.
2. Run the installer and select the "Standard" setup type.
3. Ensure the **Android SDK**, **Android SDK Platform**, and **Android Virtual Device** components are selected for installation.

## 3. Configuring the Virtual Machine (AVD)
To run and test the app without a physical device, follow these steps:
1. Open Android Studio and navigate to **Tools > Device Manager**.
2. Click **Create Device**.
3. **Select Hardware:** Choose a modern profile like "Pixel 7" or "Pixel 8".
4. **Select System Image:** 
   * Download a recent API level (e.g., API 34 or 35).
   * Choose an image with "Google Play" support to access essential services.
5. **Verify Configuration:** Under "Emulated Performance," ensure "Graphics" is set to "Hardware - GLES 2.0" for better speed.
6. Click **Finish**. You can now launch the VM by clicking the "Play" button in the Device Manager.

## 4. Project Configuration
Once the environment is ready:
1. Clone the repository: `git clone https://github.com/TaleahBrown03/EasyGoodEats.git`.
2. Open the project in Android Studio.
3. Wait for **Gradle Sync** to complete (this downloads necessary libraries like Retrofit and Jetpack Compose).
4. Select your newly created VM from the device dropdown at the top and press the green **Run** button.

## 5. Troubleshooting
* **VT-x/AMD-V Errors:** If the VM fails to start, ensure Virtualization is enabled in your computer's BIOS settings.
* **Gradle Failures:** Check your internet connection and select "File > Invalidate Caches / Restart" to refresh the build environment.
