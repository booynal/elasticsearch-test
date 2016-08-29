package mvn.elasticsearch.java;

import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Test;

/**
 *
 * @see <code>https://www.elastic.co/guide/en/elasticsearch/client/java-api/1.7/transport-client.html</code>
 * @author Booynal
 *
 */
public class TransportClientTest {

	@Test
	public void test() {
		// on startup
		TransportClient client = new TransportClient();
		client.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
		System.out.println(client);

		// on shutdown
		client.close();
	}

	@Test
	public void test2() {
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "myClusterName").build();
		// on startup
		TransportClient client = new TransportClient(settings);
		client.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
		System.out.println(client);

		AdminClient admin = client.admin();
		System.out.println(admin);

		// on shutdown
		client.close();
	}

	/**
	 * 打开网络嗅探
	 */
	@Test
	public void test3() {
		Settings settings = ImmutableSettings.settingsBuilder().put("client.transport.sniff", true).build();
		// on startup
		TransportClient client = new TransportClient(settings);
		client.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
		System.out.println(client);

		AdminClient admin = client.admin();
		System.out.println(admin);

		// on shutdown
		client.close();
	}

}
