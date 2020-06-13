import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.matchers.should.Matchers

import net.ruippeixotog.scalascraper.browser.JsoupBrowser

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

class Test extends AnyWordSpecLike with Matchers {
    "The converter" should {
        "be able to parse items from the index page" in {
            val browser = JsoupBrowser()
            val rss = Parser.parse(browser.parseResource("/index.html"))
            rss.items.length should be(5)
        }
    }
}