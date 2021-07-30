package me.asgard.neteaseproject.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.inject.Singleton;

@Singleton
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {
    private NeteaseConfig config;
}
