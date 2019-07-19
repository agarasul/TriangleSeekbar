# TriangleSeekbar

It's simple lightweight Triangle Seekbar library 

![Triangle Seekbar Demo](triangleseekbar.gif)

[![](https://jitpack.io/v/agarasul/TriangleSeekbar.svg)](https://jitpack.io/#agarasul/TriangleSeekbar)


### Attributes that you can change

All of this attributes also can be changed from code with relevant methods.

| Attribute | What it does | What type of argument it receives
| ------ | ------ | ------ |
| app:seekbarColor | color of seekbar | color (For example #b7b7b7) |
| app:seekbarLoadingColor | progress color of seekbar |  color (For example #b7b7b7) |
| app:textColor| progress text color | color (For example #b7b7b7) |
| app:textFontName | set font for progress text | font which should locate in assets/fonts folder |
| app:textFontSize | set font size for progress text | For example 14f |
| app:showProgress | Should progress text visible or not | true or false
| app:progressTextPosition | The position of progress text in the seekbar | Possible positions topLeft,topRight,bottomLeft,bottomRight,center |
| app:progress | Default progress for seekbar | value from 0.0 to 1.0

### Installation

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.agarasul:TriangleSeekbar:1.0.5'
	}



