package test.ziv.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HellowordTest extends BaseTest {

	private static final String JUNIT_TEST_INDEX = "junit_test_index";
	private static final String JUNIT_TEST_TYPE = "junit_test_type";
	private static final String JUNIT_TEST_ID = "junit_test_id";
	private static final String JUNIT_TEST_FILED = "junit_test_filed";
	private static final String JUNIT_TEST_OBJECT = "junit_test_object";
	/** 自定义集群名称，这里指要链接的集群的名称 **/
	private static final String CLUSTER_NAME = "my_cluster_name";
	private static Client client;

	/**
	 * 准备测试环境：连接服务器<br>
	 * Setting: <br>
	 * cluster.name 指定集群名称，缺省为elasticsearch<br>
	 * client.transport.sniff 自动嗅探新增加的服务器<br>
	 * 需要在本地启动一个elasticSearch集群，并且名字为{@link HellowordTest.CLUSTER_NAME}定义的名字<br>
	 * 如果用java API去连接的话，需要使用Transport端口: 9300(默认端口)
	 *
	 * @throws UnknownHostException
	 */
	@BeforeClass
	public static void setUp() throws UnknownHostException {
		Settings settings = Settings.settingsBuilder().put("cluster.name", CLUSTER_NAME).put("client.transport.sniff", true).build();
		TransportClient build = TransportClient.builder().settings(settings).build();
		client = build.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

		ClusterHealthResponse clusterHealth = client.admin().cluster().health(new ClusterHealthRequest()).actionGet();
		Assert.assertNotNull(clusterHealth);
		System.out.println("集群健康信息: " + clusterHealth);
		Assert.assertEquals(CLUSTER_NAME, clusterHealth.getClusterName());
	}

	@AfterClass
	public static void cleanUp() {
		if (null != client) {
			client.close();
		}
	}

	/**
	 * 索引一个文档<br>
	 *
	 * <pre>
	 * PUT /index/type/id -d '{...}'
	 * </pre>
	 *
	 */
	@Test
	public void test_1_index_1() {
		IndexRequest indexRequest = Requests.indexRequest(JUNIT_TEST_INDEX).type(JUNIT_TEST_TYPE).id(JUNIT_TEST_ID).source("{\"" + JUNIT_TEST_FILED + "\":\"" + JUNIT_TEST_OBJECT + "\"}");
		IndexResponse indexResponse = client.index(indexRequest).actionGet();
		Assert.assertNotNull(indexResponse);
		System.out.println(indexResponse);
		Assert.assertEquals(JUNIT_TEST_INDEX, indexResponse.getIndex());
		Assert.assertEquals(JUNIT_TEST_TYPE, indexResponse.getType());
		Assert.assertEquals(JUNIT_TEST_ID, indexResponse.getId());
		Assert.assertEquals(1, indexResponse.getShardInfo().getSuccessful());

	}

	/**
	 * 获取一个文档<br>
	 *
	 * <code>
	 * GET /index/type/id
	 * </code>
	 *
	 */
	@Test
	public void test_2_get_1() {
		GetRequest getRequest = Requests.getRequest(JUNIT_TEST_INDEX).type(JUNIT_TEST_TYPE).id(JUNIT_TEST_ID);
		GetResponse getReponse = client.get(getRequest).actionGet();
		Assert.assertNotNull(getReponse);
		System.out.println(getReponse);
		System.out.println("version: " + getReponse.getVersion());
		Assert.assertEquals(JUNIT_TEST_INDEX, getReponse.getIndex());
		Assert.assertEquals(JUNIT_TEST_TYPE, getReponse.getType());
		Assert.assertEquals(JUNIT_TEST_ID, getReponse.getId());
		Map<String, Object> source = getReponse.getSource();
		Object object = source.get(JUNIT_TEST_FILED);
		Assert.assertEquals(JUNIT_TEST_OBJECT, object);

	}

}
