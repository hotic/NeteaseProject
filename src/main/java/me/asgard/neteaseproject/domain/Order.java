package me.asgard.neteaseproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "t_order")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("orderid")
    private String orderId; //订单号
    private Integer signType;//签收状态 -1未签收 0已签收
    @JsonProperty("item_id")
    private Long itemId;//商品ID
    @JsonProperty("item_num")
    private Integer itemNum;//玩家购买的道具数量
    private String uuid;//玩家的唯一编号
    private String playerName;//玩家名字
    private String cmd;//执行的指令
    @JsonProperty("buy_time")
    private Long buyTime;//购买时间
    private Long signTime;//签收时间
    @Column(name = "group_name")
    private String group;//道具分类
    private String type;//道具类型
    @Transient
    private Map extra;//额外数据
    @Column(name = "extra")
    private String extraStr;


}
