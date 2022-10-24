package br.com.devices.methods;


import br.com.devices.frames.FrameLogin;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class App {
    public static void main(String[] args) {
        
        try {
            String currentUser = System.getProperty("user.name");
            Path dirPath = Paths.get(String.format("C:\\Users\\%s\\AppData\\Roaming\\D3V1C6", currentUser));
            Files.createDirectory(dirPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        FrameLogin frameLogin = new FrameLogin();
        frameLogin.startupJframe();
        
    }
}
