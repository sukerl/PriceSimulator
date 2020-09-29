package li.test.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import li.test.entities.db.Price;
import li.test.entities.db.Symbol;
import li.test.entities.dtos.KlineData;
import li.test.repositories.OrdersRepository;
import li.test.repositories.ParametersRepository;
import li.test.repositories.PricesRepository;
import li.test.repositories.SymbolsRepository;

@Service
public class PricesUpdateService {

	private static final Logger logger = LogManager.getLogger(PricesUpdateService.class);

	@Value("${symbol}")
	private String symbolName;

	@Value("${prices.update.service.url}")
	private String pricesUpdateServiceUrl;

	@Value("${prices.update.service.klines.interval}")
	private String pricesUpdateServiceKlinesInterval;

	private static SymbolsRepository symbolsRepository;
	private static OrdersRepository ordersRepository;
	private static ParametersRepository parametersRepository;
	private static PricesRepository pricesRepository;

	public PricesUpdateService(SymbolsRepository symbolsRepository, OrdersRepository ordersRepository, ParametersRepository parametersRepository, PricesRepository pricesRepository) {
		PricesUpdateService.symbolsRepository = symbolsRepository;
		PricesUpdateService.ordersRepository = ordersRepository;
		PricesUpdateService.parametersRepository = parametersRepository;
		PricesUpdateService.pricesRepository = pricesRepository;
	}

	//@Scheduled(cron="${prices.update.service.schedule}")
	public void run() {

		Long startTime = (long) 0;

		Symbol symbol = symbolsRepository.findBySymbol(symbolName);

		Price latestPrice =  pricesRepository.findLatestPriceBySymbol(symbol);

		if (latestPrice != null) {
			startTime = latestPrice.getPriceTime().getTime();
		}

		while (startTime <= (System.currentTimeMillis()-120000)) {
			
			logger.info("Kursabfrage beginnt mit Zeipunkt: " +new Timestamp(startTime));

			// REST-Template instanzieren, ResponseEntitiy erstellen
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> result;

			// Mapper zum mappen der JSON-Objekte erstellen
			ObjectMapper mapper = new ObjectMapper();

			// Parameter für HTTP Header setzen
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(pricesUpdateServiceUrl)
					.queryParam("symbol", String.join(",", symbolName))
					.queryParam("interval", String.join(",", pricesUpdateServiceKlinesInterval))
					.queryParam("startTime", String.join(",", startTime.toString()));
			logger.debug("URI-String: " +builder.toUriString());

			try {

				// HTTP-Entitiy für Webservicecall vorbereiten
				HttpEntity<String> entity = new HttpEntity<String>(headers);
				logger.debug(entity);

				// Call an Webservice absetzen und dem result zuweisen
				result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
				logger.debug(result);

				// Ergebnis des Response auf Objekt mappen
				List<KlineData> klineDataList = Arrays.asList(mapper.readValue(result.getBody(), KlineData[].class));
				logger.debug("Der Webservice Response wurde erfolgreich auf die Liste der Java-Objekte gemappt.");
				logger.debug(klineDataList.get(0));
				
				for(KlineData klineData : klineDataList) {
					
					Long priceTimeLong = klineData.getOpenTime()/60000*60000;
					BigDecimal avgOpenClosePrice = (klineData.getOpen().add(klineData.getClose())).divide(new BigDecimal(2));
					
					Price price = new Price();
					price.setSymbol(symbol);					
					price.setPriceTime(new Timestamp(priceTimeLong));
					price.setAvgOpenClosePrice(avgOpenClosePrice);
					price.setLowPrice(klineData.getLow());
					
					pricesRepository.save(price);
					
				}
				
				Thread.sleep(2000);

			} catch (HttpStatusCodeException e) {
				if (e.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
					logger.error("Request für Extraction war NICHT erfolgreich. Ein serverseitiger Fehler ist aufgetreten.");
					logger.error(e);
				} else if (e.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
					logger.error("Request für Extraction war NICHT erfolgreich. Der Fehler war: " +e.getStatusCode());
					logger.error(e);
				}
			} catch (JsonProcessingException e) {
				logger.error("Fehler beim Verarbeiten des Webservice Response (JSON) aufgetreten.");
				logger.error(e);
			} catch (Exception e) {
				logger.error("Genereller Fehler aufgetreten.");
				logger.error(e);
			}

			latestPrice =  pricesRepository.findLatestPriceBySymbol(symbol);
			startTime = (latestPrice.getPriceTime().getTime())+60000;
		}
	}
}
