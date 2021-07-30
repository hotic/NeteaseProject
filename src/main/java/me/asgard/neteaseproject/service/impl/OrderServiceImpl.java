package me.asgard.neteaseproject.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import me.asgard.neteaseproject.NeteaseProject;
import me.asgard.neteaseproject.bean.result.Result;
import me.asgard.neteaseproject.config.Config;
import me.asgard.neteaseproject.dao.OrderDao;
import me.asgard.neteaseproject.domain.Order;
import me.asgard.neteaseproject.service.OrderService;
import me.asgard.neteaseproject.utils.HMACUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

//import org.bukkit.entity.Player;

@Singleton
public class OrderServiceImpl implements OrderService {

    private final static String getMcItemOrderListAPI = "/get-mc-item-order-list";
    private final static String shipMcItemOrder = "/ship-mc-item-order";

    @Inject
    private OrderDao orderDao;

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private ExecutorService executorService;

    @Inject
    private NeteaseProject neteaseProject;

    @Inject
    private Config config;

    @Override
    public void shipItem(Player player) {
        executorService.execute(()->{
            try {
                List<Order> orderList = getMCOrderItemList(player);
                if (Objects.nonNull(orderList) && !orderList.isEmpty()) {
                    orderList.forEach(o -> {
                        Order order = orderDao.findByOrderId(o.getOrderId());
                        if (Objects.isNull(order)) {
                            o.setSignType(-1);
                            o.setPlayerName(player.getName());
                            if (Objects.nonNull(o.getExtra())) {
                                try {
                                    o.setExtraStr(objectMapper.writeValueAsString(o.getExtra()));
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                            }
                            order = orderDao.findById(orderDao.add(o));
                        }
                        if (shipMcItemOrder(player, order.getOrderId())) {

                            order.setSignTime(System.currentTimeMillis());
                            order.setSignType(0);
                            orderDao.update(order);
                            String cmd = order.getCmd();
                            config.getConfig().getSendOrderList().stream().filter(sendOrder -> sendOrder.getNeteaseCommand().equals(cmd)).findFirst().ifPresent(sendOrder -> sendOrder.getServerCommandLists().forEach(s -> Bukkit.getScheduler().scheduleSyncDelayedTask(neteaseProject, ()-> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("<player>", player.getName())))));
                        }
                    });
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                System.out.println(String.format("报废了，异常信息: %s", e.getMessage()));
            }

        });
    }

    private boolean shipMcItemOrder (Player player, String orderNumber) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("gameid", String.valueOf(config.getConfig().getGameId()));
        map.put("uuid", player.getUniqueId().toString());
        map.put("orderid_list", Lists.newArrayList(orderNumber));
        String body;
        try {
            body = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
        String str2sign = "POST" + shipMcItemOrder + body;
        String sign = HMACUtil.HMACSHA256(config.getConfig().getKey(), str2sign);
        HttpRequest httpRequest = HttpRequest
                .post(config.getConfig().getUrl() + shipMcItemOrder)
                .contentTypeJson()
                .acceptJson()
                .charset("UTF-8")
                .header("Netease-Server-Sign", sign)
                .body(body);
        HttpResponse httpResponse = httpRequest
                .send();
        Result result;
        try {
            result = objectMapper.readValue(httpResponse.bodyText(), Result.class);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return result.getCode() == 0;
    }

    private List<Order> getMCOrderItemList (Player player) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("gameid", config.getConfig().getGameId());
        map.put("uuid", player.getUniqueId().toString());
        String body;
        try {
            body = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("JSON 转换失败!");
        }
        String str2sign = "POST" + getMcItemOrderListAPI + body;
        String sign = HMACUtil.HMACSHA256(config.getConfig().getKey(), str2sign);
        HttpRequest httpRequest = HttpRequest
                .post(config.getConfig().getUrl() + getMcItemOrderListAPI)
                .contentTypeJson()
                .acceptJson()
                .charset("UTF-8")
                .header("Netease-Server-Sign", sign)
                .body(body);
        HttpResponse httpResponse = httpRequest
                .send();
        Result<List<Order>> result;
        try {
            System.out.println(httpResponse.bodyText());
            result = objectMapper.readValue(httpResponse.bodyText(), new TypeReference<Result<List<Order>>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("JSON 转换失败!");
        }
        if (result.getCode() != 0) throw new RuntimeException(result.getMessage());
        return result.getEntities();
    }

//    public static void main(String[] args) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, Object> map = Maps.newHashMap();
//        map.put("gameid", String.valueOf("4630895849698313291"));
//        map.put("uuid", "798480ad-80c1-44cd-8471-87bde6d534e3");
//        String body = objectMapper.writeValueAsString(map);
//        String str2sign = "POST" + getMcItemOrderListAPI + body;
////        String str2sign = "POST/apps/app1/login{\"uid\": \"123\"}";
//        System.out.println(str2sign);
//        String sign = HMACUtil.HMACSHA256("L1bY60UOxen0rJ7McWDFeLi4z9sZHnA5", str2sign);
//        System.out.println(body);
//        HttpRequest httpRequest = HttpRequest
//                .post("https://x19mclexpr.nie.netease.com:9090" + getMcItemOrderListAPI)
//                .contentTypeJson()
//                .acceptJson()
//                .header("Netease-Server-Sign", sign)
//                .body(body);
//        System.out.println(httpRequest);
//        HttpResponse httpResponse = httpRequest
//                .send();
//
//        System.out.println(httpResponse);
//    }
}
