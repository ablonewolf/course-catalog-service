package org.ablonewolf.coursecatalogservice.util

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

abstract class PostgresContainerInitializer {

	companion object {
		val postgresDB = PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine")).apply {
			withDatabaseName("course_catalog_test_db")
			withUsername("postgres")
			withPassword("secret")
			withReuse(true)
		}

		init {
			postgresDB.start()
		}

		@JvmStatic
		@DynamicPropertySource
		fun configureProperties(registry: DynamicPropertyRegistry) {
			registry.add("spring.datasource.url", postgresDB::getJdbcUrl)
			registry.add("spring.datasource.username", postgresDB::getUsername)
			registry.add("spring.datasource.password", postgresDB::getPassword)
		}
	}
}