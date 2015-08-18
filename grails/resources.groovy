import es.puravida.faces.client.StubProvider


beans = {
	
	
	stubProvider(StubProvider){ bean ->
		bean.initMethod = 'init'	
		configPath = System.properties['faceclient.axis2.configPath']
	}
		
}
