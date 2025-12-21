package org.example;
import java.io.IOException;
import java.nio.file.*;

import org.example.JsonParser.JSONparser;
import org.json.*;
import java.util.*;

public class Main {
    static void main() throws IOException {
        for(int i = 1 ; i <= 6 ; i++) {
            System.out.println("----------------- Test Case " + i + " -----------------");
            JSONparser TestCase = new JSONparser("src\\\\test\\\\Other_Schedulers\\\\test_" + i + ".json");
            Scheduler scheduler = TestCase.getScheduler() ;
            SchedulerResult result = scheduler.runPriority();
            result.printResults();
            System.out.println("-------------------------------------------------");
            System.out.println("-------------------------------------------------");
        }
    }
}
