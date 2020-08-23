package com.malyszaryczlowiek.shop.util;

import org.springframework.stereotype.Component;


/**
 * WAŻNE:
 * <p></p>
 * nie można używac statycznej klasy np. w kontrolerze, componencie
 * czy servisie zwyczajenie poprzez odwołanie się do niej i jej składowych.
 * Trzeba ją oznaczyć jako @Component i WSTRZYKNĄĆ do controllera.
 */
@Component
public class Path {

    public final String HTTP = "http";
    public final String HTTPS = "https";
    public final String DOMAIN = "localhost";
    public final String PORT = "8080";

}
