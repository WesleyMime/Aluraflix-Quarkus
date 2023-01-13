package net.aluraflix;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(info = @Info(
        title = "Aluraflix API",
        description = "API for users to assemble playlists with links to their favorite videos, separated by categories.",
        version = "2.2.0",
        license = @License(name = "MIT License", url = "https://github.com/WesleyMime/Aluraflix-Quarkus/blob/main/LICENSE")
))
@SecurityScheme(securitySchemeName = "SecurityScheme", type = SecuritySchemeType.HTTP,
                description = "authentication required to use all the features",
                scheme = "bearer", bearerFormat = "JWT")
public class AluraflixApplication extends Application {
}
