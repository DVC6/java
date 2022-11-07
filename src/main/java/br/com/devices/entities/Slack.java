/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.devices.entities;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import java.net.URI;
import java.io.IOException;

/**
 *
 * @author Aluno
 */
public class Slack {
  private static HttpClient client = HttpClient.newHttpClient();
  private static final String URL = "https://hooks.slack.com/services/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";
          
  public static void sendMessage(JSONObject content) throws IOException, InterruptedException{
    HttpRequest request = HttpRequest.newBuilder(
                          URI.create(URL))
                          .header("Accept", "application/json")
                          .POST(HttpRequest.BodyPublisher.ofString(content.toString()))
                          .build();
    
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    System.out.println(String.format("Status: %s", response.statusCode()));
    System.out.println(String.format("Response: %s", response.body()));
  }
}
