# pact-kotlin-spring-poc

## 1. Pact Broker Set Up
```
docker-compose up
```
Check docker-compose.yaml file to update server, username, and password.
By default (this file) if not changed, http://localhost:9292 (with username/password: pact_workshop/pact_workshop)

## 2. Gradle
In gradle.build file, update pactBrokerUrl, username, and password accordingly.
```
pact {
 publish {
 pactBrokerUrl = 'http://localhost:9292'
 pactBrokerUsername = ''
 pactBrokerPassword = ''
 version = ''
 }
}
```

By default, the plugin expects the contracts to be available at $buildDir/pacts

```./gradlew test``` to create a contract under /build/pacts

```./gradlew pactPublish``` to publish the contract to pact broker. Version has to be updated everytime changes have been published

## 3. Maven
In pom.xml, add the following dependency:
```
        <dependency>
            <groupId>au.com.dius.pact.provider</groupId>
            <artifactId>junit5spring</artifactId>
            <version>4.1.17</version>
            <scope>test</scope>
        </dependency>

```

and <plugin> to specify pact broker's url, userName and password.
```
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
			          <plugin>
                <groupId>au.com.dius.pact.provider</groupId>
                <artifactId>maven</artifactId>
                <version>4.1.17</version>
                <configuration>
                    <pactBrokerUrl>http://localhost:9292</pactBrokerUrl>
                    <pactBrokerUsername>pact_workshop</pactBrokerUsername>
                    <pactBrokerPassword>pact_workshop</pactBrokerPassword>
                    <tags>
                        <tag>prod</tag>
                        <tag>test</tag>
                    </tags>
                </configuration>
            </plugin>
		</plugins>
	</build>
```

```./mvnw verify``` to verify locally

```./mvnw pact:publish``` to publish (if this is consumer service)

```./mvnw verify -Dpact.verifier.publishResults=true -Dpact.provider.version=1.0-SNAPSHOT``` to verify and publish the result in pact broker

```./mvnw pact:can-i-deploy -Dpacticipant='consumerApp' -Dlatest=true```





