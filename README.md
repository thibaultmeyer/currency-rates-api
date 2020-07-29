# Currency Rates API

A powerful API to convert currencies build on top of [micronaut.io](http://micronaut.io/).


## How to build
To compile this project, you must ensure that Java 8 and Maven are correctly installed.

    #> mvn compile
    #> mvn package


## REST API

**List available currencies**
```http
GET /currency

[
  "AUD",
  "BGN",
  ...
  "USD",
  "ZAR"
]
```

**List available exchange rates for a given currency**
```http
GET /currency/EUR

[
  {
    "fromCurrency": "EUR",
    "fromValue": 1,
    "toCurrency": "AUD",
    "toValue": 1.6348
  },
  ...
  {
    "fromCurrency": "EUR",
    "fromValue": 1,
    "toCurrency": "ZAR",
    "toValue": 19.3269
  }
]
```

**Convert a value from one currency to another one**
```http
GET /currency/EUR/USD/100

{
  "fromCurrency": "EUR",
  "fromValue": 100,
  "toCurrency": "USD",
  "toValue": 117.2500
}
```
