# smartscheduler

Setup Websphere:
1. Create resources (Datastore, Url, WorkManger)
References to resources are bind by `WEB-INF/ibm-web-bnd.xml` (only Websphere needs bindings definition,  Tomcat ignores this) or you can configure it on the Admin Console with web.xml binding ref. 
Scheduler starts on `http://localhost:9080/smartscheduler/`

Setup Tomcat:
1. Copy jars from tomcat/libs to <TOMCAT_HOME>/libs
Resources configuration are defined in `META-INF/context.xml` (only Tomcat use this configuration, Websphere ignores this)
Scheduler starts on `http://localhost:8080/smartscheduler/` 

External libs for Tomcat: 
- foo-commonj-1.1.0.jar, commonj-twm.jar (JSR 237 Timer and WorkManager implementation, used in tomcat) http://commonj.myfoo.de/download.shtml
- resource-factory-0.1.jar (URL provider implementation, used in tomcat, no use in our project I think(@HT) )




## Quartz on Websphere
Websphere requires threadPool and threadExecutor to runs thread via WorkManager, see `.....quartz.ManagedThreadPool` and `......quartz.ManagedThreadExecutor`.

## Quartz on Tomcat
Tomcat requires to Quartz jobs must be wrapped by commonj Work with setup deamon to true, see `.......quartz.WorkAdapter`.

## Quartz properties
```
org.quartz.threadPool.class=.......quartz.ManagedThreadPool
org.quartz.threadPool.workManagerName=java:comp/env/wm/quartz

org.quartz.threadExecutor.class=......quartz.ManagedThreadExecutor
org.quartz.threadExecutor.workManagerName=java:comp/env/wm/quartz
```

