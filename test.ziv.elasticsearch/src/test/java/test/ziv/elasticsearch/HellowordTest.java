package test.ziv.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class HellowordTest {

	private static final String JUNIT_TEST_INDEX = "junit_test_index";
	private static final String JUNIT_TEST_TYPE = "junit_test_type";
	/** 自定义集群名称，这里指要链接的集群的名称 **/
	private static final String CLUSTER_NAME = "my_cluster_name";
	private static Client client;

	@BeforeClass
	public static void setUp() throws UnknownHostException {
		Settings settings = Settings.settingsBuilder().put("cluster.name", CLUSTER_NAME).put("client.transport.sniff", true).build();
		TransportClient build = TransportClient.builder().settings(settings).build();
		client = build.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

		ClusterHealthResponse clusterHealth = client.admin().cluster().health(new ClusterHealthRequest()).actionGet();
		System.out.println(clusterHealth);
		Assert.assertEquals(CLUSTER_NAME, clusterHealth.getClusterName());
		System.out.println("status: " + clusterHealth.getStatus().name());
		System.out.println("number of nodes: " + clusterHealth.getNumberOfNodes());
	}

	@AfterClass
	public static void cleanUp() {
		if (null != client) {
			client.close();
		}
	}

	@Test
	public void test() {
		IndexRequest indexRequest = Requests.indexRequest(JUNIT_TEST_INDEX).type(JUNIT_TEST_TYPE).source("{\"a\":\"b\"}");
		IndexResponse indexResponse = client.index(indexRequest).actionGet();
		System.out.println(indexResponse);
		GetRequest request = Requests.getRequest("bank").type("account").id("44");
		ActionFuture<GetResponse> actionFuture = client.get(request);
		GetResponse actionGet = actionFuture.actionGet();
		System.out.println("index: " + actionGet.getIndex());
		System.out.println("type: " + actionGet.getType());
		System.out.println("id: " + actionGet.getId());
		System.out.println("exists: " + actionGet.isExists());
		System.out.println("version: " + actionGet.getVersion());
	}

}
