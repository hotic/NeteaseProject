package me.asgard.neteaseproject.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendOrder {
    private String key;
    private String neteaseCommand;
    private List<String> serverCommandLists;
}
