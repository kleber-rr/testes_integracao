import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.21"
	kotlin("plugin.spring") version "1.5.21"
}

group = "com.testeintegracao"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	runtimeOnly("org.postgresql:postgresql")

	testImplementation("com.h2database:h2:1.4.200")

	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.hibernate:hibernate-envers")

	compileOnly("org.projectlombok:lombok:1.18.20")

	implementation("org.springframework.boot:spring-boot-starter-security:2.5.4")
	implementation("org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure:2.5.2")

	testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.5.30")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
