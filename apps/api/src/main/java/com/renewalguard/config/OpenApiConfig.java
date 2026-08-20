
package com.renewalguard.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI renewalGuardOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("RenewalGuard API")
                        .description(
                                "AI-powered SaaS renewal and procurement management API"
                        )
                        .version("v1.0.0"));
    }
}
