package com.example.ccuda.data;

import java.util.HashMap;
import java.util.Map;

public class ChatData {
    public Map<String, Boolean> users = new HashMap<>();
    public Map<String, Comment> comments = new HashMap<>();

    public static class Comment{
        public String user_id;
        public String msg;
        public String timestamp;
        public String type;
    }

}
