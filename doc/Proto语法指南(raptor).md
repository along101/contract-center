## Proto 语法指南 -- raptor版本

### Raptor 自定义注解介绍
Raptor 自定义了一些注解, 用来记录一些元信息,使 raptor-codegen 生成定制程度比较高的代码.

对此,我们提供了一个[例子](/#/appSummary?appId=contract-center-demo).

#### FileOptions
FileOptions 用来定义整个proto一些信息,他们包括:
 * appId -- proto文件所在应用的appId
 * appName -- proto文件所在引用的应用名
 * version -- proto文件的版本号(和契约中心的版本概念无关)Â
 * summary -- proto文件的概要说明

在契约中心中,appId是必填项.appId必须和proto文件所在的应用匹配,否则proto文件无法发布.

[//]: <> (This is also a comment.)

appId和appName可以作为寻找服务配置的key具体见 [Raptor客户端配置]().


#### ServiceOptions
目前ServiceOptions就一个: **servicePath**.这个option用于定义该service公共的url前缀,类似于在Contoller类上的@RequestMapping注解.

#### MethodOptions
MethodOption 是定义在每一个rpc方法中的option,他们包括:
* method -- 指定调用该rpc的http method
* path -- 指定方法的url,类似于在Contoller类方法上的@RequestMapping注
