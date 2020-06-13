import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.ZoneId

import akka.http.scaladsl.model.Uri

import net.ruippeixotog.scalascraper.model._
import net.ruippeixotog.scalascraper.model._
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.scraper.HtmlExtractor

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

import Model._

object Parser {
    def parseDate(datum: String): ZonedDateTime = {
        val Array(monthName, day, year) = datum.split("[, ]+")
        val month = monthName match {
            case "jan" => "01"
            case "feb" => "02"
            case "mrt" => "03"
            case "apr" => "04"
            case "mei" => "05"
            case "jun" => "06"
            case "jul" => "07"
            case "aug" => "08"
            case "sep" => "09"
            case "okt" => "10"
            case "nov" => "11"
            case "dec" => "12"
        }
        ZonedDateTime.of(LocalDateTime.parse(s"$year-$month-${day.reverse.padTo(2, '0').reverse}T12:00:00"), ZoneId.of("Europe/Amsterdam"))
    }

    def parse(item: Element): Item = {
        // Perhaps in the future fetch the whole page to get more details
        val link = Uri(item >> attr("href")(".read-more-button"))
        val url = Uri(item >> attr("src")("source"))
        Item(
            item >> element(".post-title") >> text("a"),
            link,
            parseDate(item >> text(".updated")),
            url
        )
    }

    def parse(doc: Document): Rss = {
        Rss(
            "DRTV Actueel Nieuws",
            Uri("https://www.deventerrtv.nl/category/radio/actueel-nieuws/"),
            "Deventer radio & televisie Actueel Nieuws",
            (doc >> elementList("article")).map(parse).toSeq
        )
    }
}