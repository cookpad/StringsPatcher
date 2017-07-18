# Strings Patcher

It's very common (at least in our company) that just after a new release, someone discovers a translation that is wrong, or even missing in a specific language (we support over 10 different languages).  

When this happens we have 2 options: Create a patch release with the missing translation and release again, whith all the work and overhead that it implies, or wait until next week for a release with the translation fixed. 

Of course, most of this problems can be avoided by being very careful when adding a new translation, but we are humans and we make mistakes. This library has been made for making those mistakes a little bit less painful.

## Features
- Update (Patch) String values on the fly.
- Serverless implementation, relying on Google Spreadsheets for data storage

## Setup

### Adding the library to your project

Add to top level gradle.build file
```
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
Add to app module gradle.build file
```
dependencies {
    compile 'com.github.cookpad:StringsPatcher:0.0.3'
}
```

### Setting up the spreadhseet
We recomend that, if you can, you use the first setup (A). The drawback is that your string patches spreadhseet has to be public. If you want your spreadsheets to be private use the setup (B).

<details>
<summary>A - (Easy) Public spreadhseet</summary>

##### 1 Getting the spreadsheet key
- Go to google Drive and create a new spreadsheet
- Add this 3 words into the first 3 columns, on the firt row: `lang`, `key` & `value`  
<img width="485" alt="captura de pantalla 2017-07-06 a las 10 57 11" src="https://user-images.githubusercontent.com/4237014/27916668-1a378334-626a-11e7-9a6c-b4c21fe1b470.png">
- Click on File > Publish to the web > Publish
- Copy the Spreadsheet key which is a long string of numbers and letters that you can get from the displayed url `https://docs.google.com/a/cookpad.jp/spreadsheets/d/[spreadsheet-id]/pubhtml` and don't loose it, you'll need it when setting up the library.

##### 2 Other spreasheet setups
- Name your worksheet (bottom tab) with the same name as your android build number (this is optional but you have to, at least, name the worksheet with something you'll remember)
- [optional] You can create multiple worksheets named after each android build number, that way you can mantain different String patches for different versions of your app  
<img width="241" alt="captura de pantalla 2017-07-06 a las 11 01 55" src="https://user-images.githubusercontent.com/4237014/27916819-8c12dd1e-626a-11e7-8912-37f5cd940196.png">
</details>

<details>
<summary>B - (Hard) Private spreadhseet</summary>

##### 1 Getting the spreadsheet key
- Go to google Drive a create a new spreadsheet
- Add this 3 words into the first 3 columns, on the firt row: `lang`, `key` & `value`
- Copy the Spreadsheet key which is a long string of numbers and letters that you can get from the url `https://docs.google.com/spreadsheets/d/[spreadsheet-id]/edit#gid=0` and don't loose it, you'll need it when setting up the library.  
<img width="485" alt="captura de pantalla 2017-07-06 a las 10 57 11" src="https://user-images.githubusercontent.com/4237014/27916668-1a378334-626a-11e7-9a6c-b4c21fe1b470.png">

##### 2 Other spreasheet setups
- Name your worksheet (bottom tab) with the same name as your android build number (this is optional but you have to, at least, name the worksheet with something you'll remember)
- [optional] You can create multiple worksheets named after each android build number, that way you can mantain different String patches for different versions of your app  
<img width="241" alt="captura de pantalla 2017-07-06 a las 11 01 55" src="https://user-images.githubusercontent.com/4237014/27916819-8c12dd1e-626a-11e7-8912-37f5cd940196.png">

##### 3 Getting Google App credentials
- Go to Google Dev Console https://console.developers.google.com and create a new App
- In Dashboard enable: **Google Sheets** in order to access the spreadsheets
- Now go to Credentials > Create Credential > OAuth client ID > Web application
- Add `http://localhost` to **Authorized JavaScript origins**
- Also add `http://localhost` and `https://developers.google.com/oauthplayground` to **Authorized redirect URIs**
- Copy somewhere your client ID and Secret

##### 4 Getting a Refresh Token
- Now go to https://developers.google.com/oauthplayground/
- Click on the top-right settings icon to open the settings drawer
- Check **Use your own OAuth credentials** and add your client ID and client Secret  
<img width="300" alt="captura de pantalla 2017-07-06 a las 11 39 30" src="https://user-images.githubusercontent.com/4237014/27917013-0f7d9450-626b-11e7-92e2-31904dc36297.png">

- Now on the left input bow that says *input your own scopes* type `https://www.googleapis.com/auth/spreadsheets.readonly` and press **Authorize APIs**  
<img width="300" alt="captura de pantalla 2017-07-06 a las 11 48 38" src="https://user-images.githubusercontent.com/4237014/27916893-b432a496-626a-11e7-9930-8e5ab842e927.png">

- You will see a google login form. Login with the **same user** that you used to create the spreadhseet. Press **Allow** (this only allows the app to read your spreadsheets, not modify anything)

- Wait for the playground to load and then press **Exchange authorization code for tokens**
- Copy your **Refresh token** somewhere, you'll need it too. (You might need to press in **Step 2Exchange authorization code for tokens** to see the tokens)  
<img width="300" alt="captura de pantalla 2017-07-06 a las 11 48 47" src="https://user-images.githubusercontent.com/4237014/27917050-29074dc6-626b-11e7-9fc4-5c14289e9596.png">

- You should now have the **client ID**, **client Secret** and **Refresh Token**. You're ready!
</details>


## Usage
Call `syncStringPatches()` in your Android `Application` class, supplying the `context` and your `spreadSheetKey` as the only mandatory parameters.

```kotlin
class StringsPatcherApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val spreadSheetKey = "YOUR_SPREAD_SHEET_KEY"
        syncStringPatches(this, spreadSheetKey)
    }
} 
```

`syncStringPatches` expose next optional params: 

 * *worksheetName*: the worksheet name (The spreadsheet may be composed by several worksheets). This param has as default value the versionCode of the application. That way, your spreadsheet should have as many worksheets as release versions (1,2,3,4,...).
 * *locale*: the locale used to filter strings. As default value the system locale is assigned.
 * *logger*: callback function to listen for errors emission. As default a dummy implementation does nothing.
 * *googleCredentials*: only supply these credentials if the spreadSheet has private access.
 
Once StringPatches has been initalized, in order to access the patches, you must retrieve string resources at runtime by calling either `Context::getSmartString` or `Resources::getSmartString`. For formatting strings, `Context::getSmartString(formatArgs)` and `Resources::getSmartString(formatArgs)` are exposed.

If there is no patch for a given key, the library fallbacks to the system resources.
