/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.devices.entities;

import java.io.IOException;

/**
 *
 * @author Aluno
 */
public class App {
  
  public static void main(String[] args) throws IOException, InterruptedException {
    
    JSONObject json = new JSONObject();
    json.put("Text", "Facil né?");
    Slack.sendMessage(json);
    
  }
  
}
