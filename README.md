
# 1013(bug fix: 0922) commitId:1.22 解决的bug：1。 设备方向变更，导致aty重建；现在不重建了；2。 计时到98/99%就停；现在改为增量计时，0->600,且到600

# 0922(bug!) 现在不会让用户多次点击了，但发现bug：到99后一直打转

# 0911 (solved! at 0922)更新，你TMD还是能多次点击，这个SB bug快点解决


<img src="http://o6qns6y6x.bkt.clouddn.com/18-9-11/39725696.jpg" width = "431" height = "631" alt="该死的不同步" align=center />


# 20180903更新，添加水波计时view

效果如图


<img src="http://o6qns6y6x.bkt.clouddn.com/18-9-3/17724073.jpg" width = "431" height = "631" alt="该死的不同步" align=center />


# 20171020更新，说明一下完整方案和下载地址

下载地址：https://pan.baidu.com/s/1ge5dIvT

==这个App最终和最开始的目标都是==

0. 一花一世界：简单地在真实的身边分享自己的世界by种下一朵花。
1. 让用户在当前地点发布一个故事（种下一朵花）
2. 让用户在当前地点了解到附近都有什么故事（看到周围的丰富的花园）

## 原理和实施方案

定位SDK能得到用户坐标，App通过坐标找到20个附近的“花朵”（故事），点击花朵图标，可看到具体故事，可以进行“点赞”动作（也可以到后期添加：移植这个花朵到自己花园 的动作）

<img src="http://img.blog.csdn.net/20170418184042323?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcGF1bGtnMTI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast" width = "300" height = "480" alt="图片名称" align=center />



用户将会积累种花和移植别人的花朵，拿到自己的花园世界

（即：用户将持续发表帖子，喜欢别人的帖子，并在地图上表现出自己帖子的地理状态---坐标）

用户将把帖子当作自己花园世界里的花朵，并可以在地图上通过热力图的方式看到自己城市的所有花园世界的面积，甚至可以通过别人的花朵移植，进行土地掠夺（高德地图SDK包含热力图API，待接入）。

在侧面的菜单里，单击安卓机器人头像，可以看到我的更多计划。

<img src="http://img.blog.csdn.net/20171020225033592?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcGF1bGtnMTI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast" width = "300" height = "480" alt="图片名称" align=center />

<img src="http://img.blog.csdn.net/20171020225105733?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcGF1bGtnMTI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast" width = "300" height = "480" alt="图片名称" align=center />




# 热情所在，编码不断

# T29Idea

this is an Android app demo for the dream_cup of Huawei. 

Location Based Service, more specific, it's location based personal record.

Dependencies:

Amap and Bomb and RichText

