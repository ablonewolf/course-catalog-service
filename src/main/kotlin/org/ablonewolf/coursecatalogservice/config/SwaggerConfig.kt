package org.ablonewolf.coursecatalogservice.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Swagger configuration for API documentation.
 * for local dev: visit http://localhost:7700/course-catalog/swagger-ui/index.html#/
 */
@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Course Catalog Service")
                    .version("1")
            )
    }
}
