package com.scarawooo.servlet;

import com.scarawooo.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@WebServlet(name = "Corrector", urlPatterns = {"/Corrector"})
public final class Corrector extends HttpServlet {
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try (PrintWriter out = response.getWriter()) {
            StringBuilder inputStr = new StringBuilder(request.getParameter("text"));
            Pattern pattern = Pattern.compile("[^[a-zA-Zа-яА-Я/\\-]]++");
            JSONArray spellchecking = new JSONArray();
            long id = 0;
            for (Matcher matcher = pattern.matcher(inputStr);; matcher = pattern.matcher(inputStr)) {
                JSONObject spellcheckingWordDescription;
                if (matcher.find()) {
                    if (matcher.start() != 0) {
                        spellcheckingWordDescription = new JSONObject();
                        String word = inputStr.substring(0, matcher.start());
                        if (MongoHandler.search(word) ||
                                MongoHandler.search(word.toLowerCase())) {
                            spellcheckingWordDescription.put("id", id++);
                            spellcheckingWordDescription.put("word", word);
                            spellcheckingWordDescription.put("incorrect", false);
                        } else if (inputStr.charAt(matcher.start()) == '.' && MongoHandler.search(word.toLowerCase().concat("."))) {
                            spellcheckingWordDescription.put("id", id++);
                            spellcheckingWordDescription.put("word", word);
                            spellcheckingWordDescription.put("incorrect", false);
                        } else {
                            Word wordDescription = Word.getWord(word);
                            wordDescription.findNearestCorrections();
                            spellcheckingWordDescription.put("id", id++);
                            spellcheckingWordDescription.put("word", wordDescription.getWord());
                            spellcheckingWordDescription.put("incorrect", !wordDescription.getCorrections().isEmpty());
                            spellcheckingWordDescription.put("corrections",
                                    new JSONArray(wordDescription.getCorrections().stream().
                                            map(Correction::getWord).collect(Collectors.toList()))
                            );
                        }
                        spellchecking.put(spellcheckingWordDescription);
                    }
                    spellcheckingWordDescription = new JSONObject();
                    spellcheckingWordDescription.put("id", id++);
                    spellcheckingWordDescription.put("word", inputStr.substring(matcher.start(), matcher.end()));
                    spellcheckingWordDescription.put("incorrect", false);
                    spellchecking.put(spellcheckingWordDescription);
                    inputStr.delete(0, matcher.end());
                } else {
                    spellcheckingWordDescription = new JSONObject();
                    String word = inputStr.toString();
                    if (MongoHandler.search(word) || MongoHandler.search(word.toLowerCase())) {
                        spellcheckingWordDescription.put("id", id);
                        spellcheckingWordDescription.put("word", word);
                        spellcheckingWordDescription.put("incorrect", false);
                    } else {
                        Word wordDescription = Word.getWord(word);
                        wordDescription.findNearestCorrections();
                        spellcheckingWordDescription.put("id", id);
                        spellcheckingWordDescription.put("word", wordDescription.getWord());
                        spellcheckingWordDescription.put("incorrect", !wordDescription.getCorrections().isEmpty());
                        spellcheckingWordDescription.put("corrections",
                                new JSONArray(wordDescription.getCorrections().stream().
                                        map(Correction::getWord).collect(Collectors.toList()))
                        );
                    }
                    spellchecking.put(spellcheckingWordDescription);
                    break;
                }
            }
            out.print(spellchecking.toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            processRequest(request, response);
        } catch (JSONException exception) {
            Logger.getLogger(Corrector.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("com.scarawooo.util.MongoHandler");
        } catch (ClassNotFoundException exception) {
            Logger.getLogger(Corrector.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        MongoHandler.shutdown();
    }
}