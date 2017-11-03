# EasyUpdateApplication

### Description
EasyUpdateApplication is a library allowing you to include an update feature, this library is very easy to use

### Function (of the library)
* Check whether an update is available or not
* Warning you in
* Download the application if an update is available
* Test function (optional but very useful)

### Get Library
To get the library copy the EasyUpdateApplicationLibrary folder to your project

# Use
* First, create a text file and modify its extension ```.txt``` in ```.json```

* Second, write in your file ```json``` this who figure below
```
{
    "versionName": "1.0",
    "versionCode": "1",
    "tested": "yes",
    "url": "https://raw.githubusercontent.com/Name_Dev/Name_Repository/master/update/application/app.apk",
    "release": 
    [
        "- Add support",
        "- Improvement"
    ]
}
```
* Third, put your file ```json``` in order to download it afterwards (make sure that the download link is not interchangeable), once you have uploaded your file ```json``` copy its direct download link
 
* Fourth, note this import into your activity
```gradle
import com.msay2.easyupdateapplication.update.UpdateApplication;
```

* Fifth, note this line to run the update feature
```gradle
UpdateApplication.checkUpdateApplication(this, URL_JSON, "Care");
```
* ```this``` is the context of your activity, it inherits the ```Class``` ```Activity```
* ```URL_JSON``` is the download link of the file ```json```, it inherits the ```Class``` ```String```
* ```"Care"``` is the name of your application, enter the name of YOUR application, it inherits the ```Class``` ```String```

* Sixth, compile your project and put the file online ```.apk``` of your application and copy its download link (make sure that the download link is not interchangeable) and paste it into the section ```url``` which is in your file ```.json```
```
 "url": "paste_your_URL_apk",
```

* Look at an example of use [MainActivity.java](https://github.com/MSay2/EasyUpdateApplication/blob/master/app/src/main/java/com/msay2/easy_update_application/MainActivity.java)

# More informations
* The method ```checkUpdateApplication()``` contains several functions set out above
* The method ```checkUpdateApplication2()``` contains only the update functionality available, if no update is available no dialog box will appear
* In your file ```.json``` is the section ```tested``` this feature allows you to display a special dialog box informing the user that the future application update is in test period, this is extremely useful if you want users to no longer update your application, this function includes only two texts ```" yes"``` or ```"no"```

# Clone Git Repository
Please refresh the project so that it can work !

 # License

```
Copyright 2017 MSay2

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements. See the NOTICE file distributed with this work for
additional information regarding copyright ownership. The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
```
