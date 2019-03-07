package com.gcframework.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * ClosableDruidDataSource
 * 
 * @author leejianhao
 * @since 2017-11-10 17:52
 */
public class ClosableDruidDataSource extends DruidDataSource {

	private static final Logger LOG = LoggerFactory.getLogger(DruidDataSource.class);

	@Override
	public void close() {
		super.close();
		try {
			Enumeration<Driver> drivers = DriverManager.getDrivers();
			while (drivers.hasMoreElements()) {
				Driver driver = drivers.nextElement();
				try {
					DriverManager.deregisterDriver(driver);
					LOG.info(String.format("[STOPPING]>Deregistering jdbc driver: %s", driver));
				} catch (SQLException e) {
					LOG.error(String.format("[STOPPING]>Error deregistering driver %s", driver), e);
				}

			}
			LOG.info("...........deregister driver complete...........");
		} catch (Exception ignored) {
			LOG.info("...........deregister driver..........., whoop,", ignored);
		}

		try {
			AbandonedConnectionCleanupThread.shutdown();
		} catch (Exception e) {
			LOG.error("[STOPPING]>mysql cleanup thread error. " + e.getMessage());
		}

	}

}
