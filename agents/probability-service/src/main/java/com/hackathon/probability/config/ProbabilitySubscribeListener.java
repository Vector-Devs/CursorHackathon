package com.hackathon.probability.config;

import com.hackathon.probability.dto.ProbabilityResponse;
import com.hackathon.probability.service.ProbabilityPushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class ProbabilitySubscribeListener implements ApplicationListener<SessionSubscribeEvent> {

	private static final Logger log = LoggerFactory.getLogger(ProbabilitySubscribeListener.class);

	private static final String PROBABILITY_TOPIC = "/topic/probability";

	private final SimpMessagingTemplate messagingTemplate;
	private final ProbabilityPushService pushService;

	public ProbabilitySubscribeListener(SimpMessagingTemplate messagingTemplate, ProbabilityPushService pushService) {
		this.messagingTemplate = messagingTemplate;
		this.pushService = pushService;
	}

	@Override
	public void onApplicationEvent(SessionSubscribeEvent event) {
		Object dest = event.getMessage().getHeaders().get("simpDestination");
		if (dest == null || !PROBABILITY_TOPIC.equals(dest.toString())) {
			return;
		}

		ProbabilityResponse latest = pushService.getLatest();
		if (latest == null) {
			log.info("Probability WebSocket: new subscriber, no cached data yet (first produce not complete)");
			return;
		}

		try {
			messagingTemplate.convertAndSend(PROBABILITY_TOPIC, latest);
			log.info("Probability WebSocket: sent cached data to new subscriber(s), {} items", latest.items().size());
		} catch (Exception e) {
			log.error("Probability WebSocket: failed to send cached on subscribe - {}", e.getMessage(), e);
		}
	}
}
