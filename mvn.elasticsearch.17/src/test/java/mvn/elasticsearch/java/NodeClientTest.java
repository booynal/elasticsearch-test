package mvn.elasticsearch.java;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.junit.Test;

public class NodeClientTest {

	/**
	 * 启动一个节点，并加入某个集群
	 */
	@Test
	public void test_firstCluster() {
		Node node = NodeBuilder.nodeBuilder().clusterName("myFirstCluster").node();
		Client client = node.client();
		System.out.println(client.settings().get("cluster.name"));

		node.close();
	}

}
