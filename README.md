executable-war
==============

A maven utility that augments an existing WAR artifact to make it directly executable. The artifact produced by this build
is suitable to use as an overlay for your WAR file. To use this, make the following changes to your pom:

Add the following as a dependency to your project -

```xml
<!-- the war plugin needs this to be a dependency of the project for it to work -->
<dependency>
    <groupId>com.vast.common</groupId>
    <artifactId>executable-war</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <!-- use provided scope -->
    <scope>provided</scope>
</dependency>
```

And add the following to your build/plugins section -
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-war-plugin</artifactId>
    <version>2.4</version>
    <configuration>
        <archive>
            <manifest>
                <!-- add a main class manifest entry so the war will be runnable -->
                <mainClass>com.vast.util.EmbeddedJettyServer</mainClass>
            </manifest>
        </archive>
        <!-- make sure the executable war bits don't get put in the lib dir -->
        <packagingExcludes>WEB-INF/lib/executable-war-*.jar</packagingExcludes>
        <!-- exclude any undesired dependencies - for example, we leave out log4j and slf4j here -->
        <!-- as the executable war already has slf4j and logback bundled -->
        <dependentWarExcludes>WEB-INF/lib/*log4j*.jar,WEB-INF/lib/*slf4j*.jar</dependentWarExcludes>
        <overlays>
            <overlay>
                <groupId>com.vast.common</groupId>
                <artifactId>executable-war</artifactId>
                <type>jar</type>
            </overlay>
        </overlays>
    </configuration>
</plugin>
```
