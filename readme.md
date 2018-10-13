# Common Code

This is the common code of FTC Team 8693, also know as the Hazen Scarabs. This library contains
code that the team uses for multiple seasons of the First Tech Challenge.

## Using the Library
Inside andriod studio first look at the side view where it show your file directory. By default it is on the wrong view, so click on where it says `Android` and change it to `Project`.

Next find the `build.gradle` file inside the **project** (not TeamCode folder) and add the line `maven { url 'https://jitpack.io' }` inside the `buildscript`, `allprojects` then `repositories` section. It should look something like this:
```
allprojects {
  repositories {
    jcenter()
    google()
    maven { url 'https://jitpack.io' }
  }
}
```
This adds Jitpack as a repository from which code (specificlly this library) can be download.

Next find the `build.gradle` file inside the **TeamCode** folder and add the line `implementation 'com.github.HazenRobotics:common-code-library:TAG_NUMBER'` inside the `dependencies` section, where the TAG_NUMBER is of whatever version of the library you wish to use. Make sure that the version of the library you are using has the same FTC SDK version that the project is using, otherwise the code might not be compatable. It should look something like this:
```
dependencies {
    implementation 'com.github.HazenRobotics:common-code-library:1.0.0'
}
```
This adds the library as something which the project is using as a dependency.

To use the code from the library we need to ensure that it has been downloaded. The updated gradle scripts need to be run by going to the `File` section at the top of the screen and clicking `Sync Project with Gradle Files`.

## Making Changes

### Conventions and rules

- All public functions and classes must have [JavaDoc](https://en.wikipedia.org/wiki/Javadoc).

Since the purpose of this library is to simplify doing tasks within the First Tech Challenge, your code must be able to be used without necessarily understanding how exactly it works internally. Documentation is key so that your new ~~useless~~ useful methods can be used.

To easily add JavaDoc in most IDEs type `/**` and hit enter above a varible, class, or function definition (once all the prameters have been specified) and the format should be auto generate to look something like this:
```
/**
 * 
 * @param varA
 * @param varB
 * @return
 */
```
Then you can just write a description of the function, each of the paramaters, and what the function will return. This is also a good place to mention requirements of the parameters an limits on what can be returned. (For example maybe a negitive number can't be passed for a speed.)

- Internal Source should be (self) commented

Its likely with the organization members changing over the years that some ~~fool~~ ingenious programmer other than you will have to eventually look at your code. Make their life less miserible by making your code understandable. 

This means chosing method and varible names that make sense, using constants in place of [magic numbers](https://en.wikipedia.org/wiki/Magic_number_(programming)), and adding actual comments to explain sections with tricky logic or where many diffrent things are being done. (Dont add a comment which explains that `rightWheel.setPower(WHEEL_POWER)` is `//Setting the wheel power`)

### Tagging your new version

Okay, you're new code for the team is all complete, committed, and pushed onto the repository; Time to make a tag so that the game code can use—Stop... Stop right there. Test your code first (using the specific commit on Jitpack) before you tag it.

Once you're new version has been test fully, you're ready to tag it. Your tag name should follow the [Semantic Versioning conventions](https://semver.org/), so check what the previous tag created was and consider if your new tag would constitute a bugfix, new feature, or codebreaking change.

You next need to find the commit number of the commit you wish to tag. Go to the Github repositiory, go to the [list of commits](https://github.com/HazenRobotics/common-code-library/commits/master), and click on the clipboard icon from your commit on the list to copy it. (You should probably paste this somewhere for later use.) 

Once you have the tag and commit number you will use, open up Git Bash (or some command line interface that can use git), navigate to where you cloned the library, and enter the following commands:
```
git tag -a TAG_NUMBER COMMIT_NUMBER
git push origin :refs/tags/TAG_NUMBER
```
After entering the first command a new view should pop up where you will write a description of the tag. To do this by default on windows Git Bash, first hit <kbd>i</kbd> to enter insert mode and then enter your description. Once you are finished press <kbd>esc</kbd> once, type `:x!`, and hit <kbd>enter</kbd>.
