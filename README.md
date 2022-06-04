# GenerateCodeManyDataSource

#### 介绍
多数据库 代码生成工具 可部署为公司个性化代码生成工具项目 局域网内共享代码生成

#### 软件架构
软件架构说明 Spring-Boot + Vue


#### 安装教程

1.  下载源码直接打开 安装依赖 启动项目 访问127.0.0.1:10086 即可访问
2.  若想部署到局域网提供内部使用 将vue项目中的代码request/index中的BaseUrl修改 重新打包放入boot的static目录下 即可打包boot项目部署


#### 使用说明

1.  本地启动项目
2.  访问127.0.0.1:10086 
3.  填入对应参数查询 查询数据库信息 选择对应数据表
4.  设置导出目录前缀 模块名 和不需要的表前缀 点击导出 生成即可

#### 功能描述
1.  现阶段实现了 MYSQL ORACLE SQLSERVER Postgresql 的对应数据库表对照生成 CRUD的代码
2.  以生成后端api接口为模板 生成了前端的js的Api以及对应传输实体对象的对应代码（封装的前端的Request.js 也一并打入其中）

#### 界面
![微信截图_20220602094855](https://user-images.githubusercontent.com/105904115/171529982-f28166eb-5cd9-43b0-b022-030f9c91be56.png)
![微信截图_20220604103116](https://user-images.githubusercontent.com/105904115/171974819-11afb3ae-aa74-44cc-b939-a7be6403a0cd.png)

#### 生成文件目录
![image](https://user-images.githubusercontent.com/105904115/171974815-5c24726d-2a93-4a12-9c61-ac12451d0459.png)

#### 生成后端代码controller
![image](https://user-images.githubusercontent.com/105904115/171974836-5d33ac76-0d93-4d56-a73e-44169f5df005.png)

#### 生成后端代码service
![image](https://user-images.githubusercontent.com/105904115/171974869-62b7a254-c573-481a-89d1-bae7ad5ca231.png)

#### 生成后端代码实体
![image](https://user-images.githubusercontent.com/105904115/171974883-bb433cf4-927f-425d-b426-358a285cae82.png)

#### 生成后端代码XML
![image](https://user-images.githubusercontent.com/105904115/171974915-a3c725a9-7071-4a2d-8049-95329f73297d.png)

#### 生成后端代码返回值实体代码
![微信截图_20220604101331](https://user-images.githubusercontent.com/105904115/171974305-00173cd8-65be-4855-bfd0-0ca00c4eae5d.png)

#### 生成前端Api代码
![image](https://user-images.githubusercontent.com/105904115/171975005-4883168d-f62e-4c3f-873a-3bc949b3093a.png)
![image](https://user-images.githubusercontent.com/105904115/171975085-255d5331-2f16-4781-a989-f7223b86a940.png)

