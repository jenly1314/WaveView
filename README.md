# WaveView

[![Download](https://img.shields.io/badge/download-App-blue.svg)](https://raw.githubusercontent.com/jenly1314/WaveView/master/app/release/app-release.apk)
[![JitPack](https://jitpack.io/v/jenly1314/WaveView.svg)](https://jitpack.io/#jenly1314/WaveView)
[![CI](https://travis-ci.org/jenly1314/WaveView.svg?branch=master)](https://travis-ci.org/jenly1314/WaveView)
[![API](https://img.shields.io/badge/API-16%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/mit-license.php)

WaveView for Android 是一个水波纹动画控件视图，支持波纹数，波纹振幅，波纹颜色，波纹速度，波纹方向等属性完全可配。

## Gif 展示
![Image](GIF.gif)

> 你也可以直接下载 [演示App](https://raw.githubusercontent.com/jenly1314/WaveView/master/app/release/app-release.apk) 体验效果

## 引入

### Gradle:

1. 在Project的 **build.gradle** 或 **setting.gradle** 中添加远程仓库

    ```gradle
    repositories {
        //...
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
    ```

2. 在Module的 **build.gradle** 里面添加引入依赖项

    ```gradle
    implementation 'com.github.jenly1314:WaveView:1.0.1'
    ```

## 使用

### WaveView自定义属性说明
| 属性 | 值类型 | 默认值 | 说明 |
| :------| :------ | :------ | :------ |
| waveCount | integer |2| 波纹数量 |
| waveColor | color |<font color=#00B9D2>#3F00B9D2</font>| 波纹颜色 |
| waveAmplitude | dimension | 20dp | 波纹振幅 |
| waveMaxSpeed | dimension | 4dp | 波纹最大速度 |
| waveMinSpeed | dimension | 2dp | 波纹最小速度 |
| waveRefreshInterval | integer | 15 | 刷新频率时间间隔，单位:毫秒 |
| waveAutoAnim | boolean | true | 是否自动播放动画 |
| waveInverted | boolean | false | 是否倒置 |
| waveDirection | enum | DIRECTION.LEFT_TO_RIGHT | 波纹方向，默认从左到右 |
| waveVertical | boolean | false | 是否垂直 |


### 示例

布局示例
```Xml
    <com.king.view.waveview.WaveView
        android:id="@+id/waveView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```

代码示例
```Java
    //开始动画
    waveView.start();
    //停止动画
    waveView.stop();

```
更多使用详情，请查看[app](app)中的源码使用示例或直接查看 [API帮助文档](https://jitpack.io/com/github/jenly1314/WaveView/latest/javadoc/)

## 版本记录

#### v1.0.1：2019-11-8
* 移除Support.appcompat-v7依赖

#### v1.0.0：2019-2-23
*  WaveView初始版本

## 赞赏
如果您喜欢WaveView，或感觉WaveView帮助到了您，可以点右上角“Star”支持一下，您的支持就是我的动力，谢谢 :smiley:
<p>您也可以扫描下面的二维码，请作者喝杯咖啡 :coffee:

<div>
   <img src="https://jenly1314.github.io/image/page/rewardcode.png">
</div>

## 关于我

| 我的博客                                                                                | GitHub                                                                                  | Gitee                                                                                  | CSDN                                                                                 | 博客园                                                                            |
|:------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------|
| <a title="我的博客" href="https://jenly1314.github.io" target="_blank">Jenly's Blog</a> | <a title="GitHub开源项目" href="https://github.com/jenly1314" target="_blank">jenly1314</a> | <a title="Gitee开源项目" href="https://gitee.com/jenly1314" target="_blank">jenly1314</a>  | <a title="CSDN博客" href="http://blog.csdn.net/jenly121" target="_blank">jenly121</a>  | <a title="博客园" href="https://www.cnblogs.com/jenly" target="_blank">jenly</a>  |

## 联系我

| 微信公众号        | Gmail邮箱                                                                          | QQ邮箱                                                                              | QQ群                                                                                                                       | QQ群                                                                                                                       |
|:-------------|:---------------------------------------------------------------------------------|:----------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------|
| [Jenly666](http://weixin.qq.com/r/wzpWTuPEQL4-ract92-R) | <a title="给我发邮件" href="mailto:jenly1314@gmail.com" target="_blank">jenly1314</a> | <a title="给我发邮件" href="mailto:jenly1314@vip.qq.com" target="_blank">jenly1314</a> | <a title="点击加入QQ群" href="https://qm.qq.com/cgi-bin/qm/qr?k=6_RukjAhwjAdDHEk2G7nph-o8fBFFzZz" target="_blank">20867961</a> | <a title="点击加入QQ群" href="https://qm.qq.com/cgi-bin/qm/qr?k=Z9pobM8bzAW7tM_8xC31W8IcbIl0A-zT" target="_blank">64020761</a> |

<div>
   <img src="https://jenly1314.github.io/image/page/footer.png">
</div>
