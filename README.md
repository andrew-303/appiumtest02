本项目是在appiumtest01的基础上，进行测试步骤数据驱动的优化

操作步骤
元素定位符
多平台、多版本、多个备选定位符
操作步骤不同，直接使用文件区分、方法名区分、步骤区分
测试用例的执行流程

参数化用例，读取Parameters指定的数据源，对应到TestNG的data provider，或者JUnit5的MethodSource
读取TestSearch.yaml读取三行测试数据
对每一行数据做如下处理
参数化执行 stock=alibaba
执行单个参数化的用例 searchPage.search(stock).getCurrentPrice()
SearchPage中的search方法被调用，search('alibaba')
参数保存：keyword='alibaba' params={'keyword': "alibaba"}
测试步骤数据驱动 parse steps，从SearchPage.yaml中读出${keyword}
测试步骤参数化：用传递进来的keyword命名变量，也就是内容为keyword=alibaba这个值替换
测试步骤：getAttribute get关键字 结果内容保存为data，当dump出现的时候，把数据保存为 results={ 'price': data}
读取result数据获得中间的各种控件读取结果
断言

appiumtest01的基础功能：
实现了基本的Appium 安卓的UI自动化测试

1、使用了基本的PageObject模型
2、隐式和显示等待
3、Junit4和Junit5的参数化
4、处理广告弹窗