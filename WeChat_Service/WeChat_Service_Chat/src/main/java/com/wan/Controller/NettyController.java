package com.wan.Controller;

import com.wan.util.Constact;
import com.wan.util.ResultData;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author 万星明
 * @Date 2019/2/21
 */

@RestController
@RequestMapping("/netty")
public class NettyController {

    @Autowired
    private ZooKeeper zooKeeper;

    private AtomicInteger index = new AtomicInteger(0);


    @RequestMapping("/getServer")
    public ResultData<String> getNettyServer(){

        try {
            List<String> children = zooKeeper.getChildren("/netty", null);
            if (children != null && children.size() > 0){

                //从这些子节点中选择一个节点返回给浏览器，让浏览器连接该服务器
                String nodeName = loadBalance(children);
                byte[] data = zooKeeper.getData("/netty/" + nodeName, null, null);
                String ip = new String(data, "utf-8");

                return ResultData.createResultData(ip);
            }

            return ResultData.createResultData("");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultData.createResultData(Constact.ERROR_CODE, "服务地址获取异常！");
    }

    /**
     * 负载均衡返回服务的地址 - 轮询
     * @return
     */
    public String loadBalance(List<String> strList){
        int i = index.getAndIncrement() % strList.size();
        return strList.get(i);
    }


}
