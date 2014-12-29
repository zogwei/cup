2、jmx 
	来自tomcat7.0，
	
	默认情况下在被管理类的包中查找mbeans-descriptors.xml，使用digester将xml装换成对象树
	流程：
	  
	 1、mbeans-descriptors.xml 描述了被管理对象的元信息，包括属性，操作，通知等
     2、modeler的Registry加载这个文件获得元信息，生成ManagedBean，
     3、ManagedBean 通过参数为真正被管理对象的createMBean方法生成 DynamicMBean
     4、通过Registry的getMBeanServer()方法获得MBeanServer
     5、MBeanServer调用registerMBean ( mbean, oname)方法注册被管理的MBean
	
2.1 依赖项目：
    digester ，util，logging
2.2 依赖jar
	
2.3 包	
	com.cup.jmx.modeler
	com.cup.jmx.modeler.modules	
2.4 其他
	jmxtest中的web方法的管理 这些类才需要lib\jmx下的包
	
5 测试：
   http://localhost:8888/