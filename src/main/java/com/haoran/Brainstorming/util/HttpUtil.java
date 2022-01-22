package com.haoran.Brainstorming.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class HttpUtil {

    public static boolean isApiRequest(HttpServletRequest request) {
        return request.getHeader("Accept") == null || !request.getHeader("Accept").contains("text/html");
    }


    // Définir différentes méthodes de réponse selon le type de requête reçue
    // Détermine le type de réception du champ accept dans l'en-tête de l'objet de requête request
    // S'il s'agit de text/html, il répondra à un morceau de js. Ici, le type de contenu de réponse de l'objet de réponse doit également être défini sur text/javascript
    // S'il s'agit d'application/json, il répondra avec une chaîne de json et le type de contenu de réponse de l'objet de réponse doit être défini sur application/json
    public static void responseWrite(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!HttpUtil.isApiRequest(request)) {
            response.setContentType("text/html;charset=utf-8");
            response.sendRedirect("/login");
        } else  {
            response.setContentType("application/json;charset=utf-8");
            Result result = new Result();
            result.setCode(201);
            result.setDescription("login svp");
            response.getWriter().write(JsonUtil.objectToJson(result));
        }
    }
}
