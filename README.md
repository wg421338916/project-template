springboot模板项目

#原则：约定大于配置

- 1.项目默认产生的文件都统一放到 linux: /data/项目名称/xx 目录下 windows: d:/data/项目名称/xxx
  ，默认的log日志还是和放到项目路径下
- 2.环境配置文件使用yml格式，不使用properties
- 3.项目中自定义的配置都用aegis开头，如下:
```javascript
    aegis:
      cors:
        #携带cookie调用需要配置为域名
        origins: '*'
      test:
        test: xx
```
- 4.公共的配置不允许出现在 dev，pro，test 等环境配置中，应该配置在：application.yml中
- 5.运维统一监控路径：

    http://ip:port/aegis/health
    返回：{"status":"UP"}

|  status   | 对应的值  |
|  ----  | ----  |
| DOWN  | SERVICE_UNAVAILABLE (503) |
| OUT_OF_SERVICE  | SERVICE_UNAVAILABLE (503) |
| UP  | No mapping by default, so http status is 200) |
| UNKNOWN  | No mapping by default, so http status is 200 |
- 6.bo dto entity po vo enums 使用规则 <br />
>各层传输数据禁止使用Map <br />
>entity:标识对应的数据库实体,严格禁止直接输出到页面  <br />
>po：数据层传输对象,主要是对entity的扩展辅助<br />
>bo，dto：业务层传输对象<br />
>vo：controller 层数据传输对象，禁止传入到业务层<br />


![image](https://www.showdoc.cc/server/api/common/visitfile/sign/aa10a7a76dcf5dc2d7b0b827f2aba04a?showdoc=.jpg)
- 7.接口URL规范 项目页面使用接口：web/v1/模块，其他系统调用本系统接口：api/v1/模块
> web 开头的会做form表单登录鉴权，接口写在webcontroller目录下 <br />
> api 开头的会做oauth2 client 模式鉴权，接口写在apicontroller目录下

- 8.方法返回list对象，默认不是null
