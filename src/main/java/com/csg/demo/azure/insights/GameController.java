package com.csg.demo.azure.insights;
import com.csg.demo.azure.insights.model.WinLoss;
import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.MetricTelemetry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Controller
@RequestMapping("/")
@Scope("session")
@SessionAttributes("winLoss")
public class GameController {


    @Autowired
    private WinLoss record;

	Logger logger = LogManager.getLogger();

    private static String [] choices = {"Rock", "Paper", "Scissors"};

	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		return "play";
	}

	@RequestMapping(method = RequestMethod.GET, value = "error")
    public String goToError(ModelMap model) {


		logger.error("Error Page");

		TelemetryClient client = new TelemetryClient();
		client.trackException(new Exception("My Custom Exception"));

		model.addAttribute("message", "Error PAGE");
		return "error";
	}


    private int getRandomChoice(){
        Random r = new Random();
        int Low = 0;
        int High = 3;
        int randomChoice = r.nextInt(High-Low) + Low;
        return randomChoice;
    }

    private int determineUserChoice(String userChoice){
        for (int x = 0 ; x < choices.length  ; x++){
            if (choices[x].equalsIgnoreCase(userChoice))
                return x;
        }
        return -1;
    }

	@RequestMapping(value="play/{choice}", method = RequestMethod.GET)
	public String contest(@PathVariable(value="choice") String choice, ModelMap model, HttpServletRequest request) {
        String sessionID = RequestContextHolder.currentRequestAttributes().getSessionId();


        String outcome = new String();
        int randSelection = getRandomChoice();
        int userSelection = determineUserChoice(choice);

        if (userSelection == -1 ){
            model.addAttribute("exception", "Choice not valid");
            return "error";
        }

        String youchose = new String(choices[userSelection]);
        String ichose = new String(choices[randSelection]);

        logger.debug("You chose" + youchose);





        TelemetryClient client = new TelemetryClient();
        MetricTelemetry mt = new MetricTelemetry();


       // WinLoss record = (WinLoss)request.ki98ju000o-getSession().getAttribute("record");
        if (record == null){
            record = new WinLoss();
        }

        if (userSelection == randSelection){
            outcome = "tie";
            client.trackEvent("tie");
            
            record.addTie();
        } else if ((userSelection == 0 && randSelection == 2)
                    || (userSelection == 2 && randSelection == 1 )
                    || (userSelection == 1 && randSelection == 0 )){
            outcome = "You win";
            client.trackEvent("win");
            record.addWin();
        } else {
            outcome = "You lose";
            client.trackEvent("lose");
            record.addLoss();
        }

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("sessionID", sessionID);
        Map<String, Double> metrics = new HashMap<String, Double>();
        metrics.put("userChoice", (double) userSelection);
        metrics.put("compChoice", (double) randSelection);

        client.trackEvent(outcome, properties, metrics);

        client.trackEvent(outcome);
        client.trackEvent(youchose);
        client.trackEvent(ichose);
        client.trackTrace("End of game" + outcome + ": " + youchose +" vs. " + ichose);


        client.flush();
        logger.debug("End of game controller");

        model.addAttribute("outcome", outcome);
        model.addAttribute("youchose", youchose);
        model.addAttribute("ichose", ichose);
        model.addAttribute("record", record);
        model.addAttribute("curTime", String.format("%tR", new Date()));

		return "play";
	}

}