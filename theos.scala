import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties}
import com.sksamuel.elastic4s.requests.common.RefreshPolicy
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.requests.searches.SearchHit

import scala.io.Source


case class Book(title: String, author: String, file_number: Int, publish_year: Int, file_uri: String)

object theos extends App {
  val props = ElasticProperties("http://127.0.0.1:9200")
  val client = ElasticClient(JavaClient(props))

  def create_elastic_sentence_index(client: ElasticClient) = {
    client.execute {
      createIndex("sentence")
    }.await
  }

  def write_sentence_to_es(client: ElasticClient, book: Book, sentence: String): Unit = {
    client.execute {
      indexInto("sentence").fields(
        "title" -> book.title,
                "author" -> book.author,
                "year" -> book.publish_year,
                "sentence" -> sentence
      ).refresh(RefreshPolicy.Immediate)
    }.await
  }

  def search_keyword(client: ElasticClient, keyword: String) = {
    val resp = client.execute {
      search("sentence").query(keyword)
    }.await
    resp
  }

  def breakdown_text(book: Book): List[String] = {
    val paragraphs = Source.fromFile(book.file_uri).mkString.split("\\n\\n") // split book into paragraphs
    val sentences = paragraphs.map(f => f.split('.')).map(f => f.mkString.replace("\n", "")).toList
    sentences
  }

  def pull_hit_sentence(hit: SearchHit) = {
    val sentence: String = hit.sourceAsMap("sentence").toString
    println(sentence)
  }

  val b = Book("Confessions", "St. Agustine", 1, 1200, "src/main/scala/com.theos/confessions.txt")
  val ss = breakdown_text(book = b)
  create_elastic_sentence_index(client)
  for (s <- ss){
    write_sentence_to_es(client, b, s)
  }

  val resp = search_keyword(client,"faith")
  val hits = resp.result.hits.hits.toList
  hits.map(pull_hit_sentence)

  client.close()
}