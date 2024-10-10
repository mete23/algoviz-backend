plugins {
	java
	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "de.algoviz"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("javax.servlet:javax.servlet-api:4.0.1")
	implementation("commons-io:commons-io:2.11.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.testng:testng:7.1.0")
	testImplementation("org.testng:testng:7.1.0")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	testImplementation("org.mockito:mockito-core:5.2.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
