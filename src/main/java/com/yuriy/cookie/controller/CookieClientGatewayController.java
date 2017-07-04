package com.yuriy.cookie.controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.yuriy.cookie.model.client.newp.Cookie;

/**
 * Created by yyakymchuk on 5/23/2017.
 */


@RestController
@RequestMapping("/cookies")
public class CookieClientGatewayController {

	private final RestTemplate restTemplate;

	private final DiscoveryClient discoveryClient;

	@Autowired
	public CookieClientGatewayController(final RestTemplate restTemplate, final DiscoveryClient discoveryClient) {
		this.restTemplate = restTemplate;
		this.discoveryClient = discoveryClient;
	}

	@RequestMapping(method = RequestMethod.POST)
	public void create(@RequestBody final Cookie cookie) {

	}

	@RequestMapping(value = "/names", method = RequestMethod.GET)
	public Collection<String> getCookieNames() {
		ParameterizedTypeReference<Resources<Cookie>> parameterizedTypeReference =
			new ParameterizedTypeReference<Resources<Cookie>>() { };

		final ResponseEntity<Resources<Cookie>> exchange = this.restTemplate.exchange("http://cookie-service/cookies",
			HttpMethod.GET,
			null,
			parameterizedTypeReference
		);

		return exchange
			.getBody()
			.getContent()
			.stream()
			.map(Cookie::getName)
			.collect(Collectors.toList());
	}

	@RequestMapping("/service-instances/{applicationName}")
	public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
		return this.discoveryClient.getInstances(applicationName);
	}

}
