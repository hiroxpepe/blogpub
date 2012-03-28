/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 

package org.examproject.blogpub.functor

import java.net.URL
import java.util.ArrayList
import java.util.List
import javax.inject.Inject

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.Parser;
import org.apache.commons.collections.Factory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext

import org.examproject.blogpub.dto.EntryDto

import scala.collection.JavaConversions._

class AbderaFindAllEntryFactory(
    val feedUrl: String
) extends Factory {

    private val LOG: Logger = LoggerFactory.getLogger(classOf[AbderaFindAllEntryFactory])
    
    @Inject
    private val context: ApplicationContext = null
    
    override def create(): Object  = {
        LOG.info("called.")
        try {
            return findAll()
        } catch {
            case e: Exception => {
                LOG.error("Exception occurred. " + e.getMessage())
                throw new RuntimeException(e)
            }
        }
    }
    
    private def findAll(): List[EntryDto] = {
        val abdera: Abdera = new Abdera()
        val parser: Parser = abdera.getParser()
        val url: URL = new URL(feedUrl)
        val doc: Document[Feed] = parser.parse(url.openStream(), url.toString())
        val feed: Feed = doc.getRoot()
        val entryList = feed.getEntries()
        val resultList: List[EntryDto] = new ArrayList[EntryDto]()
        entryList.foreach(
            (entry: Entry) => {
                LOG.debug("title: " + entry.getTitle());
                LOG.debug("content: " + entry.getContent());
                val entrydto: EntryDto = context.getBean(
                    "entryDto",
                    new Integer(1),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    entry.getTitle(),
                    entry.getContent(),
                    null,
                    null
                ).asInstanceOf[EntryDto]
                resultList.add(entrydto)
            }
        )        
        return resultList
    }
    
}
