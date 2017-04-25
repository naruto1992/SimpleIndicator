**自动无限轮播图，支持多种自定义效果**

用法：<br>
1、下载library，导入项目，并引用：compile project(':library')<br>
2、概述：支持图片轮播、文字显示、指示点显示以及自定义指示点；图片支持加载本地资源文件图片，以及加载网络图片
3、具体方法说明：(请参考MainActivity.class)<br><br>
   //构造方法，传入context、viewpager和数据。数据类型可自定义，一般包括图片的链接、文字，本地图片资源<br>
   ①SimpleIndicator indicator = new SimpleIndicator(this, indicatorViewPager, entityList)；<br><br>
   ②setPointLayout：设置指示点所在线性布局，暂支持水平方向，指示点的位置可自己设置(gravity)<br><br>
   ③setPointColor： 设置指示点颜色，需要传入选中和未选中时两种颜色，需要用getResources().getColor(R.color.green)这种形式<br><br>
   ④setPointSize： 设置指示点大小，单位为dp<br><br>
   ⑤setPointMargin： 设置指示点的间距，单位为dp<br><br>
   ⑥setCustomPoint： 设置自定义指示点，需要传入选中和未选中时两种drawable，此时setPointColor将不生效<br><br>
   ⑦setTxtHolder： 传入显示文字的textView<br><br>
   ⑧setPagingDelay： 设置翻页间隔，单位为毫秒<br><br>
   ⑨setPagingSpeed： 设置翻页速度，单位为毫秒<br><br>
   ⑩loadNetImage： 设置是否加载网络图片，默认为false，当设置为true时，本地图片将不加载<br><br>
   ⑪setDefaultImage： 设置默认图片，当没有设置本地图片资源时生效<br><br>
   ⑫setClickListener： 设置点击事件，滑动时不冲突<br><br>
   ⑬init： 初始化，必须调用，而且必须在调用start()前<br><br>
   ⑭start： 启动，开始自动轮播<br><br>
   ⑮destroyView： 停止线程，一般在界面销毁时调用
   
   
   
   
