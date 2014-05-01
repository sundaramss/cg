project{
	 name='empapp'
	 author='sundaramss'
     packageName='com.xyz.srf'
     model='model'
     modelvalue='model.value'
     service='service'
     controller='controller'
     constant='constant'
     exception='exception'
     build='build'
     jtaDatasource="java:/xyzDatasource"
     hibernateDialect="org.hibernate.dialect.MySQL5Dialect"
     subsystems=['MasterdataManagement','EmpManagement']
}

softwareStack{
	springVersion = "3.1.1.RELEASE"
	sourceCompatibility = "1.6"
	slf4jVersion="1.6.1"
}

