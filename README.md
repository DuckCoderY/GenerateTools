# GenerateCodeManyDataSource

#### 介绍
多数据库 代码生成工具 可部署为公司个性化代码生成工具项目 局域网内共享代码生成

#### 软件架构
软件架构说明 Spring-Boot + Vue


#### 安装教程

1.  下载源码直接打开 安装依赖 访问127.0.0.1:10086 即可访问
2.  若想部署到局域网提供内部使用 将vue项目中的代码request/index中的BaseUrl修改 重新打包放入boot的static目录下 即可打包boot项目部署


#### 使用说明

1.  本地启动项目
2.  访问127.0.0.1:10086 
3.  填入对应参数查询 生成即可


#### 功能描述
1.  现阶段实现了 MYSQL ORACLE SQLSERVER Postgresql 的对应数据库表对照生成 CRUD的代码
2.  以生成后端api接口为模板 生成了前端的js的Api以及对应传输实体对象的对应代码

#### 界面
![微信截图_20220602094855](https://user-images.githubusercontent.com/105904115/171529982-f28166eb-5cd9-43b0-b022-030f9c91be56.png)

#### 生成文件目录
![微信截图_20220604100754](https://user-images.githubusercontent.com/105904115/171974178-3294c1b8-64c8-4c4a-931f-9629c3dd42dd.png)

#### 生成后端代码controller
![微信截图_20220604101234](https://user-images.githubusercontent.com/105904115/171974200-b75bb71e-bcc8-46fb-ba74-e1de5d2094f0.png)

#### 生成后端代码service
![微信截图_20220604101254](https://user-images.githubusercontent.com/105904115/171974237-15e81dc2-c3ee-4dd4-8e13-548d0214426d.png)

#### 生成后端代码实体
![微信截图_20220604101344](https://user-images.githubusercontent.com/105904115/171974264-b2c4cf01-58e0-472b-8937-f214cb263b7b.png)

#### 生成后端代码XML
![微信截图_20220604101429](https://user-images.githubusercontent.com/105904115/171974294-ee6aa2bb-a35f-4589-8b69-078380619dbf.png)

#### 生成后端代码返回值实体代码
![微信截图_20220604101331](https://user-images.githubusercontent.com/105904115/171974305-00173cd8-65be-4855-bfd0-0ca00c4eae5d.png)

#### 生成前端Api代码
![微信截图_20220604101546](https://user-images.githubusercontent.com/105904115/171974318-dd533897-7b4c-4c27-9c12-3ea879c86ced.png)
