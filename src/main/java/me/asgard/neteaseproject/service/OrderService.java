package me.asgard.neteaseproject.service;

import com.google.inject.ImplementedBy;
import me.asgard.neteaseproject.service.impl.OrderServiceImpl;
import org.bukkit.entity.Player; 


@ImplementedBy(OrderServiceImpl.class)
public interface OrderService {
    void shipItem (Player player);
}
