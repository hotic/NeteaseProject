package me.asgard.neteaseproject.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.asgard.neteaseproject.bean.SendOrder;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NeteaseConfig {

    private String url;
    private String key;
    private String gameId;
    private Integer orderPostInterval;
    private List<SendOrder> sendOrderList;
}
