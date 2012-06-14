project{
	 name='empapp'
	 author='sundaramss'
     packageName='com.xyz.srf'
     model='model'
     modelvalue='model.value'
     service='service'
     controller='controller'
     constant='constant'
     persistenceUnitName="xyzUnit"
     jtaDatasource="java:/xyzDatasource"
     hibernateDialect="org.hibernate.dialect.MySQL5Dialect"
     subsystems=['masterdata','emp']
}

softwareStack{
        spring_RESTful{	}
}
