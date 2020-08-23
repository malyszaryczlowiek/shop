package com.malyszaryczlowiek.shop.profiles;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Profile(value = "rest_mysql_auth")
public @interface RestMySqlAuth {
}
