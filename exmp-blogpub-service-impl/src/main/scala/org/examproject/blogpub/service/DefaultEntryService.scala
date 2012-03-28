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

package org.examproject.blogpub.service

import java.util.List
import javax.inject.Inject

import org.apache.commons.collections.Closure
import org.apache.commons.collections.Factory
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.context.ApplicationContext

import org.examproject.blogpub.dto.EntryDto

class DefaultEntryService(
    saveEntryClosureId: String,
    findAllEntryFactoryId: String
) extends EntryService {
    
    private val log: Log = LogFactory.getLog(classOf[DefaultEntryService])
    
    @Inject
    private val context: ApplicationContext = null
     
    def saveEntry(entryDto: EntryDto) = {
        log.info("called.")
        val closure: Closure = context.getBean(
            saveEntryClosureId,
            classOf[Closure]
        )
        closure.execute(
            entryDto
        )
    }
    
    def findAllEntry(feedUrl: String): List[EntryDto] = {
        log.info("called.")
        val factory: Factory = context.getBean(
            findAllEntryFactoryId,
            feedUrl
        ).asInstanceOf[Factory]
        return factory.create().asInstanceOf[List[EntryDto]]
    }
    
}
