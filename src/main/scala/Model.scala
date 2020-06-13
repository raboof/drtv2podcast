import java.time.ZonedDateTime

import akka.http.scaladsl.model.Uri

object Model {
    case class Item(
        title: String,
        link: Uri,
        pubDate: ZonedDateTime,
        url: Uri
    )
    case class Rss(
        title: String,
        link: Uri,
        description: String,
        items: Seq[Item]
    ) {
        def +(rss: Rss): Rss = copy(items = items ++ rss.items)
    }
}