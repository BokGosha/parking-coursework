# Учебный проект "Парковка"

Данный проект был создан для того, чтобы научиться взаимодействовать в команде, разрабатывать приложение совместно. Написан на языке программирования Java с применением фреймворка Spring, используется система сборки Gradle, осуществляет имитацию сервиса парковки.

## Зависимости проекта

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-data-rest'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-web-services'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-mail'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.session:spring-session-jdbc'
    implementation 'org.springframework.security:spring-security-web'
    implementation 'org.springframework.security:spring-security-core'
    implementation 'org.springframework.security:spring-security-config'
    implementation 'jakarta.persistence:jakarta.persistence-api'
    runtimeOnly 'org.postgresql:postgresql'
    compileOnly 'org.projectlombok:lombok'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

## Команды для запуска из консоли

```sh
gradle build
gradle run
```
