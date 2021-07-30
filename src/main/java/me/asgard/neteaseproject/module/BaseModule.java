package me.asgard.neteaseproject.module;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import me.asgard.neteaseproject.NeteaseProject;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class BaseModule extends AbstractModule {

    private NeteaseProject neteaseProject;

    public BaseModule (NeteaseProject neteaseProjec) {
        this.neteaseProject = neteaseProjec;
    }

    @Override
    protected void configure() {
        initConfig();
        initObjectMapper();
        initYamlObjectMapper();
        this.bind(NeteaseProject.class).toInstance(this.neteaseProject);
        bind(ExecutorService.class).toInstance(Executors.newFixedThreadPool(10));
        bind(ScheduledExecutorService.class).toInstance(Executors.newScheduledThreadPool(10));
        initDatabase();
    }

    private void initObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //序列化的时候序列对象的所有属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        //反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //如果是空对象的时候,不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        bind(ObjectMapper.class).toInstance(objectMapper);
    }

    private void initYamlObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        //序列化的时候序列对象的所有属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        //反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //如果是空对象的时候,不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        bind(ObjectMapper.class).annotatedWith(Names.named("yamlObjectMapper")).toInstance(objectMapper);
    }

    private void initDatabase () {
        this.neteaseProject.saveResource("hibernate.cfg.xml", false);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure(new File(this.neteaseProject.getDataFolder(), "hibernate.cfg.xml")).build();
        SessionFactory sessionFactory = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
        bind(SessionFactory.class).toInstance(sessionFactory);
    }

    private void initConfig () {
        this.neteaseProject.saveDefaultConfig();
    }
}
