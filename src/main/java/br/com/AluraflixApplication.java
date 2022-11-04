package br.com;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(info = @Info(
        title = "Aluraflix API",
        description = "API for users to assemble playlists with links to their favorite videos, separated by categories.",
        version = "1.1.0",
        license = @License(name = "MIT License", url = "https://github.com/WesleyMime/Aluraflix-Quarkus/blob/main/LICENSE")
))
public class AluraflixApplication extends Application {
}
