package com.backend.backend.assigment.tracking.service.tests;

import com.kumuluz.ee.testing.arquillian.spi.MavenDependencyAppender;
import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.repository.MavenRemoteRepositories;
import org.jboss.shrinkwrap.resolver.api.maven.repository.MavenRemoteRepository;
import org.jboss.shrinkwrap.resolver.api.maven.repository.MavenUpdatePolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DependencyAppender implements MavenDependencyAppender {

    @Override
    public List<String> addLibraries() {

        List<String> libs = new ArrayList<>();

        libs.add("com.kumuluz.ee:kumuluzee-microProfile-1.0:3.11.0");
        libs.add("com.beust:jcommander:1.27");
        libs.add("com.kumuluz.ee:kumuluzee-jpa-hibernate:3.11.0");
        libs.add("com.kumuluz.ee:kumuluzee-jta-narayana:3.11.0");
        libs.add("org.postgresql:postgresql:42.0.0");
        libs.add("com.kumuluz.ee.streaming:kumuluzee-streaming-kafka:1.2.0");

        return libs;
    }
}