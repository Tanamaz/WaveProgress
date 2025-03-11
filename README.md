# WaveProgress - Android Wave Progress View  

[![JitPack](https://jitpack.io/v/Tanamaz/WaveProgress.svg)](https://jitpack.io/#Tanamaz/WaveProgress)

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

A dynamic wave progress view for Android with gradient color support. Perfect for download progress, liquid level visualization, and other fluid animations.  

![Demo GIF](https://github.com/Tanamaz/WaveProgress/blob/378710ac4d8415d50f6951b508c1451be436334c/gif/demo.gif)

## Features  
- 🌊 Smooth wave animation (60 FPS)  
- 🎨 Customizable 3-color gradient (horizontal/vertical/diagonal)  
- ⚙️ Dynamic progress control (0-100%)  
- 📱 XML attributes and programmatic configuration  

## Installation  
To get a Git project into your build:

**Step 1.** Add the JitPack repository to your build file:

```css
dependencyResolutionManagement {
	repositories {
		mavenCentral()
		maven { url = uri("https://jitpack.io") }
	}
}
```

**Step 2.** Add to your module's `build.gradle`:  

```css
dependencies {
	implementation("com.github.Tanamaz:WaveProgress:Tag")
}
```

## Basic Usage  

### XML Layout  
```xml  
    <com.navobytes.ui.WaveProgress
        android:id="@+id/waveProgress"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:amplitude="24dp"
        app:waveSpeed="0.1"
        android:alpha="0.8"/>
```

### Programmatic Configuration  
```kotlin  
val waveProgress = findViewById<WaveProgress>(R.id.waveProgress)  

// Set progress (0.0-1.0)  
waveProgress.setProgress(0.65f)  

// Configure wave parameters  
waveProgress.apply {  
    amplitude = 25f    // Wave height in dp  
    waveSpeed = 1.2f   // Animation speed  
}  
```

## Attributes  
| Attribute       | Type  | Default | Description                    |
| --------------- | ----- | ------- | ------------------------------ |
| waveSpeed       | float | 0.1     | Wave animation speed (0.1-0.5) |
| amplitude       | float | 20      | Wave peak height (in dp)       |
| initialProgress | float | 0       | Initial progress (0.0-1.0)     |

## Advanced Usage  
### Dynamic Color Configuration  
```kotlin  
// Set 3-color gradient (right-to-left: blue → purple → orange)  
waveProgress.setWaveColors(  
    ContextCompat.getColor(this, R.color.wave_blue),  
    ContextCompat.getColor(this, R.color.wave_purple),  
    ContextCompat.getColor(this, R.color.wave_orange)  
)  

// Diagonal gradient (top-left to bottom-right)  
waveProgress.setWaveColors(  
    Color.parseColor("#FFFF9800"), // Top-left: orange  
    Color.parseColor("#FF9C27B0"), // Middle: purple  
    Color.parseColor("#FF2196F3")  // Bottom-right: blue  
)  
```

### Gradient Direction  
Override `updateGradient` for custom directions:  
```kotlin  
private fun updateGradient() {  
    gradient = LinearGradient(  
        0f,                      // Start X  
        viewHeight * (1 - progress), // Start Y  
        viewWidth,               // End X  
        viewHeight,              // End Y  
        colors,                  // Color array  
        floatArrayOf(0f, 0.5f, 1f), // Color positions  
        Shader.TileMode.CLAMP  
    )  
}  
```

## Recommended Color Setup  
Define in `res/values/colors.xml`:  
```xml  
    <color name="wave_blue">#FF2196F3</color>
    <color name="wave_purple">#FF9C27B0</color>
    <color name="wave_orange">#FFFF9800</color>
```

## Notes  
1. Minimum API level: 21 (Android 5.0+)  
2. Color array must contain exactly 3 colors  
3. Progress updates automatically trigger redraw  
4. For best performance, test on physical devices  

## Sample App  
See full implementation: [Sample Project](https://github.com/Tanamaz/WaveProgress)  

---
*Powered by Canvas hardware acceleration. Smooth rendering guaranteed on modern devices.* [0]
