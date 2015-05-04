# GMHttpEngine
[![Build Status](https://travis-ci.org/MyLifeForTheOrc/gm-httpengine-studio.svg?branch=master)](https://travis-ci.org/MyLifeForTheOrc/gm-httpengine-studio)

Build On Android Studio with Gradle

A Simple Http Engine

## How to use

Request:
```java
GMHttpParameters httpParameters = new GMHttpParameters();
GMHttpRequest httpRequest = new GMHttpRequest();
// set url
httpRequest.setUrl("http://www.baidu.com/s?wd=%E6%9D%9C%E7%91%9E%E9%9B%AA");

// set parameter
httpRequest.setHttpParameters(httpParameters);

// set response listener
httpRequest.setOnResponseListener(l);

// set http method
httpRequest.setMethod(HttpMethod.HTTP_GET);

// execute and wait for response
mService.executeHttpMethod(httpRequest);
```
Response:
Implements OnResponseListener
```java
@Override
public void onResponse(GMHttpResponse response, GMHttpRequest request) {
  // run in no-ui thread

  String result = null;
  try {
    result = response.parseAsString();
    Log.d("test", result);

    // now if you had resolve this response, you can pass the result to an UI Handler to update UI
  } catch (Exception e) {
    result = "no content";
  }

  //        Toast.makeText(this,result,Toast.LENGTH_LONG).show();
}
```
