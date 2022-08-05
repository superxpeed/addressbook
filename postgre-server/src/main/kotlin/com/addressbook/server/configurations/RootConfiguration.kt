package com.addressbook.server.configurations

import com.mchange.v2.c3p0.ComboPooledDataSource
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
    fun dataSource(): DataSource{
        val ds = ComboPooledDataSource()
        ds.driverClass = env.getProperty("datasource.main.class")
        ds.jdbcUrl = env.getProperty("datasource.main.url")
        ds.user = env.getProperty("datasource.main.user")
        ds.password = env.getProperty("datasource.main.password")
        env.getProperty("datasource.main.min-pool-size")?.let { ds.minPoolSize = it.toInt() }
        env.getProperty("datasource.main.max-pool-size")?.let { ds.maxPoolSize = it.toInt() }
        env.getProperty("datasource.main.max-idle-time")?.let { ds.maxIdleTime = it.toInt() }
        env.getProperty("datasource.main.idle-connection-test-period")?.let { ds.idleConnectionTestPeriod = it.toInt() }
        env.getProperty("datasource.main.test-connection-on-checkout")?.let { ds.isTestConnectionOnCheckout = it.toBoolean() }
        ds.preferredTestQuery = env.getProperty("datasource.main.preferred-test-query")
        return ds
    }
}