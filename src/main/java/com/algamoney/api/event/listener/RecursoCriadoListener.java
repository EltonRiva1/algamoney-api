package com.algamoney.api.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algamoney.api.event.RecursoCriadoEvent;

@Component
public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvent> {

	@Override
	public void onApplicationEvent(RecursoCriadoEvent event) {
		// TODO Auto-generated method stub
		HttpServletResponse httpServletResponse = event.getHttpServletResponse();
		Long codigo = event.getCodigo();
		this.adicionarHeaderLocation(httpServletResponse, codigo);
	}

	private void adicionarHeaderLocation(HttpServletResponse httpServletResponse, Long codigo) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}").buildAndExpand(codigo).toUri();
		httpServletResponse.setHeader("Location", uri.toASCIIString());
	}
}
