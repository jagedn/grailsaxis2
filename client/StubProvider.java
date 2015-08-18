package es.puravida.faces.client;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

import es.face.sspp.SSPPWebServiceProxyServiceStub;

public class StubProvider {

	private static java.util.logging.Logger theLogger = java.util.logging.Logger
			.getLogger(StubProvider.class.getName());

	public StubProvider() {

	}

	String configPath = "axis2";

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	public void init() {
		try {
			InputStream inputStream = StubProvider.class
					.getResourceAsStream("/faceclient.properties");
			if (inputStream == null) {
				inputStream = new FileInputStream("faceclient.properties");
			}
			Properties prp = new Properties();
			prp.load(inputStream);
			for (Map.Entry<Object, Object> entry : prp.entrySet()) {
				System.getProperties().put(entry.getKey(), entry.getValue());
			}
		} catch (Exception e) {
			theLogger.severe("faceclient.properties not found");
		}

		try {
			if (System.getProperty("faceclient.axis2.configPath")!=null) {
				configPath = System.getProperty("faceclient.axis2.configPath");
			}
			ConfigurationContext ctx = ConfigurationContextFactory
					.createConfigurationContextFromFileSystem(configPath, null);

			theStub = new SSPPWebServiceProxyServiceStub(ctx);
			new SecurityHandler(theStub._getServiceClient(),
					new DummyPasswordCallback(),
					StubProvider.class
							.getResourceAsStream("/policies/facepolicy.xml"))
					.engage();

		} catch (Throwable e) {
			theLogger.severe("Error inicializando stub");
			theLogger
					.throwing(StubProvider.class.getName(), "loadConstants", e);
			throw new RuntimeException(e);
		}
	}

	private SSPPWebServiceProxyServiceStub theStub;

	public SSPPWebServiceProxyServiceStub getSSPPWebServiceProxyServiceStub() {
		return theStub;
	}

}
