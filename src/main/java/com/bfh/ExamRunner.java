package com.bfh;

import com.bfh.config.CandidateProperties;
import com.bfh.dto.GenerateWebhookRequest;
import com.bfh.dto.GenerateWebhookResponse;
import com.bfh.service.ExamClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ExamRunner implements ApplicationRunner {

    private final CandidateProperties props;
    private final ExamClient client;

    public ExamRunner(CandidateProperties props, ExamClient client) {
        this.props = props;
        this.client = client;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("=== Starting exam flow ===");

        GenerateWebhookRequest req = new GenerateWebhookRequest(
                props.getName(),
                props.getRegNo(),
                props.getEmail()
        );
        GenerateWebhookResponse resp = client.generateWebhook(req).block();

        if (resp == null) {
            System.out.println("❌ Failed to get response from API");
            return;
        }

        String webhook = resp.getWebhook();
        String accessToken = resp.getAccessToken();
        System.out.println("✅ Got webhook: " + webhook);
        System.out.println("✅ Got accessToken: " + accessToken.substring(0, 15) + "...");

        int lastTwoDigits = extractLastTwoDigits(props.getRegNo());
        boolean isOdd = (lastTwoDigits % 2 == 1);
        if (isOdd) {
            System.out.println("👉 You must solve: Question 1");
            System.out.println("Link: https://drive.google.com/file/d/1IeSI6l6KoSQAFfRihIT9tEDICtoz-G/view?usp=sharing");
        } else {
            System.out.println("👉 You must solve: Question 2");
            System.out.println("Link: https://drive.google.com/file/d/143MR5cLFrlNEuHzzWJ5RHnEWuijuM9X/view?usp=sharing");
        }

        String finalQuery = new String(
                getClass().getClassLoader().getResourceAsStream("answer.sql").readAllBytes()
        ).trim();
        System.out.println("📄 Loaded SQL: " + finalQuery);

        String submitUrl = (webhook != null && !webhook.isBlank())
                ? webhook
                : "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

        String result = client.submitFinalQuery(submitUrl, accessToken, finalQuery).block();
        System.out.println("✅ Submission result: " + result);

        System.out.println("=== Flow complete ===");
    }

    private int extractLastTwoDigits(String regNo) {
        String digits = regNo.replaceAll("\\D", "");
        if (digits.length() >= 2) {
            return Integer.parseInt(digits.substring(digits.length() - 2));
        } else if (digits.length() == 1) {
            return Integer.parseInt(digits);
        } else {
            return 0;
        }
    }
}
