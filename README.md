# Test TapPay

## Getting Started

### Android Environment

#### STEP 1

複製 <this project>/android/app/libs/tpdirect.aar 到 <your project>/android/app/libs/tpdirect.aar


#### STEP 2

複製 PrimeDialog 到你的 android 專案底下

![](/screenshots/screenshots1.png)

MainActivity.kt
```
MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call, result ->

                if (call.method == "tappay") {
                    Log.d(TAG, "i got u ^.<")

                    val dialog = PrimeDialog(context, object : PrimeDialogListener {
                        override fun onSuccess(prime: String) {
                            Log.d(TAG, "onSuccess, prime=$prime")
                            result.success(prime)
                        }

                        override fun onFailure(error: String) {
                            Log.d(TAG, "onFailure, error=$error")
                            result.success(error)
                        }

                    })

                    dialog.show()

                } else {
                    Log.d(TAG, "u know nothing ${call.method}")

                    result.error("404", "404", null)
                }

//                result.error("404", "404", null)
            }
```

#### STEP 3
Android 專案必要檔案：
 - test_tappay/android/app/src/main/res/layout/dialog_message.xml
 - test_tappay/android/app/src/main/res/drawable/button_black_ripple.xml
 - test_tappay/android/app/src/main/res/values/colors.xml (內容要新增)
 - test_tappay/android/app/src/main/res/values/styles.xml (內容要新增)

 #### STEP 4 Manifest
 /Users/waynechen/git/workspace-flutter/test_tappay/android/app/src/main/AndroidManifest.xml

加上 INTERNET 權限
```
<uses-permission android:name="android.permission.INTERNET" />
```

在最外層 <manifest> tag 底下加上
```
xmlns:tools="http://schemas.android.com/tools"
```

在 <application> tag 底下加上
```
tools:replace="android:label"
```


#### STEP 5 platform channel 設定
```
static const platform = MethodChannel('test_tappay');

  Future<void> _inputCreditCard() async {
    String message;

    try {
      final String result = await platform.invokeMethod('tappay');
      message = result;
    } on PlatformException catch (e) {
      message = e.message ?? '';
    }

    setState(() {
      _tappayMessage = message;
    });
  }
```