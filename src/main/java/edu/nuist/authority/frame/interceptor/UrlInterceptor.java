package edu.nuist.authority.frame.interceptor;

import edu.nuist.authority.frame.annotations.RequiredPermission;
import edu.nuist.authority.frame.UserRoleEnable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
@Component
public class UrlInterceptor implements HandlerInterceptor {
    @Value("${errorhtml}")
    private String errorurl;
    @Value("${errormessage}")
    private String errormessage;
    @Value("${sessionname}")
    private String sessionName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {

        return this.hasPermission(request, handler, response);

    }

    public boolean hasPermission(HttpServletRequest request, Object handler, HttpServletResponse response) throws IOException {
        if (handler instanceof HandlerMethod) {
            System.out.println(handler);
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.getMethod().getDeclaringClass().getAnnotation(Controller.class) != null)
            {
                RequiredPermission requiredPermission = handlerMethod.getMethod().getAnnotation(RequiredPermission.class);

                if (requiredPermission != null) {

                    HttpSession session = request.getSession();
                    UserRoleEnable user = (UserRoleEnable) session.getAttribute(sessionName);
                    if (user != null) {
                        return isMatch(request, user, response, "controller");
                    } else {
                        System.out.println("error");
                        return false;
                    }
                } else return true;
            }
            else if (handlerMethod.getMethod().getDeclaringClass().getAnnotation(RestController.class) != null)
            {
                System.out.println("是restcontroller");
                RequiredPermission requiredPermission = handlerMethod.getMethod().getAnnotation(RequiredPermission.class);

                if (requiredPermission != null) {
                    HttpSession session = request.getSession();
                    UserRoleEnable user = (UserRoleEnable) session.getAttribute(sessionName);
                    if (user != null) {
                        return isMatch(request, user, response, "restcontroller");
                    } else {
                        System.out.println("error");
                        return false;
                    }
                }

            } else return true;
        }
        return true;
    }

    public boolean isMatch(HttpServletRequest request, UserRoleEnable user, HttpServletResponse response, String type) throws IOException {
        String url = request.getRequestURI();
        if (url.split("/").length < 1) {
            System.out.println("未在url中加入角色名");
            return false;
        }
        String role = url.split("/")[1];
        String[] roleList = user.getRoles();
        int i;
        for (i = 0; i < roleList.length; i++) {
            if (role.equals(roleList[i])) {
                return true;
            }
        }
        if (i == roleList.length) {
            if (type.equals("controller"))
                controllerError(response);
            else restcontrollerError(response);
            return false;
        }
        return false;
    }

    public void controllerError(HttpServletResponse response) throws IOException {
        response.sendRedirect(errorurl);
    }

    public void restcontrollerError(HttpServletResponse response) throws IOException {
        PrintWriter out = null;
        out = response.getWriter();
        out.append(errormessage);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

}