# ProgressButton

An Android custom button to display the status at the end of the process.
[![Build Status](https://travis-ci.org/FlorianArnould/ProgressButton.svg?branch=master)](https://travis-ci.org/FlorianArnould/ProgressButton)


## How to use
Import the library using `jitpack.io`

1) Add the JitPack repository in your root build.gradle at the end of repositories:
```css
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
2) Add the dependency to your `app/build.gradle`:
```css
dependencies {
	implementation 'com.github.FlorianArnould:ProgressButton:master-SNAPSHOT'
}
```