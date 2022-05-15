package dto

import scala.concurrent.duration.{DurationInt, FiniteDuration}

case class Configuration(
                        transactionsUrl: String = "https://horizon-testnet.stellar.org/transactions",
                        batchSize: Int = 50,
                        startLedger: Int = 0,
                        endLedger: Int = Int.MaxValue,
                        fetcherForwardDelayOnEmpty: FiniteDuration = 5.seconds,
                        fetcherRetryDelayOnError: FiniteDuration = 5.seconds,
                        initRetryDelayOnError: FiniteDuration = 5.seconds,
                        initRetriesCount: Int = 10
                        )
