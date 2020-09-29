package li.test.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import li.test.entities.db.Order;
import li.test.entities.db.Parameter;
import li.test.entities.db.Price;
import li.test.entities.db.Symbol;
import li.test.repositories.OrdersRepository;
import li.test.repositories.ParametersRepository;
import li.test.repositories.PricesRepository;
import li.test.repositories.SymbolsRepository;

@Service
public class OrderService {

	private static final Logger logger = LogManager.getLogger(OrderService.class);

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

	public OrderService(SymbolsRepository symbolsRepository, OrdersRepository ordersRepository, ParametersRepository parametersRepository, PricesRepository pricesRepository) {
		OrderService.symbolsRepository = symbolsRepository;
		OrderService.ordersRepository = ordersRepository;
		OrderService.parametersRepository = parametersRepository;
		OrderService.pricesRepository = pricesRepository;
	}

	@Scheduled(cron="${prices.update.service.schedule}")
	public void run() {

		Symbol symbol = symbolsRepository.findBySymbol(symbolName);

		if (pricesRepository.countBySymbol(symbol)>0) {

			//Timestamp latestPriceTime = pricesRepository.getLatestPriceTimeBySymbol(symbol);

			List<Parameter> allParameters = parametersRepository.findAll();

			for (Parameter parameter : allParameters ) {

				if (ordersRepository.countBySymbolAndParameter(symbol, parameter)>0) {
					
					//Timestamp currentPriceTime = ordersRepository.findMinOrderTimeOpenOrdersBySymbolAndParameter(symbol, parameter);

					//while(currentPriceTime.getTime()<latestPriceTime.getTime()) {
					ArrayList<Price> allPrices = pricesRepository.getAllPricesBySymbolAndStartTime(symbol, parameter.getStartTime());
					for (Price currentPrice : allPrices) {

						// TODO: Wenn Summe Profit kleiner als Anfangsbudget, Schleife beenden.

						//Price currentPrice = pricesRepository.findByPriceTime(currentPriceTime);

						if (currentPrice!=null) {

							//logger.debug("Current priceTime: " +currentPrice.getPriceTime());

							ArrayList<Order> openOrders = ordersRepository.findOpenOrdersBySymbolAndParameter(symbol, parameter);

							BigDecimal desiredMultiplier = ((new BigDecimal(100).add(parameter.getFeesPercentage())).add(parameter.getDesiredGainPercentage())).divide(new BigDecimal(100));
							BigDecimal stopLossMultiplier = ((new BigDecimal(100).subtract(parameter.getFeesPercentage())).subtract(parameter.getStopLossTriggerPercentage())).divide(new BigDecimal(100));

							for (Order openOrder : openOrders) {

								// Wenn gewünschter Verlaufspreis erreicht wurde, Order abschliessen (Mit AVG-Preis berechnet)
								if((openOrder.getOrderPrice().multiply(desiredMultiplier)).compareTo(currentPrice.getAvgOpenClosePrice())==-1) {
									closeOrderAvg(openOrder, currentPrice);
								}

								// Wenn Stop-Loss-Trigger erreicht wurde, Order mit nächsten LowPrice abschliessen. (Mit LOW-Preis berechnet)
								if((openOrder.getOrderPrice().multiply(stopLossMultiplier)).compareTo(currentPrice.getLowPrice())==1) {
									closeOrderLow(openOrder, currentPrice);
								}

							}

							// Wenn die Höchstzahl an laufeneden Orders noch nicht erreicht ist
							if (ordersRepository.getCountOpenOrdersBySymbolAndParameter(symbol, parameter)<parameter.getMaxRunningOrders()) {

								// Wenn die zeitliche Mindestdifferenz zwischen 2 Orders erreicht ist, neuen Order anlegen.
								if (currentPrice.getPriceTime().getTime()>(ordersRepository.findMaxOrderTimeBySymbolAndParameter(symbol, parameter).getTime()+parameter.getTimeDifferenceBetweenOrders())) {

									Order order = new Order();
									order.setSymbol(symbol);
									order.setParameter(parameter);
									order.setOrderTime(currentPrice.getPriceTime());
									order.setOrderPrice(currentPrice.getAvgOpenClosePrice());
									order.setOrderAmount(parameter.getMaxOrderAmount());

									ordersRepository.save(order);

								}
							}		
						}

						//currentPriceTime = new Timestamp(currentPriceTime.getTime()+60000);
					}

				} else {

					Price firstPrice = pricesRepository.findByPriceTime(parameter.getStartTime());

					Order firstOrder = new Order();
					firstOrder.setSymbol(symbol);
					firstOrder.setParameter(parameter);
					firstOrder.setOrderTime(firstPrice.getPriceTime());
					firstOrder.setOrderPrice(firstPrice.getAvgOpenClosePrice());
					firstOrder.setOrderAmount(parameter.getMaxOrderAmount());

					ordersRepository.save(firstOrder);

				}				
			}

		}
	}

	private void closeOrderAvg(Order openOrder, Price closePrice) {

		openOrder.setSettlementTime(closePrice.getPriceTime());
		openOrder.setSettlementPrice(closePrice.getAvgOpenClosePrice());

		BigDecimal orderQuantity = openOrder.getOrderAmount().divide(openOrder.getOrderPrice(), MathContext.DECIMAL32);
		BigDecimal settlementAmount = orderQuantity.multiply(closePrice.getAvgOpenClosePrice());		

		openOrder.setProfit(settlementAmount.subtract(openOrder.getOrderAmount()));

		ordersRepository.save(openOrder);
	}

	private void closeOrderLow(Order openOrder, Price closePrice) {

		openOrder.setSettlementTime(closePrice.getPriceTime());
		openOrder.setSettlementPrice(closePrice.getLowPrice());

		BigDecimal orderQuantity = openOrder.getOrderAmount().divide(openOrder.getOrderPrice(), MathContext.DECIMAL32);
		BigDecimal settlementAmount = orderQuantity.multiply(closePrice.getLowPrice());

		openOrder.setProfit(settlementAmount.subtract(openOrder.getOrderAmount()));

		ordersRepository.save(openOrder);
	}

}
