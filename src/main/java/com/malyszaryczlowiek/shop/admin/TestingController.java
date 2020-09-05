package com.malyszaryczlowiek.shop.admin;

import com.malyszaryczlowiek.shop.client.Client;
import com.malyszaryczlowiek.shop.feature.Feature;
import com.malyszaryczlowiek.shop.feature.FeatureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@RestController
@RequestMapping("/testing")
public class TestingController {

    Logger logger = LoggerFactory.getLogger(AdminProductAndCategoryController.class);
    private final FeatureRepository featureRepository;


    TestingController(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;

    }


    /**
     * to jest ogólniejsz metoda ponieważ bierze ona za argumenty obiekty
     * **Servlet** które są zdefiniowane w pakiecie {@link javax.servlet.http}
     * czlyli pakiecie Javy EE.
     * <p></p>
     * Bardzo dobra dokumentacja użycia Response headerów jest tutaj
     * <a href="https://www.baeldung.com/spring-response-header">
     *     baeldung.com/spring-response-header</a>
     *
     * @return string
     */
    @RequestMapping(path = "/httpheaders", method = RequestMethod.GET)
    ResponseEntity<String> workWithServlet() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("header name", "header value");
        logger.trace("servlet method done.");

        // ważne tutaj do response entity dodaje dodatkowy nagłówek,
        // który będzie dodany do response
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body("response build");
    }


    /**
     * Tworzenie własnych filtrów i dodawanie ich do konkretnych ścieżek omówiłem tu:<p>
     * {@link com.malyszaryczlowiek.shop.filters.TestingFilter TestingFilter}
     * oraz tu {@link com.malyszaryczlowiek.shop.filters.TestingConfiguration TestingConfiguration}
     * @return string
     */
    @RequestMapping(path = "/filterTest", method = RequestMethod.GET)
    ResponseEntity<String> workWithFilter() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("MyFilterHeader", "MyFilterValue");
        logger.trace("Inside method workWithFilter().");

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body("method executed");
    }


    /**
     * wyłuskiwanie informacji z argumentów metody
     * @param sessionStatus
     * @param principal
     * @param authentication
     * @return
     */
    @RequestMapping(path = "/sessionTest", method = RequestMethod.GET)
    String getAdminPanel(SessionStatus sessionStatus, Principal principal, Authentication authentication) {
        logger.debug("Authentication details: " + authentication.getDetails().toString());
        //logger.debug(authentication.getCredentials().toString());

        //sessionStatus.setComplete();
        logger.debug("session status is now: " + sessionStatus.isComplete());

        Collection<? extends GrantedAuthority> authorityList = authentication.getAuthorities();
        authorityList.forEach(auth -> logger.debug(auth.getAuthority()));
        return "welcome admin panel\n" +
                "session information: " + sessionStatus.isComplete() +"\n" +
                "principal name: " + principal.getName() + "\n" +
                "principal toString(): " + principal.toString() + "\n" +
                "authentication toString(): " + authentication.toString() + "\n" +
                "authentication getName(): " + authentication.getName();
    }





    @RequestMapping(path = "/cookies", method = RequestMethod.GET)
    ResponseEntity<String> workWithCookies(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            Arrays.stream(cookies).forEach(cookie -> {
                logger.trace("cookie name " + cookie.getName());
                logger.trace("cookie value " + cookie.getValue());
            });
        else
            logger.warn("cookies are empty");

        Locale locale = request.getLocale();
        if (locale != null)
            logger.trace("locale: " + locale.getLanguage());
        else
            logger.warn("locale is null");

        // sprawdzam usera
        String email = request.getRemoteUser();
        if (email == null)
            logger.warn("user login is null");
        else
            logger.trace("user login is: " + email);

        // dodaje jeszcze swojego cookies'a
        // TODO to jest extremalnie ważne aby ustwić tu flagę HttpOnly na true,
        // TODO bo bez tego dostęp do ciasteczek ma równiez JavaScript
        // TODO a tego chcemy uniknąc aby nie mogło dochodzić do ataków XSS
        // TODO bo to skrypty mogą następnie wykorzystać takie ciasteczko.
        Cookie myCookie = new Cookie("CookieHash", Integer.toString(12345));
        myCookie.setHttpOnly(true); // <-- Tu jest ważne
        myCookie.setPath("/"); // ustawia ścieżkę tak, ze to ciastko jest dodawane do każdego żądania którę będzie
        // na ścieżce od / i wo wszystkich podfolderach. Bez tego ścieżką będzie ścieżka kontrolera.

        response.addCookie(myCookie);
        response.sendRedirect("/");



        return ResponseEntity.status(HttpStatus.OK).body("working with cookie");
    }


    /**
     * w ten sposób nie jesteśmy w stanie zmienić session cookie tak aby czas jego
     * wazności nie był równy sesji czyli wylogownaiu się z przeglądarki.
     */
    @RequestMapping(path = "/cookiesTime", method = RequestMethod.GET)
    ResponseEntity<String> invalidateSessionCookie(HttpServletRequest request, HttpServletResponse response) {

        Cookie sessionCookie = null;
                //Arrays.stream(request.getCookies())
                //.filter(cookie -> cookie.getName().equals("JSESSIONID")).collect(Collectors.toList()).get(0);
        if (sessionCookie != null) {
            sessionCookie.setMaxAge(5);
            logger.trace("session id cookie is changed to time cookie in seconds: " + sessionCookie.getMaxAge());
        }
        else
            logger.warn("jsession cookie does not exists");


        return ResponseEntity.status(HttpStatus.OK).body("cookie should be invalidated");
    }


    /**
     * testowa metoda sprawdzająca zarządzanie sesją, zmiany jej id i invalidacji.
     *
     * @param request
     * @return
     */
    @RequestMapping(path = "/invalidationOfSession", method = RequestMethod.GET)
    ResponseEntity<String> invalidationOfSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("session is null cannot create it.");

        logger.trace("session id is: " + session.getId());
        logger.trace("session id changing to value: " + request.changeSessionId());
        logger.trace("session validation: " + request.isRequestedSessionIdValid());


        // invaliduje sesje i sprawdzam jej parametry
        session.invalidate();
        logger.warn("session validation: " + request.isRequestedSessionIdValid());
        // cikawe info o id sesji jest przechowywane w jej obiekcie ale sama sesja jest nieważna
        logger.warn("session id is: " + session.getId());
        // poniważ sesja została unieważniona nie można zmienić id sesji
        //logger.warn("sprawdzam czy po invalidacji można zmienić id. session id changing to value: " + request.changeSessionId());

        return ResponseEntity.status(HttpStatus.OK).body("method returned normally");
    }


    /**
     * testowanie obiektu zawirającego mapę.
     * @return
     */
    @RequestMapping(path = "/map", method = RequestMethod.GET)
    ResponseEntity<Map<String, String>> getSearchingCriteria() {
        Map<String, String> testingMap = new LinkedHashMap<>();
        testingMap.put("key1", "value1");
        testingMap.put("key2", "value2");
        return ResponseEntity.status(HttpStatus.OK).body(testingMap);
    }

    @RequestMapping(path = "/userInfo", method = RequestMethod.GET)
    ResponseEntity<Client> getClient() {
        Client client = new Client("fake@email.com", Integer.valueOf("123456789"), "jakiś pass" ) ;
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @RequestMapping(path = "/featureMap", method = RequestMethod.GET)
    ResponseEntity<List<String[]>> getFeatureMap() {
        List<String[]> list = featureRepository.getDescriptorMap();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @RequestMapping(path = "/requestParam", method = RequestMethod.GET)
    ResponseEntity<?> requestParam(@RequestParam(name = "before", required = false) Long before) {

        if (before == null) {
            logger.debug("before is null.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else {
            logger.debug("before is NOT null");
            long now = System.currentTimeMillis();
            LocalDateTime date = LocalDateTime.ofEpochSecond(now,0, ZoneOffset.ofHours(0));
            LocalDateTime date2 = LocalDateTime.ofEpochSecond(before,0, ZoneOffset.ofHours(2));
            logger.debug("now is: " + date + " and ofset is: " + date2);
            return ResponseEntity.status(HttpStatus.OK).body(date);
        }
    }

}




















