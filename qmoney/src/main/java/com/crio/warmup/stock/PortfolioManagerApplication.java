
package com.crio.warmup.stock;


import com.crio.warmup.stock.dto.*;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;


public class PortfolioManagerApplication {

  private final static String TIINGO_API_TOKEN = "74babdce6ab0d4c3ea73d796c654885e2ed530a9";

  private static RestTemplate restTemplate = new RestTemplate();
  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  // Task:
  // - Read the json file provided in the argument[0], The file is available in the classpath.
  // - Go through all of the trades in the given file,
  // - Prepare the list of all symbols a portfolio has.
  // - if "trades.json" has trades like
  // [{ "symbol": "MSFT"}, { "symbol": "AAPL"}, { "symbol": "GOOGL"}]
  // Then you should return ["MSFT", "AAPL", "GOOGL"]
  // Hints:
  // 1. Go through two functions provided - #resolveFileFromResources() and #getObjectMapper
  // Check if they are of any help to you.
  // 2. Return the list of all symbols in the same order as provided in json.

  // Note:
  // 1. There can be few unused imports, you will need to fix them to make the build pass.
  // 2. You can use "./gradlew build" to check if your code builds successfully.

  public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException {
    File reader = resolveFileFromResources(args[0]);
    ObjectMapper mapper = getObjectMapper();
    PortfolioTrade[] portfolios = mapper.readValue(reader, PortfolioTrade[].class);
    List<String> list =
        Arrays.stream(portfolios).map(PortfolioTrade::getSymbol).collect(Collectors.toList());
    return list;
  }






  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Now that you have the list of PortfolioTrade and their data, calculate annualized returns
  //  for the stocks provided in the Json.
  //  Use the function you just wrote #calculateAnnualizedReturns.
  //  Return the list of AnnualizedReturns sorted by annualizedReturns in descending order.

  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.



  // TODO: CRIO_TASK_MODULE_REST_API
  // Find out the closing price of each stock on the end_date and return the list
  // of all symbols in ascending order by its close value on end date.

  // Note:
  // 1. You may have to register on Tiingo to get the api_token.
  // 2. Look at args parameter and the module instructions carefully.
  // 2. You can copy relevant code from #mainReadFile to parse the Json.
  // 3. Use RestTemplate#getForObject in order to call the API,
  // and deserialize the results in List<Candle>



  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }

  private static File resolveFileFromResources(String filename) throws URISyntaxException {
    return Paths.get(Thread.currentThread().getContextClassLoader().getResource(filename).toURI())
        .toFile();
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }


  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  // Follow the instructions provided in the task documentation and fill up the correct values for
  // the variables provided. First value is provided for your reference.
  // A. Put a breakpoint on the first line inside mainReadFile() which says
  // return Collections.emptyList();
  // B. Then Debug the test #mainReadFile provided in PortfoliomanagerApplicationTest.java
  // following the instructions to run the test.
  // Once you are able to run the test, perform following tasks and record the output as a
  // String in the function below.
  // Use this link to see how to evaluate expressions -
  // https://code.visualstudio.com/docs/editor/debugging#_data-inspection
  // 1. evaluate the value of "args[0]" and set the value
  // to the variable named valueOfArgument0 (This is implemented for your reference.)
  // 2. In the same window, evaluate the value of expression below and set it
  // to resultOfResolveFilePathArgs0
  // expression ==> resolveFileFromResources(args[0])
  // 3. In the same window, evaluate the value of expression below and set it
  // to toStringOfObjectMapper.
  // You might see some garbage numbers in the output. Dont worry, its expected.
  // expression ==> getObjectMapper().toString()
  // 4. Now Go to the debug window and open stack trace. Put the name of the function you see at
  // second place from top to variable functionNameFromTestFileInStackTrace
  // 5. In the same window, you will see the line number of the function in the stack trace window.
  // assign the same to lineNumberFromTestFileInStackTrace
  // Once you are done with above, just run the corresponding test and
  // make sure its working as expected. use below command to do the same.
  // ./gradlew test --tests PortfolioManagerApplicationTest.testDebugValues

  public static List<String> debugOutputs() {

    String valueOfArgument0 = "trades.json";
    String resultOfResolveFilePathArgs0 = "";
    String toStringOfObjectMapper = "";
    String functionNameFromTestFileInStackTrace = "";
    String lineNumberFromTestFileInStackTrace = "";


    return Arrays.asList(
        new String[] {valueOfArgument0, resultOfResolveFilePathArgs0, toStringOfObjectMapper,
            functionNameFromTestFileInStackTrace, lineNumberFromTestFileInStackTrace});
  }


  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.
  
  public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException {
    final String tiingoToken = "953e5d1702c35f1aabd7a475fd8d256e2274d731";
    List<PortfolioTrade> portfolioTrades = readTradesFromJson(args[0]);
    LocalDate endDate = LocalDate.parse(args[1]);
    RestTemplate restTemplate = new RestTemplate();
    List<TotalReturnsDto> totalReturnsDtos = new ArrayList<>();
    List<String> listOfSortSymbolsOnClosingPrice = new ArrayList<>();
    for (PortfolioTrade portfolioTrade : portfolioTrades) {
      String tiingoURL = prepareUrl(portfolioTrade, endDate, tiingoToken);
      TiingoCandle[] tiingoCandleArray = restTemplate.getForObject(tiingoURL, TiingoCandle[].class);
      totalReturnsDtos.add(new TotalReturnsDto(portfolioTrade.getSymbol(),
          tiingoCandleArray[tiingoCandleArray.length - 1].getClose()));
    }
    Collections.sort(totalReturnsDtos,
        (a, b) -> Double.compare(a.getClosingPrice(), b.getClosingPrice()));
    for (TotalReturnsDto totalReturnsDto : totalReturnsDtos) {
      listOfSortSymbolsOnClosingPrice.add(totalReturnsDto.getSymbol());
    }
    return listOfSortSymbolsOnClosingPrice;
  }


  // TODO:
  // After refactor, make sure that the tests pass by using these two commands
  // ./gradlew test --tests PortfolioManagerApplicationTest.readTradesFromJson
  // ./gradlew test --tests PortfolioManagerApplicationTest.mainReadFile
  public static List<PortfolioTrade> readTradesFromJson(String filename)
      throws IOException, URISyntaxException {
    File reader = resolveFileFromResources(filename);
    ObjectMapper mapper = getObjectMapper();
    PortfolioTrade[] portfolios = mapper.readValue(reader, PortfolioTrade[].class);
    return Arrays.asList(portfolios);
  }


  // TODO:
  // Build the Url using given parameters and use this function in your code to cann the API.
  // public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
  //   return Collections.emptyList().toString();
  // }

  public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
    // Check if end date is greater than purchase date
    if (trade.getPurchaseDate().compareTo(endDate) > 0) {
        throw new RuntimeException("End date is greater than purchased date");
    }

    // Tiingo API base URL for fetching daily stock prices
    String baseUrl = "https://api.tiingo.com/tiingo/daily/";

    // Build the complete URL with the required parameters
    String url = String.format("%s%s/prices?startDate=%s&endDate=%s&token=%s",
            baseUrl, trade.getSymbol(), trade.getPurchaseDate(), endDate, token);

    return url;
}

  // TODO:
  //  Ensure all tests are passing using below command
  //  ./gradlew test --tests ModuleThreeRefactorTest
  static Double getOpeningPriceOnStartDate(List<Candle> candles) {
     return 0.0;
  }


  public static Double getClosingPriceOnEndDate(List<Candle> candles) {
     return 0.0;
  }


  public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
     return Collections.emptyList();
  }

  // public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
  //     throws IOException, URISyntaxException {
  //    return Collections.emptyList();
  // }

  public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
      throws IOException, URISyntaxException {
    List<PortfolioTrade> portfolioTrades = readTradesFromJson(args[0]);
    List<AnnualizedReturn> annualizedReturns = new ArrayList<>();
    LocalDate localDate = LocalDate.parse(args[1]);
    for (PortfolioTrade portfolioTrade : portfolioTrades) {
      List<Candle> candles = fetchCandles(portfolioTrade, localDate, getToken());
      AnnualizedReturn annualizedReturn = calculateAnnualizedReturns(localDate, portfolioTrade,
          getOpeningPriceOnStartDate(candles), getClosingPriceOnEndDate(candles));
      annualizedReturns.add(annualizedReturn);
    }
    return annualizedReturns.stream()
        .sorted((a1, a2) -> Double.compare(a2.getAnnualizedReturn(), a1.getAnnualizedReturn()))
        .collect(Collectors.toList());
  }


  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Return the populated list of AnnualizedReturn for all stocks.
  //  Annualized returns should be calculated in two steps:
  //   1. Calculate totalReturn = (sell_value - buy_value) / buy_value.
  //      1.1 Store the same as totalReturns
  //   2. Calculate extrapolated annualized returns by scaling the same in years span.
  //      The formula is:
  //      annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
  //      2.1 Store the same as annualized_returns
  //  Test the same using below specified command. The build should be successful.
  //     ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn

  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate, PortfolioTrade trade, Double buyPrice, Double sellPrice) {
    int quantity = trade.getQuantity();
    Double totalSoldValue = quantity * sellPrice;
    Double totalBoughtValue = quantity * buyPrice;

    Double totalReturn = (totalSoldValue - totalBoughtValue) / totalBoughtValue;
    LocalDate start = trade.getPurchaseDate();

    Long days = ChronoUnit.DAYS.between(start, endDate);

    String dayString = days.toString();
    Double totalDays = Double.valueOf(dayString);
    Double totalYear = totalDays / 365.0;
    Double annualReturn = (Math.pow((1 + totalReturn), (1 / totalYear))) - 1;
    return new AnnualizedReturn(trade.getSymbol(), annualReturn, totalReturn);
}













  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());

    printJsonObject(mainReadFile(args));


    printJsonObject(mainReadQuotes(args));



    printJsonObject(mainCalculateSingleReturn(args));

  }






  public static String getToken() {
    return TIINGO_API_TOKEN;
  }
}

