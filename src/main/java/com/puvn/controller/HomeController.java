package com.puvn.controller;

import com.puvn.dao.LinkDAO;
import com.puvn.dao.UserDAO;
import com.puvn.models.LinkClass;
import com.puvn.models.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
public class HomeController {

    @Autowired
    MessageSource messageSource;

    private ApplicationContext context =
            new ClassPathXmlApplicationContext("Spring-Module.xml");
    private UserDAO userDAO = (UserDAO) context.getBean("userDAO");
    private LinkDAO linkDAO = (LinkDAO) context.getBean("linksDAO");
    private ModelAndView modelAndView;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String refreshAfterLogin(HttpServletRequest request, HttpSession session) throws ServletException {
        if (request.isUserInRole("ROLE_USER")) {
            String yourName = request.getRemoteUser();
            UserClass you = userDAO.findByLogin(yourName);

            ArrayList<UserClass> yourSubscriptions = getSubscriptions(yourName);
            ArrayList<String> yourSubscriptionsNames = new ArrayList<>();

            for (UserClass yourSubscription : yourSubscriptions)
                yourSubscriptionsNames.add(yourSubscription.getLogin());

            int yourMaxLinks = getMaxLinks(yourName);

            session.setAttribute("you", you.getLogin());
            session.setAttribute("yourMail", you.getEmail());
            session.setAttribute("yourSubscriptions", yourSubscriptionsNames);
            session.setAttribute("yourMaxLinks", yourMaxLinks);
            session.setAttribute("yourAvatarLink", you.getAvatarLink());

            return "redirect:/home/" + yourName;
        } else {
            request.logout();
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/home/{username}", method = RequestMethod.GET)
    public ModelAndView buildHome(@PathVariable("username") String username, HttpServletRequest request,
                                  HttpSession session)
            throws ServletException {
        modelAndView = new ModelAndView();

        String[] sessionKeys = {"you", "yourMail", "yourSubscriptions", "yourMaxLinks", "yourAvatarLink"};

        session.setAttribute("link", new LinkClass(null));

        //Если в сессии есть null объекты
        //TODO
        //fix bug with null objects
        if (sessionHasNullObjects(session, sessionKeys))
            refreshAfterLogin(request, session);

        //Для каждого юзера формируем таблицу links его ссылок
        ArrayList<LinkClass> linksList = getLinks(username, request);

        //Если юзер запросил свою страницу
        if (username.equals(request.getRemoteUser())) {
            //То данные берем из сессии
            modelAndView.addObject("username", session.getAttribute("you"));
            modelAndView.addObject("email", session.getAttribute("yourMail"));
            ArrayList<String> subscriptions = (ArrayList<String>) session.getAttribute("yourSubscriptions");
            modelAndView.addObject("subscriptions", subscriptions);
            modelAndView.addObject("subscriptionsNumber", subscriptions.size());
            modelAndView.addObject("linksToAdd", (Integer) session.getAttribute("yourMaxLinks") - linksList.size());
            modelAndView.addObject("avatarLink", session.getAttribute("yourAvatarLink"));

            if ((Integer) modelAndView.getModel().get("linksToAdd") == 0)
                modelAndView.addObject("error", messageSource.getMessage("label.linksLimit", null,
                        "You've reached links limit", LocaleContextHolder.getLocale()));

            //И запрашиваем из базы обновления подписок
            if (!subscriptions.isEmpty())
                modelAndView.addObject("feed", getUpdates(subscriptions));
            else modelAndView.addObject("feed", "");

        } else {
            //Иначе берем все данные из базы
            UserClass userClass = userDAO.findByLogin(username);

            //Если пользователя не существует
            if (userClass == null) {
                //Ищем похожих пользователей
                ArrayList<UserClass> similarUsers = userDAO.findSimilarUsers(username);
                if (!similarUsers.isEmpty()) {
                    modelAndView.addObject("infoMsg", messageSource.getMessage("label.mayBeYouSearch", null,
                            "No such user", LocaleContextHolder.getLocale()));
                    modelAndView.addObject("similarUsers", similarUsers);
                }
                modelAndView.addObject("errorMessage", messageSource.getMessage("label.noSuchUser", null,
                        "No such user", LocaleContextHolder.getLocale()));
                modelAndView.addObject("errorPic", "<i class=\"fa fa-user-secret fa-6\"></i>");
                modelAndView.setViewName("error");
                return modelAndView;
            } else {
                //Если пользователь найден то по нему собираются все данные

                ArrayList<UserClass> subscriptionsList = getSubscriptions(username);

                ArrayList<String> subscriptions = new ArrayList<>();
                for (UserClass subscription : subscriptionsList)
                    subscriptions.add(subscription.getLogin());

                //Если пользователь находится в ваших подписках
                ArrayList<String> inSubscribtions;
                inSubscribtions = (ArrayList<String>) session.getAttribute("yourSubscriptions");
                if (inSubscribtions.contains("/" + username)) {
                    modelAndView.addObject("subscribe", messageSource.getMessage("label.youAreSubscribed", null,
                            "You are subscribed", LocaleContextHolder.getLocale()));
                    modelAndView.addObject("subscribeDisabled", "disabled");
                } else modelAndView.addObject("subscribe", messageSource.getMessage("label.subscribe", null,
                        "Subscribe", LocaleContextHolder.getLocale()));


                modelAndView.addObject("subscriptionsNumber", subscriptions.size());
                modelAndView.addObject("subscriptions", subscriptions);
                modelAndView.addObject("username", userClass.getLogin());
                modelAndView.addObject("email", userClass.getEmail());
                modelAndView.addObject("avatarLink", userClass.getAvatarLink());
            }
        }

        //Для каждого пользователя выводим список его ссылок
        modelAndView.addObject("linksNumber", linksList.size());
        Collections.reverse(linksList);
        modelAndView.addObject("links", linksList);

        modelAndView.setViewName("home");
        return modelAndView;
    }

    @RequestMapping(value = "/addLink", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseStatus(value = HttpStatus.OK)
    public void addLink(@RequestParam("link") String link, @RequestParam("isPrivate") boolean isPrivate,
                        HttpServletRequest request) throws ServletException, UnsupportedEncodingException {
        if ((Integer) modelAndView.getModel().get("linksToAdd") != 0) {
            if (!link.contains("//"))
                link = "//" + link;

            LinkClass linkClass = new LinkClass(link, isPrivate);

            String content = getLinkContent(link);

            if (content != null) {
                try {
                    byte[] linkTitleBytes = getLinkTitle(content).getBytes();
                    String linkTitle;
                    if (linkTitleBytes.length > 600)
                        linkTitle = new String(linkTitleBytes, StandardCharsets.UTF_8).substring(0, 599);
                    else linkTitle = new String(linkTitleBytes, StandardCharsets.UTF_8);
                    linkClass.setLinkTitle(linkTitle);
                } catch (NullPointerException ignored) {
                }
            }

            if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
                linkDAO.addLink(request.getRemoteUser(), linkClass);
            else request.logout();
        }
    }

    @RequestMapping(value = "/deleteLink", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteLink(@RequestParam("linkId") int linkId,
                           HttpServletRequest request) throws ServletException {
        LinkClass linkClass = new LinkClass(linkId);
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
            linkDAO.deleteLink(request.getRemoteUser(), linkClass);
        else request.logout();

    }

    @RequestMapping(value = "/addSubscription", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void addSubscription(@RequestParam("subscription") String subscription,
                                HttpServletRequest request, HttpSession session) throws ServletException {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            //Добавляем подписку в базу и обновляем в сессии
            UserClass user = new UserClass();
            user.setLogin(request.getRemoteUser());
            UserClass subscriptionUser = new UserClass();
            subscriptionUser.setLogin(subscription);
            userDAO.addSubscription(user, subscriptionUser);

            ArrayList<String> yourSubscriptions = (ArrayList<String>) session.getAttribute("yourSubscriptions");
            yourSubscriptions.add(subscription);
            session.setAttribute("yourSubscriptions", yourSubscriptions);
        }
    }

    @RequestMapping(value = "/deleteSubscription", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteSubscription(@RequestParam("subscription") String subscription,
                                   HttpServletRequest request, HttpSession session) throws ServletException {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            //Удаляем подписку из базы и обновляем в сессии
            UserClass user = new UserClass();
            user.setLogin(request.getRemoteUser());
            UserClass subscriptionUser = new UserClass();
            subscriptionUser.setLogin(subscription);
            userDAO.deleteSubscription(user, subscriptionUser);

            ArrayList<String> yourSubscriptions = (ArrayList<String>) session.getAttribute("yourSubscriptions");
            yourSubscriptions.remove(subscription);
            session.setAttribute("yourSubscriptions", yourSubscriptions);
        }
    }

    private ArrayList<LinkClass> getLinks(String username, HttpServletRequest request) {
        ArrayList<LinkClass> allLinks = linkDAO.getLinks(username);
        ArrayList<LinkClass> shownLinks = new ArrayList<>();
        if (request.getRemoteUser().equals(username))
            return allLinks;
        else {
            for (LinkClass link : allLinks)
                if (!link.isPrivate())
                    shownLinks.add(link);
            return shownLinks;
        }
    }

    private int getMaxLinks(String username) {
        return linkDAO.getMaxLinks(username);
    }

    private ArrayList<UserClass> getSubscriptions(String username) {
        return userDAO.getSubscriptions(username);
    }

    private LinkedHashMap getUpdates(ArrayList<String> subscriptions) {
        return userDAO.getUpdates(subscriptions);
    }

    private ArrayList<Object> getSessionObjects(HttpSession session, String[] keys) {
        ArrayList<Object> sessionObjects = new ArrayList<>();
        for (String key : keys)
            sessionObjects.add(session.getAttribute(key));

        return sessionObjects;
    }

    private boolean sessionHasNullObjects(HttpSession session, String[] keys) {
        ArrayList<Object> sessionObjects = getSessionObjects(session, keys);
        for (Object sessionObject : sessionObjects)
            if (sessionObject == null)
                return true;
        return false;
    }

    private String getLinkContent(String link) {
        URL url;
        String content = null;
        try {
            url = new URL(link);
            URLConnection conn = url.openConnection();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;

            while ((inputLine = br.readLine()) != null) {
                content += inputLine;
                if (inputLine.contains("</title>")) break;
            }
        } catch (IOException ignored) {
        }
        return content;
    }

    private String getLinkTitle(String content) {
        String title = " ";
        if (content != null) {
            Pattern pattern = Pattern.compile("<title>(.*?)</title>");
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                title = matcher.group(1);
            }
        }
        return title;
    }
}
