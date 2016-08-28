package mvn.elasticsearch.java;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.junit.Test;

/**
 * Node Client有缺点：<br/>
 * 将节点客户端内嵌到应用中，是连接到Elasticsearch集群最简便的方法，但是有些缺点。<br/>
 * 1. 频繁的启动和停止node client会产生不必要的噪声到集群<br/>
 * 2. 内嵌的节点客户端将会响应外部的请求，所以应该在内嵌的node client中禁用HTTP<br/>
 *
 * @see <code>https://www.elastic.co/guide/en/elasticsearch/client/java-api/1.7/node-client.html<code>
 * @author Booynal
 *
 */
public class NodeClientTest {

	/**
	 * 启动一个节点，并加入某个集群
	 */
	@Test
	public void test_firstCluster() {
		// 设置集群的名字，可选，默认为随机选择
		Node node = NodeBuilder.nodeBuilder().clusterName("myFirstCluster").node();

		Client client = node.client();
		System.out.println(client.settings().get("cluster.name"));

		node.close();
	}

	/**
	 * 以下来自官网英文解释的翻译：<br/>
	 * 启动一个node之后最重要的决定是要不要在这个node上存储数据，换句话说，索引和分片是否应该分配给这个node。<br/>
	 * 多数情况下我们希望client仅仅只是一个client，不需要分配分片给他们。<br/>
	 * 配置很简单：可以通过设置node.data为false或者node.client为true<br/>
	 * 作为一个client，不需要开启http的服务,http.enabled=false
	 */
	@Test
	public void test_client_only() {
		Builder setting = ImmutableSettings.settingsBuilder().put("http.enabled", false);
		NodeBuilder nodeBuilder = NodeBuilder.nodeBuilder().settings(setting);

		// node.clent=true
		nodeBuilder.client(true);
		// node.data=false
		// nodeBuilder.data(false);

		Node node = nodeBuilder.node();
		Client client = node.client();

		client.close();
	}

	/**
	 * 本地模式-用于测试<br/>
	 * 官网翻译：<br/>
	 * 另外一个通用的用法是启动一个node并使用client进行单元/继承测试。<br/>
	 * 在这样的情况下，我们想启动一个本地节点(带有"local"发现和传输)<br/>
	 * 在启动的时候做简单的设置：如果有两个local的servers在jvm启动，他们会相互发现并构成一个集群<br/>
	 * 注："local"在这里意思是jvm本地级别(实际上是同一个类加载器)
	 */
	@Test
	public void test_local() {
		// local node
		Node node = NodeBuilder.nodeBuilder().local(true).node();
		Client client = node.client();

		client.close();
	}

}
