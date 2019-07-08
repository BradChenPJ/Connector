# Connector
The connector can help transmit data between oneM2M and SensorThings API following their own data model. Utilize Java Servlet and oneM2M subscription and notification function to do the real time transmission.
## Setup
* Chekc oneM2M, SensorThings API and subscription URL are correct.
* User need to download `Apache Tomcat` as server.
* Create a folder in `..\xampp\tomcat\webapps`. For example we create a folder name `Sensing`
* Create a folder name `WEB-INF` in `..\webapps\Sensing`
* Create 2 folders and 1 file in `..\Sensing\WEB-INF`
  * `classes`
  * `lib`
  * `web.xml`
* Put library into `..WEB-INF\lib`
* Put Java file `.class` into `..WEB-INF\classes`
* Check xml tag in `web.xml`
  * <servlet-name>
  * <servlet-class>
  * <servlet-name>
  * <url-pattern>
* Restart `Tomcat`
## Author
[Brad Chen](https://github.com/BradChenPJ)
