package com.hackathon.probability.service;

import com.hackathon.probability.dto.ProbabilityResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ProbabilityPushService {

	private static final Logger log = LoggerFactory.getLogger(ProbabilityPushService.class);

	private final ProbabilityService probabilityService;
	private final SimpMessagingTemplate messagingTemplate;

	private final BlockingQueue<ProbabilityResponse> queue = new LinkedBlockingQueue<>(1);
	private final AtomicReference<ProbabilityResponse> latest = new AtomicReference<>(null);
	private volatile boolean running = true;
	private Thread consumerThread;

	public ProbabilityPushService(ProbabilityService probabilityService, SimpMessagingTemplate messagingTemplate) {
		this.probabilityService = probabilityService;
		this.messagingTemplate = messagingTemplate;
	}

	@PostConstruct
	public void startConsumer() {
		consumerThread = new Thread(this::consume, "probability-push-consumer");
		consumerThread.setDaemon(true);
		consumerThread.start();
		log.info("Probability push consumer started");
	}

	@PreDestroy
	public void stopConsumer() {
		running = false;
		consumerThread.interrupt();
	}

	@Scheduled(fixedDelay = 600_000, initialDelay = 5_000)
	public void produce() {
		log.info("Probability produce: starting getProbabilities (news-agent AI + ship-mobility)");
		try {
			ProbabilityResponse response = probabilityService.getProbabilities();
			queue.drainTo(new java.util.LinkedList<>());
			queue.offer(response);
			latest.set(response);
			log.info("Probability produce: success, {} items, broadcasting to WebSocket", response.items().size());
		} catch (Exception e) {
			log.error("Probability produce: FAILED - {}", e.getMessage(), e);
		}
	}

	private void consume() {
		while (running) {
			try {
				ProbabilityResponse response = queue.take();
				messagingTemplate.convertAndSend("/topic/probability", response);
				log.info("Probability WebSocket: broadcast OK, {} items", response.items().size());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			} catch (Exception e) {
				log.error("Probability WebSocket: broadcast FAILED - {}", e.getMessage(), e);
			}
		}
	}

	public ProbabilityResponse getLatest() {
		return latest.get();
	}
}
