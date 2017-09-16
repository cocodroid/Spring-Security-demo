src/main/java/目录下放着所有的 java 源代码。 
src/main/resources/hsqldb/目录下放置着 hsqldb 数据库表结构和演示数据。 
applicationContext-security.xml权限控制相关的配置文件。 
applicatoinContext-service.xml进行用户管理和权限管理所需的配置文件。 
src/main/webapp/目录下放着 web 应用所需的 JavaScript 脚本与 jsp 文件。

一、ACL基本操作
1、准备数据库和aclService
	1.1. 为acl配置cache
	1.2. 配置lookupStrategy
	1.3. 配置aclService

2、 使用aclService管理acl信息

3、 使用acl控制delete操作

4、 控制用户可以看到哪些信息

二、管理acl

1. 管理多个domain类
	对多个domain类实现acl权限控制时，不必给每一
	个类都配置一个Voter，只需要定义一个接口，让所有需要进行acl权限
	控制的domain类实现这个接口，然后在Voter中配置这个统一接口就可以了
	
2. 动态授权与收回授权
	2.1. 获得对象的acl权限
	2.2. 添加授权
	2.3. 收回授权

三、acl自动提醒

1. 自动创建acl


