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

import javax.inject.Inject

import org.apache.abdera.Abdera
import org.apache.abdera.factory.Factory
import org.apache.abdera.model.Category
import org.apache.abdera.model.Collection
import org.apache.abdera.model.Document
import org.apache.abdera.model.Entry
import org.apache.abdera.model.Service
import org.apache.abdera.model.Workspace
import org.apache.abdera.parser.stax.util.FOMHelper
import org.apache.abdera.protocol.Response
import org.apache.abdera.protocol.client.AbderaClient
import org.apache.commons.collections.Closure
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.UsernamePasswordCredentials
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext

import org.examproject.blogpub.dto.EntryDto

/**
 * @author hiroxpepe
 */
class AbderaSaveEntryClosure extends Closure {

    private val LOG: Logger = LoggerFactory.getLogger(classOf[AbderaSaveEntryClosure])
    
    @Inject
    private val context: ApplicationContext = null
    
    override def execute(o: Object) = {
        LOG.info("called.")
        val entryDto: EntryDto = o.asInstanceOf[EntryDto]
        try {
            save(entryDto)
        } catch {
            case e: Exception => {
                LOG.error("Exception occurred." + e.getMessage())
                throw new RuntimeException(e)
            }
        }
    }
    
    private def save(entryDto: EntryDto) = {
        try {
            // set the URI of the introspection document
            val start: String = entryDto.getUrl()

            val abdera: Abdera = new Abdera()
            val factory: Factory = abdera.getFactory()
            
            LOG.debug("create factory.")

            // create the entry that will be posted
            val entry: Entry = factory.newEntry()
            entry.setId(FOMHelper.generateUuid())
            entry.setUpdated(new java.util.Date())
            entry.addAuthor(entryDto.getAuthor())
            entry.setTitle(entryDto.getTitle())
            entry.setContentAsHtml(entryDto.getContent())
            
            LOG.debug("create entry.")

            // set the category
            val category: Category = factory.newCategory()
            category.setScheme(entryDto.getScheme())
            category.setTerm(entryDto.getCategory())
            category.setLabel(entryDto.getCategory())
            entry.addCategory(category)
            
            LOG.debug("create category.")
            
            // set the tags
            val tags = entryDto.getTags().split(',')
            for (val tag <- tags) {
                entry.addCategory(tag)
            }

            // initialize the client
            val httpClient: HttpClient = new HttpClient()
            val abderaClient: AbderaClient = new AbderaClient(abdera, httpClient)
            
            LOG.debug("create abderaClient.")

            // set the authentication credentials
            abderaClient.addCredentials(
                start,
                null,
                null,
                new UsernamePasswordCredentials(
                    entryDto.getUsername(),
                    entryDto.getPassword()
                )
            )
            
            LOG.debug("authentication abderaClient.")

            // get the service document
            val document: Document[Service] = abderaClient.get(start).getDocument()
            
            LOG.debug("abderaClient getDocument() : " + document.toString())
            
            val service: Service = document.getRoot()

            // get the workspace
            val workspace: Workspace = service.getWorkspace(entryDto.getBlog())

            val collection: Collection = workspace.getCollections().get(0)
            val uri: String = collection.getHref().toString()

            LOG.debug("uri : " + uri)
            
            // post the entry to the collection
            val response: Response = abderaClient.post(
                uri,
                entry
            )

            LOG.debug("post entry.")
            
            // check the result
            if (response.getStatus() == 201) {
                LOG.info("success.")
            }
            else {
                LOG.error("failed.")
            }        
        } catch {
            case e: Exception => {
                LOG.error("Exception occurred. " + e.getMessage())
                throw new RuntimeException(e)
            }
        }
    }

}
