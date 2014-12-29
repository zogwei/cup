--------------------------------------------------
3、log
	来自Netty 4.0
	1、在JCL common-loging、slf4j、jul、log4j的基础上又封装了一层，可以任意选择那个实现
	2、优先slf4j，然后common-loging，最后jul，但由于common-loging一定有实现（最差情况就是自己common-loging自己的简单实现），故jul没用
	3、根据自己的需要添加lib\log下面的实现到classpath中，配置文件需要直接放到src下
	4、resources下面包括了logback  和 log4j 需要 配置文件样例
	5、pom文件中包括了 logback 或 log4j 需要的实现包，应用时选择一个就够了，现在选择的是logback
	
3.1 依赖项目
	
	
3.2 必须依赖jar
	commons-logging-1.1.1.jar
	slf4j-api-1.7.4.jar
	
3.3 必选依赖	