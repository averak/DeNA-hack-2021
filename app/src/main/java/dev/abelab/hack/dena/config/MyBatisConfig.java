package dev.abelab.hack.dena.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan("dev.abelab.hack.dena.db.mapper")
@Configuration
public class MyBatisConfig {
}
