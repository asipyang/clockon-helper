### 0.1.0 (2016-02-21)
#### Features
+ holiday checking
	+ check the day is holiday or not before do the clock on job
	+ get the holiday data from the [open data](http://data.ntpc.gov.tw/od/detail?oid=308DCD75-6434-45BC-A95F-584DA4FED251)
	+ toggle this feature by setting the **clockon.use\_holiday\_calendar** field in the *clock-on.properties*
+ run with headless browser
	+ you can run *clock-on helper* with headless browser by switching from the FireFox(default) to the HtmlUnit
	+ configure this feature by setting the **clockon.browser\_used** field in the *clock-on.properties*
+ logging
	+ use log4j for logging
	+ log some detail and result during the process in the **clockon.log** file
	+ configure log4j by setting the fields start with **log4j.** in the clock-on.properties