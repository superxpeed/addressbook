package com.addressbook.server.configurations

import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import javax.sql.DataSource

@Configuration
class RootConfiguration {

    @Autowired
    lateinit var env: Environment

    @Bean
    fun dataSource(): DataSource {
        val ds = HikariDataSource()
        ds.poolName = "main"
        ds.driverClassName = env.getProperty("datasource.main.class")
        ds.jdbcUrl = env.getProperty("datasource.main.url")
        ds.username = env.getProperty("datasource.main.user")
        ds.password = env.getProperty("datasource.main.password")
        env.getProperty("datasource.main.min-pool-size")?.let { ds.minimumIdle = it.toInt() }
        env.getProperty("datasource.main.max-pool-size")?.let { ds.maximumPoolSize = it.toInt() }
        env.getProperty("datasource.main.max-idle-time")?.let { ds.idleTimeout = it.toLong() }
        env.getProperty("datasource.main.idle-connection-test-period")?.let { ds.validationTimeout = it.toLong() }
        ds.connectionTestQuery = env.getProperty("datasource.main.preferred-test-query")
        return ds
    }
}