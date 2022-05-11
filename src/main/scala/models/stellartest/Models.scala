package models.stellartest

case class Embedded (
                      records: Seq[Record]
                    )

case class FeeBumpTransaction (
                                hash: String,
                                signatures: Seq[String]
                              )

case class InnerTransaction (
                              hash: String,
                              signatures: Seq[String],
                              max_fee: String
                            )

case class Links (
                   self: Self,
                   next: Self,
                   prev: Self
                 )

case class Links1 (
                    self: Self,
                    account: Self,
                    ledger: Self,
                    operations: Operations,
                    effects: Operations,
                    precedes: Self,
                    succeeds: Self,
                    transaction: Self
                  )

case class Operations (
                        href: String,
                        templated: Boolean
                      )

case class Preconditions (
                           timebounds: Timebounds
                         )

case class Record(
                     _links: Links1,
                     id: String,
                     paging_token: Long,
                     successful: Boolean,
                     hash: String,
                     ledger: Int,
                     created_at: String,
                     source_account: String,
                     source_account_sequence: String,
                     fee_account: String,
                     fee_charged: String,
                     max_fee: String,
                     operation_count: Int,
                     envelope_xdr: String,
                     result_xdr: String,
                     result_meta_xdr: String,
                     fee_meta_xdr: String,
                     memo_type: String,
                     signatures: Seq[String],
                     valid_after: Option[String],
                     valid_before: Option[String],
                     preconditions: Option[Preconditions],
                     fee_bump_transaction: Option[FeeBumpTransaction],
                     inner_transaction: Option[InnerTransaction],
                     memo: Option[String],
                     memo_bytes: Option[String]
                   )

case class RootInterface (
                           _links: Links,
                           _embedded: Embedded
                         )

case class Self (
                  href: String
                )

case class Timebounds (
                        min_time: String
                      )

