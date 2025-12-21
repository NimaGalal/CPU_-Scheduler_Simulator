package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParser {
        public class JsonToHashMap {

        public static HashMap<String, Object> toMap(JSONObject json) {
            HashMap<String, Object> map = new HashMap<>();

            for (String key : json.keySet()) {
                Object value = json.get(key);

                if (value instanceof JSONObject) {
                    map.put(key, toMap((JSONObject) value));
                }
                else if (value instanceof JSONArray) {
                    map.put(key, toList((JSONArray) value));
                }
                else {
                    map.put(key, value);
                }
            }
            return map;
        }

        private static List<Object> toList(JSONArray array) {
            List<Object> list = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                Object value = array.get(i);

                if (value instanceof JSONObject) {
                    list.add(toMap((JSONObject) value));
                }
                else if (value instanceof JSONArray) {
                    list.add(toList((JSONArray) value));
                }
                else {
                    list.add(value);
                }
            }
            return list;
        }
    }

    public static class JSONparser {
        Scheduler scheduler;
        JSONparser(String FilePath) throws IOException {
            String jsonText = Files.readString(Path.of(FilePath));

            JSONObject jsonObject = new JSONObject(jsonText);
            JSONObject input = jsonObject.getJSONObject("input");
            JSONArray processes = input.getJSONArray("processes") ;

            this.scheduler = new Scheduler();
            if(input.has("rrQuantum")) {
                this.scheduler.setRrQuantum(input.getInt("rrQuantum"));
            }
            if(input.has("agingInterval")) {
                this.scheduler.setAgingInterval(input.getInt("agingInterval"));
            }
            if(input.has("contextSwitch")) {
                this.scheduler.setContextSwitch(input.getInt("contextSwitch"));
            }
            for (int i = 0 ; i < processes.length() ; i++) {
                JSONObject process  = (JSONObject) processes.get(i);
                int arrivalTime = 0 ;
                if(process.has("arrival"))
                {
                    arrivalTime = process.getInt("arrival");
                }
                int burstTime = 0 ;
                if(process.has("burst")){
                    burstTime = process.getInt("burst");
                }
                int priority  = 0 ;
                if(process.has("priority")){
                    priority = process.getInt("priority");
                }
                String name = " ";
                if(process.has("name") ){
                    name = (String) process.getString("name");
                }
                int quantum  = 0 ;
                if(process.has("quantum")){
                    quantum = process.getInt("quantum");
                }
                Process tempProcess = new Process(name, arrivalTime, burstTime, priority, quantum);
                this.scheduler.addProcess(tempProcess);
            }
        }
        public Scheduler getScheduler() {
            return scheduler;
        }
    }

}
