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
     persistenceUnitName="xyzUnit"
     jtaDatasource="java:/xyzDatasource"
     hibernateDialect="org.hibernate.dialect.MySQL5Dialect"
     subsystems=['masterdatamanagement','empmanagement']
}

softwareStack{
        spring_RESTful{	}
}
