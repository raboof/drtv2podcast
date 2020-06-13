import scala.concurrent.Await
import scala.concurrent.duration._

import scala.xml.XML
import scala.xml.PrettyPrinter

import akka.actor.ActorSystem
import akka.http.scaladsl.client.RequestBuilding.RequestBuilder
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.Http

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import java.time.format.DateTimeFormatter

object Main extends App {
    val browser = JsoupBrowser()
    val rss = Parser.parse(
        browser.get("https://www.deventerrtv.nl/category/radio/actueel-nieuws/"),
        uri => browser.get(uri.toString)
    ) + Parser.parse(
        browser.get("https://www.deventerrtv.nl/category/radio/actueel-nieuws/page/2/"),
        uri => browser.get(uri.toString)
    )

    implicit val system = ActorSystem("scraper")
    val http = Http()

    val xml = <rss version="2.0">
        <channel>
            <title>DRTV Actueel Nieuws</title>
            <description>Deventer Radio Actueel Nieuws</description>
            <language>nl</language>
            {rss.items.map(item => {
                val len = {
                    val result = Await.result(http.singleRequest(HttpRequest(HEAD, uri = item.url)), 10.seconds)
                    result.discardEntityBytes()
                    result.entity.contentLengthOption.get.toString
                }
                <item>
                    <title>{item.title}</title>
                    <link>{item.link}</link>
                    <pubDate>{item.pubDate.format(DateTimeFormatter.RFC_1123_DATE_TIME)}</pubDate>
                    <enclosure url={item.url.toString} length={len} type="audio/mpeg"/>
                </item>
            })}
        </channel>
    </rss>
    
    val formatted = new PrettyPrinter(24, 2).format(xml)
    XML.save("out.xml", xml)
    println(formatted)

    system.terminate()
}