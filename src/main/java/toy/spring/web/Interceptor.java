package toy.spring.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Interceptor {

    default boolean preHandle(HttpServletRequest request, HttpServletResponse response){
        return true;
    }
    default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    }
}
